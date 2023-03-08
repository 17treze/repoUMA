package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia;

import static it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria.CONTROLLI_CALCOLO_KO;
import static it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria.CONTROLLI_CALCOLO_OK;
import static it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria.INTEGRATO;
import static it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria.RICHIESTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import it.tndigitale.a4gistruttoria.action.acz.CalcoloOutputAccoppiatoZootecniaChainBuilder;
import it.tndigitale.a4gistruttoria.action.acz.InitVariabiliACZInputChainBuilder;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoAmmissibilitaAccoppiatoZootecnia;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoAmmissibilitaAccoppiatoZootecniaBuilder;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.FoglieAlgoritmoWorkflow;
import it.tndigitale.a4gistruttoria.dto.lavorazione.InterventiSanzionati;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MappaEsitiFoglieAmmissibilitaAccoppiatoZootecnia;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloIstruttoriaAccoppiatoAbstractService;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoControllo.LivelloControllo;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Service
public class CalcoloAccoppiatoZootecniaService extends CalcoloIstruttoriaAccoppiatoAbstractService<EsitoAmmissibilitaAccoppiatoZootecnia> {

	@Autowired
	private InitVariabiliACZInputChainBuilder chainInitVariabili;

	@Autowired
	private CalcoloOutputAccoppiatoZootecniaChainBuilder chainCalcolo;

	@Override
	protected TipologiaPassoTransizione getPassoLavorazione() {
		return TipologiaPassoTransizione.CALCOLO_ACZ;
	}

	@Override
	protected CalcoloAccoppiatoHandler calcolo(IstruttoriaModel istruttoria, MapVariabili inputListaVariabiliCalcolo) {
		MapVariabili variabiliOutput = new MapVariabili();
		CalcoloAccoppiatoHandler infoCalcolo = new CalcoloAccoppiatoHandler(inputListaVariabiliCalcolo, variabiliOutput);
		chainCalcolo.initVariabili(infoCalcolo, istruttoria);
		return infoCalcolo;
	}

	@Override
	protected DatiSintesi calcolaDatiSintesiLavorazione(EsitoAmmissibilitaAccoppiatoZootecnia esito,
			CalcoloAccoppiatoHandler handler) throws Exception {
		if (handler != null) {
			DatiSintesi datiSintesi = new DatiSintesi();
			datiSintesi.setEsitiControlli(loadEsitiControlli(esito));
			return datiSintesi;
		} 
		return null;
	}

	@Override
	protected EsitoAmmissibilitaAccoppiatoZootecnia calcoloEsito(CalcoloAccoppiatoHandler handler) throws Exception {
		return EsitoAmmissibilitaAccoppiatoZootecniaBuilder
				.buildEsitoAmmissibilitaAccoppiatoZootecnia(handler);
	}

	@Override
	protected MapVariabili initVariabiliInput(IstruttoriaModel istruttoria) throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		chainInitVariabili.initVariabili(handler, istruttoria);
		return handler.getVariabiliInput();
	}

	@Override
	protected FoglieAlgoritmoWorkflow calcoloFoglia(EsitoAmmissibilitaAccoppiatoZootecnia esito) throws Exception {
		return MappaEsitiFoglieAmmissibilitaAccoppiatoZootecnia.getFoglia(esito);
	}

	@Override
	protected Sostegno getIdentificativoSostegno() {
		return Sostegno.ZOOTECNIA;
	}

	@Override
	protected Boolean eseguiControlliPreliminari(IstruttoriaModel istruttoria) throws Exception {
		return eseguiControlliPreliminariXIstruttoria(istruttoria) &&
				( RICHIESTO.getStatoIstruttoria().equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()) ||
						CONTROLLI_CALCOLO_OK.getStatoIstruttoria().equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()) ||
						CONTROLLI_CALCOLO_KO.getStatoIstruttoria().equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()) ||
						INTEGRATO.getStatoIstruttoria().equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo())
						);
	}





	private List<EsitoControllo> loadEsitiControlli(EsitoAmmissibilitaAccoppiatoZootecnia esito) {
		List<EsitoControllo> result = new ArrayList<>();
		result.add(trasformaEsito(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo, esito.isInfoAgricoltoreAttivo().orElse(null)));
		result.add(trasformaEsito(TipoControllo.BRIDUSDC010_agricoltoreAttivo, esito.isAgricoltoreAttivo().orElse(null)));
		result.add(trasformaEsito(TipoControllo.BRIDUSDC022_idDomandaCampione, esito.isCampione().orElse(null)));
		result.add(trasformaEsito(TipoControllo.BRIDUACZ107_UbaAmmessi, esito.isUbaMinime().orElse(null)));
		result.add(trasformaEsito(TipoControllo.BRIDUACZ123_VerificaControlliInLoco, esito.isEsitoControlliInLoco().orElse(null)));
		result.add(trasformaEsitoVerificaSanzioni(esito.getInterventiSanzionati()));
		result.add(trasformaEsito(TipoControllo.BRIDUACZ127_Riduzioni, esito.isRiduzioni().orElse(null)));
		return result;
	}


	private EsitoControllo trasformaEsitoVerificaSanzioni(Optional<InterventiSanzionati> interventiSanzionati) {
		EsitoControllo result = new EsitoControllo(TipoControllo.BRIDUACZ126_VerificaSanzioni, 
				ObjectUtils.nullSafeToString(interventiSanzionati.orElse(null)));
		if (interventiSanzionati.isPresent()) {
			switch (interventiSanzionati.get()) {
			case CON_SANZIONI:
				result.setLivelloControllo(LivelloControllo.WARNING);
				break;
			case NESSUNA_SANZIONE:
				result.setLivelloControllo(LivelloControllo.INFO);
				break;
			case TUTTI_SANZIONATI:
				result.setLivelloControllo(LivelloControllo.ERROR);
				break;
			default:
				break;
			}
		}
		return result;
	}

	private Boolean eseguiControlliPreliminariXIstruttoria(IstruttoriaModel istruttoria) throws Exception {
		// Se non è stata presentata la domanda integrativa, la domanda finisce in stato NON AMMISSIBILE
		// e tutto il calcolo NON viene eseguito
		/*
		Boolean result = controlloDomandaIntegrativa.checkDomandaIntegrativa(istruttoria.getDomandaUnicaModel().getId());

		if (!result) {
			// Avvio la transizione
			TransizioneIstruttoriaModel transizione = transizioneService.avviaTransizioneCalcolo(istruttoria);

			// Simulo l'esito per l'assenza di domanda integrativa
			EsitoAmmissibilitaAccoppiatoZootecnia esito = EsitoAmmissibilitaAccoppiatoZootecnia.empty()
					.withDomandaIntegrativa(Optional.of(false));
			FoglieAlgoritmoWorkflow foglia = calcoloFoglia(esito);
			StatoIstruttoria codiceStatoLavorazioneFinale = foglia.getStatoWF();
			A4gdStatoLavSostegno statoLavorazioneSostegnoObject =
					statoDomandaService.caricaStatoLavorazioneSostegno(codiceStatoLavorazioneFinale);
			istruttoria.setA4gdStatoLavSostegno(statoLavorazioneSostegnoObject);
			statoDomandaService.aggiornaLavorazioneSostegnoDellaDomanda(istruttoria);
			// salvo il passo di lavorazione
			salvaPassoLavorazione(transizione, foglia, esito, null);
			// aggiorno (chiudo) la transizione
			transizione.setA4gdStatoLavSostegno1(statoLavorazioneSostegnoObject);
			transizione.setDataEsecuzione(new Date());
			transizioneService.aggiornaTransizione(transizione);
		}
		return result;
		 */
		//il check precedente non ha più senso visto che l'istruttoria non ha più la domanda integrativa
		return true;
	}

}
