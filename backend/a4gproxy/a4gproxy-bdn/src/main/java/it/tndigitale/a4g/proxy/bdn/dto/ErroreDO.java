package it.tndigitale.a4g.proxy.bdn.dto;

import java.util.GregorianCalendar;

public class ErroreDO {

	public static final String anomaliaCoordinamento = "AnomaliaCoordinamento";
	public static final String ingressiAlPascoloPartiteOvini = "IngressiAlPascoloPartiteOvini";
	public static final String usciteDaPascoloPartiteOvini = "UsciteDaPascoloPartiteOvini";
	public static final String consistenzaUBAConsimOvini2012 = "ConsistenzaUBACensimOvini2012";
	public static final String consistenzaAllevamento = "ConsistenzaAllevamento";
	public static final String consistenzaAlPascoloPAC2015 = "ConsistenzaAlPascoloPAC2015";
	public static final String trovaPascoloPAC2015 = "TrovaPascoloPAC2015";
	public static final String segnalazione = "Segnalazione";
	public static final String capiPremio = "GetElencoCapiPremio";

	private String CUAA;
	private String tipoOperazione;
	private GregorianCalendar dateTime;
	private Exception eccezione;

	public ErroreDO(String cUAA, String tipoOperazione, GregorianCalendar dateTime, Exception eccezione) {
		super();
		CUAA = cUAA;
		this.tipoOperazione = tipoOperazione;
		this.dateTime = dateTime;
		this.eccezione = eccezione;
	}

	public String getCUAA() {
		return CUAA;
	}

	public void setCUAA(String cUAA) {
		CUAA = cUAA;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public GregorianCalendar getDateTime() {
		return dateTime;
	}

	public void setDateTime(GregorianCalendar dateTime) {
		this.dateTime = dateTime;
	}

	public Exception getEccezione() {
		return eccezione;
	}

	public void setEccezione(Exception eccezione) {
		this.eccezione = eccezione;
	}

}
