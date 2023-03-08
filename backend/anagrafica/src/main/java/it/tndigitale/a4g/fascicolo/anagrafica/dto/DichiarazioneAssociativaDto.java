package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloOrganizzazioneModel;

@ApiModel(description = "Dati per l'aggiunta o chiusura di un'autodichiarazione associativa")
public class DichiarazioneAssociativaDto  implements Serializable {
	
	private static final long serialVersionUID = 8756101598257009143L;

	@ApiModelProperty(value = "Identificativo dichiarazione associativa")
	private Long id;
	
	@ApiModelProperty(value = "Data di iscrizione all'organizzazione")
	private LocalDate dataInizioAssociazione;
	
	@ApiModelProperty(value = "Data in cui e' stata fatta la dichiarazione di iscrizione all'associazione")
	private LocalDateTime dataInserimentoAssociazione;
	
	@ApiModelProperty(value = "Data di fine associazione all'organizzazione")
	private LocalDate dataFineAssociazione;
	
	@ApiModelProperty(value = "Data in cui e' stata fatta la dichiarazione di chiusura di una autodichiarazione associativa")
	private LocalDateTime dataCancellazioneAssociazione;
	
	@ApiModelProperty(value = "Organizzazione associata ad un'azienda")
	private OrganizzazioneDto organizzazione;

	@ApiModelProperty(value = "Cuaa")
	private String cuaa;

	@ApiModelProperty(value = "Utente che ha aggiunto l'associazione")
	private String utenteInserimento;

	@ApiModelProperty(value = "Utente che ha chiuso l'associazione")
	private String utenteCancellazione;

	public String getUtenteInserimento() {
		return utenteInserimento;
	}

	public void setUtenteInserimento(String utenteInserimento) {
		this.utenteInserimento = utenteInserimento;
	}

	public String getUtenteCancellazione() {
		return utenteCancellazione;
	}

	public void setUtenteCancellazione(String utenteCancellazione) {
		this.utenteCancellazione = utenteCancellazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDate getDataInizioAssociazione() {
		return dataInizioAssociazione;
	}

	public void setDataInizioAssociazione(LocalDate dataInizioAssociazione) {
		this.dataInizioAssociazione = dataInizioAssociazione;
	}

	public LocalDateTime getDataInserimentoAssociazione() {
		return dataInserimentoAssociazione;
	}

	public void setDataInserimentoAssociazione(LocalDateTime dataInserimentoAssociazione) {
		this.dataInserimentoAssociazione = dataInserimentoAssociazione;
	}

	public LocalDate getDataFineAssociazione() {
		return dataFineAssociazione;
	}

	public void setDataFineAssociazione(LocalDate dataFineAssociazione) {
		this.dataFineAssociazione = dataFineAssociazione;
	}

	public LocalDateTime getDataCancellazioneAssociazione() {
		return dataCancellazioneAssociazione;
	}

	public void setDataCancellazioneAssociazione(LocalDateTime dataCancellazioneAssociazione) {
		this.dataCancellazioneAssociazione = dataCancellazioneAssociazione;
	}

	public OrganizzazioneDto getOrganizzazione() {
		return organizzazione;
	}

	public void setOrganizzazione(OrganizzazioneDto organizzazione) {
		this.organizzazione = organizzazione;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	// G.De Vincentiis 30 giugno 2021
	public static List<DichiarazioneAssociativaDto> build(final List<FascicoloOrganizzazioneModel> input) {
		var output = new ArrayList<DichiarazioneAssociativaDto>();
		if (input == null || input.isEmpty()) {
			return output;
		}
		input.forEach(dichiarazione -> {
			output.add(DichiarazioneAssociativaDto.mapper(dichiarazione));
		});
		return output;
	}
	
	private static DichiarazioneAssociativaDto mapper(FascicoloOrganizzazioneModel input) {
		var dto = new DichiarazioneAssociativaDto();
		dto.setId(input.getId());
		dto.setDataInizioAssociazione(input.getDataInizioAssociazione());
		dto.setDataInserimentoAssociazione(input.getDataInserimentoAssociazione());
		dto.setDataFineAssociazione(input.getDataFineAssociazione());
		dto.setDataCancellazioneAssociazione(input.getDataCancellazioneAssociazione());
		dto.setOrganizzazione(OrganizzazioneDto.mapper(input.getOrganizzazione()));
		dto.setCuaa(input.getCuaa());
		dto.setUtenteInserimento(input.getUtenteInserimento());
		dto.setUtenteCancellazione(input.getUtenteCancellazione());
		return dto;
	}
}
