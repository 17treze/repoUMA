package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiPassoLavorazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.OpzioniImportoMinimo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CambioStatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaboraPassoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ControlloIntersostegnoService extends CambioStatoIstruttoria implements ElaborazioneIstruttoria {

	private static final Logger logger = LoggerFactory.getLogger(ControlloIntersostegnoService .class);
	
	@Autowired
	private PassoImportoMinimoService importoMinimoService;
	@Autowired
	private IstruttoriaComponent istruttoriaComponent;
	@Autowired
	private InizializzaDatiDomanda inizializzatoreDatiDomanda;
	@Autowired
	private InizializzaVariabiliInputIntersostegno inizializzatoreVariabili;
	@Autowired
	private InizializzaDatiIntersostegnoFactory inizializzatoreDatiIntersostegnoFactory;
	@Autowired
	private TransizioneIstruttoriaDao transizioneIstruttoriaDao;
	
	
	@Autowired
	private PassoControlloIntersostegnoService controlloIntersostegnoService;
	@Autowired
	private PassoImportoMinimoAntimafiaService importoMinimoAntimafiaService;
	
	@Autowired
	private Bridu50ControlloAntimafiaFactory esitoAntimafiaFactory;
	
	@Autowired
	private PassoDisciplinaFinanziariaFactory passoDisciplinaFactory;
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	
	private static Map<OpzioniImportoMinimo, Foglia> statiImportoMinimoKO = new EnumMap<>(OpzioniImportoMinimo.class);
	
	private static EsitoControllo IMPORTO_MINIMO_NON_RAGGIUNTO =  
			new EsitoControllo(TipoControllo.importoMinimoPagamento, false, OpzioniImportoMinimo.NON_RAGGIUNTO.name());
	private static EsitoControllo IMPORTO_MINIMO_NON_DETERMINABILE =  
			new EsitoControllo(TipoControllo.importoMinimoPagamento, false, OpzioniImportoMinimo.NON_DETERMINABILE.name());
	private static EsitoControllo IMPORTO_MINIMO_RAGGIUNTO =  
			new EsitoControllo(TipoControllo.importoMinimoPagamento, false, OpzioniImportoMinimo.RAGGIUNTO.name());
	private static EsitoControllo BRIDUSDS050_ESITO_ANTIMAFIA_OK =  
			new EsitoControllo(TipoControllo.BRIDUSDS050_esitoAntimafia, true);
	private static EsitoControllo BRIDUSDS050_ESITO_ANTIMAFIA_KO =  
			new EsitoControllo(TipoControllo.BRIDUSDS050_esitoAntimafia, false);
	// questo
	private static EsitoControllo IMPORTO_MINIMO_ANTIMAFIA_NON_DETERMINABILE =  
			new EsitoControllo(TipoControllo.importoMinimoAntimafia, false, OpzioniImportoMinimo.NON_DETERMINABILE.name());
	private static EsitoControllo IMPORTO_MINIMO_ANTIMAFIA_RAGGIUNTO =  
			new EsitoControllo(TipoControllo.importoMinimoAntimafia, false, OpzioniImportoMinimo.RAGGIUNTO.name());
	private static EsitoControllo IMPORTO_MINIMO_ANTIMAFIA_NON_RAGGIUNTO =  
			new EsitoControllo(TipoControllo.importoMinimoAntimafia, true, OpzioniImportoMinimo.NON_RAGGIUNTO.name());
	
	private static class Foglia {
		
		private static Foglia FOGLIA_35 = new Foglia(CodiceEsito.DUF_035,
				StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO, Arrays.asList(IMPORTO_MINIMO_NON_DETERMINABILE));
		private static Foglia FOGLIA_28 = new Foglia(CodiceEsito.DUF_028, StatoIstruttoria.NON_LIQUIDABILE,
				Arrays.asList(IMPORTO_MINIMO_NON_RAGGIUNTO));
		private static Foglia FOGLIA_31 = new Foglia(CodiceEsito.DUF_031, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK,
				Arrays.asList(IMPORTO_MINIMO_RAGGIUNTO, BRIDUSDS050_ESITO_ANTIMAFIA_OK));
		private static Foglia FOGLIA_30_ANTIMAFIA_RAGGIUNTO = new Foglia(CodiceEsito.DUF_030, StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO,
				Arrays.asList(IMPORTO_MINIMO_RAGGIUNTO, BRIDUSDS050_ESITO_ANTIMAFIA_KO, IMPORTO_MINIMO_ANTIMAFIA_RAGGIUNTO));
		private static Foglia FOGLIA_30_ANTIMAFIA_NON_DETERMINABILE = new Foglia(CodiceEsito.DUF_030, StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO,
				Arrays.asList(IMPORTO_MINIMO_RAGGIUNTO, BRIDUSDS050_ESITO_ANTIMAFIA_KO, IMPORTO_MINIMO_ANTIMAFIA_NON_DETERMINABILE));
		private static Foglia FOGLIA_33 = new Foglia(CodiceEsito.DUF_033, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK,
				Arrays.asList(IMPORTO_MINIMO_RAGGIUNTO, BRIDUSDS050_ESITO_ANTIMAFIA_KO, IMPORTO_MINIMO_ANTIMAFIA_NON_RAGGIUNTO));
		private static Foglia FOGLIA_33_ANTIMAFIA_NON_DETERMINABILE = new Foglia(CodiceEsito.DUF_033, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK,
				Arrays.asList(IMPORTO_MINIMO_RAGGIUNTO, BRIDUSDS050_ESITO_ANTIMAFIA_KO, IMPORTO_MINIMO_ANTIMAFIA_NON_RAGGIUNTO));
		
		public Foglia(CodiceEsito esito, StatoIstruttoria statoWF, List<EsitoControllo> controlli) {
			super();
			this.esito = esito;
			this.statoWF = statoWF;
			this.controlli = controlli;
		}
		private CodiceEsito esito;
		private StatoIstruttoria statoWF;
		private List<EsitoControllo> controlli;
	}
	static {
		statiImportoMinimoKO.put(OpzioniImportoMinimo.NON_DETERMINABILE, Foglia.FOGLIA_35);
		statiImportoMinimoKO.put(OpzioniImportoMinimo.NON_RAGGIUNTO, Foglia.FOGLIA_28);
	}
	

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ElaborazioneIstruttoriaException.class)
	public void elabora(Long idIstruttoria) throws ElaborazioneIstruttoriaException {
		logger.debug("Elabora istruttoria {}", idIstruttoria);
		IstruttoriaModel istruttoria = istruttoriaComponent.load(idIstruttoria);
		elabora(istruttoria);
	}
	
	protected void elabora(IstruttoriaModel istruttoria) throws ElaborazioneIstruttoriaException {
		try {
			DatiDomanda dati = inizializzatoreDatiDomanda.apply(istruttoria.getDomandaUnicaModel());
			List<VariabileCalcolo> variabiliInput = new ArrayList<VariabileCalcolo>();
			inizializzatoreVariabili.accept(variabiliInput, dati);
			InizializzaDatiIntersostegno inizializzatoreDatiIntersostegno = inizializzatoreDatiIntersostegnoFactory
					.getInizializzatoreDatiIntersostegno(
							"InizializzaDatiIntersostegno_" + istruttoria.getSostegno());
			DatiLavorazioneControlloIntersostegno datiLavorazioneIntersostegno =
					inizializzatoreDatiIntersostegno.trasforma(dati);
			TransizioneIstruttoriaModel transizione = 
					cleanCreateTransizione(istruttoria);
			datiLavorazioneIntersostegno.setTransizione(transizione);
			OpzioniImportoMinimo checkImportoMinimo = 
					importoMinimoService.elaboraPasso(variabiliInput, datiLavorazioneIntersostegno);
			Foglia foglia = null;
			if (isImportoMinimoRaggiunto(checkImportoMinimo)) { // proseguo con i controlli
				Bridu50ControlloAntimafia esitoAntimafia = esitoAntimafiaFactory.getBridu50ControlloAntimafia(
						"Bridu50ControlloAntimafia_" + istruttoria.getSostegno());
				Boolean esitoAntimafiaOK = esitoAntimafia.isDocumentazioneAntimafiaInRegola(istruttoria);
				Boolean applicoAlgoritmoDisciplina = esitoAntimafiaOK;
				OpzioniImportoMinimo checkImportoMinimoAntimafia = null;
				if (!Boolean.TRUE.equals(applicoAlgoritmoDisciplina)) {
					checkImportoMinimoAntimafia = 
							importoMinimoAntimafiaService.elaboraPasso(variabiliInput, datiLavorazioneIntersostegno);
					applicoAlgoritmoDisciplina = isAntimafiaNonNecessaria(checkImportoMinimoAntimafia);
				}
				if (Boolean.TRUE.equals(applicoAlgoritmoDisciplina)) {
					algoritmoDisciplina(transizione);
				}
				final OpzioniImportoMinimo risultatoControlloImportoAntimafia = checkImportoMinimoAntimafia;
				final Boolean disciplinaApplicata = applicoAlgoritmoDisciplina;
				foglia = 
				Optional.ofNullable(esitoAntimafiaOK)
					.filter(Boolean::booleanValue)
					.map(e -> Foglia.FOGLIA_31)
					.orElseGet(() -> Optional.of(disciplinaApplicata)
							.filter(Boolean::booleanValue)
							.map(e -> Foglia.FOGLIA_33)
							.orElseGet(() -> Optional.ofNullable(risultatoControlloImportoAntimafia)
									.filter(importoAntimafia -> OpzioniImportoMinimo.RAGGIUNTO.equals(importoAntimafia))
									.map(antimafia -> Foglia.FOGLIA_30_ANTIMAFIA_RAGGIUNTO)
									.orElse(Foglia.FOGLIA_33_ANTIMAFIA_NON_DETERMINABILE)));
				
				
			} else { // stati finali
				foglia = statiImportoMinimoKO.get(checkImportoMinimo);
			}
			controlloIntersostegnoService.elaboraPasso(variabiliInput, foglia, transizione);
			cambiaStato(transizione, foglia.statoWF);
			transizione.setA4gdStatoLavSostegno1(loadNuovoStato(foglia.statoWF));
			transizioneIstruttoriaDao.save(transizione);
		} catch (Exception e) {
			logger.error("Errore imprevisto eseguendo i controlli intersostegno per l'istruttoria {}", istruttoria.getId(), e);
			throw new ElaborazioneIstruttoriaException("Errore elaborando intersostegno per istruttoria " + istruttoria.getId(), e);
		} 
	}
	
	protected void algoritmoDisciplina(TransizioneIstruttoriaModel transizione) throws Exception {
		Integer anno = transizione.getIstruttoria().getDomandaUnicaModel().getCampagna();
		if(anno.compareTo(2020)>=0){
			PassoDisciplinaFinanziaria2020 passoDisciplina2020 = passoDisciplinaFactory.getPassoDisciplinaFinanziaria2020(
					"PassoDisciplinaFinanziaria2020_" + transizione.getIstruttoria().getSostegno());
			passoDisciplina2020.elaboraPasso(transizione);
		}else {
			PassoDisciplinaFinanziaria passoDisciplina = passoDisciplinaFactory.getPassoDisciplinaFinanziaria(
					"PassoDisciplinaFinanziaria_" + transizione.getIstruttoria().getSostegno());
			passoDisciplina.elaboraPasso(transizione);
		}
	}
	
	protected boolean isAntimafiaNonNecessaria(OpzioniImportoMinimo checkImportoMinimo) {
		return OpzioniImportoMinimo.NON_RAGGIUNTO.equals(checkImportoMinimo);
	}

	protected boolean isImportoMinimoRaggiunto(OpzioniImportoMinimo checkImportoMinimo) {
		return OpzioniImportoMinimo.RAGGIUNTO.equals(checkImportoMinimo);
	}

	private TransizioneIstruttoriaModel cleanCreateTransizione(IstruttoriaModel istruttoria) {
		// Eliminazione transizioni precedenti
		List<TransizioneIstruttoriaModel> vecchieTransizioni =
				transizioneIstruttoriaDao.findTransizioneControlloIntersostegno(istruttoria);
		if (vecchieTransizioni != null) {
			vecchieTransizioni.stream()
			.forEach(transizionePrecedente -> {
				istruttoria.getTransizioni().remove(transizionePrecedente);
				transizionePrecedente.setIstruttoria(null);
				// dovrebbe cancellare anche passi e anomalie tramite il cascade remove
				transizioneIstruttoriaDao.delete(transizionePrecedente);
			});
		}

		// inizializzazione transizione
		TransizioneIstruttoriaModel transizione = avviaTransizione(istruttoria);
		//

		return transizione;
	}

	@Service
	public static class PassoControlloIntersostegnoService extends ElaboraPassoIstruttoria {
		
		protected void elaboraPasso(List<VariabileCalcolo> variabiliInput, Foglia foglia, TransizioneIstruttoriaModel transizione) 
			throws Exception {
			DatiPassoLavorazione passo = new DatiPassoLavorazione();
			passo.setIdTransizione(transizione.getId());
			passo.setPasso(getPasso());
			passo.getDatiInput().setVariabiliCalcolo(variabiliInput);
			passo.getDatiSintesi().getEsitiControlli().addAll(foglia.controlli);
			passo.setCodiceEsito(foglia.esito.getCodiceEsito());
			passo.setEsito(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.equals(foglia.statoWF));
			
			salvaPassoLavorazioneSostegno(passo);
		}

		@Override
		public TipologiaPassoTransizione getPasso() {
			return TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO;
		}
		
	}
}
