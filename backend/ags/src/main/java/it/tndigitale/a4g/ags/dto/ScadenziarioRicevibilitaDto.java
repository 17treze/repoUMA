package it.tndigitale.a4g.ags.dto;

import java.io.Serializable;
import java.util.Date;

public class ScadenziarioRicevibilitaDto implements Serializable {

	private static final long serialVersionUID = -5457839696055758718L;

	private Date scadenzaDomandaInizialeInRitardo;
	private Date scadenzaDomandaRitiroParziale;
	private Date scadenzaDomandaModificaInRitardo;


	public Date getScadenzaDomandaInizialeInRitardo() {
		return scadenzaDomandaInizialeInRitardo;
	}

	public ScadenziarioRicevibilitaDto setScadenzaDomandaInizialeInRitardo(Date scadenzaDomandaInizialeInRitardo) {
		this.scadenzaDomandaInizialeInRitardo = scadenzaDomandaInizialeInRitardo;
		return this;
	}

	public Date getScadenzaDomandaRitiroParziale() {
		return scadenzaDomandaRitiroParziale;
	}

	public ScadenziarioRicevibilitaDto setScadenzaDomandaRitiroParziale(Date scadenzaDomandaRitiroParziale) {
		this.scadenzaDomandaRitiroParziale = scadenzaDomandaRitiroParziale;
		return this;
	}

	public Date getScadenzaDomandaModificaInRitardo() {
		return scadenzaDomandaModificaInRitardo;
	}

	public ScadenziarioRicevibilitaDto setScadenzaDomandaModificaInRitardo(Date scadenzaDomandaModificaInRitardo) {
		this.scadenzaDomandaModificaInRitardo = scadenzaDomandaModificaInRitardo;
		return this;
	}
}
