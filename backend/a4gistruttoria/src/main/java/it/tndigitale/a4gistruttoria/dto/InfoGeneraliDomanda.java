package it.tndigitale.a4gistruttoria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "pac", "tipoDomanda", "campagna", "modulo", "codModulo", "cuaaIntestatario", "ragioneSociale", "numeroDomanda", "numeroDomandaRettificata", "dataPresentazione",
		"codEnteCompilatore", "enteCompilatore" })
public class InfoGeneraliDomanda {

	@JsonProperty("pac")
	private String pac;
	@JsonProperty("tipoDomanda")
	private String tipoDomanda;
	@JsonProperty("campagna")
	private Integer campagna;
	@JsonProperty("modulo")
	private String modulo;
	@JsonProperty("codModulo")
	private String codModulo;
	@JsonProperty("cuaaIntestatario")
	private String cuaaIntestatario;
	@JsonProperty("ragioneSociale")
	private String ragioneSociale;
	@JsonProperty("numeroDomanda")
	private BigDecimal numeroDomanda;
	@JsonProperty("numeroDomandaRettificata")
	private BigDecimal numeroDomandaRettificata;
	@JsonProperty("dataPresentazione")
	private Date dataPresentazione;
	@JsonProperty("dataProtocollazOriginaria")
	private Date dataProtocollazOriginaria;
	@JsonProperty("dataProtocollazione")
	private Date dataProtocollazione;
	@JsonProperty("dataPresentazOriginaria")
	private Date dataPresentazOriginaria;
	@JsonProperty("codEnteCompilatore")
	private String codEnteCompilatore;
	@JsonProperty("enteCompilatore")
	private String enteCompilatore;
	@JsonProperty("stato")
	private String stato;
	@JsonProperty("dtProtocollazioneUltimaModifica")
	private Date dtProtocollazioneUltimaModifica;
	@JsonProperty("numeroDomandaUltimaModifica")
	private BigDecimal numeroDomandaUltimaModifica;

	@JsonView(Pagina.class)
	@JsonProperty("pac")
	public String getPac() {
		return pac;
	}

	@JsonProperty("pac")
	public void setPac(String pac) {
		this.pac = pac;
	}

	@JsonView(Pagina.class)
	@JsonProperty("tipoDomanda")
	public String getTipoDomanda() {
		return tipoDomanda;
	}

	@JsonProperty("tipoDomanda")
	public void setTipoDomanda(String tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
	}

	@JsonView(Pagina.class)
	@JsonProperty("campagna")
	public Integer getCampagna() {
		return campagna;
	}

	@JsonProperty("campagna")
	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	@JsonView(Pagina.class)
	@JsonProperty("modulo")
	public String getModulo() {
		return modulo;
	}

	@JsonProperty("modulo")
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	@JsonView(Pagina.class)
	@JsonProperty("codModulo")
	public String getCodModulo() {
		return codModulo;
	}

	@JsonProperty("codModulo")
	public void setCodModulo(String codModulo) {
		this.codModulo = codModulo;
	}

	@JsonView(Pagina.class)
	@JsonProperty("cuaaIntestatario")
	public String getCuaaIntestatario() {
		return cuaaIntestatario;
	}

	@JsonProperty("cuaaIntestatario")
	public void setCuaaIntestatario(String cuaaIntestatario) {
		this.cuaaIntestatario = cuaaIntestatario;
	}

	@JsonView(Pagina.class)
	@JsonProperty("ragioneSociale")
	public String getRagioneSociale() {
		return ragioneSociale;
	}

	@JsonProperty("ragioneSociale")
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	@JsonView(Pagina.class)
	@JsonProperty("numeroDomanda")
	public BigDecimal getNumeroDomanda() {
		return numeroDomanda;
	}

	@JsonProperty("numeroDomanda")
	public void setNumeroDomanda(BigDecimal numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	@JsonView(Pagina.class)
	@JsonProperty("numeroDomandaRettificata")
	public BigDecimal getNumeroDomandaRettificata() {
		return numeroDomandaRettificata;
	}

	@JsonProperty("numeroDomandaRettificata")
	public void setNumeroDomandaRettificata(BigDecimal numeroDomandaRettificata) {
		this.numeroDomandaRettificata = numeroDomandaRettificata;
	}

	@JsonView(Pagina.class)
	@JsonProperty("dataPresentazione")
	public Date getDataPresentazione() {
		return dataPresentazione;
	}

	@JsonProperty("dataPresentazione")
	public void setDataPresentazione(Date dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	@JsonView(Pagina.class)
	@JsonProperty("codEnteCompilatore")
	public String getCodEnteCompilatore() {
		return codEnteCompilatore;
	}

	@JsonProperty("codEnteCompilatore")
	public void setCodEnteCompilatore(String codEnteCompilatore) {
		this.codEnteCompilatore = codEnteCompilatore;
	}

	@JsonView(Pagina.class)
	@JsonProperty("enteCompilatore")
	public String getEnteCompilatore() {
		return enteCompilatore;
	}

	@JsonProperty("enteCompilatore")
	public void setEnteCompilatore(String enteCompilatore) {
		this.enteCompilatore = enteCompilatore;
	}

	@JsonView(Pagina.class)
	@JsonProperty("dataProtocollazione")
	public Date getDataProtocollazione() {
		return dataProtocollazione;
	}

	@JsonProperty("dataProtocollazione")
	public void setDataProtocollazione(Date dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
	}

	@JsonView(Pagina.class)
	@JsonProperty("dataProtocollazOriginaria")
	public Date getDataProtocollazOriginaria() {
		return dataProtocollazOriginaria;
	}

	@JsonProperty("dataProtocollazOriginaria")
	public void setDataProtocollazOriginaria(Date dataProtocollazOriginaria) {
		this.dataProtocollazOriginaria = dataProtocollazOriginaria;
	}

	@JsonView(Pagina.class)
	@JsonProperty("dataPresentazOriginaria")
	public Date getDataPresentazOriginaria() {
		return dataPresentazOriginaria;
	}

	@JsonProperty("dataPresentazOriginaria")
	public void setDataPresentazOriginaria(Date dataPresentazOriginaria) {
		this.dataPresentazOriginaria = dataPresentazOriginaria;
	}

	@JsonView(Pagina.class)
	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
	
	@JsonView(Pagina.class)
	@JsonProperty("dtProtocollazioneUltimaModifica")
	public Date getDtProtocollazioneUltimaModifica() {
		return dtProtocollazioneUltimaModifica;
	}

	@JsonProperty("dtProtocollazioneUltimaModifica")
	public void setDtProtocollazioneUltimaModifica(Date dtProtocollazioneUltimaModifica) {
		this.dtProtocollazioneUltimaModifica = dtProtocollazioneUltimaModifica;
	}

	@JsonView(Pagina.class)
	public BigDecimal getNumeroDomandaUltimaModifica() {
		return numeroDomandaUltimaModifica;
	}

	public void setNumeroDomandaUltimaModifica(BigDecimal numeroDomandaUltimaModifica) {
		this.numeroDomandaUltimaModifica = numeroDomandaUltimaModifica;
	}


}
