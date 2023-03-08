package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class SedeModel {
	
	@Embedded
	@AttributeOverride( name = "descrizioneEstesa", column = @Column(name = "SEDE_DESCRIZIONE_ESTESA"))
	@AttributeOverride( name = "toponimo", column = @Column(name = "SEDE_TOPONIMO"))
	@AttributeOverride( name = "via", column = @Column(name = "SEDE_VIA"))
	@AttributeOverride( name = "numeroCivico", column = @Column(name = "SEDE_NUMERO_CIVICO"))
	@AttributeOverride( name = "cap", column = @Column(name = "SEDE_CAP"))
	@AttributeOverride( name = "codiceIstat", column = @Column(name = "SEDE_CODICE_ISTAT"))
	@AttributeOverride( name = "frazione", column = @Column(name = "SEDE_FRAZIONE"))
	@AttributeOverride( name = "provincia", column = @Column(name = "SEDE_PROVINCIA"))
	@AttributeOverride( name = "comune", column = @Column(name = "SEDE_COMUNE"))
	private IndirizzoModel indirizzo;

	@Embedded
	private IscrizioneRegistroImpreseModel iscrizioneRegistroImprese;

	@Embedded
	@AttributeOverride( name = "descrizioneEstesa", column = @Column(name = "SEDE_CC_DESCRIZIONE_ESTESA"))
	@AttributeOverride( name = "toponimo", column = @Column(name = "SEDE_CC_TOPONIMO"))
	@AttributeOverride( name = "via", column = @Column(name = "SEDE_CC_VIA"))
	@AttributeOverride( name = "numeroCivico", column = @Column(name = "SEDE_CC_NUMERO_CIVICO"))
	@AttributeOverride( name = "cap", column = @Column(name = "SEDE_CC_CAP"))
	@AttributeOverride( name = "codiceIstat", column = @Column(name = "SEDE_CC_CODICE_ISTAT"))
	@AttributeOverride( name = "frazione", column = @Column(name = "SEDE_CC_FRAZIONE"))
	@AttributeOverride( name = "provincia", column = @Column(name = "SEDE_CC_PROVINCIA"))
	@AttributeOverride( name = "comune", column = @Column(name = "SEDE_CC_COMUNE"))
	private IndirizzoModel indirizzoCameraCommercio;

	@Column(name = "PEC")
	private String pec;

	@Column(name = "TELEFONO")
	private String telefono;

	@OneToMany(cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY,
			mappedBy="personaModel")
	private List<AttivitaAtecoModel> attivita;

	public IndirizzoModel getIndirizzo() {
		return indirizzo;
	}

	public SedeModel setIndirizzo(IndirizzoModel indirizzo) {
		this.indirizzo = indirizzo;
		return this;
	}

	public String getPec() {
		return pec;
	}

	public SedeModel setPec(String pec) {
		this.pec = pec;
		return this;
	}

	public String getTelefono() {
		return telefono;
	}

	public SedeModel setTelefono(String telefono) {
		this.telefono = telefono;
		return this;
	}

	public IscrizioneRegistroImpreseModel getIscrizioneRegistroImprese() {
		return iscrizioneRegistroImprese;
	}

	public SedeModel setIscrizioneRegistroImprese(IscrizioneRegistroImpreseModel iscrizioneRegistroImprese) {
		this.iscrizioneRegistroImprese = iscrizioneRegistroImprese;
		return this;
	}

	public List<AttivitaAtecoModel> getAttivita() {
		return attivita;
	}

	public SedeModel setAttivita(List<AttivitaAtecoModel> attivita) {
		this.attivita = attivita;
		return this;
	}

	public IndirizzoModel getIndirizzoCameraCommercio() {
		return indirizzoCameraCommercio;
	}

	public SedeModel setIndirizzoCameraCommercio(IndirizzoModel indirizzoCameraCommercio) {
		this.indirizzoCameraCommercio = indirizzoCameraCommercio;
		return this;
	}

	public Boolean isEmpty() {
		return indirizzo == null
				&& iscrizioneRegistroImprese == null 
				&& indirizzoCameraCommercio == null
				&& pec == null
				&& telefono == null 
				&& (attivita == null || attivita.isEmpty());
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SedeModel sedeModel = (SedeModel) o;
		return Objects.equals(indirizzo, sedeModel.indirizzo) &&
				Objects.equals(iscrizioneRegistroImprese, sedeModel.iscrizioneRegistroImprese) &&
				Objects.equals(indirizzoCameraCommercio, sedeModel.indirizzoCameraCommercio) &&
				Objects.equals(pec, sedeModel.pec) &&
				Objects.equals(telefono, sedeModel.telefono) &&
				Objects.equals(attivita, sedeModel.attivita);
	}

	@Override
	public int hashCode() {
		return Objects.hash(indirizzo, iscrizioneRegistroImprese, indirizzoCameraCommercio, pec, telefono, attivita);
	}
}
