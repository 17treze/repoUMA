package it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.proxy.client.model.SuperficiAccertateDto;
import it.tndigitale.a4g.proxy.client.model.SuperficiAccertateDto.MotivazioneA1Enum;
import it.tndigitale.a4g.proxy.client.model.SuperficiAccertateDto.MotivazioneA2Enum;
import it.tndigitale.a4g.proxy.client.model.SuperficiAccertateDto.MotivazioneA3Enum;
import it.tndigitale.a4g.proxy.client.model.SuperficiAccertateDto.MotivazioneB0Enum;
import it.tndigitale.a4g.proxy.client.model.SuperficiAccertateDto.MotivazioneB1Enum;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.InterventoDisaccoppiato;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.SincronizzazioneAgeaService;
import it.tndigitale.a4gistruttoria.strategy.SincronizzazioneInputData;
import it.tndigitale.a4gistruttoria.strategy.SuperficiAccertateInputData;
import it.tndigitale.a4gistruttoria.util.ConversioniCalcoli;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.TipologiaSincronizzazioneAGEA;

@Component
public class GeneraDatiSincronizzazioneSuperficiAccertate 
	extends GeneraDatiSincronizzazioneBase<TipologiaSincronizzazioneAGEA> {
	
	@Autowired
	private RichiestaSuperficieDao richiestaSuperficieDao;
	@Autowired
	private JsonTranslator translator;
	
	@Autowired
	private SincronizzazioneAgeaService sincronizzazioneAgeaService;

	@Autowired
	private PassoLavorazioneComponent passoLavorazioneComponent;

	private static final Logger logger = LoggerFactory.getLogger(GeneraDatiSincronizzazioneSuperficiAccertate.class);
	
	@Override
	public TipologiaSincronizzazioneAGEA getTipoDatoAnnuale() {
		return TipologiaSincronizzazioneAGEA.SINCRONIZZAZIONE_SUPERFICI_ACCERTATE;
	}
		
	@Override
	public void cancellaDatiEsistenti(TipologiaSincronizzazioneAGEA tipoDatoAnnuale, Integer annoCampagna) {
		try {
			sincronizzazioneAgeaService.pulisciDatiSuperficiAccertate(annoCampagna);
		} catch (Exception e) {
			logger.error("Errore cancellazione dati esistenti durante la sincronizzazione AGEA per il processo {} anno {}", tipoDatoAnnuale.name(), annoCampagna);
		}
	}

	@Override
	public void generaDatiPerIstruttoria(IstruttoriaModel istruttoria, Integer annoCampagna) {
		try {
			SuperficiAccertateInputData inputData = this.caricaDatiInput(istruttoria);
		    sincronizzazioneAgeaService.creaDatiSuperficiAccertate(build(inputData));
		} catch (Exception e) {
			logger.error("Impossibile generare le informazioni di sincronizzazione per l'istruttoria con id ".concat(istruttoria.getId().toString()), e);
		}
	}
	
	@Override
	protected SuperficiAccertateInputData caricaDatiInput(IstruttoriaModel istruttoria) {
		SuperficiAccertateInputData result = SincronizzazioneInputData.from(istruttoria.getDomandaUnicaModel(), SuperficiAccertateInputData.class);
		result.setStatoLavorazioneSostegno(
				StatoIstruttoria.valueOfByIdentificativo(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()));
		result.setPassiLavorazioneEntities(
				passoLavorazioneComponent.recuperaPassiLavorazioneIstruttoria(istruttoria).get());
		result.setVariabiliCalcolo(recuperaValoriVariabili(result.getPassiLavorazioneEntities()));
		return result;
	}

	private Map<TipoVariabile, VariabileCalcolo> recuperaValoriVariabili(List<PassoTransizioneModel> passiBps) {
		EnumMap<TipoVariabile, VariabileCalcolo> result = new EnumMap<>(TipoVariabile.class);
		try {
			result.put(TipoVariabile.BPSSUPDETIST, translator.getVariabile(TipoVariabile.BPSSUPDETIST, passiBps, TipologiaPassoTransizione.RIDUZIONI_BPS).orElse(null));
			result.put(TipoVariabile.BPSSUPDET, translator.getVariabile(TipoVariabile.BPSSUPDET, passiBps, TipologiaPassoTransizione.RIDUZIONI_BPS).orElse(null));
			result.put(TipoVariabile.BPSSUPAMM, translator.getVariabile(TipoVariabile.BPSSUPAMM, passiBps, TipologiaPassoTransizione.AMMISSIBILITA).orElse(null));
			result.put(TipoVariabile.BPSSUPIMP, translator.getVariabile(TipoVariabile.BPSSUPIMP, passiBps, TipologiaPassoTransizione.AMMISSIBILITA).orElse(null));
			result.put(TipoVariabile.AGRATT, translator.getVariabile(TipoVariabile.AGRATT, passiBps, TipologiaPassoTransizione.AMMISSIBILITA).orElse(null));
			result.put(TipoVariabile.BPSSUPSCOST, translator.getVariabile(TipoVariabile.BPSSUPSCOST, passiBps, TipologiaPassoTransizione.RIDUZIONI_BPS).orElse(null));
		} catch (Exception e) {
			logger.debug("Errore in recupero variabili", e);
		}
		return result;
	}

	private MotivazioneA1Enum getMotivazioneA1(SuperficiAccertateInputData inputData) {
		Boolean isAgricoltoreAttivo = translator.getValoreVariabilePrecaricata(inputData.getVariabiliCalcolo(), TipoVariabile.AGRATT, Boolean.class, false);
		return isAgricoltoreAttivo != null && !isAgricoltoreAttivo 
				&& inputData.getStatoLavorazioneSostegno().equals(StatoIstruttoria.NON_AMMISSIBILE) ?
						MotivazioneA1Enum.SI :
							MotivazioneA1Enum.NO;
	}
	
	private MotivazioneA2Enum getMotivazioneA2(SuperficiAccertateInputData inputData) {
		return inputData.getStatoLavorazioneSostegno().equals(StatoIstruttoria.NON_LIQUIDABILE) ?
				MotivazioneA2Enum.SI :
					MotivazioneA2Enum.NO;
	}
	
	private MotivazioneA3Enum getMotivazioneA3() {
		return MotivazioneA3Enum.NO;
	}
	
	private MotivazioneB0Enum getMotivazioneB0(SuperficiAccertateInputData inputData) {
		return (Arrays.asList(StatoIstruttoria.NON_AMMISSIBILE,
						StatoIstruttoria.NON_LIQUIDABILE,
						StatoIstruttoria.PAGAMENTO_AUTORIZZATO,
						StatoIstruttoria.DEBITI)
					.contains(inputData.getStatoLavorazioneSostegno()) &&
					translator.getValoreVariabilePrecaricata(inputData.getVariabiliCalcolo(), TipoVariabile.BPSSUPSCOST, Float.class) > 0
				) ?
				MotivazioneB0Enum.SI :
					MotivazioneB0Enum.NO;
	}

	private Float getSuperficieAccertata(SuperficiAccertateInputData inputData) {
		// Da specifiche, il valore è lo stesso della superficie determinata
		return getSuperficieDeterminata(inputData);
	}

	private Float getSuperficieDeterminata(SuperficiAccertateInputData inputData) {
		Float result;
		if (Arrays.asList(StatoIstruttoria.NON_AMMISSIBILE,
						StatoIstruttoria.NON_LIQUIDABILE,
						StatoIstruttoria.PAGAMENTO_AUTORIZZATO,
						StatoIstruttoria.DEBITI)
					.contains(inputData.getStatoLavorazioneSostegno())) {
			Float bpsSupDetIst = translator.getValoreVariabilePrecaricata(inputData.getVariabiliCalcolo(), TipoVariabile.BPSSUPDETIST, Float.class, false);
			Float bpsSupDet = translator.getValoreVariabilePrecaricata(inputData.getVariabiliCalcolo(), TipoVariabile.BPSSUPDET, Float.class, false);
			Float bpsSupAmm = translator.getValoreVariabilePrecaricata(inputData.getVariabiliCalcolo(), TipoVariabile.BPSSUPAMM, Float.class, false);
			result = nvl(bpsSupDetIst, nvl(bpsSupDet, bpsSupAmm));
		} else {
			result = translator.getValoreVariabilePrecaricata(inputData.getVariabiliCalcolo(), TipoVariabile.BPSSUPIMP, Float.class);
		}
		return nvl(ConversioniCalcoli.convertiEttariInMetriQuadri(result), 0f);
	}

	private SuperficiAccertateDto build(SuperficiAccertateInputData inputData) {
		SuperficiAccertateDto superficiAccertate = new SuperficiAccertateDto();
		superficiAccertate.setAnnoCampagna(inputData.getAnnoCampagna());
		superficiAccertate.setCuaa(inputData.getCuaaIntestatario());
		superficiAccertate.setIdentificativoDomanda(inputData.getNumeroDomanda().toString());
		superficiAccertate.setMotivazioneA1(getMotivazioneA1(inputData));
		superficiAccertate.setMotivazioneA2(getMotivazioneA2(inputData));
		superficiAccertate.setMotivazioneA3(getMotivazioneA3());
		superficiAccertate.setMotivazioneB0(getMotivazioneB0(inputData));
		superficiAccertate.setMotivazioneB1(MotivazioneB1Enum.NO);
		superficiAccertate.setSuperficieAccertata(Double.valueOf(getSuperficieAccertata(inputData)));
		superficiAccertate.setSuperficieDeterminata(Double.valueOf(getSuperficieDeterminata(inputData)));
		return superficiAccertate;
	}

	@Override
	public void addIstruttorie(DomandaUnicaModel domanda, List<IstruttoriaModel> istruttorie) {
		//Non vanno sincronizzate le domande che non hanno richiesto l'intervento BPS '026'
		boolean hasBps = richiestaSuperficieDao.getCountfindByDomandaInterventoPaginata1(domanda.getId(), CodiceInterventoAgs.BPS)>0;
		//errore rollback transazione e lazy load se preso tramite stream
//		boolean hasBps = 
//				domanda.getA4gtRichiestaSuperficies().stream()
//					.anyMatch(
//						richiesta -> InterventoDisaccoppiato.BPS.getCodiceAgea()
//										.equals(richiesta.getIntervento().getCodiceAgea())
//							);
		if (hasBps) super.addIstruttorie(domanda, istruttorie);
	}

}
