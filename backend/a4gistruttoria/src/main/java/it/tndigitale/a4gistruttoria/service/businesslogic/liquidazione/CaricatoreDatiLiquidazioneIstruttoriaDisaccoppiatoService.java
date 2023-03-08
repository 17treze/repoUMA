package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElencoLiquidazioneException;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.TipoPagamento;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.VoceSpesa;
import it.tndigitale.a4gistruttoria.util.TipoCapitoloSpesa;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Service(CaricatoreDatiLiquidazioneIstruttoria.PREFISSO_CARICATORE_DATI_QUALIFIER + "DISACCOPPIATO")
public class CaricatoreDatiLiquidazioneIstruttoriaDisaccoppiatoService extends CaricatoreDatiLiquidazioneIstruttoria {

	private static final Map<TipoIstruttoria, TipoPagamento> mappaTipoPagamento = new HashMap<TipoIstruttoria, DatiLiquidazioneIstruttoria.TipoPagamento>();
	static {
		mappaTipoPagamento.put(TipoIstruttoria.ANTICIPO, TipoPagamento.ANTICIPO);
		mappaTipoPagamento.put(TipoIstruttoria.SALDO, TipoPagamento.SALDO);
		mappaTipoPagamento.put(TipoIstruttoria.INTEGRAZIONE, TipoPagamento.INTEGRAZIONE);
	}

	@Override
	protected void popolaTipoPagamento(IstruttoriaModel istruttoria, DatiLiquidazioneIstruttoria datiLiquidazione) {
		datiLiquidazione.setTipoPagamento(mappaTipoPagamento.get(istruttoria.getTipologia()));
	}
	

	@Override
	protected void popolaImportoTotale(IstruttoriaModel istruttoria, DatiLiquidazioneIstruttoria datiLiquidazione) throws ElencoLiquidazioneException {
		datiLiquidazione.setImportoTotale(recuperaVariabileDisciplinaFinanziaria(istruttoria, TipoVariabile.DFIMPLIQDIS).doubleValue());
	}

	@Override
	protected void popolaVociSpesa(IstruttoriaModel istruttoria, DatiLiquidazioneIstruttoria datiLiquidazione) throws ElencoLiquidazioneException {
		Integer campagna = istruttoria.getDomandaUnicaModel().getCampagna();
		BigDecimal importoCapping = recuperaVariabileCalcolo(istruttoria, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.BPSIMPBCCAP);
		boolean capping = isCapping(importoCapping);

		// Voce 1
		VoceSpesa voceSpesa = caricaCapitolo(campagna, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.BPS);
		voceSpesa.setQuantita(getQuantita(istruttoria, TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSSUPAMM));
		voceSpesa.setImporto(getImporto(istruttoria, TipoVariabile.DFFRPAGDISBPS, capping));
		datiLiquidazione.setVoce1(voceSpesa);
		
		// voce 2
		voceSpesa = caricaCapitolo(campagna, TipoCapitoloSpesa.CONDISC, CodiceInterventoAgs.BPS);
		voceSpesa.setQuantita(getQuantita(istruttoria, TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSSUPAMM));
		voceSpesa.setImporto(getImporto(istruttoria, TipoVariabile.DFIMPDFDISBPS, capping));
		datiLiquidazione.setVoce2(voceSpesa);
		
		// voce 3
		voceSpesa = caricaCapitolo(campagna, TipoCapitoloSpesa.RID50, CodiceInterventoAgs.BPS);
		voceSpesa.setQuantita(getQuantita(istruttoria, TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSSUPAMM));
		voceSpesa.setImporto(null);
		datiLiquidazione.setVoce3(voceSpesa);
		
		// voce 4
		voceSpesa = caricaCapitolo(campagna, TipoCapitoloSpesa.RID100, CodiceInterventoAgs.BPS);
		voceSpesa.setQuantita(getQuantita(istruttoria, TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSSUPAMM));
		voceSpesa.setImporto(null);
		datiLiquidazione.setVoce4(voceSpesa);
		
		// voce 5
		voceSpesa = caricaCapitolo(campagna, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.GREE);
		voceSpesa.setQuantita(getQuantita(istruttoria, TipologiaPassoTransizione.GREENING, TipoVariabile.GRESUPAMM));
		voceSpesa.setImporto(getImporto(istruttoria, TipoVariabile.DFFRPAGDISGRE, false));
		datiLiquidazione.setVoce5(voceSpesa);
		
		// voce 6
		voceSpesa = caricaCapitolo(campagna, TipoCapitoloSpesa.CONDISC, CodiceInterventoAgs.GREE);
		voceSpesa.setQuantita(getQuantita(istruttoria, TipologiaPassoTransizione.GREENING, TipoVariabile.GRESUPAMM));
		voceSpesa.setImporto(getImporto(istruttoria, TipoVariabile.DFIMPDFDISGRE, false));
		datiLiquidazione.setVoce6(voceSpesa);
		
		// voce 7
		voceSpesa = caricaCapitolo(campagna, TipoCapitoloSpesa.SENZADISC, CodiceInterventoAgs.GIOV);
		voceSpesa.setQuantita(getQuantita(istruttoria, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOSUPAMM));
		voceSpesa.setImporto(getImporto(istruttoria, TipoVariabile.DFFRPAGDISGIO, false));
		datiLiquidazione.setVoce7(voceSpesa);
		
		// voce 8
		voceSpesa = caricaCapitolo(campagna, TipoCapitoloSpesa.CONDISC, CodiceInterventoAgs.GIOV);
		voceSpesa.setQuantita(getQuantita(istruttoria, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOSUPAMM));
		voceSpesa.setImporto(getImporto(istruttoria, TipoVariabile.DFIMPDFDISGIO, false));
		datiLiquidazione.setVoce8(voceSpesa);

		// voce 9
		datiLiquidazione.setVoce9(getVoceVuota());

		// voce 10
		datiLiquidazione.setVoce10(getVoceVuota());

		// voce 11
		datiLiquidazione.setVoce11(getVoceVuota());

		// voce 12
		datiLiquidazione.setVoce12(getVoceVuota());

		// voce 13
		datiLiquidazione.setVoce13(getVoceVuota());

		// voce 14
		datiLiquidazione.setVoce14(getVoceVuota());
	}
	
	protected boolean isCapping(BigDecimal capping) {
		return capping.compareTo(BigDecimal.valueOf(150000)) >= 0;
		
	}
	
	protected Double getImporto(IstruttoriaModel istruttoria, TipoVariabile variabile, boolean capping) throws ElencoLiquidazioneException {
		if (capping) return null;
		return recuperaVariabileDisciplinaFinanziaria(istruttoria, variabile).doubleValue();
	}

	protected Double getQuantita(IstruttoriaModel istruttoria, TipologiaPassoTransizione passo, TipoVariabile variabile) throws ElencoLiquidazioneException {
		BigDecimal superficie = recuperaVariabileCalcolo(istruttoria, passo, variabile);
		// la moltiplicazione la fa il serializzatore
		//return superficie.multiply(BigDecimal.valueOf(10000)).setScale(0, RoundingMode.FLOOR).doubleValue();
		return superficie.doubleValue();
	}

}
