package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElencoLiquidazioneException;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.TipoPagamento;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.VoceSpesa;
import it.tndigitale.a4gistruttoria.util.TipoCapitoloSpesa;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Service(CaricatoreDatiLiquidazioneIstruttoria.PREFISSO_CARICATORE_DATI_QUALIFIER + "SUPERFICIE")
public class CaricatoreDatiLiquidazioneIstruttoriaSuperficieService extends CaricatoreDatiLiquidazioneIstruttoria {

	private static final List<Casistica> casistiche = new ArrayList<CaricatoreDatiLiquidazioneIstruttoriaSuperficieService.Casistica>();
	static {
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACS_M8,		TipoVariabile.ACSSUPAMM_M8,  TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.SOIA));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACS_M8,	TipoVariabile.ACSSUPAMM_M8,  TipoCapitoloSpesa.CONDISC,   CodiceInterventoAgs.SOIA));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACS_M9,		TipoVariabile.ACSSUPAMM_M9,  TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.GDURO));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACS_M9,	TipoVariabile.ACSSUPAMM_M9,  TipoCapitoloSpesa.CONDISC,   CodiceInterventoAgs.GDURO));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACS_M10,		TipoVariabile.ACSSUPAMM_M10, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.CPROT));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACS_M10,	TipoVariabile.ACSSUPAMM_M10, TipoCapitoloSpesa.CONDISC,   CodiceInterventoAgs.CPROT));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACS_M11,		TipoVariabile.ACSSUPAMM_M11, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.LEGUMIN));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACS_M11,	TipoVariabile.ACSSUPAMM_M11, TipoCapitoloSpesa.CONDISC,   CodiceInterventoAgs.LEGUMIN));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACS_M14,		TipoVariabile.ACSSUPAMM_M14, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.POMOD));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACS_M14,	TipoVariabile.ACSSUPAMM_M14, TipoCapitoloSpesa.CONDISC,   CodiceInterventoAgs.POMOD));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACS_M15,		TipoVariabile.ACSSUPAMM_M15, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.OLIO));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACS_M15,	TipoVariabile.ACSSUPAMM_M15, TipoCapitoloSpesa.CONDISC,   CodiceInterventoAgs.OLIO));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACS_M16,		TipoVariabile.ACSSUPAMM_M16, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.OLIVE_PEND75));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACS_M16,	TipoVariabile.ACSSUPAMM_M16, TipoCapitoloSpesa.CONDISC,   CodiceInterventoAgs.OLIVE_PEND75));
		casistiche.add(new Casistica(TipoVariabile.DFFRPAGACS_M17,		TipoVariabile.ACSSUPAMM_M17, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.OLIVE_DISC));
		casistiche.add(new Casistica(TipoVariabile.DFIMPDFDISACS_M17,	TipoVariabile.ACSSUPAMM_M17, TipoCapitoloSpesa.CONDISC,   CodiceInterventoAgs.OLIVE_DISC));
	}
	
	@Override
	protected void popolaTipoPagamento(IstruttoriaModel istruttoria, DatiLiquidazioneIstruttoria datiLiquidazione) {
		datiLiquidazione.setTipoPagamento(TipoPagamento.INTEGRAZIONE);
	}
	

	@Override
	protected void popolaImportoTotale(IstruttoriaModel istruttoria, DatiLiquidazioneIstruttoria datiLiquidazione) throws ElencoLiquidazioneException {
		datiLiquidazione.setImportoTotale(recuperaVariabileDisciplinaFinanziaria(istruttoria, TipoVariabile.DFIMPLIQACS).doubleValue());
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
		BigDecimal superficie = recuperaVariabileCalcolo(istruttoria, TipologiaPassoTransizione.CALCOLO_ACS, variabile);
		// la moltiplicazione la fa il serializzatore
		//return superficie.multiply(BigDecimal.valueOf(10000)).setScale(0, RoundingMode.FLOOR).doubleValue();
		return superficie.doubleValue();
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
