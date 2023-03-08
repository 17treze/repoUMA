package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.Alimentazione;

@Entity
@Table(name="A4GT_MACCHINA_MOTORIZZATA")
public class MacchinaMotorizzataModel extends MacchinaModel {
	private static final long serialVersionUID = 2024179847448922819L;

	@Column(name="MARCA_MOTORE", length = 50)
	private String marcaMotore;
	
	@Column(name="TIPO_MOTORE", length = 50)
	private String tipoMotore;
	
	@Column(name="ALIMENTAZIONE", length = 50)
	@Enumerated(EnumType.STRING)
	private Alimentazione alimentazione;
	
	@Column(name="POTENZA")
	private Double potenza;
	
	@Column(name="MATRICOLA", length = 50)
	private String matricola;

	public String getMarcaMotore() {
		return marcaMotore;
	}

	public MacchinaMotorizzataModel setMarcaMotore(String marcaMotore) {
		this.marcaMotore = marcaMotore;
		return this;
	}

	public String getTipoMotore() {
		return tipoMotore;
	}

	public MacchinaMotorizzataModel setTipoMotore(String tipoMotore) {
		this.tipoMotore = tipoMotore;
		return this;
	}

	public Alimentazione getAlimentazione() {
		return alimentazione;
	}

	public MacchinaMotorizzataModel setAlimentazione(Alimentazione alimentazione) {
		this.alimentazione = alimentazione;
		return this;
	}

	public Double getPotenza() {
		return potenza;
	}

	public MacchinaMotorizzataModel setPotenza(Double potenza) {
		this.potenza = potenza;
		return this;
	}

	public String getMatricola() {
		return matricola;
	}

	public MacchinaMotorizzataModel setMatricola(String matricola) {
		this.matricola = matricola;
		return this;
	}
}
