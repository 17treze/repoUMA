package it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione;

import it.tndigitale.a4g.proxy.client.model.DatiPagamentiDto;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.service.SincronizzazioneAgeaService;
import it.tndigitale.a4gistruttoria.strategy.SincronizzazioneInputData;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipologiaSincronizzazioneAGEA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GeneraDatiSincronizzazionePagamenti 
	extends GeneraDatiSincronizzazioneBase<TipologiaSincronizzazioneAGEA> {

	private static final Logger logger = LoggerFactory.getLogger(GeneraDatiSincronizzazionePagamenti.class);
	
	private static Map<String, Integer> INTERVENTO_CODICE_AGEA;
	static {
		INTERVENTO_CODICE_AGEA = new HashMap<>();
		INTERVENTO_CODICE_AGEA.put("310", 310);
		INTERVENTO_CODICE_AGEA.put("311", 311);
		INTERVENTO_CODICE_AGEA.put("313", 313);
		INTERVENTO_CODICE_AGEA.put("315", 315);
		INTERVENTO_CODICE_AGEA.put("316", 316);
		INTERVENTO_CODICE_AGEA.put("318", 318);
		INTERVENTO_CODICE_AGEA.put("321", 321);
		INTERVENTO_CODICE_AGEA.put("322", 322);
		INTERVENTO_CODICE_AGEA.put("M8", 122);
		INTERVENTO_CODICE_AGEA.put("M9", 124);
		INTERVENTO_CODICE_AGEA.put("M10", 123);
		INTERVENTO_CODICE_AGEA.put("M11", 125);
		INTERVENTO_CODICE_AGEA.put("M14", 128);
		INTERVENTO_CODICE_AGEA.put("M15", 129);
		INTERVENTO_CODICE_AGEA.put("M16", 132);
		INTERVENTO_CODICE_AGEA.put("M17", 138);
		INTERVENTO_CODICE_AGEA.put(CodiceInterventoAgs.BPS.name(), 26);
		INTERVENTO_CODICE_AGEA.put(CodiceInterventoAgs.GREE.name(), 204);
		INTERVENTO_CODICE_AGEA.put(CodiceInterventoAgs.GIOV.name(), 300);
	}
	
	@Autowired
	private SincronizzazioneAgeaService sincronizzazioneAgeaService;

	@Autowired
	private PassoLavorazioneComponent passoLavorazioneComponent;


	@Autowired
	private PagamentoDelegatorMethodFactory delegatorFactory;
	
	@Override
	public TipologiaSincronizzazioneAGEA getTipoDatoAnnuale() {
		return TipologiaSincronizzazioneAGEA.SINCRONIZZAZIONE_PAGAMENTI;
	}

	@Override
	public void cancellaDatiEsistenti(TipologiaSincronizzazioneAGEA tipoDatoAnnuale, Integer annoCampagna) {
		try {
			sincronizzazioneAgeaService.pulisciDatiPagamenti(annoCampagna);
		} catch (Exception e) {
			logger.error("Errore cancellazione dati esistenti durante la sincronizzazione AGEA per il processo {} anno {}", tipoDatoAnnuale.name(), annoCampagna);
		}
	}

	@Override
	public void generaDatiPerIstruttoria(IstruttoriaModel istruttoria, Integer annoCampagna) {
		try {            
			PagamentiData inputData = caricaDatiInput(istruttoria);
			List<DatiPagamentiDto> datiPagamentiPerDomanda = recuperaDatiPagamentoPerDomanda(inputData);
			datiPagamentiPerDomanda.forEach(sincronizzazioneAgeaService::creaDatiPagamenti);
		} catch (Exception e) {
			logger.error("Impossibile generare le informazioni di sincronizzazione per l'istruttoria con id ".concat(istruttoria.getId().toString()), e);
		}
	}

	@Override
	protected void addIstruttorie(DomandaUnicaModel domanda, List<IstruttoriaModel> istruttorie) {
		for (Sostegno tipoSostegno :  Sostegno.values()) {
			IstruttoriaModel istruttoria = istruttoriaComponent.getUltimaIstruttoriaPagamenti(domanda, tipoSostegno);
			if(istruttoria != null) {
				istruttorie.add(istruttoria);
			}
			
		}
	}

	@Override
	protected PagamentiData caricaDatiInput(IstruttoriaModel istruttoria) {
		PagamentiData result = new PagamentiData();

		try {
			// Impostazione dati domanda
			result = SincronizzazioneInputData.from(istruttoria.getDomandaUnicaModel(), PagamentiData.class);
			result.setIstruttoria(istruttoria);

			Optional<List<PassoTransizioneModel>> passi = passoLavorazioneComponent.recuperaPassiLavorazioneIstruttoria(istruttoria);

			if (passi.isPresent())
				result.setPassiLavorazioneTransizione(passi.get());

			PagamentoDelegator delegator = delegatorFactory.getDelegator(istruttoria.getSostegno());
			
			// Impostazione variabili calcolo da precaricare
			result.setVariabiliCalcolo(delegator.recuperaValoriVariabili(result.getPassiLavorazioneTransizione()));
			delegator.caricaPagamenti(result);

		} catch (Exception e) {
			logger.warn("Errore recupero dati input per istruttoria ".concat(istruttoria.getId().toString()), e);
		}
		return result;
	}

	private List<DatiPagamentiDto> recuperaDatiPagamentoPerDomanda(PagamentiData inputData) {
		List<DatiPagamentiDto> datiPagamentiPerDomanda = new ArrayList<>();
		inputData.getDataItems().forEach((k, v) -> datiPagamentiPerDomanda.add(build(inputData, k)));
		return datiPagamentiPerDomanda;
	}
	
	private DatiPagamentiDto build(PagamentiData inputData, String intervento) {
		return inputData.getDataItem(intervento).map(item -> {
			DatiPagamentiDto result = new DatiPagamentiDto();
			result.setAnnoCampagna(inputData.getAnnoCampagna());
			result.setCuaa(inputData.getCuaaIntestatario());
			result.setNumeroDomanda(inputData.getNumeroDomanda().toString());
			result.setCodiceIntervento(Long.valueOf(INTERVENTO_CODICE_AGEA.get(intervento)));
			result.setNumeroProgressivoLavorazione(inputData.getIstruttoria().getId());
			result.setImportoDeterminato(item.getImportoDeterminato());
			result.setImportoLiquidato(item.getImportoLiquidato());
			result.setImportoRichiesto(item.getImportoRichiesto());
			result.setPagamentoAutorizzato(item.isPagamentoAutorizzato());
			return result;
		}).orElseThrow(IllegalArgumentException::new);
	}

	@Component
	private class PagamentoDelegatorMethodFactory {
		@Autowired
		private PagamentoDisaccoppiatoDelegator delegatorDisaccoppiato;

		@Autowired
		private PagamentoSuperficieDelegator delegatorSuperficie;

		@Autowired
		private PagamentoZootecniaDelegator delegatorZootecnia;

		private PagamentoDelegator getDelegator(Sostegno sostegno) {
			if(Sostegno.DISACCOPPIATO.equals(sostegno))
				return delegatorDisaccoppiato;
			if(Sostegno.ZOOTECNIA.equals(sostegno))
				return delegatorZootecnia;
			if(Sostegno.SUPERFICIE.equals(sostegno))
				return delegatorSuperficie;

			return null;
		}
	}


}
