package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.util.Date;
@Deprecated
public class DetenzioneBdn {
	
	private Date dtInizioDetenzione;
	private Date dtFineDetenzione;
	private String cuaa;
	private String codiceAsl;
	private String allevId;
	private Boolean allevamentoMontagna;
	private Date vaccaDtIngresso;
	private Date vaccaDtComAutoritaIngresso;
	private Date vaccaDtInserimentoBdnIngresso;
	private Date vaccaDtInserimentoBdnUscita; 
	private Date dtflagDelegatoIngresso;
	private Date dtUscita;
	
	public Date getDtInizioDetenzione() {
		return dtInizioDetenzione;
	}
	public void setDtInizioDetenzione(Date dtInizioDetenzione) {
		this.dtInizioDetenzione = dtInizioDetenzione;
	}
	public Date getDtFineDetenzione() {
		return dtFineDetenzione;
	}
	public void setDtFineDetenzione(Date dtFineDetenzione) {
		this.dtFineDetenzione = dtFineDetenzione;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public String getCodiceAsl() {
		return codiceAsl;
	}
	public void setCodiceAsl(String codiceAsl) {
		this.codiceAsl = codiceAsl;
	}
	public Boolean getAllevamentoMontagna() {
		return allevamentoMontagna;
	}
	public void setAllevamentoMontagna(Boolean allevamentoMontagna) {
		this.allevamentoMontagna = allevamentoMontagna;
	}
	public Date getVaccaDtIngresso() {
		return vaccaDtIngresso;
	}
	public void setVaccaDtIngresso(Date vaccaDtIngresso) {
		this.vaccaDtIngresso = vaccaDtIngresso;
	}
	public Date getVaccaDtComAutoritaIngresso() {
		return vaccaDtComAutoritaIngresso;
	}
	public void setVaccaDtComAutoritaIngresso(Date vaccaDtComAutoritaIngresso) {
		this.vaccaDtComAutoritaIngresso = vaccaDtComAutoritaIngresso;
	}
	public Date getVaccaDtInserimentoBdnIngresso() {
		return vaccaDtInserimentoBdnIngresso;
	}
	public void setVaccaDtInserimentoBdnIngresso(Date vaccaDtInserimentoBdnIngresso) {
		this.vaccaDtInserimentoBdnIngresso = vaccaDtInserimentoBdnIngresso;
	}
	public Date getDtUscita() {
		return dtUscita;
	}
	public void setDtUscita(Date dtUscita) {
		this.dtUscita = dtUscita;
	}
	public Date getVaccaDtInserimentoBdnUscita() {
		return vaccaDtInserimentoBdnUscita;
	}
	public void setVaccaDtInserimentoBdnUscita(Date vaccaDtInserimentoBdnUscita) {
		this.vaccaDtInserimentoBdnUscita = vaccaDtInserimentoBdnUscita;
	}
	public Date getDtflagDelegatoIngresso() {
		return dtflagDelegatoIngresso;
	}
	public void setDtflagDelegatoIngresso(Date dtflagDelegatoIngresso) {
		this.dtflagDelegatoIngresso = dtflagDelegatoIngresso;
	}
	public String getAllevId() {
		return allevId;
	}
	public void setAllevId(String allevId) {
		this.allevId = allevId;
	}
	
}
