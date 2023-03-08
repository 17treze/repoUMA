package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;


@Entity
@Table(name = "A4GT_PERSONA_FISICA")
public class PersonaFisicaModel extends PersonaModel {
	
	private static final long serialVersionUID = -7481702806029848620L;

	@Column(name = "NOME", length = 50)
	private String nome;
	
	@Column(name = "COGNOME", length = 50)
	private String cognome;
	
	@Column(name = "SESSO", length = 7)
	@Enumerated(EnumType.STRING)
	private Sesso sesso;
	
	@Column(name = "DATA_NASCITA")
	private LocalDate dataNascita;
	
	@Column(name = "COMUNE_NASCITA", length = 100)
	private String comuneNascita;
	
	@Column(name = "PROVINCIA_NASCITA", length = 100)
	private String provinciaNascita;

	@Embedded
	private IndirizzoModel domicilioFiscale;
	
	@Column(name = "DECEDUTO")
	private Boolean deceduto;
	
	@Column(name = "PEC", length = 100, insertable = false,  updatable = false)
	private String pec;
	
	@Column(name = "DATA_MORTE")
	private LocalDate dataMorte;

	@Embedded
	private ImpresaIndividualeModel impresaIndividuale;

	@OneToMany(mappedBy="personaFisicaModel")
	private List<CaricaModel> cariche;

	public String getNome() {
		return nome;
	}

	public PersonaFisicaModel setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public String getCognome() {
		return cognome;
	}

	public PersonaFisicaModel setCognome(String cognome) {
		this.cognome = cognome;
		return this;
	}

	public Sesso getSesso() {
		return sesso;
	}

	public PersonaFisicaModel setSesso(Sesso sesso) {
		this.sesso = sesso;
		return this;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public PersonaFisicaModel setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
		return this;
	}

	public String getComuneNascita() {
		return comuneNascita;
	}

	public PersonaFisicaModel setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
		return this;
	}

	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public PersonaFisicaModel setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
		return this;
	}

	public IndirizzoModel getDomicilioFiscale() {
		return domicilioFiscale;
	}

	public PersonaFisicaModel setDomicilioFiscale(IndirizzoModel domicilioFiscale) {
		this.domicilioFiscale = domicilioFiscale;
		return this;
	}

	public Boolean getDeceduto() {
		return deceduto;
	}

	public PersonaFisicaModel setDeceduto(Boolean deceduto) {
		this.deceduto = deceduto;
		return this;
	}
	
	public String getPec() {
		return pec;
	}
	
	public PersonaFisicaModel setPec(String pec) {
		this.pec = pec;
		return this;
	}

	public ImpresaIndividualeModel getImpresaIndividuale() {
		return impresaIndividuale;
	}

	public PersonaFisicaModel setImpresaIndividuale(ImpresaIndividualeModel impresaIndividuale) {
		this.impresaIndividuale = impresaIndividuale;
		return this;
	}

	public List<CaricaModel> getCariche() {
		return cariche;
	}

	public void setCariche(List<CaricaModel> cariche) {
		this.cariche = cariche;
	}
	
	public LocalDate getDataMorte() {
		return dataMorte;
	}

	public PersonaFisicaModel setDataMorte(LocalDate dataMorte) {
		this.dataMorte = dataMorte;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		PersonaFisicaModel that = (PersonaFisicaModel) o;
		return Objects.equals(nome, that.nome) &&
				Objects.equals(cognome, that.cognome) &&
				sesso == that.sesso &&
				Objects.equals(dataNascita, that.dataNascita) &&
				Objects.equals(comuneNascita, that.comuneNascita) &&
				Objects.equals(provinciaNascita, that.provinciaNascita) &&
				Objects.equals(domicilioFiscale, that.domicilioFiscale) &&
				Objects.equals(deceduto, that.deceduto) &&
				Objects.equals(impresaIndividuale, that.impresaIndividuale) &&
				Objects.equals(pec, that.pec) &&
				Objects.equals(dataMorte, that.dataMorte);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), nome, cognome, sesso, dataNascita, comuneNascita, provinciaNascita, domicilioFiscale, deceduto, impresaIndividuale, pec, dataMorte);
	}
}
