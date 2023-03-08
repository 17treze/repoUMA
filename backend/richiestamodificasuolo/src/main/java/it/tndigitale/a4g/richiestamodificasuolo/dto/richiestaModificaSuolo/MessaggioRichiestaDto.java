package it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo;

import java.io.Serializable;
import java.time.LocalDateTime;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProfiloUtente;

public class MessaggioRichiestaDto  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String utente;
	private ProfiloUtente profiloUtente;
	private LocalDateTime dataInserimento;
	private String testo;
	private Long IdPoligonoDichiarato;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getIdPoligonoDichiarato() {
		return IdPoligonoDichiarato;
	}

	public void setIdPoligonoDichiarato(Long idPoligonoDichiarato) {
		IdPoligonoDichiarato = idPoligonoDichiarato;
	}
}
