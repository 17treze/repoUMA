package it.tndigitale.a4gutente.repository.model;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4GT_PRIVACY_PERSONA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_INFORMATIVA")
public abstract class AccettazioneInformativaEntita extends EntitaDominio {

	@ManyToOne(optional = false)
    @JoinColumn(name="ID_PERSONA")	
	private PersonaEntita persona;

    @Basic
    @Column(name = "DT_ACCETTAZIONE")
	private LocalDate dataAccettazione;

    @Basic
    @Column(name = "DT_PROTOCOLLAZIONE")
	private LocalDate dataProtocollazione;

    @Basic
    @Column(name = "NR_PROTOCOLLO")
	private String numeroProtocollazione;
    
	public PersonaEntita getPersona() {
		return persona;
	}

	public void setPersona(PersonaEntita persona) {
		this.persona = persona;
	}

	public LocalDate getDataAccettazione() {
		return dataAccettazione;
	}

	public void setDataAccettazione(LocalDate dataAccettazione) {
		this.dataAccettazione = dataAccettazione;
	}

	public LocalDate getDataProtocollazione() {
		return dataProtocollazione;
	}

	public void setDataProtocollazione(LocalDate dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
	}

	public String getNumeroProtocollazione() {
		return numeroProtocollazione;
	}

	public void setNumeroProtocollazione(String numeroProtocollazione) {
		this.numeroProtocollazione = numeroProtocollazione;
	}
}
