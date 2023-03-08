package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.Alimentazione;

/**
 * Dto di comodo per visualizzare i dati generici di una macchina
 * 
 * @author B.Conetta
 *
 */
public class MacchinaDto {

	private Long id;
	private String tipologia; // descrizione tipologia macchina (macro)
	private String modello;
	private String targa;
	private Alimentazione alimentazione; // presente solo se macchina a motore


	public Long getId() {
		return id;
	}
	public MacchinaDto setId(Long id) {
		this.id = id;
		return this;
	}
	public String getTipologia() {
		return tipologia;
	}
	public MacchinaDto setTipologia(String tipologia) {
		this.tipologia = tipologia;
		return this;
	}
	public String getModello() {
		return modello;
	}
	public MacchinaDto setModello(String modello) {
		this.modello = modello;
		return this;
	}
	public String getTarga() {
		return targa;
	}
	public MacchinaDto setTarga(String targa) {
		this.targa = targa;
		return this;
	}
	public Alimentazione getAlimentazione() {
		return alimentazione;
	}
	public MacchinaDto setAlimentazione(Alimentazione alimentazione) {
		this.alimentazione = alimentazione;
		return this;
	}
}
