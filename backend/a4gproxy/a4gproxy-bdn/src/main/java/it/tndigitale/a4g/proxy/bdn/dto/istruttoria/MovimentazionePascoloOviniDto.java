package it.tndigitale.a4g.proxy.bdn.dto.istruttoria;

import java.math.BigDecimal;
import java.sql.Date;

public class MovimentazionePascoloOviniDto {

	private BigDecimal idAllevamento;
	private String codiceFiscaleDetentore;
	private BigDecimal idPascolo;
	private String codPascolo;
	private String comunePascolo;
	private BigDecimal annoCampagna;
	private Date dataIngresso;
	private Date dataUscita;
	private Long giorniPascolamento;
	private BigDecimal numeroCapi;

	public BigDecimal getIdAllevamento() {
		return idAllevamento;
	}

	public void setIdAllevamento(BigDecimal idAllevamento) {
		this.idAllevamento = idAllevamento;
	}

	public String getCodiceFiscaleDetentore() {
		return codiceFiscaleDetentore;
	}

	public void setCodiceFiscaleDetentore(String codiceFiscaleDetentore) {
		this.codiceFiscaleDetentore = codiceFiscaleDetentore;
	}

	public BigDecimal getIdPascolo() {
		return idPascolo;
	}

	public void setIdPascolo(BigDecimal idPascolo) {
		this.idPascolo = idPascolo;
	}

	public String getCodPascolo() {
		return codPascolo;
	}

	public void setCodPascolo(String codPascolo) {
		this.codPascolo = codPascolo;
	}

	public String getComunePascolo() {
		return comunePascolo;
	}

	public void setComunePascolo(String comunePascolo) {
		this.comunePascolo = comunePascolo;
	}

	public BigDecimal getAnnoCampagna() {
		return annoCampagna;
	}

	public void setAnnoCampagna(BigDecimal annoCampagna) {
		this.annoCampagna = annoCampagna;
	}

	public Date getDataIngresso() {
		return dataIngresso;
	}

	public void setDataIngresso(Date dataIngresso) {
		this.dataIngresso = dataIngresso;
	}

	public Date getDataUscita() {
		return dataUscita;
	}

	public void setDataUscita(Date dataUscita) {
		this.dataUscita = dataUscita;
	}

	public Long getGiorniPascolamento() {
		return giorniPascolamento;
	}

	public void setGiorniPascolamento(Long giorniPascolamento) {
		this.giorniPascolamento = giorniPascolamento;
	}

	public BigDecimal getNumeroCapi() {
		return numeroCapi;
	}

	public void setNumeroCapi(BigDecimal numeroCapi) {
		this.numeroCapi = numeroCapi;
	}

}
