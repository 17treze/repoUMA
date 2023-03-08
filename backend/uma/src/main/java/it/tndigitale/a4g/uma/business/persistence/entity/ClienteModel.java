package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="A4GT_CLIENTE")
public class ClienteModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = -4213500312901722134L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CONSUMI")
	private DichiarazioneConsumiModel dichiarazioneConsumi;

	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
	private List<FatturaClienteModel> allegati;
	
	@Column(name="CUAA", length = 16)
	private String cuaa;

	@Column(name="DENOMINAZIONE", length = 200)
	private String denominazione;

	@Column(name="ID_FASCICOLO")
	private Long idFascicolo;

	public DichiarazioneConsumiModel getDichiarazioneConsumi() {
		return dichiarazioneConsumi;
	}
	public ClienteModel setDichiarazioneConsumi(DichiarazioneConsumiModel dichiarazioneConsumi) {
		this.dichiarazioneConsumi = dichiarazioneConsumi;
		return this;
	}
	public String getCuaa() {
		return cuaa;
	}
	public ClienteModel setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public ClienteModel setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public Long getIdFascicolo() {
		return idFascicolo;
	}
	public ClienteModel setIdFascicolo(Long idFascicolo) {
		this.idFascicolo = idFascicolo;
		return this;
	}
	public List<FatturaClienteModel> getAllegati() {
		return allegati;
	}
	public void setAllegati(List<FatturaClienteModel> allegati) {
		this.allegati = allegati;
	}
	
}
