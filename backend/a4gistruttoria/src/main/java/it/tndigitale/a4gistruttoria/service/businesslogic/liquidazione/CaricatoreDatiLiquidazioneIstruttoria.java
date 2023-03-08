package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.CapitoloSpesaDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElencoLiquidazioneException;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.Beneficiario;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.VoceSpesa;
import it.tndigitale.a4gistruttoria.util.*;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public abstract class CaricatoreDatiLiquidazioneIstruttoria {

	private static final Logger logger = LoggerFactory.getLogger(CaricatoreDatiLiquidazioneIstruttoria.class);
	
	public static final String PREFISSO_CARICATORE_DATI_QUALIFIER = "CaricatoreDati_";
	
	@Autowired
	private BeneficiarioLiquidazioneIstruttoriaService beneficiarioService;	
	@Autowired
	private TransizioneIstruttoriaDao daoTransizionesostegno;
	@Autowired
	private VariabileCalcoloUtils utilsVariabileCalcolo;
	@Autowired
	private CapitoloSpesaDao capitoloDao;
	
	protected DatiLiquidazioneIstruttoria creaRecordElenco(IstruttoriaModel istruttoria) throws ElencoLiquidazioneException {
		DatiLiquidazioneIstruttoria datiLiquidazione = new DatiLiquidazioneIstruttoria();
		popolaDatiDomanda(istruttoria.getDomandaUnicaModel(), datiLiquidazione);
		datiLiquidazione.setBeneficiario(creaDatiBeneficiario(istruttoria));
		popolaTipoPagamento(istruttoria, datiLiquidazione);
		popolaImportoTotale(istruttoria, datiLiquidazione);
		popolaVociSpesa(istruttoria, datiLiquidazione);
		return datiLiquidazione;
	}
	
	protected abstract void popolaTipoPagamento(IstruttoriaModel istruttoria, DatiLiquidazioneIstruttoria datiLiquidazione) throws ElencoLiquidazioneException ;
	protected abstract void popolaImportoTotale(IstruttoriaModel istruttoria, DatiLiquidazioneIstruttoria datiLiquidazione) throws ElencoLiquidazioneException ;
	protected abstract void popolaVociSpesa(IstruttoriaModel istruttoria, DatiLiquidazioneIstruttoria datiLiquidazione) throws ElencoLiquidazioneException ;

	protected void popolaDatiDomanda(DomandaUnicaModel domanda, DatiLiquidazioneIstruttoria datiLiquidazione) throws ElencoLiquidazioneException {
		datiLiquidazione.setNumeroDomanda(domanda.getNumeroDomanda().longValue());
		datiLiquidazione.setAnnoCampagna(domanda.getCampagna());
		datiLiquidazione.setDataProtocollazione(LocalDateConverter.fromDate(domanda.getDtProtocollazione()));
	}

	protected Beneficiario creaDatiBeneficiario(IstruttoriaModel istruttoria) throws ElencoLiquidazioneException {
		return beneficiarioService.creaDatiBeneficiario(istruttoria);
	}
	
	protected BigDecimal recuperaVariabileDisciplinaFinanziaria(IstruttoriaModel istruttoria, TipoVariabile variabile) throws ElencoLiquidazioneException{
		List<TransizioneIstruttoriaModel> transizioneControlliIntersostegno = 
				daoTransizionesostegno.findTransizioneControlloIntersostegno(istruttoria);
		
		if (transizioneControlliIntersostegno.isEmpty() || transizioneControlliIntersostegno.size() > 1) {
			String errorMessage = 
				"Impossibile recuperare la transizione di controlli intersostegno per la domanda ".concat(istruttoria.getId().toString());
			throw new ElencoLiquidazioneException(errorMessage);
		}
		TransizioneIstruttoriaModel transizione = transizioneControlliIntersostegno.get(0);
		try {
			VariabileCalcolo variabileCalcolo = utilsVariabileCalcolo.recuperaVariabileCalcolata(transizione, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, variabile);
			if (variabileCalcolo == null)
				return BigDecimal.ZERO;
			else
				return variabileCalcolo.getValNumber();
		} catch (NotFoundException | IOException e) {
			logger.error("Errore calcolando la variabile {} nel passo disciplina per l'istruttoria {}", variabile, istruttoria.getId(), e);
			throw new ElencoLiquidazioneException(e.getMessage());
		}
	}

	protected BigDecimal recuperaVariabileCalcolo(IstruttoriaModel istruttoria, TipologiaPassoTransizione passo, TipoVariabile variabile) throws ElencoLiquidazioneException{
		List<TransizioneIstruttoriaModel> transizioneCalcolo = 
				daoTransizionesostegno.findTransizioneCalcoloPremio(istruttoria);
		
		if (transizioneCalcolo.isEmpty()) {
			String errorMessage = 
				"Transizione di calcolo non trovata per l'istruttoria ".concat(istruttoria.getId().toString());
			throw new ElencoLiquidazioneException(errorMessage);
		}
		TransizioneIstruttoriaModel transizione = 
				transizioneCalcolo.stream().max(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione)).get();
		return recuperaVariabileCalcolata(transizione, passo, variabile);
	}
	
	protected BigDecimal recuperaVariabileCalcolata(TransizioneIstruttoriaModel transizione, TipologiaPassoTransizione passo, TipoVariabile variabile) throws ElencoLiquidazioneException {
		try {
			VariabileCalcolo variabileCalcolo = utilsVariabileCalcolo.recuperaVariabileCalcolata(transizione, passo, variabile);
			if (variabileCalcolo == null)
				return BigDecimal.ZERO;
			else
				return variabileCalcolo.getValNumber();
		} catch (NotFoundException | IOException e) {
			logger.error("Errore calcolando la variabile {} nel passo {} per l'istruttoria {}", variabile, passo, transizione.getIstruttoria().getId(), e);
			throw new ElencoLiquidazioneException(e.getMessage());
		}
	}

	protected VoceSpesa caricaCapitolo(Integer anno, TipoCapitoloSpesa tipo, CodiceInterventoAgs intervento) {
		CapitoloSpesaModel capitolo = capitoloDao.findByAnnoTipoIntervento(anno, tipo, intervento);
		if (capitolo == null) {
			logger.error("Capitolo non trovato per anno {} e intervento {}", anno, intervento);
			throw new EntityNotFoundException("Capitolo non trovato per anno " + anno + " e intervento " + intervento);
		}
		VoceSpesa result = new VoceSpesa();
		result.setCapitolo(capitolo.getCapitolo());
		result.setCodiceProdotto(capitolo.getCodiceProdotto().doubleValue());
		return result;
	}
	
	protected VoceSpesa getVoceVuota() {
		VoceSpesa result = new VoceSpesa();
		result.setCapitolo("");
		result.setCodiceProdotto(null);
		result.setQuantita(null);
		result.setImporto(null);
		return result;
	}
}
