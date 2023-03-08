package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public class UsoSuoloDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String codUsoSuolo;
	private String descrizione;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataInizio;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataFine;
	private Integer visualizzaBo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodUsoSuolo() {
		return codUsoSuolo;
	}

	public void setCodUsoSuolo(String codUsoSuolo) {
		this.codUsoSuolo = codUsoSuolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public LocalDateTime getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(LocalDateTime dataInizio) {
		this.dataInizio = dataInizio;
	}

	public LocalDateTime getDataFine() {
		return dataFine;
	}

	public void setDataFine(LocalDateTime dataFine) {
		this.dataFine = dataFine;
	}

	public Integer getVisualizzaBo() {
		return visualizzaBo;
	}

	public void setVisualizzaBo(Integer visualizzaBo) {
		this.visualizzaBo = visualizzaBo;
	}

}
