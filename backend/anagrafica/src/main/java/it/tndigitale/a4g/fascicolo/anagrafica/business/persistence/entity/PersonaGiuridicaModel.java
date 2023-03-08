package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "A4GT_PERSONA_GIURIDICA")
public class PersonaGiuridicaModel extends PersonaModel {
	
    private static final long serialVersionUID = 2518759839265575959L;

	@Column(name = "DENOMINAZIONE", length = 100)
	private String denominazione;

	@Column(name = "PARTITA_IVA", length = 11)
	private String partitaIVA;

	@Column(name = "FORMA_GIURIDICA", length = 100)
	private String formaGiuridica;

	@Lob
	@Column(name = "OGGETTO_SOCIALE")
	private String oggettoSociale;

	@Column(name = "DATA_COSTITUZIONE")
	private LocalDate dataCostituzione;

	@Column(name = "DATA_TERMINE")
	private LocalDate dataTermine;

	@Column(name = "CAPITALE_SOCIALE_DELIBERATO")
	private Double capitaleSocialeDeliberato;
    
    @Column(name = "CF_RAPPRESENTANTE_LEGALE", length = 16)
    private String codiceFiscaleRappresentanteLegale;

	@Column(name = "NOME_RAPPRESENTANTE_LEGALE", length = 200)
    private String nominativoRappresentanteLegale;
	
	@Column(name = "PEC", length = 100, insertable = false,  updatable = false)
	private String pec;

	@Embedded
	private SedeModel sedeLegale;
	
	@OneToMany(mappedBy="personaGiuridicaModel")
	private List<CaricaModel> cariche;
	
	public String getDenominazione() {
		return denominazione;
	}

	public PersonaGiuridicaModel setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public String getPartitaIVA() {
		return partitaIVA;
	}

	public PersonaGiuridicaModel setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
		return this;
	}

	public String getFormaGiuridica() {
		return formaGiuridica;
	}

	public PersonaGiuridicaModel setFormaGiuridica(String formaGiuridica) {
		this.formaGiuridica = formaGiuridica;
		return this;
	}

	public String getOggettoSociale() {
		return oggettoSociale;
	}

	public PersonaGiuridicaModel setOggettoSociale(String oggettoSociale) {
		this.oggettoSociale = oggettoSociale;
		return this;
	}

	public LocalDate getDataCostituzione() {
		return dataCostituzione;
	}

	public PersonaGiuridicaModel setDataCostituzione(LocalDate dataCostituzione) {
		this.dataCostituzione = dataCostituzione;
		return this;
	}

	public LocalDate getDataTermine() {
		return dataTermine;
	}

	public PersonaGiuridicaModel setDataTermine(LocalDate dataTermine) {
		this.dataTermine = dataTermine;
		return this;
	}

	public Double getCapitaleSocialeDeliberato() {
		return capitaleSocialeDeliberato;
	}

	public PersonaGiuridicaModel setCapitaleSocialeDeliberato(Double capitaleSocialeDeliberato) {
		this.capitaleSocialeDeliberato = capitaleSocialeDeliberato;
		return this;
	}

	public String getCodiceFiscaleRappresentanteLegale() {
		return codiceFiscaleRappresentanteLegale;
	}

	public PersonaGiuridicaModel setCodiceFiscaleRappresentanteLegale(String codiceFiscaleRappresentanteLegale) {
		this.codiceFiscaleRappresentanteLegale = codiceFiscaleRappresentanteLegale;
		return this;
	}

	public String getNominativoRappresentanteLegale() {
		return nominativoRappresentanteLegale;
	}

	public PersonaGiuridicaModel setNominativoRappresentanteLegale(String nominativoRappresentanteLegale) {
		this.nominativoRappresentanteLegale = nominativoRappresentanteLegale;
		return this;
	}
	
	public String getPec() {
		return pec;
	}
	
	public PersonaGiuridicaModel setPec(String pec) {
		this.pec = pec;
		return this;
	}

	public SedeModel getSedeLegale() {
		return sedeLegale;
	}

	public PersonaGiuridicaModel setSedeLegale(SedeModel sedeLegale) {
		this.sedeLegale = sedeLegale;
		return this;
	}
	
	public List<CaricaModel> getCariche() {
		return cariche;
	}

	public void setCariche(List<CaricaModel> cariche) {
		this.cariche = cariche;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonaGiuridicaModel other = (PersonaGiuridicaModel) obj;
		return Objects.equals(capitaleSocialeDeliberato, other.capitaleSocialeDeliberato)
				&& Objects.equals(cariche, other.cariche)
				&& Objects.equals(codiceFiscaleRappresentanteLegale, other.codiceFiscaleRappresentanteLegale)
				&& Objects.equals(dataCostituzione, other.dataCostituzione)
				&& Objects.equals(dataTermine, other.dataTermine) && Objects.equals(denominazione, other.denominazione)
				&& Objects.equals(formaGiuridica, other.formaGiuridica)
				&& Objects.equals(nominativoRappresentanteLegale, other.nominativoRappresentanteLegale)
				&& Objects.equals(oggettoSociale, other.oggettoSociale) && Objects.equals(partitaIVA, other.partitaIVA)
				&& Objects.equals(sedeLegale, other.sedeLegale)
				&& Objects.equals(pec, other.pec);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(capitaleSocialeDeliberato, cariche, codiceFiscaleRappresentanteLegale,
				dataCostituzione, dataTermine, denominazione, formaGiuridica, nominativoRappresentanteLegale,
				oggettoSociale, partitaIVA, sedeLegale, pec);
		return result;
	}
}