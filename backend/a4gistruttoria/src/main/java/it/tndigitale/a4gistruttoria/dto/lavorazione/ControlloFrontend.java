package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import it.tndigitale.a4gistruttoria.util.GruppoControllo;
import it.tndigitale.a4gistruttoria.util.MessaggioControllo;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoControllo.LivelloControllo;

public class ControlloFrontend implements Comparable<ControlloFrontend> {
	String codice1;
	String descrizione1;
	Integer ordine1;

	String codice2;
	String descrizione2;
	Integer ordine2;

	String controllo;
	String controlloDescrizione;
	Integer ordineControllo;

	String valore;

	public static ControlloFrontend newControlloFrontend(TipologiaPassoTransizione tipologiaPasso, EsitoControllo esito) throws Exception {
		ControlloFrontend contr = new ControlloFrontend();
		TipoControllo tipoControllo = esito.getTipoControllo();

		List<MessaggioControllo> messaggioControlloVal = Arrays.asList(MessaggioControllo.values());

		String esitoString = esito.recuperaValoreString();

		Optional<MessaggioControllo> messaggioOpt = messaggioControlloVal.stream().filter(x -> x.getCodiceBridu().equals(tipoControllo.getCodice()) && x.getCodiceResBridu().equals(esitoString))
				.findFirst();

		String messaggioString = "";

		if (messaggioOpt.isPresent())
			messaggioString = messaggioOpt.get().getMessaggio();
		else
			throw new Exception("Errore configurazione MessaggioControllo " + tipoControllo.getCodice());

		if (messaggioString == null || messaggioString.isEmpty())
			return null;

		if (esito == null || esito.getEsito() == null)
			throw new Exception("Esito non settato correttamente " + tipoControllo.getCodice());

		LivelloControllo esitoLivello = (esito.getEsito() == true) ? tipoControllo.getLivelloPositivo() : tipoControllo.getLivelloNegativo();

		if (esitoLivello.equals(LivelloControllo.NULL))
			return null;

		// Condiziono le anomalie da mostrare a frontend in base al gruppo di appartenenza
		String codice1 = null;

		if (esito.getTipoControllo().getGruppoControllo().equals(GruppoControllo.LIQUIDABILE)) {
			codice1 = "ANOMALIE DI LIQUIDABILITA";
			contr.setCodice1(codice1);
			contr.setDescrizione1(codice1);
			contr.setCodice2(codice1);
			contr.setDescrizione2(codice1);
		} else if (esito.getTipoControllo().getGruppoControllo().equals(GruppoControllo.INTERSOSTEGNO)) {
			codice1 = "ANOMALIE INTERSOSTEGNO";
			contr.setCodice1(codice1);
			contr.setDescrizione1(codice1);
			contr.setCodice2(codice1);
			contr.setDescrizione2(codice1);
		} else {
			codice1 = (esito.getEsito() == true) ? tipoControllo.getLivelloPositivo().getDescrizione() : tipoControllo.getLivelloNegativo().getDescrizione();
			contr.setCodice1(codice1);
			contr.setDescrizione1(codice1);
			contr.setCodice2(codice1);
			contr.setDescrizione2(codice1);
		}
		contr.setControllo(esito.getTipoControllo().getCodice());
		contr.setControlloDescrizione(esito.getTipoControllo().getDescrizione());
		contr.setValore(messaggioString);
		contr.setOrdine1((esito != null && esito.getEsito() != null && esito.getEsito() == true) ? tipoControllo.getLivelloPositivo().getOrdine() : tipoControllo.getLivelloNegativo().getOrdine());
		contr.setOrdine2(1);
		contr.setOrdineControllo(esito.getTipoControllo().getOrdine());

		return contr;

	}

	public ControlloFrontend(TipologiaPassoTransizione tipologiaPasso, String pcodice2, String pdescrizione2, Integer pOrdine2, VariabileCalcolo variabileCalcolo) {
		codice1 = tipologiaPasso.name();
		descrizione1 = tipologiaPasso.getDescrizione();
		codice2 = pcodice2;
		descrizione2 = pdescrizione2;
		controllo = variabileCalcolo.getTipoVariabile().name();
		controlloDescrizione = variabileCalcolo.getTipoVariabile().getDescrizione();
		valore = variabileCalcolo.recuperaValoreString();
		ordine1 = tipologiaPasso.getOrdine();
		ordine2 = pOrdine2;
		if (variabileCalcolo.getTipoVariabile().getOrdinePasso() != null && variabileCalcolo.getTipoVariabile().getOrdinePasso().get(tipologiaPasso) != null)
			ordineControllo = variabileCalcolo.getTipoVariabile().getOrdinePasso().get(tipologiaPasso);
		else
			ordineControllo = variabileCalcolo.getTipoVariabile().getOrdine();
	}

	public ControlloFrontend(String pcodice2, String pcontrollo, String pcontrolloDescr, String pvalore, Integer pordineControllo) {
		codice1 = "SINTESI";
		descrizione1 = "Sintesi";
		codice2 = pcodice2;
		descrizione2 = "";
		controllo = pcontrollo;
		controlloDescrizione = pcontrolloDescr;
		valore = pvalore;
		ordine1 = 1;
		ordine2 = 1;
		ordineControllo = pordineControllo;
	}

	public String getCodice1() {
		return codice1;
	}

	public void setCodice1(String codice1) {
		this.codice1 = codice1;
	}

	public String getDescrizione1() {
		return descrizione1;
	}

	public void setDescrizione1(String descrizione1) {
		this.descrizione1 = descrizione1;
	}

	public String getCodice2() {
		return codice2;
	}

	public void setCodice2(String codice2) {
		this.codice2 = codice2;
	}

	public String getDescrizione2() {
		return descrizione2;
	}

	public void setDescrizione2(String descrizione2) {
		this.descrizione2 = descrizione2;
	}

	public String getControllo() {
		return controllo;
	}

	public String getValore() {
		return valore;
	}

	public String getControlloDescrizione() {
		return controlloDescrizione;
	}

	public void setControlloDescrizione(String controlloDescrizione) {
		this.controlloDescrizione = controlloDescrizione;
	}

	public Integer getOrdine1() {
		return ordine1;
	}

	public Integer getOrdine2() {
		return ordine2;
	}

	public Integer getOrdineControllo() {
		return ordineControllo;
	}

	public void setOrdine1(Integer ordine1) {
		this.ordine1 = ordine1;
	}

	public void setOrdine2(Integer ordine2) {
		this.ordine2 = ordine2;
	}

	public void setControllo(String controllo) {
		this.controllo = controllo;
	}

	public void setOrdineControllo(Integer ordineControllo) {
		this.ordineControllo = ordineControllo;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public ControlloFrontend() {
	}

	@Override
	public int compareTo(ControlloFrontend o) {
		return Comparator.comparing(ControlloFrontend::getOrdine1).thenComparing(ControlloFrontend::getOrdine2).thenComparingInt(ControlloFrontend::getOrdineControllo).compare(this, o);
	}
}
