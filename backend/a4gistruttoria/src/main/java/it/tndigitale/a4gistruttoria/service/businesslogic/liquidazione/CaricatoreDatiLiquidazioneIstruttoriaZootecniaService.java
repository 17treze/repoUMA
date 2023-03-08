package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElencoLiquidazioneException;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.TipoPagamento;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.VoceSpesa;
import it.tndigitale.a4gistruttoria.util.TipoCapitoloSpesa;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.VariabileCalcoloUtils;
import javassist.NotFoundException;

@Service(CaricatoreDatiLiquidazioneIstruttoria.PREFISSO_CARICATORE_DATI_QUALIFIER + "ZOOTECNIA")
public class CaricatoreDatiLiquidazioneIstruttoriaZootecniaService extends CaricatoreDatiLiquidazioneIstruttoria {

	private static final Logger logger = LoggerFactory.getLogger(CaricatoreDatiLiquidazioneIstruttoriaZootecniaService.class);

	@Autowired
	private VariabileCalcoloUtils utilsVariabileCalcolo;
	
	private static final List<Casistica> casistiche = new LinkedList<CaricatoreDatiLiquidazioneIstruttoriaZootecniaService.Casistica>();
	static {
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACZ_310,		TipoVariabile.ACZCAPIACC_310, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.LATTE));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACZ_310,	TipoVariabile.ACZCAPIACC_310, TipoCapitoloSpesa.CONDISC, CodiceInterventoAgs.LATTE));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACZ_311,		TipoVariabile.ACZCAPIACC_311, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.LATTE_BMONT));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACZ_311,	TipoVariabile.ACZCAPIACC_311, TipoCapitoloSpesa.CONDISC, CodiceInterventoAgs.LATTE_BMONT));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACZ_313,		TipoVariabile.ACZCAPIACC_313, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.BOVINI_VAC));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACZ_313,	TipoVariabile.ACZCAPIACC_313, TipoCapitoloSpesa.CONDISC, CodiceInterventoAgs.BOVINI_VAC));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACZ_322,		TipoVariabile.ACZCAPIACC_322, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.BOVINI_VAC_NO_ISCR));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACZ_322,	TipoVariabile.ACZCAPIACC_322, TipoCapitoloSpesa.CONDISC, CodiceInterventoAgs.BOVINI_VAC_NO_ISCR));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACZ_315,		TipoVariabile.ACZCAPIACC_315, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.BOVINI_MAC));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACZ_315,	TipoVariabile.ACZCAPIACC_315, TipoCapitoloSpesa.CONDISC, CodiceInterventoAgs.BOVINI_MAC));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACZ_316,		TipoVariabile.ACZCAPIACC_316, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.BOVINI_MAC_12M));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACZ_316,	TipoVariabile.ACZCAPIACC_316, TipoCapitoloSpesa.CONDISC, CodiceInterventoAgs.BOVINI_MAC_12M));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACZ_318,		TipoVariabile.ACZCAPIACC_318, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.BOVINI_MAC_ETIC));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACZ_318,	TipoVariabile.ACZCAPIACC_318, TipoCapitoloSpesa.CONDISC, CodiceInterventoAgs.BOVINI_MAC_ETIC));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACZ_320,		TipoVariabile.ACZCAPIACC_320, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.OVICAP_AGN));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACZ_320,	TipoVariabile.ACZCAPIACC_320, TipoCapitoloSpesa.CONDISC, CodiceInterventoAgs.OVICAP_AGN));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACZ_321,		TipoVariabile.ACZCAPIACC_321, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.OVICAP_MAC));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACZ_321,	TipoVariabile.ACZCAPIACC_321, TipoCapitoloSpesa.CONDISC, CodiceInterventoAgs.OVICAP_MAC));
	}
	
	@Override
	protected void popolaTipoPagamento(IstruttoriaModel istruttoria, DatiLiquidazioneIstruttoria datiLiquidazione) {
		datiLiquidazione.setTipoPagamento(TipoPagamento.INTEGRAZIONE);
	}
	

	@Override
	protected void popolaImportoTotale(IstruttoriaModel istruttoria, DatiLiquidazioneIstruttoria datiLiquidazione) throws ElencoLiquidazioneException {
		datiLiquidazione.setImportoTotale(recuperaVariabileDisciplinaFinanziaria(istruttoria, TipoVariabile.DFIMPLIQACZ).doubleValue());
	}

	@Override
	protected void popolaVociSpesa(IstruttoriaModel istruttoria, DatiLiquidazioneIstruttoria datiLiquidazione) throws ElencoLiquidazioneException {
		Integer campagna = istruttoria.getDomandaUnicaModel().getCampagna();

		// Voce 1
		AtomicInteger progressivoCasistica = new AtomicInteger(0);
		datiLiquidazione.setVoce1(creaVoce(progressivoCasistica, campagna, istruttoria));
		
		// voce 2
		datiLiquidazione.setVoce2(creaVoce(progressivoCasistica, campagna, istruttoria));
		
		// voce 3
		datiLiquidazione.setVoce3(creaVoce(progressivoCasistica, campagna, istruttoria));
		
		// voce 4
		datiLiquidazione.setVoce4(creaVoce(progressivoCasistica, campagna, istruttoria));
		
		// voce 5
		datiLiquidazione.setVoce5(creaVoce(progressivoCasistica, campagna, istruttoria));
		
		// voce 6
		datiLiquidazione.setVoce6(creaVoce(progressivoCasistica, campagna, istruttoria));
		
		// voce 7
		datiLiquidazione.setVoce7(creaVoce(progressivoCasistica, campagna, istruttoria));
		
		// voce 8
		datiLiquidazione.setVoce8(creaVoce(progressivoCasistica, campagna, istruttoria));

		// voce 9
		datiLiquidazione.setVoce9(creaVoce(progressivoCasistica, campagna, istruttoria));

		// voce 10
		datiLiquidazione.setVoce10(creaVoce(progressivoCasistica, campagna, istruttoria));

		// voce 11
		datiLiquidazione.setVoce11(creaVoce(progressivoCasistica, campagna, istruttoria));

		// voce 12
		datiLiquidazione.setVoce12(creaVoce(progressivoCasistica, campagna, istruttoria));

		// voce 13
		datiLiquidazione.setVoce13(creaVoce(progressivoCasistica, campagna, istruttoria));

		// voce 14
		datiLiquidazione.setVoce14(creaVoce(progressivoCasistica, campagna, istruttoria));
	}
	
	private VoceSpesa creaVoce(AtomicInteger progressivo, Integer campagna, IstruttoriaModel istruttoria) throws ElencoLiquidazioneException {
		int i = progressivo.get();
		if (i > casistiche.size() - 1) {
			return getVoceVuota();
		}
		Casistica caso = casistiche.get(progressivo.getAndIncrement());
		VoceSpesa result =  creaVoceSeEsiste(campagna, caso, istruttoria);
		if (result != null) {
			return result;
		}
		return creaVoce(progressivo, campagna, istruttoria);
	}

	private VoceSpesa creaVoceSeEsiste(Integer campagna, Casistica caso, IstruttoriaModel istruttoria) throws ElencoLiquidazioneException {
		if (!esisteVariabileDisciplina(caso, istruttoria)) {
			return null;
		}
		VoceSpesa voceSpesa = caricaCapitolo(campagna, caso.tipoCapitolo, caso.intervento);
		voceSpesa.setQuantita(getQuantita(istruttoria, caso.variabileQuantita));
		voceSpesa.setImporto(getImporto(istruttoria, caso.variabileDisciplinaFinanziaria));
		return voceSpesa;
	}
	
	private boolean esisteVariabileDisciplina(Casistica caso, IstruttoriaModel istruttoria) throws ElencoLiquidazioneException {
		return (BigDecimal.ZERO.compareTo(recuperaVariabileDisciplinaFinanziaria(istruttoria, caso.variabileDisciplinaFinanziaria)) < 0);
	}

	protected Double getImporto(IstruttoriaModel istruttoria, TipoVariabile variabile) throws ElencoLiquidazioneException {
		return recuperaVariabileDisciplinaFinanziaria(istruttoria, variabile).doubleValue();
	}

	protected Double getQuantita(IstruttoriaModel istruttoria, TipoVariabile variabile) throws ElencoLiquidazioneException {
		BigDecimal capi = recuperaVariabileCalcolo(istruttoria, TipologiaPassoTransizione.CALCOLO_ACZ, variabile);
		return capi.doubleValue();
	}
	
	@Override
	protected BigDecimal recuperaVariabileCalcolata(TransizioneIstruttoriaModel transizione, TipologiaPassoTransizione passo, TipoVariabile variabile) throws ElencoLiquidazioneException {
		try {
			VariabileCalcolo variabileCalcolo = utilsVariabileCalcolo.recuperaVariabileInput(transizione, passo, variabile);
			if (variabileCalcolo == null)
				return BigDecimal.ZERO;
			else
				return variabileCalcolo.getValNumber();
		} catch (NotFoundException | IOException e) {
			logger.error("Errore calcolando la variabile {} nel passo {} per l'istruttoria {}", variabile, passo, transizione.getIstruttoria().getId(), e);
			throw new ElencoLiquidazioneException(e.getMessage());
		}
	}
	
	
	private static class Casistica {
		private TipoVariabile variabileDisciplinaFinanziaria;
		private TipoVariabile variabileQuantita;
		private TipoCapitoloSpesa tipoCapitolo;
		private CodiceInterventoAgs intervento;
		
		public Casistica(TipoVariabile variabileDisciplinaFinanziaria, TipoVariabile variabileQuantita,
				TipoCapitoloSpesa tipoCapitolo, CodiceInterventoAgs intervento) {
			super();
			this.variabileDisciplinaFinanziaria = variabileDisciplinaFinanziaria;
			this.variabileQuantita = variabileQuantita;
			this.tipoCapitolo = tipoCapitolo;
			this.intervento = intervento;
		}
	}

}
