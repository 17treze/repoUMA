package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4GT_MOVIMENTAZIONE_FASCICOLO")
public class MovimentazioneModel extends EntitaDominio {

	private static final long serialVersionUID = -7910876572697048117L;

	@Column(name = "DATA_INIZIO", nullable = false)
	private LocalDateTime dataInizio;

	@Column(name = "DATA_FINE", nullable = true)
	private LocalDateTime dataFine;

	@Column(name = "MOTIVAZIONE_INIZIO", length = 4000, nullable = true)
	private String motivazioneInizio;

	@Column(name = "MOTIVAZIONE_FINE", length = 4000, nullable = true)
	private String motivazioneFine;

	@Column(name = "UTENTE", length = 100, nullable = false)
	private String utente;

	@Column(name = "TIPO", length = 30, nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoMovimentazioneFascicolo tipo;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name="FASCICOLO_ID", referencedColumnName = "ID" )
	@JoinColumn(name="FASCICOLO_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private FascicoloModel fascicolo;


	public LocalDateTime getDataInizio() {
		return dataInizio;
	}
	public MovimentazioneModel setDataInizio(LocalDateTime dataInizio) {
		this.dataInizio = dataInizio;
		return this;
	}
	public String getMotivazioneInizio() {
		return motivazioneInizio;
	}
	public MovimentazioneModel setMotivazioneInizio(String motivazioneInizio) {
		this.motivazioneInizio = motivazioneInizio;
		return this;
	}
	public LocalDateTime getDataFine() {
		return dataFine;
	}
	public MovimentazioneModel setDataFine(LocalDateTime dataFine) {
		this.dataFine = dataFine;
		return this;
	}
	public String getMotivazioneFine() {
		return motivazioneFine;
	}
	public MovimentazioneModel setMotivazioneFine(String motivazioneFine) {
		this.motivazioneFine = motivazioneFine;
		return this;
	}
	public String getUtente() {
		return utente;
	}
	public MovimentazioneModel setUtente(String utente) {
		this.utente = utente;
		return this;
	}
	public FascicoloModel getFascicolo() {
		return fascicolo;
	}
	public MovimentazioneModel setFascicolo(FascicoloModel fascicolo) {
		this.fascicolo = fascicolo;
		return this;
	}
	public TipoMovimentazioneFascicolo getTipo() {
		return tipo;
	}
	public MovimentazioneModel setTipo(TipoMovimentazioneFascicolo tipo) {
		this.tipo = tipo;
		return this;
	}
}
