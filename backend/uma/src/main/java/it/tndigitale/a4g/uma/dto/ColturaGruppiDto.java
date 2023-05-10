package it.tndigitale.a4g.uma.dto;

public class ColturaGruppiDto {
	
	private Integer annoFine;
	private Integer annoInizio;
	private String codiceDestUso;
	private String codiceQualita;
	private String codiceSuolo;
	private String codiceUso;
	private String codiceVarieta;
	private Long gruppoLavorazione;
	
	public Integer getAnnoFine() {
		return this.annoFine;
	}
	
	public ColturaGruppiDto setAnnoFine(Integer annoFine) {
		this.annoFine = annoFine;
		return this;
	}
	
	public Integer getAnnoInizio() {
		return this.annoInizio;
	}
	
	public ColturaGruppiDto setAnnoInizio(Integer annoInizio) {
		this.annoInizio = annoInizio;
		return this;
	}
	
	public String getCodiceDestUso() {
		return this.codiceDestUso;
	}
	
	public ColturaGruppiDto setCodiceDestUso(String codiceDestUso) {
		this.codiceDestUso = codiceDestUso;
		return this;
	}
	
	public String getCodiceQualita() {
		return this.codiceQualita;
	}
	
	public ColturaGruppiDto setCodiceQualita(String codiceQualita) {
		this.codiceQualita = codiceQualita;
		return this;
	}
	
	public String getCodiceSuolo() {
		return this.codiceSuolo;
	}
	
	public ColturaGruppiDto setCodiceSuolo(String codiceSuolo) {
		this.codiceSuolo = codiceSuolo;
		return this;
	}
	
	public String getCodiceUso() {
		return this.codiceUso;
	}
	
	public ColturaGruppiDto setCodiceUso(String codiceUso) {
		this.codiceUso = codiceUso;
		return this;
	}
	
	public String getCodiceVarieta() {
		return this.codiceVarieta;
	}
	
	public ColturaGruppiDto setCodiceVarieta(String codiceVarieta) {
		this.codiceVarieta = codiceVarieta;
		return this;
	}
	
	public Long getGruppoLavorazione() {
		return gruppoLavorazione;
	}
	
	public ColturaGruppiDto setGruppoLavorazione(Long gruppoLavorazione) {
		this.gruppoLavorazione = gruppoLavorazione;
		return this;
	}
}