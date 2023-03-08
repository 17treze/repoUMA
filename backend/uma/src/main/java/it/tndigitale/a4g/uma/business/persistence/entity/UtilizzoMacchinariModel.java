package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="A4GT_UTILIZZO_MACCHINARI")
public class UtilizzoMacchinariModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = -2146875654751667900L;

	@Column(name="DESCRIZIONE", length = 50)
	private String descrizione;

	@Column(name="CLASSE", length = 50)
	private String classe;

	@Column(name="MARCA", length = 50)
	private String marca;

	@Column(name="ALIMENTAZIONE", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private TipoCarburante alimentazione;

	@Column(name="TARGA", length = 50)
	private String targa;

	@Column(name="POSSESSO", length = 50)
	private String possesso;

	@Column(name="FLAG_UTILIZZO", nullable = false)
	private Boolean flagUtilizzo;

	@Column(name="IDENTIFICATIVO_AGS", nullable = false)
	private Long identificativoAgs;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RICHIESTA")
	private RichiestaCarburanteModel richiestaCarburante;

	public String getDescrizione() {
		return descrizione;
	}
	public UtilizzoMacchinariModel setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}
	public String getClasse() {
		return classe;
	}
	public UtilizzoMacchinariModel setClasse(String classe) {
		this.classe = classe;
		return this;
	}
	public String getMarca() {
		return marca;
	}
	public UtilizzoMacchinariModel setMarca(String marca) {
		this.marca = marca;
		return this;
	}
	public TipoCarburante getAlimentazione() {
		return alimentazione;
	}
	public UtilizzoMacchinariModel setAlimentazione(TipoCarburante alimentazione) {
		this.alimentazione = alimentazione;
		return this;
	}
	public String getTarga() {
		return targa;
	}
	public UtilizzoMacchinariModel setTarga(String targa) {
		this.targa = targa;
		return this;
	}
	public String getPossesso() {
		return possesso;
	}
	public UtilizzoMacchinariModel setPossesso(String possesso) {
		this.possesso = possesso;
		return this;
	}
	public Boolean getFlagUtilizzo() {
		return flagUtilizzo;
	}
	public UtilizzoMacchinariModel setFlagUtilizzo(Boolean flagUtilizzo) {
		this.flagUtilizzo = flagUtilizzo;
		return this;
	}
	public Long getIdentificativoAgs() {
		return identificativoAgs;
	}
	public UtilizzoMacchinariModel setIdentificativoAgs(Long identificativoAgs) {
		this.identificativoAgs = identificativoAgs;
		return this;
	}
	public RichiestaCarburanteModel getRichiestaCarburante() {
		return richiestaCarburante;
	}
	public UtilizzoMacchinariModel setRichiestaCarburante(RichiestaCarburanteModel richiestaCarburante) {
		this.richiestaCarburante = richiestaCarburante;
		return this;
	}
}