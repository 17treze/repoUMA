package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.util.Date;
import java.util.List;

@Deprecated
public class CapoOvicaprinoBdn {

	private List<DetenzioneBdn>  detenzioniBdn;
	private Date dtApplMarchio;
	private Date dtInserimentoBdnNascita;

	public List<DetenzioneBdn> getDetenzioniBdn() {
		return detenzioniBdn;
	}
	public void setDetenzioniBdn(List<DetenzioneBdn> detenzioniBdn) {
		this.detenzioniBdn = detenzioniBdn;
	}
	public Date getDtApplMarchio() {
		return dtApplMarchio;
	}
	public void setDtApplMarchio(Date dtApplMarchio) {
		this.dtApplMarchio = dtApplMarchio;
	}
	public Date getDtInserimentoBdnNascita() {
		return dtInserimentoBdnNascita;
	}
	public void setDtInserimentoBdnNascita(Date dtInserimentoBdnNascita) {
		this.dtInserimentoBdnNascita = dtInserimentoBdnNascita;
	}
}
