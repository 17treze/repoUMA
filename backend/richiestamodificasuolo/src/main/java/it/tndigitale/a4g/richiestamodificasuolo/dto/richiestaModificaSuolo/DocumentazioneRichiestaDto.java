package it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProfiloUtente;

public class DocumentazioneRichiestaDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nomeFile;
	private String descrizione;
	private Long dimensione;
	private String utente;
	private ProfiloUtente profiloUtente;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataInserimento;
	private byte[] docContent;
	private Long IdPoligonoDichiarato;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Long getDimensione() {
		return dimensione;
	}

	public void setDimensione(Long dimensione) {
		this.dimensione = dimensione;
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

	public byte[] getDocContent() {
		return docContent;
	}

	public void setDocContent(byte[] docContent) {
		this.docContent = docContent;
	}

	public Long getIdPoligonoDichiarato() {
		return IdPoligonoDichiarato;
	}

	public void setIdPoligonoDichiarato(Long idPoligonoDichiarato) {
		IdPoligonoDichiarato = idPoligonoDichiarato;
	}
}
