package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4ST_MESSAGGIO_RICHIESTA")
public class MessaggioRichiestaModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RICHIESTA")
	private RichiestaModificaSuoloModel richiestaModificaSuolo;

	private String utente;
	
	@Enumerated(EnumType.STRING)
	private ProfiloUtente profiloUtente;

	@Column(name = "DATA_INSERIMENTO")
	private LocalDateTime dataInserimento;

	private String testo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_POLIGONO_DICHIARATO")
	private SuoloDichiaratoModel relSuoloDichiarato;

	public RichiestaModificaSuoloModel getRichiestaModificaSuolo() {
		return richiestaModificaSuolo;
	}

	public void setRichiestaModificaSuolo(RichiestaModificaSuoloModel richiestaModificaSuolo) {
		this.richiestaModificaSuolo = richiestaModificaSuolo;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public ProfiloUtente getProfiloUtente() {
		return profiloUtente;
	}

	public void setProfiloUtente(ProfiloUtente profiloUtente) {
		this.profiloUtente = profiloUtente;
	}

	public LocalDateTime getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(LocalDateTime dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public SuoloDichiaratoModel getRelSuoloDichiarato() {
		return relSuoloDichiarato;
	}

	public void setRelSuoloDichiarato(SuoloDichiaratoModel relSuoloDichiarato) {
		this.relSuoloDichiarato = relSuoloDichiarato;
	}
}
