package it.tndigitale.a4gistruttoria.service.businesslogic.superficie;

import it.tndigitale.a4gistruttoria.action.acs.CalcoloOutputAccoppiatoSuperficieChainBuilder;
import it.tndigitale.a4gistruttoria.action.acs.InitVariabiliACSInputChainBuilder;
import it.tndigitale.a4gistruttoria.dto.lavorazione.*;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloIstruttoriaAccoppiatoAbstractService;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria.*;

@Service
public class CalcoloAccoppiatoSuperficieService extends CalcoloIstruttoriaAccoppiatoAbstractService<EsitoAmmissibilitaAccoppiatoSuperficie> {

	@Autowired
	private InitVariabiliACSInputChainBuilder chainInitVariabili;

	@Autowired
	private CalcoloOutputAccoppiatoSuperficieChainBuilder chainCalcoloOutput;	

	@Override
	protected TipologiaPassoTransizione getPassoLavorazione() {
		return TipologiaPassoTransizione.CALCOLO_ACS;
	}	

	@Override
	protected CalcoloAccoppiatoHandler calcolo(IstruttoriaModel istruttoria, MapVariabili inputListaVariabiliCalcolo) {
		MapVariabili variabiliOutput = new MapVariabili();
		CalcoloAccoppiatoHandler infoCalcolo = new CalcoloAccoppiatoHandler(inputListaVariabiliCalcolo, variabiliOutput);
		chainCalcoloOutput.initVariabili(infoCalcolo, istruttoria);
		return infoCalcolo;
	}
	
	@Override
	protected DatiSintesi calcolaDatiSintesiLavorazione(EsitoAmmissibilitaAccoppiatoSuperficie esito, CalcoloAccoppiatoHandler handler) throws Exception {
		DatiSintesi datiSintesi = new DatiSintesi();
		List<EsitoControllo> esitiControlli = new ArrayList<>();
		datiSintesi.setEsitiControlli(esitiControlli);
		esitiControlli.add(trasformaEsito(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo, esito.isInfoAgricoltoreAttivo()));
		esitiControlli.add(trasformaEsito(TipoControllo.BRIDUSDC010_agricoltoreAttivo, esito.isAgricoltoreAttivo()));
		esitiControlli.add(trasformaEsito(TipoControllo.BRIDUSDC022_idDomandaCampione, esito.isCampione()));
		esitiControlli.add(trasformaEsito(TipoControllo.BRIDUSDC034_sigeco, esito.isSigeco()));
		esitiControlli.add(trasformaEsito(TipoControllo.BRIDUACS090_olivo75, esito.isOlivo75()));
		esitiControlli.add(trasformaEsito(TipoControllo.BRIDUACS091_olivoNazionale, esito.isOlivoNazionale()));
		esitiControlli.add(trasformaEsito(TipoControllo.BRIDUACS092_olivoQualita, esito.isOlivoQualita()));
		esitiControlli.add(trasformaEsito(TipoControllo.BRIDUACS083_supMinima, esito.isSuperficieMinima()));
		return datiSintesi;
	}

	@Override
	protected EsitoAmmissibilitaAccoppiatoSuperficie calcoloEsito(CalcoloAccoppiatoHandler handler)
			throws Exception {
		return EsitoAmmissibilitaAccoppiatoSuperficieBuilder
				.buildEsitoAmmissibilitaAccoppiatoSuperficie(handler);
	}

	@Override
	protected MapVariabili initVariabiliInput(IstruttoriaModel istruttoria) throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		chainInitVariabili.initVariabili(handler, istruttoria);
		return handler.getVariabiliInput();
	}

	@Override
	protected FoglieAlgoritmoWorkflow calcoloFoglia(EsitoAmmissibilitaAccoppiatoSuperficie esito) throws Exception {
		return MappaEsitiFoglieAmmissibilitaAccoppiatoSuperficie.getFoglia(esito);
	}

	@Override
	protected Sostegno getIdentificativoSostegno() {
		return Sostegno.SUPERFICIE;
	}

	@Override
	protected Boolean eseguiControlliPreliminari(IstruttoriaModel istruttoria) throws Exception {
		return RICHIESTO.getStatoIstruttoria().equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()) ||
			   CONTROLLI_CALCOLO_OK.getStatoIstruttoria().equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()) ||
			   CONTROLLI_CALCOLO_KO.getStatoIstruttoria().equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo());
	}



}
