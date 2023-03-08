package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.util.Date;
import java.util.List;

@Deprecated
public class CapoMacellatoBdn {
	
	private List<DetenzioneBdn>  detenzioniBdn;
	private Date dtMacellazione;
	private Date dtApplMarchio;
	
	public List<DetenzioneBdn> getDetenzioniBdn() {
		return detenzioniBdn;
	}

	public void setDetenzioniBdn(List<DetenzioneBdn> detenzioniBdn) {
		this.detenzioniBdn = detenzioniBdn;
	}

	public Date getDtMacellazione() {
		return dtMacellazione;
	}

	public void setDtMacellazione(Date dtMacellazione) {
		this.dtMacellazione = dtMacellazione;
	}

	public Date getDtApplMarchio() {
		return dtApplMarchio;
	}

	public void setDtApplMarchio(Date dtApplMarchio) {
		this.dtApplMarchio = dtApplMarchio;
	}

}
