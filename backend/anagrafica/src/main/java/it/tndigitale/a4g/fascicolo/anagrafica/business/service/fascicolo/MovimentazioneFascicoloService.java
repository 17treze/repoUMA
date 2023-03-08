package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MovimentazioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.TipoMovimentazioneFascicolo;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MandatoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MovimentazioneDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.StatusMessagesEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.MandatoService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.ApriFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.FascicoloCreationResultDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.SospensioneDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.mapper.FascicoloMapper;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.time.Clock;

/**
 * Gestisce i movimenti del fascicolo tra lo stato SOSPESO e CHIUSO.
 * @author B.Conetta
 *
 */
@Service
public class MovimentazioneFascicoloService {

	private static final Logger logger = LoggerFactory.getLogger(MovimentazioneFascicoloService.class);

	private static final List<StatoFascicoloEnum> STATI_CONSENTITI_CHIUSURA = Arrays.asList(StatoFascicoloEnum.IN_AGGIORNAMENTO, StatoFascicoloEnum.IN_VALIDAZIONE, StatoFascicoloEnum.VALIDATO, StatoFascicoloEnum.CONTROLLATO_OK, StatoFascicoloEnum.IN_CHIUSURA);
	private static final List<StatoFascicoloEnum> STATI_CONSENTITI_SOSPENSIONE = Arrays.asList(StatoFascicoloEnum.IN_AGGIORNAMENTO, StatoFascicoloEnum.VALIDATO, StatoFascicoloEnum.CONTROLLATO_OK);

	@Autowired private Clock clock;
	@Autowired private FascicoloDao fascicoloDao;
	@Autowired private UtenteComponent utenteComponent;
	@Autowired private MovimentazioneDao movimentazioneDao;
	@Autowired private MandatoDao mandatoDao;
	@Autowired private FascicoloComponentMethodFactory fascicoloComponentFactory;
	@Autowired private MandatoService mandatoService;

	// setta lo stato del fasicolo a CHIUSO e chiude il mandato corrente, creando una nuova movimentazione.
	@Transactional
	public void chiudi(String cuaa) throws ChiusuraFascicoloException {
		FascicoloModel fascicolo = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0).orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
		chiudi(fascicolo);
	}

	@Transactional
	public void chiudi(FascicoloModel fascicolo) throws ChiusuraFascicoloException {
		var today = clock.today();
		var stato = fascicolo.getStato();
		var utente = utenteComponent.username();

		if (!STATI_CONSENTITI_CHIUSURA.contains(fascicolo.getStato())) {
			throw new ChiusuraFascicoloException("Il fascicolo non può essere chiuso nello stato " + stato);
		}
//		si verifica l'esistenza di un mandato corrente per la chiusura
		Optional<MandatoModel> mandatoOpt = fascicolo.getMandatoCorrente();
		if (!mandatoOpt.isPresent()) {
			throw new ChiusuraFascicoloException("Nessun mandato trovato per l'azienda " + fascicolo.getCuaa());
		}

		// aggiorno fascicolo
		fascicoloDao.save(fascicolo
				.setStato(StatoFascicoloEnum.CHIUSO)
				.setDataModifica(today)
				.setUtenteModifica(utente));

		// creo nuova movimentazione
		movimentazioneDao.save(new MovimentazioneModel()
				.setUtente(utente)
				.setDataInizio(clock.now())
				.setFascicolo(fascicolo)
				.setTipo(TipoMovimentazioneFascicolo.CHIUSURA));

		// chiudo il mandato
		var mandatoToSave = mandatoOpt.get();
		mandatoToSave.setDataFine(today);
		mandatoDao.save(mandatoToSave);
	}

	@Transactional
	public FascicoloCreationResultDto ricostituisci(ApriFascicoloDto mandatoDto) throws Exception {
		logger.debug("Cerco di aprire il fascicolo per {} associandolo allo sportello {}", mandatoDto.getCodiceFiscale(), mandatoDto.getIdentificativoSportello());
		FascicoloAbstractComponent<?> fascicoloComponent = fascicoloComponentFactory.from(mandatoDto.getCodiceFiscale());
		var anomaliesList = fascicoloComponent.validaOperazioneFascicolo(FascicoloOperationEnum.RIAPRI);
		FascicoloModel fascicolo = fascicoloComponent.aggiorna();
		mandatoDto.setCodiceFiscale(fascicolo.getCuaa());
		mandatoService.associaMandatoANuovoFascicolo(mandatoDto);

		// chiusura record in movimentazione
		MovimentazioneModel movimentazione = movimentazioneDao.findByFascicoloAndDataFineIsNullAndTipo(fascicolo.getCuaa(), TipoMovimentazioneFascicolo.CHIUSURA).orElseThrow(() -> new MovimentazioneFascicoloException("Errore reperimento dal registro dei fascicoli chiusi"));
		movimentazioneDao.save(movimentazione.setDataFine(clock.now()).setUtente(utenteComponent.username()));
		return new FascicoloCreationResultDto(anomaliesList, FascicoloMapper.fromFascicolo(fascicolo));
	}

	// sospendi il fascicolo
	@Transactional
	public void sospendi(final SospensioneDto sospensioneDto)  throws RestClientException, NoSuchElementException, MovimentazioneFascicoloException {

		if (sospensioneDto.getDataInizio() == null || sospensioneDto.getMotivazioneInizio() == null) {
			throw new MovimentazioneFascicoloException("Informazioni sospensione assenti o incomplete");
		}
		FascicoloModel fascicolo = fascicoloDao.findByCuaaAndIdValidazione(sospensioneDto.getCuaa(), 0).orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));

		if (!STATI_CONSENTITI_SOSPENSIONE.contains(fascicolo.getStato())) {
			throw new MovimentazioneFascicoloException("L'attuale stato del fascicolo non permette la sospensione");	
		}

		fascicoloDao.save(fascicolo.setStato(StatoFascicoloEnum.SOSPESO)
				.setDataModifica(LocalDate.now())
				.setUtenteModifica(utenteComponent.username()));

		movimentazioneDao.save(new MovimentazioneModel()
				.setFascicolo(fascicolo)
				.setDataInizio(sospensioneDto.getDataInizio())
				.setMotivazioneInizio(sospensioneDto.getMotivazioneInizio())
				.setUtente(utenteComponent.username())
				.setTipo(TipoMovimentazioneFascicolo.SOSPENSIONE));
	}

	// rimuovi la sospensione del fascicolo
	@Transactional
	public void rimuoviSospensione(final SospensioneDto sospensioneDto) throws RestClientException, NoSuchElementException, MovimentazioneFascicoloException  {

		if (sospensioneDto.getDataFine() == null || sospensioneDto.getMotivazioneFine() == null) {
			throw new MovimentazioneFascicoloException("Informazioni rimozione sospensione assenti o incomplete");
		}
		FascicoloModel fascicolo = fascicoloDao.findByCuaaAndIdValidazione(sospensioneDto.getCuaa(), 0).orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));

		if (!StatoFascicoloEnum.SOSPESO.equals(fascicolo.getStato())) {
			throw new MovimentazioneFascicoloException("Il fascicolo non è sospeso");
		}

		fascicoloDao.save(fascicolo
				.ripristinaStato(StatoFascicoloEnum.IN_AGGIORNAMENTO)
				.setDataModifica(LocalDate.now())
				.setUtenteModifica(utenteComponent.username()));

		// devo chiudere la precedente sospensione
		MovimentazioneModel sospensione = movimentazioneDao.findByFascicoloAndDataFineIsNullAndTipo(sospensioneDto.getCuaa(), TipoMovimentazioneFascicolo.SOSPENSIONE).orElseThrow(() -> new MovimentazioneFascicoloException("Successione sospensioni non corretta"));
		movimentazioneDao.save(sospensione
				.setDataFine(sospensioneDto.getDataFine())
				.setMotivazioneFine(sospensioneDto.getMotivazioneFine())
				.setUtente(utenteComponent.username()));
	}

}
