package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.SezioneEnum;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

@Entity
@Table(name = "A4GT_ISCRIZIONE_SEZIONE")
public class IscrizioneSezioneModel extends EntitaDominioFascicolo {

	private static final long serialVersionUID = 2261567843022158179L;

	@Column(name = "SEZIONE", length = 30, nullable = false)
	@Enumerated(EnumType.STRING)
	private SezioneEnum sezione;
	
	@Column(name = "DATA_ISCRIZIONE", nullable = false)
	private LocalDate dataIscrizione;
	
	@Column(name = "QUALIFICA", length = 100, nullable = true)
	private String qualifica;
	
	@Column(name = "COLTIVATORE_DIRETTO", length = 100, nullable = true)
	private String coltivatoreDiretto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERSONA", referencedColumnName = "ID")
    @JoinColumn(name = "PERSONA_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private PersonaModel persona;

	public SezioneEnum getSezione() {
		return sezione;
	}

	public void setSezione(SezioneEnum sezione) {
		this.sezione = sezione;
	}

	public LocalDate getDataIscrizione() {
		return dataIscrizione;
	}

	public void setDataIscrizione(LocalDate dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}

	public String getQualifica() {
		return qualifica;
	}

	public void setQualifica(String qualifica) {
		this.qualifica = qualifica;
	}

	public String getColtivatoreDiretto() {
		return coltivatoreDiretto;
	}

	public void setColtivatoreDiretto(String coltivatoreDiretto) {
		this.coltivatoreDiretto = coltivatoreDiretto;
	}
	
	public PersonaModel getPersona() {
		return persona;
	}

	public void setPersona(PersonaModel persona) {
		this.persona = persona;
	}

	@Override
	public int hashCode() {
		return Objects
				.hash(sezione, dataIscrizione, qualifica, coltivatoreDiretto, persona);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj
				.getClass())
			return false;
		IscrizioneSezioneModel other = (IscrizioneSezioneModel) obj;
		return Objects
				.equals(sezione, other.sezione)
				&& Objects
				.equals(dataIscrizione, other.dataIscrizione)
				&& Objects
				.equals(qualifica, other.qualifica)
				&& Objects
				.equals(coltivatoreDiretto, other.coltivatoreDiretto)
				&& Objects
				.equals(persona, other.persona);
	}

	
}
