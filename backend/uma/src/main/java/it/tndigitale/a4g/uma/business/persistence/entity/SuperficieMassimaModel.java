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
@Table(name="TAB_AGRI_UMAL_SUPERFICIE_MASSIMA")
public class SuperficieMassimaModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = -7772351481913281929L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RICHIESTA")
	private RichiestaCarburanteModel richiestaCarburante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRUPPO")
	private GruppoLavorazioneModel gruppoLavorazione;

	@Column(name = "SUP_MASSIMA", nullable = false, length = 20)
	private Long superficieMassima;

	public RichiestaCarburanteModel getRichiestaCarburante() {
		return richiestaCarburante;
	}

	public SuperficieMassimaModel setRichiestaCarburante(RichiestaCarburanteModel richiestaCarburante) {
		this.richiestaCarburante = richiestaCarburante;
		return this;
	}

	public GruppoLavorazioneModel getGruppoLavorazione() {
		return gruppoLavorazione;
	}

	public SuperficieMassimaModel setGruppoLavorazione(GruppoLavorazioneModel gruppoLavorazione) {
		this.gruppoLavorazione = gruppoLavorazione;
		return this;
	}

	public Long getSuperficieMassima() {
		return superficieMassima;
	}

	public SuperficieMassimaModel setSuperficieMassima(Long superficieMassima) {
		this.superficieMassima = superficieMassima;
		return this;
	}
}
