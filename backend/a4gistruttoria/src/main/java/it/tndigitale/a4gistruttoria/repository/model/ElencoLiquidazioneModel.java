package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import it.tndigitale.a4gistruttoria.util.StatoTrasmissioneElencoLiquidazione;

/**
 * The persistent class for the A4GT_ELENCO_LIQUIDAZIONE database table.
 * 
 */
@Entity
@Table(name = "A4GT_ELENCO_LIQUIDAZIONE")
public class ElencoLiquidazioneModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "COD_ELENCO")
	private String codElenco;

	@Column(name = "DT_CREAZIONE")
	private LocalDateTime dtCreazione;

	@Enumerated(EnumType.STRING)
	private StatoTrasmissioneElencoLiquidazione stato;

	@Lob
	@Column(name = "TRACCIATO_SOC")
	private String tracciatoSoc;

	@Lob
	@Column(name = "VERBALE_LIQUIDAZIONE")
	private byte[] verbaleLiquidazione;

	public ElencoLiquidazioneModel() {
	}

	public String getCodElenco() {
		return this.codElenco;
	}

	public void setCodElenco(String codElenco) {
		this.codElenco = codElenco;
	}

	public LocalDateTime getDtCreazione() {
		return this.dtCreazione;
	}

	public void setDtCreazione(LocalDateTime dtCreazione) {
		this.dtCreazione = dtCreazione;
	}

	public StatoTrasmissioneElencoLiquidazione getStato() {
		return this.stato;
	}

	public void setStato(StatoTrasmissioneElencoLiquidazione stato) {
		this.stato = stato;
	}

	public String getTracciatoSoc() {
		return this.tracciatoSoc;
	}

	public void setTracciatoSoc(String tracciatoSoc) {
		this.tracciatoSoc = tracciatoSoc;
	}

	public byte[] getVerbaleLiquidazione() {
		return this.verbaleLiquidazione;
	}

	public void setVerbaleLiquidazione(byte[] verbaleLiquidazione) {
		this.verbaleLiquidazione = verbaleLiquidazione;
	}
}