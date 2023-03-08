package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.util.Date;

@Deprecated
public class VitelloBdn {
	
	private String codVitello;
	private Date dtNascita;
	private String tipoOrigine;
	private Date dtInserimentoBdnNascita;
	private String flagDelegatoNascitaVitello;
	private String flagProrogaMarcatura;
	private String allevId;
	private String aziendaCodice;
	
	public String getCodVitello() {
		return codVitello;
	}
	public void setCodVitello(String codVitello) {
		this.codVitello = codVitello;
	}
	public Date getDtNascita() {
		return dtNascita;
	}
	public void setDtNascita(Date dtNascita) {
		this.dtNascita = dtNascita;
	}
	public String getTipoOrigine() {
		return tipoOrigine;
	}
	public void setTipoOrigine(String tipoOrigine) {
		this.tipoOrigine = tipoOrigine;
	}
	public Date getDtInserimentoBdnNascita() {
		return dtInserimentoBdnNascita;
	}
	public void setDtInserimentoBdnNascita(Date dtInserimentoBdnNascita) {
		this.dtInserimentoBdnNascita = dtInserimentoBdnNascita;
	}
	public String getFlagDelegatoNascitaVitello() {
		return flagDelegatoNascitaVitello;
	}
	public void setFlagDelegatoNascitaVitello(String flagDelegatoNascitaVitello) {
		this.flagDelegatoNascitaVitello = flagDelegatoNascitaVitello;
	}
	public String getFlagProrogaMarcatura() {
		return flagProrogaMarcatura;
	}
	public void setFlagProrogaMarcatura(String flagProrogaMarcatura) {
		this.flagProrogaMarcatura = flagProrogaMarcatura;
	}
	public String getAllevId() {
		return allevId;
	}
	public void setAllevId(String allevId) {
		this.allevId = allevId;
	}
	public String getAziendaCodice() {
		return aziendaCodice;
	}
	public void setAziendaCodice(String aziendaCodice) {
		this.aziendaCodice = aziendaCodice;
	}

}
