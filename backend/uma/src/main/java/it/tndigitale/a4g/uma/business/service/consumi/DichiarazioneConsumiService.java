package it.tndigitale.a4g.uma.business.service.consumi;

import java.time.LocalDate;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

// import it.tndigitale.a4g.fascicolo.anagrafica.client.model.DetenzioneAgsDto.TipoDetenzioneEnum;
// import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.Ruoli;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.repository.AllegatiConsuntivoDao;
import it.tndigitale.a4g.uma.business.persistence.repository.ClienteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.ConsumiClientiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.ConsuntiviConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.FattureClientiDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.consumi.calcoli.CarburanteDecimal;
import it.tndigitale.a4g.uma.business.service.consumi.calcoli.DichiarazioneConsumiCarburanteComponent;
import it.tndigitale.a4g.uma.dto.aual.FascicoloAualDto;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiPatch;
import it.tndigitale.a4g.uma.dto.richiesta.PresentaRichiestaDto;

@Service
public class DichiarazioneConsumiService {

	private static final String DICHIARAZIONE_NOT_FOUND = "Nessuna Dichiarazione Consumi trovata per id ";
	private static final Logger logger = LoggerFactory.getLogger(DichiarazioneConsumiService.class);

	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private ConsuntiviConsumiDao consuntiviConsumiDao;
	@Autowired
	private AllegatiConsuntivoDao allegatiConsuntivoDao;
	@Autowired
	private ConsumiClientiDao consumiClientiDao;
	@Autowired
	private FattureClientiDao fattureClientiDao;
	@Autowired
	private ClienteDao clienteDao;
	@Autowired
	private DichiarazioneConsumiValidator dichiarazioneConsumiValidator;
	@Autowired
	private DichiarazioneConsumiCarburanteComponent dichiarazioneConsumiCarburanteComponent;
	@Autowired
	private Clock clock;
	@Autowired
	private UtenteComponent utenteComponent;
	@Autowired
	private UmaAnagraficaClient anagraficaClient;

	public Long presentaDichiarazione(PresentaRichiestaDto presentaRichiestaDto) {
		RichiestaCarburanteModel richiesta = dichiarazioneConsumiValidator.validaPresentazione(presentaRichiestaDto.getCuaa());

		var primoNovembreAnnoCampagna = Clock.ofEndOfDay(LocalDate.of(Integer.valueOf(richiesta.getCampagna().toString()), 11, 1));
		var dataConduzione = primoNovembreAnnoCampagna.isAfter(clock.now()) ? clock.now() : primoNovembreAnnoCampagna;

		FascicoloAualDto fascicoloAual = anagraficaClient.getFascicolo(presentaRichiestaDto.getCuaa());

		// Reperisco la detenzione del fascicolo ags
		var det = fascicoloAual.getDescDete();
		
		DichiarazioneConsumiModel dichiarazioneDaSalvare = new DichiarazioneConsumiModel()
				.setRichiestaCarburante(richiesta)
				.setCfRichiedente(presentaRichiestaDto.getCodiceFiscaleRichiedente())
				.setDataPresentazione(clock.now())
				.setDataConduzione(dataConduzione)
				.setStato(StatoDichiarazioneConsumi.IN_COMPILAZIONE)
				.setEntePresentatore(det);

		DichiarazioneConsumiModel dichiarazioneSalvata = dichiarazioneConsumiDao.save(dichiarazioneDaSalvare);

		logger.info("Creazione dichiarazione consumi - CUAA: {} RICHIEDENTE: {}", dichiarazioneSalvata.getRichiestaCarburante().getCuaa(), dichiarazioneSalvata.getCfRichiedente());
		return dichiarazioneSalvata.getId();
	}

	@Transactional
	public void aggiornaDichiarazione(Long idConsumi, DichiarazioneConsumiPatch dichiarazioneConsumiPatch) {
		var dichiarazioneConsumi = dichiarazioneConsumiDao.findById(idConsumi).orElseThrow(() -> new EntityNotFoundException(DICHIARAZIONE_NOT_FOUND.concat(String.valueOf(idConsumi))));

		/* solo caso istruttore uma/appag */
		if (utenteComponent.haRuolo(Ruoli.ISTRUTTORE_UMA)) {
			logger.info("Aggiornamento data di conduzione dichiarazione {} alla data {}", idConsumi, dichiarazioneConsumiPatch.getDataConduzione());
			dichiarazioneConsumi.setDataConduzione(dichiarazioneConsumiPatch.getDataConduzione());
		} else { // operatore caa setta accisa e valida
			dichiarazioneConsumi.setMotivazioneAccisa(dichiarazioneConsumiPatch.getMotivazioneAccisa());
			dichiarazioneConsumiValidator.validaMotivazioneAccisa(dichiarazioneConsumi);
		} 

		dichiarazioneConsumiDao.save(dichiarazioneConsumi);
		logger.info("Aggiornamento dichiarazione consumi id {}", idConsumi);
	}

	public CarburanteDto calcolaCarburanteAmmissibile(Long id) {
		DichiarazioneConsumiModel dichiarazione = dichiarazioneConsumiDao.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Nessuna Dichiarazione Consumi trovata con id %s", id)));
		RichiestaCarburanteModel richiesta = dichiarazione.getRichiestaCarburante();

		var dataConduzione = dichiarazione.getDataConduzione();

		dataConduzione = dataConduzione.isAfter(dichiarazione.getDataPresentazione()) ? dichiarazione.getDataPresentazione() : dataConduzione;

		return new CarburanteDecimal()
				.add(dichiarazioneConsumiCarburanteComponent.calcolaSuperfici(richiesta, dataConduzione))
				.add(dichiarazioneConsumiCarburanteComponent.calcolaFabbricati(richiesta, dataConduzione))
				.add(dichiarazioneConsumiCarburanteComponent.calcolaAltre(richiesta))
				.round()
				.build();
	}

	@Transactional
	public void valida(Long id) {
		DichiarazioneConsumiModel dichiarazioneConsumi = dichiarazioneConsumiDao.findById(id).orElseThrow(() -> new EntityNotFoundException(DICHIARAZIONE_NOT_FOUND.concat(String.valueOf(id))));
		dichiarazioneConsumiValidator.validaProtocollazione(dichiarazioneConsumi);
	}

	@Transactional
	public void deleteDichiarazioneConsumi(Long id) {
		DichiarazioneConsumiModel dichiarazioneConsumi = dichiarazioneConsumiDao.findById(id).orElseThrow(() -> new EntityNotFoundException(DICHIARAZIONE_NOT_FOUND.concat(String.valueOf(id))));

		if (!CollectionUtils.isEmpty(dichiarazioneConsumi.getClienti())) {
			// Delete Consumi Clienti + Fatture Clienti
			dichiarazioneConsumi.getClienti().forEach(cliente -> {
				consumiClientiDao.deleteByCliente_id(cliente.getId());
				fattureClientiDao.deleteByCliente_id(cliente.getId());
			});
			// Delete Clienti
			clienteDao.deleteByDichiarazioneConsumi_id(id);
		}

		if (!CollectionUtils.isEmpty(dichiarazioneConsumi.getConsuntivi())) {
			// Delete allegati consuntivi + consuntivi
			dichiarazioneConsumi.getConsuntivi().forEach(consuntivo -> allegatiConsuntivoDao.deleteByConsuntivoModel_id(consuntivo.getId()));
			consuntiviConsumiDao.deleteByDichiarazioneConsumi_id(id);
		}
		// Delete dichiarazione
		dichiarazioneConsumiDao.deleteById(id);
		logger.info("[UMA] - Cancellazione Dichiarazione Consumi CUAA {} , anno {} " , dichiarazioneConsumi.getRichiestaCarburante().getCuaa(), dichiarazioneConsumi.getRichiestaCarburante().getCampagna());
	}

}
