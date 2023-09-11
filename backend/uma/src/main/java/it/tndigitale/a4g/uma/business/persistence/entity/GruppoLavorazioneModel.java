package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="TAB_AGRI_UMAL_GRUPPI_LAVORAZIONE")
public class GruppoLavorazioneModel extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = -6668410312338715941L;

	@Column(name="ANNO_FINE", nullable = true, length = 4)
	private Integer annoFine;

	@Column(name="ANNO_INIZIO", nullable = false, length = 4)
	private Integer annoInizio;

	@Column(name="INDICE")
	private Integer indice;

	@Column(name="NOME")
	private String nome;

	@Enumerated(EnumType.STRING)
	@Column(name="AMBITO")
	private AmbitoLavorazione ambitoLavorazione;

	@OneToMany(mappedBy = "gruppoLavorazione", fetch = FetchType.LAZY)
	private List<LavorazioneModel> lavorazioneModels;

	public Integer getAnnoFine() {
		return this.annoFine;
	}
	public GruppoLavorazioneModel setAnnoFine(Integer annoFine) {
		this.annoFine = annoFine;
		return this;
	}
	public Integer getAnnoInizio() {
		return this.annoInizio;
	}
	public GruppoLavorazioneModel setAnnoInizio(Integer annoInizio) {
		this.annoInizio = annoInizio;
		return this;
	}
	public Integer getIndice() {
		return this.indice;
	}
	public GruppoLavorazioneModel setIndice(Integer indice) {
		this.indice = indice;
		return this;
	}
	public String getNome() {
		return this.nome;
	}
	public GruppoLavorazioneModel setNome(String nome) {
		this.nome = nome;
		return this;
	}
	public AmbitoLavorazione getAmbitoLavorazione() {
		return this.ambitoLavorazione;
	}
	public GruppoLavorazioneModel setAmbitoLavorazione(AmbitoLavorazione tipoLavorazione) {
		this.ambitoLavorazione = tipoLavorazione;
		return this;
	}
	public List<LavorazioneModel> getLavorazioneModels() {
		return lavorazioneModels;
	}
	public GruppoLavorazioneModel setLavorazioneModels(List<LavorazioneModel> lavorazioneModels) {
		this.lavorazioneModels = lavorazioneModels;
		return this;
	}
}