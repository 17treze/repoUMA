package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipologiaPossesso;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

@Entity
@Table(name = "A4GT_MACCHINA")
@Inheritance(strategy = InheritanceType.JOINED)
public class MacchinaModel extends EntitaDominioFascicolo {
	private static final long serialVersionUID = 5434941794078730642L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FASCICOLO", referencedColumnName = "ID")
	@JoinColumn(name = "ID_VALIDAZIONE_FASCICOLO", referencedColumnName = "ID_VALIDAZIONE")
	private FascicoloModel fascicolo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SOTTOTIPO", referencedColumnName = "ID")
	private SottotipoModel sottotipoMacchinario;

	@Column(name = "MARCA", length = 50)
	private String marca;

	@Column(name = "MODELLO", length = 50)
	private String modello;

	@Column(name = "NUMERO_MATRICOLA", length = 50)
	private String numeroMatricola;

	@Column(name = "NUMERO_TELAIO", length = 50)
	private String numeroTelaio;

	@Column(name = "ANNO_DI_COSTRUZIONE", length = 4)
	private Integer annoDiCostruzione;

	@Column(name = "TIPO_POSSESSO", length = 50)
	@Enumerated(EnumType.STRING)
	private TipologiaPossesso tipoPossesso;

	@Lob
	@Column(name = "DOCUMENTO_POSSESSO")
	private byte[] documentoPossesso;

	@Column(name = "TARGA", length = 50)
	private String targa;

	@Column(name = "DATA_IMMATRICOLAZIONE")
	private LocalDateTime dataImmatricolazione;

	@Column(name = "CF_PROPRIETARIO")
	private String codiceFiscale;

	@Column(name = "DENOMINAZIONE")
	private String ragioneSociale;

	@Column(name = "FLAG_MIGRATO")
	private Long flagMigrato;

	public String getMarca() {
		return marca;
	}

	public MacchinaModel setMarca(String marca) {
		this.marca = marca;
		return this;
	}

	public String getModello() {
		return modello;
	}

	public MacchinaModel setModello(String modello) {
		this.modello = modello;
		return this;
	}

	public String getNumeroMatricola() {
		return numeroMatricola;
	}

	public MacchinaModel setNumeroMatricola(String numeroMatricola) {
		this.numeroMatricola = numeroMatricola;
		return this;
	}

	public String getNumeroTelaio() {
		return numeroTelaio;
	}

	public MacchinaModel setNumeroTelaio(String numeroTelaio) {
		this.numeroTelaio = numeroTelaio;
		return this;
	}

	public Integer getAnnoDiCostruzione() {
		return annoDiCostruzione;
	}

	public MacchinaModel setAnnoDiCostruzione(Integer annoDiCostruzione) {
		this.annoDiCostruzione = annoDiCostruzione;
		return this;
	}

	public TipologiaPossesso getTipoPossesso() {
		return tipoPossesso;
	}

	public MacchinaModel setTipoPossesso(TipologiaPossesso tipoPossesso) {
		this.tipoPossesso = tipoPossesso;
		return this;
	}

	public byte[] getDocumentoPossesso() {
		return documentoPossesso;
	}

	public MacchinaModel setDocumentoPossesso(byte[] documentoPossesso) {
		this.documentoPossesso = documentoPossesso;
		return this;
	}

	public String getTarga() {
		return targa;
	}

	public MacchinaModel setTarga(String targa) {
		this.targa = targa;
		return this;
	}

	public LocalDateTime getDataImmatricolazione() {
		return dataImmatricolazione;
	}

	public MacchinaModel setDataImmatricolazione(LocalDateTime dataImmatricolazione) {
		this.dataImmatricolazione = dataImmatricolazione;
		return this;
	}

	public SottotipoModel getSottotipoMacchinario() {
		return sottotipoMacchinario;
	}

	public MacchinaModel setSottotipoMacchinario(SottotipoModel sottotipoMacchinario) {
		this.sottotipoMacchinario = sottotipoMacchinario;
		return this;
	}

	public FascicoloModel getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(FascicoloModel fascicolo) {
		this.fascicolo = fascicolo;
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

	public Long getFlagMigrato() {
		return flagMigrato;
	}

	public void setFlagMigrato(Long flagMigrato) {
		this.flagMigrato = flagMigrato;
	}

}
