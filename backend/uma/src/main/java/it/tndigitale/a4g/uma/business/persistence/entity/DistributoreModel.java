package it.tndigitale.a4g.uma.business.persistence.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="A4GT_DISTRIBUTORI")
public class DistributoreModel extends EntitaDominio {

	private static final long serialVersionUID = 5205894881626430379L;

	@Column(name = "DENOMINAZIONE", length = 150, nullable = false)
	private String denominazione;

	@Column(name = "COMUNE", length = 50, nullable = false)
	private String comune;

	@Column(name = "INDIRIZZO", length = 100, nullable = false)
	private String indirizzo;

	@Column(name = "PROVINCIA", length = 50, nullable = false)
	private String provincia;

	@Column(name = "IDENTIFICATIVO", nullable = false, unique = true)
	private Long identificativo;

	@OneToMany(mappedBy = "distributore", fetch = FetchType.LAZY)
	private List<PrelievoModel> prelievi;

	public String getDenominazione() {
		return denominazione;
	}
	public DistributoreModel setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public String getComune() {
		return comune;
	}
	public DistributoreModel setComune(String comune) {
		this.comune = comune;
		return this;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public DistributoreModel setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
		return this;
	}
	public String getProvincia() {
		return provincia;
	}
	public DistributoreModel setProvincia(String provincia) {
		this.provincia = provincia;
		return this;
	}
	public Long getIdentificativo() {
		return identificativo;
	}
	public DistributoreModel setIdentificativo(Long identificativo) {
		this.identificativo = identificativo;
		return this;
	}
	public List<PrelievoModel> getPrelievi() {
		return prelievi;
	}
	public DistributoreModel setPrelievi(List<PrelievoModel> prelievi) {
		this.prelievi = prelievi;
		return this;
	}

}
