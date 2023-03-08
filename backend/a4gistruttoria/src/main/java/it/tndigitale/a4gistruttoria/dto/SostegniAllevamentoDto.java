package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;

import java.io.Serializable;

public class SostegniAllevamentoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sostegno;
	private Long idIntervento;
	private CodiceInterventoAgs codIntervento;
	private String descIntervento;
	private Long idAllevamento;
	private String codIdAllevamento;
	private String codIdBdn;
	private String descAllevamento;
	private String specie;
	private String comune;
	private String indirizzo;
	private String codFiscaleProprietario;
	private String denominazioneProprietario;
	private String codFiscaleDetentore;
	private String denominazioneDetentore;

	public String getSostegno() {
		return sostegno;
	}

	public void setSostegno(String sostegno) {
		this.sostegno = sostegno;
	}

	public Long getIdIntervento() {
		return idIntervento;
	}

	public void setIdIntervento(Long idIntervento) {
		this.idIntervento = idIntervento;
	}

	public CodiceInterventoAgs getCodIntervento() {
		return codIntervento;
	}

	public void setCodIntervento(CodiceInterventoAgs codIntervento) {
		this.codIntervento = codIntervento;
	}

	public String getDescIntervento() {
		return descIntervento;
	}

	public void setDescIntervento(String descIntervento) {
		this.descIntervento = descIntervento;
	}

	public Long getIdAllevamento() {
		return idAllevamento;
	}

	public void setIdAllevamento(Long idAllevamento) {
		this.idAllevamento = idAllevamento;
	}

	public String getCodIdAllevamento() {
		return codIdAllevamento;
	}

	public void setCodIdAllevamento(String codIdAllevamento) {
		this.codIdAllevamento = codIdAllevamento;
	}

	public String getCodIdBdn() {
		return codIdBdn;
	}

	public void setCodIdBdn(String codIdBdn) {
		this.codIdBdn = codIdBdn;
	}

	public String getDescAllevamento() {
		return descAllevamento;
	}

	public void setDescAllevamento(String descAllevamento) {
		this.descAllevamento = descAllevamento;
	}

	public String getSpecie() {
		return specie;
	}

	public void setSpecie(String specie) {
		this.specie = specie;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCodFiscaleProprietario() {
		return codFiscaleProprietario;
	}

	public void setCodFiscaleProprietario(String codFiscaleProprietario) {
		this.codFiscaleProprietario = codFiscaleProprietario;
	}

	public String getDenominazioneProprietario() {
		return denominazioneProprietario;
	}

	public void setDenominazioneProprietario(String denominazioneProprietario) {
		this.denominazioneProprietario = denominazioneProprietario;
	}

	public String getCodFiscaleDetentore() {
		return codFiscaleDetentore;
	}

	public void setCodFiscaleDetentore(String codFiscaleDetentore) {
		this.codFiscaleDetentore = codFiscaleDetentore;
	}

	public String getDenominazioneDetentore() {
		return denominazioneDetentore;
	}

	public void setDenominazioneDetentore(String denominazioneDetentore) {
		this.denominazioneDetentore = denominazioneDetentore;
	}
}
