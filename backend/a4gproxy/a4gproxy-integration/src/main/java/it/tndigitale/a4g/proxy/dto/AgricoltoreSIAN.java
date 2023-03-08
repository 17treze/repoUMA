package it.tndigitale.a4g.proxy.dto;

import java.math.BigDecimal;
import java.util.List;

public class AgricoltoreSIAN {

	private InfoAgricoltoreSIAN infoAgricoltoreSIAN;
	private List<TitoloAgricoltoreSIAN> titoliSIAN;
	private BigDecimal flagGiovAgri;

	public AgricoltoreSIAN(InfoAgricoltoreSIAN infoAgricoltoreSIAN, List<TitoloAgricoltoreSIAN> titoliAgricoltoreSIAN, BigDecimal flagGiovAgri) {
		this.infoAgricoltoreSIAN = infoAgricoltoreSIAN;
		this.titoliSIAN = titoliAgricoltoreSIAN;
		this.flagGiovAgri = flagGiovAgri;
	}

	public AgricoltoreSIAN() {

	}

	public InfoAgricoltoreSIAN getInfoAgricoltoreSIAN() {
		return infoAgricoltoreSIAN;
	}

	public void setInfoAgricoltoreSIAN(InfoAgricoltoreSIAN infoAgricoltoreSIAN) {
		this.infoAgricoltoreSIAN = infoAgricoltoreSIAN;
	}

	public List<TitoloAgricoltoreSIAN> getTitoliSIAN() {
		return titoliSIAN;
	}

	public void setTitoliSIAN(List<TitoloAgricoltoreSIAN> titoliSIAN) {
		this.titoliSIAN = titoliSIAN;
	}

	public BigDecimal getFlagGiovAgri() {
		return flagGiovAgri;
	}

	public void setFlagGiovAgri(BigDecimal flagGiovAgri) {
		this.flagGiovAgri = flagGiovAgri;
	}

}
