package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipologiaPossesso;

public class DettaglioMacchinaDto implements Serializable {

	private static final long serialVersionUID = -8231808295467361325L;

	private Long id;
	private SottotipoDto sottotipo;
	private String marca;
	private String modello;
	private String numeroMatricola;
	private String numeroTelaio;
	private Integer annoCostruzione;
	private TipologiaPossesso tipoPossesso;
	private String targa;
	private LocalDateTime dataImmatricolazione;
	private MotoreDto motore;
	private String codiceFiscale;
	private String ragioneSociale;
	private long flagMigrato;

	public Long getId() {
		return id;
	}

	public DettaglioMacchinaDto setId(Long id) {
		this.id = id;
		return this;
	}

	public SottotipoDto getSottotipo() {
		return sottotipo;
	}

	public DettaglioMacchinaDto setSottotipo(SottotipoDto sottotipo) {
		this.sottotipo = sottotipo;
		return this;
	}

	public String getMarca() {
		return marca;
	}

	public DettaglioMacchinaDto setMarca(String marca) {
		this.marca = marca;
		return this;
	}

	public String getModello() {
		return modello;
	}

	public DettaglioMacchinaDto setModello(String modello) {
		this.modello = modello;
		return this;
	}

	public String getNumeroMatricola() {
		return numeroMatricola;
	}

	public DettaglioMacchinaDto setNumeroMatricola(String numeroMatricola) {
		this.numeroMatricola = numeroMatricola;
		return this;
	}

	public String getNumeroTelaio() {
		return numeroTelaio;
	}

	public DettaglioMacchinaDto setNumeroTelaio(String numeroTelaio) {
		this.numeroTelaio = numeroTelaio;
		return this;
	}

	public Integer getAnnoCostruzione() {
		return annoCostruzione;
	}

	public DettaglioMacchinaDto setAnnoCostruzione(Integer annoCostruzione) {
		this.annoCostruzione = annoCostruzione;
		return this;
	}

	public TipologiaPossesso getTipoPossesso() {
		return tipoPossesso;
	}

	public DettaglioMacchinaDto setTipoPossesso(TipologiaPossesso tipoPossesso) {
		this.tipoPossesso = tipoPossesso;
		return this;
	}

	public String getTarga() {
		return targa;
	}

	public DettaglioMacchinaDto setTarga(String targa) {
		this.targa = targa;
		return this;
	}

	public LocalDateTime getDataImmatricolazione() {
		return dataImmatricolazione;
	}

	public DettaglioMacchinaDto setDataImmatricolazione(LocalDateTime dataImmatricolazione) {
		this.dataImmatricolazione = dataImmatricolazione;
		return this;
	}

	public MotoreDto getMotore() {
		return motore;
	}

	public DettaglioMacchinaDto setMotore(MotoreDto motore) {
		this.motore = motore;
		return this;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public long getFlagMigrato() {
		return flagMigrato;
	}

	public void setFlagMigrato(long flagMigrato) {
		this.flagMigrato = flagMigrato;
	}

}
