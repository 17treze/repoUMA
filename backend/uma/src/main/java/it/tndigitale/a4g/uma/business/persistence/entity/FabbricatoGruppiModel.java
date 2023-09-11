package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="TAB_AGRI_UMAL_FABBRICATO_GRUPPI")
public class FabbricatoGruppiModel extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = 640794986576667867L;

	@Column(name ="CODICE_FABBRICATO")
	private String codiceFabbricato;

	@Column(name ="TIPO_FABBRICATO")
	private String tipoFabbricato;

	@OneToOne
	@JoinColumn(name = "ID_GRUPPO", referencedColumnName = "ID")
	private GruppoLavorazioneModel gruppoLavorazione;

	public String getCodiceFabbricato() {
		return codiceFabbricato;
	}
	public FabbricatoGruppiModel setCodiceFabbricato(String codiceFabbricato) {
		this.codiceFabbricato = codiceFabbricato;
		return this;
	}
	public String getTipoFabbricato() {
		return tipoFabbricato;
	}
	public FabbricatoGruppiModel setTipoFabbricato(String tipoFabbricato) {
		this.tipoFabbricato = tipoFabbricato;
		return this;
	}
	public GruppoLavorazioneModel getGruppoLavorazione() {
		return gruppoLavorazione;
	}
	public FabbricatoGruppiModel setGruppoLavorazione(GruppoLavorazioneModel gruppoLavorazione) {
		this.gruppoLavorazione = gruppoLavorazione;
		return this;
	}
}
