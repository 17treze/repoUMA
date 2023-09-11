package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;


@Entity
@Table(name="TAB_AGRI_UMAL_COLTURA_GRUPPI")
public class ColturaGruppiModel  extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = -962678114269147325L;

	@Column(name="ANNO_FINE", nullable = true, length = 4)
	private Integer annoFine;

	@Column(name="ANNO_INIZIO", nullable = false, length = 4)
	private Integer annoInizio;

	@Column(name="CODICE_DEST_USO", nullable = false, length = 3)
	private String codiceDestUso;

	@Column(name="CODICE_QUALITA", nullable = false, length = 3)
	private String codiceQualita;

	@Column(name="CODICE_SUOLO", nullable = false, length = 3)
	private String codiceSuolo;

	@Column(name="CODICE_USO", nullable = false, length = 3)
	private String codiceUso;

	@Column(name="CODICE_VARIETA", nullable = false, length = 3)
	private String codiceVarieta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRUPPO_LAVORAZIONE")
	private GruppoLavorazioneModel gruppoLavorazione;

	public Integer getAnnoFine() {
		return this.annoFine;
	}
	public ColturaGruppiModel setAnnoFine(Integer annoFine) {
		this.annoFine = annoFine;
		return this;
	}
	public Integer getAnnoInizio() {
		return this.annoInizio;
	}
	public ColturaGruppiModel setAnnoInizio(Integer annoInizio) {
		this.annoInizio = annoInizio;
		return this;
	}
	public String getCodiceDestUso() {
		return this.codiceDestUso;
	}
	public ColturaGruppiModel setCodiceDestUso(String codiceDestUso) {
		this.codiceDestUso = codiceDestUso;
		return this;
	}
	public String getCodiceQualita() {
		return this.codiceQualita;
	}
	public ColturaGruppiModel setCodiceQualita(String codiceQualita) {
		this.codiceQualita = codiceQualita;
		return this;
	}
	public String getCodiceSuolo() {
		return this.codiceSuolo;
	}
	public ColturaGruppiModel setCodiceSuolo(String codiceSuolo) {
		this.codiceSuolo = codiceSuolo;
		return this;
	}
	public String getCodiceUso() {
		return this.codiceUso;
	}
	public ColturaGruppiModel setCodiceUso(String codiceUso) {
		this.codiceUso = codiceUso;
		return this;
	}
	public String getCodiceVarieta() {
		return this.codiceVarieta;
	}
	public ColturaGruppiModel setCodiceVarieta(String codiceVarieta) {
		this.codiceVarieta = codiceVarieta;
		return this;
	}
	public GruppoLavorazioneModel getGruppoLavorazione() {
		return gruppoLavorazione;
	}
	public ColturaGruppiModel setGruppoLavorazione(GruppoLavorazioneModel gruppiLavorazioneModel) {
		this.gruppoLavorazione = gruppiLavorazioneModel;
		return this;
	}
}