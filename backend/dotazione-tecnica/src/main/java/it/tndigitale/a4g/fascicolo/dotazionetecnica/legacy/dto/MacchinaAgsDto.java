package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto;

public class MacchinaAgsDto {

	private Long idAgs;
	private String descrizione;
	private String targa;
	private String classe;
	private String sottoClasse;
	private String marca;
	private TipoCarburante alimentazione;
	private String possesso;
	private String codiceClasse;
	private String codiceSottoClasse;
	private String telaio;
	private String matricola;
	private String marcaMotore;
	private String tipoMotore;
	private Double potenzaKw;
	private Long idTipoMacchina;
	private Long idMacchina;

	public Long getIdAgs() {
		return idAgs;
	}

	public MacchinaAgsDto setIdAgs(Long idAgs) {
		this.idAgs = idAgs;
		return this;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public MacchinaAgsDto setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}

	public String getTarga() {
		return targa;
	}

	public MacchinaAgsDto setTarga(String targa) {
		this.targa = targa;
		return this;
	}

	public String getClasse() {
		return classe;
	}

	public MacchinaAgsDto setClasse(String classe) {
		this.classe = classe;
		return this;
	}

	public String getMarca() {
		return marca;
	}

	public MacchinaAgsDto setMarca(String marca) {
		this.marca = marca;
		return this;
	}

	public TipoCarburante getAlimentazione() {
		return alimentazione;
	}

	public MacchinaAgsDto setAlimentazione(TipoCarburante alimentazione) {
		this.alimentazione = alimentazione;
		return this;
	}

	public String getPossesso() {
		return possesso;
	}

	public MacchinaAgsDto setPossesso(String possesso) {
		this.possesso = possesso;
		return this;
	}

	public String getSottoClasse() {
		return sottoClasse;
	}

	public MacchinaAgsDto setSottoClasse(String sottoClasse) {
		this.sottoClasse = sottoClasse;
		return this;
	}

	public String getCodiceClasse() {
		return codiceClasse;
	}

	public MacchinaAgsDto setCodiceClasse(String codiceClasse) {
		this.codiceClasse = codiceClasse;
		return this;
	}

	public String getCodiceSottoClasse() {
		return codiceSottoClasse;
	}

	public MacchinaAgsDto setCodiceSottoClasse(String codiceSottoClasse) {
		this.codiceSottoClasse = codiceSottoClasse;
		return this;
	}

	public String getTelaio() {
		return telaio;
	}

	public MacchinaAgsDto setTelaio(String telaio) {
		this.telaio = telaio;
		return this;
	}

	public String getMatricola() {
		return matricola;
	}

	public MacchinaAgsDto setMatricola(String matricola) {
		this.matricola = matricola;
		return this;
	}

	public String getMarcaMotore() {
		return marcaMotore;
	}

	public MacchinaAgsDto setMarcaMotore(String marcaMotore) {
		this.marcaMotore = marcaMotore;
		return this;
	}

	public String getTipoMotore() {
		return tipoMotore;
	}

	public MacchinaAgsDto setTipoMotore(String tipoMotore) {
		this.tipoMotore = tipoMotore;
		return this;
	}

	public Double getPotenzaKw() {
		return potenzaKw;
	}

	public MacchinaAgsDto setPotenzaKw(Double potenzaKw) {
		this.potenzaKw = potenzaKw;
		return this;
	}

	public Long getIdTipoMacchina() {
		return idTipoMacchina;
	}

	public void setIdTipoMacchina(Long idTipoMacchina) {
		this.idTipoMacchina = idTipoMacchina;
	}

	public Long getIdMacchina() {
		return idMacchina;
	}

	public void setIdMacchina(Long idMacchina) {
		this.idMacchina = idMacchina;
	}

}
