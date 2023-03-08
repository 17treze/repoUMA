package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.time.LocalDate;

public class VitelloDto {

	private String marcaAuricolare;
	private LocalDate dataNascita;	
	private String tipoOrigine; 
	private LocalDate dtInserimentoBdnNascita;
	private String flagDelegatoNascitaVitello;
	private String flagProrogaMarcatura;
	private LocalDate dtApplicazioneMarchio;

	public String getMarcaAuricolare() {
		return marcaAuricolare;
	}
	public VitelloDto setMarcaAuricolare(String marcaAuricolare) {
		this.marcaAuricolare = marcaAuricolare;
		return this;
	}
	public LocalDate getDataNascita() {
		return dataNascita;
	}
	public VitelloDto setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
		return this;
	}
	public String getTipoOrigine() {
		return tipoOrigine;
	}
	public VitelloDto setTipoOrigine(String tipoOrigine) {
		this.tipoOrigine = tipoOrigine;
		return this;
	}
	public LocalDate getDtInserimentoBdnNascita() {
		return dtInserimentoBdnNascita;
	}
	public VitelloDto setDtInserimentoBdnNascita(LocalDate dtInserimentoBdnNascita) {
		this.dtInserimentoBdnNascita = dtInserimentoBdnNascita;
		return this;
	}
	public String getFlagDelegatoNascitaVitello() {
		return flagDelegatoNascitaVitello;
	}
	public VitelloDto setFlagDelegatoNascitaVitello(String flagDelegatoNascitaVitello) {
		this.flagDelegatoNascitaVitello = flagDelegatoNascitaVitello;
		return this;
	}
	public String getFlagProrogaMarcatura() {
		return flagProrogaMarcatura;
	}
	public VitelloDto setFlagProrogaMarcatura(String flagProrogaMarcatura) {
		this.flagProrogaMarcatura = flagProrogaMarcatura;
		return this;
	}
	public LocalDate getDtApplicazioneMarchio() {
		return dtApplicazioneMarchio;
	}
	public VitelloDto setDtApplicazioneMarchio(LocalDate dtApplicazioneMarchio) {
		this.dtApplicazioneMarchio = dtApplicazioneMarchio;
		return this;
	}
}
