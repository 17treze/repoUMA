package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloAllaFirmaAziendaException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloAllaFirmaCAAException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloChiusoException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloInValidazioneException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloSospesoException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloStatoReadOnlyException;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4GT_EREDE")
public class EredeModel extends EntitaDominio {
	
	private static final long serialVersionUID = -3034546771084462014L;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="FASCICOLO_ID", referencedColumnName = "ID" )
    @JoinColumn(name="FASCICOLO_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private FascicoloModel fascicolo;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="PERSONA_FISICA_ID", referencedColumnName = "ID" )
    @JoinColumn(name="PERSONA_FISICA_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private PersonaFisicaModel personaFisica;

	@Column(name = "FIRMATARIO")
	private Boolean firmatario = false;
	
	public Boolean isFirmatario() {
		return firmatario;
	}

	public EredeModel setFirmatario(Boolean firmatario) {
		this.firmatario = firmatario;
		return this;
	}

	public FascicoloModel getFascicolo() {
		return fascicolo;
	}

	public EredeModel setFascicolo(FascicoloModel fascicolo) {
		this.fascicolo = fascicolo;
		return this;
	}

	public PersonaFisicaModel getPersonaFisica() {
		return personaFisica;
	}

	public EredeModel setPersonaFisica(PersonaFisicaModel personaFisica) {
		this.personaFisica = personaFisica;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(firmatario, fascicolo, personaFisica);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EredeModel other = (EredeModel) obj;
		return Objects.equals(firmatario, other.firmatario) 
				&& Objects.equals(fascicolo, other.fascicolo) 
				&& Objects.equals(personaFisica, other.personaFisica);
	}

	@PreUpdate
	@PreRemove
	public void preventUpdateInValidazione() throws FascicoloStatoReadOnlyException {
	    if (fascicolo.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA)) {
	    	throw new FascicoloAllaFirmaAziendaException();
	    } else if (fascicolo.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_CAA)) {
	    	throw new FascicoloAllaFirmaCAAException();
	    } else if (fascicolo.getStato().equals(StatoFascicoloEnum.IN_VALIDAZIONE)) {
	    	throw new FascicoloInValidazioneException();
	    } else if (fascicolo.getStatoPrecedente().equals(StatoFascicoloEnum.SOSPESO)) {
	    	throw new FascicoloSospesoException();
		} else if (fascicolo.getStatoPrecedente().equals(StatoFascicoloEnum.CHIUSO)) {
			throw new FascicoloChiusoException();
	    }
	    if (this.fascicolo.getIdValidazione() == 0 && this.fascicolo.getStato().equals(StatoFascicoloEnum.VALIDATO)) {
		    	this.fascicolo.setStato(StatoFascicoloEnum.IN_AGGIORNAMENTO);
	    }
	}
	
	@PrePersist
	public void updateStatoFascicolo() {
		if (this.fascicolo.getIdValidazione() == 0 && this.fascicolo.getStato().equals(StatoFascicoloEnum.VALIDATO)) {
	    	this.fascicolo.setStato(StatoFascicoloEnum.IN_AGGIORNAMENTO);
	    }
	}
}
