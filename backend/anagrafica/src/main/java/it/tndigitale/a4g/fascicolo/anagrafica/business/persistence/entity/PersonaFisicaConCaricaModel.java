package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;


@Entity
@Table(name = "A4GT_PERSONA_FISICA_CON_CARICA", uniqueConstraints = {@UniqueConstraint(columnNames={"ID_VALIDAZIONE", "CODICE_FISCALE"})})
public class PersonaFisicaConCaricaModel extends EntitaDominioFascicolo {
	
	private static final long serialVersionUID = 780015391733297324L;
	
	@Column(name = "CODICE_FISCALE", length = 16)
	protected String codiceFiscale;

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
	
	@Column(name = "VERIFICA_CODICE_FISCALE", length = 20)
	@Enumerated(EnumType.STRING)
	private VerificaCodiceFiscale verificaCodiceFiscale;

	@Column(name = "DECEDUTO")
	private Boolean deceduto;
	
	@Embedded
    private IndirizzoModel indirizzo;
	
	@OneToMany(mappedBy="personaFisicaConCaricaModel")
	private List<CaricaModel> cariche;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public PersonaFisicaConCaricaModel setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getNome() {
		return nome;
	}

	public PersonaFisicaConCaricaModel setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public String getCognome() {
		return cognome;
	}

	public PersonaFisicaConCaricaModel setCognome(String cognome) {
		this.cognome = cognome;
		return this;
	}

	public Sesso getSesso() {
		return sesso;
	}

	public PersonaFisicaConCaricaModel setSesso(Sesso sesso) {
		this.sesso = sesso;
		return this;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public PersonaFisicaConCaricaModel setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
		return this;
	}

	public String getComuneNascita() {
		return comuneNascita;
	}

	public PersonaFisicaConCaricaModel setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
		return this;
	}

	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public PersonaFisicaConCaricaModel setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
		return this;
	}

	public Boolean getDeceduto() {
		return deceduto;
	}

	public PersonaFisicaConCaricaModel setDeceduto(Boolean deceduto) {
		this.deceduto = deceduto;
		return this;
	}

	public IndirizzoModel getIndirizzo() {
        return indirizzo;
    }

    public PersonaFisicaConCaricaModel setIndirizzo(IndirizzoModel indirizzo) {
        this.indirizzo = indirizzo;
        return this;
    }
    

	public List<CaricaModel> getCariche() {
		return cariche;
	}

	public void setCariche(List<CaricaModel> cariche) {
		this.cariche = cariche;
	}

	
	public VerificaCodiceFiscale getVerificaCodiceFiscale() {
		return verificaCodiceFiscale;
	}

	public void setVerificaCodiceFiscale(VerificaCodiceFiscale verificaCodiceFiscale) {
		this.verificaCodiceFiscale = verificaCodiceFiscale;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cariche, codiceFiscale, cognome, comuneNascita, dataNascita, deceduto, indirizzo, nome,
				provinciaNascita, sesso);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonaFisicaConCaricaModel other = (PersonaFisicaConCaricaModel) obj;
		return Objects.equals(cariche, other.cariche) && Objects.equals(codiceFiscale, other.codiceFiscale)
				&& Objects.equals(cognome, other.cognome) && Objects.equals(comuneNascita, other.comuneNascita)
				&& Objects.equals(dataNascita, other.dataNascita) && Objects.equals(deceduto, other.deceduto)
				&& Objects.equals(indirizzo, other.indirizzo) && Objects.equals(nome, other.nome)
				&& Objects.equals(provinciaNascita, other.provinciaNascita) && sesso == other.sesso;
	}


}
