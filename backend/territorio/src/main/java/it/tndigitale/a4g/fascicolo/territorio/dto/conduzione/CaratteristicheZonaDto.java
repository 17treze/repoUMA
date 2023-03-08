package it.tndigitale.a4g.fascicolo.territorio.dto.conduzione;

import java.io.Serializable;

public class CaratteristicheZonaDto implements Serializable {

	private static final long serialVersionUID = 1492621942600824568L;

	private String casiParticolari;
	private String casiParticolariDescrizione;
	private String flagGiust;
	private String flagGiustDescrizione;
	private String codiZVN;
	private String codiZVNDescrizione;

	public String getCasiParticolari() {
		return casiParticolari;
	}

	public void setCasiParticolari(String casiParticolari) {
		this.casiParticolari = casiParticolari;
	}

	public String getFlagGiust() {
		return flagGiust;
	}

	public void setFlagGiust(String flagGiust) {
		this.flagGiust = flagGiust;
	}

	public String getCodiZVN() {
		return codiZVN;
	}

	public void setCodiZVN(String codiZVN) {
		this.codiZVN = codiZVN;
	}

	public String getCasiParticolariDescrizione() {
		return casiParticolariDescrizione;
	}

	public void setCasiParticolariDescrizione(String casiParticolariDescrizione) {
		this.casiParticolariDescrizione = casiParticolariDescrizione;
	}

	public String getFlagGiustDescrizione() {
		return flagGiustDescrizione;
	}

	public void setFlagGiustDescrizione(String flagGiustDescrizione) {
		this.flagGiustDescrizione = flagGiustDescrizione;
	}

	public String getCodiZVNDescrizione() {
		return codiZVNDescrizione;
	}

	public void setCodiZVNDescrizione(String codiZVNDescrizione) {
		this.codiZVNDescrizione = codiZVNDescrizione;
	}
}
