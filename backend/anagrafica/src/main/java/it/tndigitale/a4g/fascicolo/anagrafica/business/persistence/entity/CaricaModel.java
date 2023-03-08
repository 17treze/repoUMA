package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

@Entity
@Table(name = "A4GT_CARICA")
public class CaricaModel extends EntitaDominioFascicolo {
	
	private static final long serialVersionUID = 4239008871215348287L;

	@Column(name = "DESCRIZIONE", length = 100)
	private String descrizione;
	
	@Column(name = "IDENTIFICATIVO", length = 20)
	private String identificativo;
	
	@Column(name = "DATA_INIZIO_CARICA")
	private LocalDate dataInizio;
	
	@Column(name = "FIRMATARIO")
	private Boolean firmatario = false;
	
	public Boolean isFirmatario() {
		return firmatario;
	}

	public void setFirmatario(Boolean firmatario) {
		this.firmatario = firmatario;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERSONA_FISICA_CON_CARICA", referencedColumnName = "ID")
    @JoinColumn(name = "PERSONA_FISICA_CARICA_IDVAL", referencedColumnName = "ID_VALIDAZIONE")
	private PersonaFisicaConCaricaModel personaFisicaConCaricaModel;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERSONA_GIURIDICA", referencedColumnName = "ID")
    @JoinColumn(name = "PERS_GIURIDICA_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private PersonaGiuridicaModel personaGiuridicaModel;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERSONA_GIURIDICA_CARICA", referencedColumnName = "ID")
    @JoinColumn(name = "PERSONA_GIURIDICA_CARICA_IDVAL", referencedColumnName = "ID_VALIDAZIONE")
	private PersonaGiuridicaConCaricaModel personaGiuridicaConCaricaModel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSONA_FISICA_ID", referencedColumnName = "ID")
	@JoinColumn(name = "PERSONA_FISICA_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private PersonaFisicaModel personaFisicaModel;

	public String getDescrizione() {
		return descrizione;
	}

	public CaricaModel setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public CaricaModel setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
		return this;
	}

	public LocalDate getDataInizio() {
		return dataInizio;
	}

	public CaricaModel setDataInizio(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
		return this;
	}

	public PersonaFisicaConCaricaModel getPersonaFisicaConCaricaModel() {
		return personaFisicaConCaricaModel;
	}

	public CaricaModel setPersonaFisicaConCaricaModel(PersonaFisicaConCaricaModel personaFisicaConCaricaModel) {
		this.personaFisicaConCaricaModel = personaFisicaConCaricaModel;
		return this;
	}

	public PersonaGiuridicaModel getPersonaGiuridicaModel() {
		return personaGiuridicaModel;
	}

	public CaricaModel setPersonaGiuridicaModel(PersonaGiuridicaModel personaGiuridicaModel) {
		this.personaGiuridicaModel = personaGiuridicaModel;
		return this;
	}

	public PersonaFisicaModel getPersonaFisicaModel() {
		return personaFisicaModel;
	}

	public CaricaModel setPersonaFisicaModel(PersonaFisicaModel personaFisicaModel) {
		this.personaFisicaModel = personaFisicaModel;
		return this;
	}

	public PersonaGiuridicaConCaricaModel getPersonaGiuridicaConCaricaModel() {
		return personaGiuridicaConCaricaModel;
	}

	public CaricaModel setPersonaGiuridicaConCaricaModel(PersonaGiuridicaConCaricaModel personaGiuridicaConCaricaModel) {
		this.personaGiuridicaConCaricaModel = personaGiuridicaConCaricaModel;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataInizio, descrizione, identificativo, personaFisicaConCaricaModel,
				personaGiuridicaConCaricaModel, personaGiuridicaModel, personaGiuridicaModel, firmatario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CaricaModel other = (CaricaModel) obj;
		return Objects.equals(dataInizio, other.dataInizio) && Objects.equals(descrizione, other.descrizione)
				&& Objects.equals(identificativo, other.identificativo)
				&& Objects.equals(firmatario, other.firmatario)
				&& Objects.equals(personaFisicaConCaricaModel, other.personaFisicaConCaricaModel)
				&& Objects.equals(personaGiuridicaConCaricaModel, other.personaGiuridicaConCaricaModel)
				&& Objects.equals(personaGiuridicaModel, other.personaGiuridicaModel)
				&& Objects.equals(personaFisicaModel, other.personaFisicaModel);
	}
	
}
