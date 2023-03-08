package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MovimentazioneModel;

@ApiModel(description = "Dati per inserimento o rimozione di una sospensione")
public class SospensioneDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "Data di inizio sospensione")
	private LocalDateTime dataInizio;
	@ApiModelProperty(value = "Motivazione della sospensione")
	private String motivazioneInizio;
	@ApiModelProperty(value = "Data di rimozione sospensione")
	private LocalDateTime dataFine;
	@ApiModelProperty(value = "Motivazione rimozione della sospensione")
	private String motivazioneFine;
	@ApiModelProperty(value = "Cuaa")
	private String cuaa;
	@ApiModelProperty(value = "Utente")
	private String utente;

	public LocalDateTime getDataInizio() {
		return dataInizio;
	}
	public SospensioneDto setDataInizio(LocalDateTime dataInizioSospensione) {
		this.dataInizio = dataInizioSospensione;
		return this;
	}
	public String getMotivazioneInizio() {
		return motivazioneInizio;
	}
	public SospensioneDto setMotivazioneInizio(String motivazioneInizioSospensione) {
		this.motivazioneInizio = motivazioneInizioSospensione;
		return this;
	}
	public LocalDateTime getDataFine() {
		return dataFine;
	}
	public SospensioneDto setDataFine(LocalDateTime dataFineSospensione) {
		this.dataFine = dataFineSospensione;
		return this;
	}
	public String getMotivazioneFine() {
		return motivazioneFine;
	}
	public SospensioneDto setMotivazioneFine(String motivazioneFineSospensione) {
		this.motivazioneFine = motivazioneFineSospensione;
		return this;
	}
	public String getCuaa() {
		return cuaa;
	}
	public SospensioneDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getUtente() {
		return utente;
	}
	public SospensioneDto setUtente(String utente) {
		this.utente = utente;
		return this;
	}
	
	public static List<SospensioneDto> build(final Optional<List<MovimentazioneModel>> optional) {
		List<SospensioneDto> output = new ArrayList<>();
		if (optional.isPresent()) {
			optional.get().forEach(sospensione -> {
				output.add(SospensioneDto.mapper(sospensione));
			});
		}
		return output;
	}
	
	private static SospensioneDto mapper(MovimentazioneModel input) {
		SospensioneDto dto = new SospensioneDto();
		dto.setCuaa(input.getFascicolo().getCuaa());
		dto.setDataFine(input.getDataFine());
		dto.setDataInizio(input.getDataInizio());
		dto.setMotivazioneFine(input.getMotivazioneFine());
		dto.setMotivazioneInizio(input.getMotivazioneInizio());
		dto.setUtente(input.getUtente());
		return dto;
	}
	
}
