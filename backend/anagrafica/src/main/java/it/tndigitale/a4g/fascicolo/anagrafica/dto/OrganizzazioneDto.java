package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.OrganizzazioneModel;

@ApiModel(description = "Dati a supporto della modifica della sede operativa del CAA mandatario  ")
public class OrganizzazioneDto  implements Serializable {
	private static final long serialVersionUID = -4413322001905685132L;
	
	@ApiModelProperty(value = "Identificativo modo pagamento")
	private Long id;
	@ApiModelProperty(value = "Denominazione Organizzazione")
	private String denominazione;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	
	public static List<OrganizzazioneDto> build(final List<OrganizzazioneModel> input) {
		List<OrganizzazioneDto> output = new ArrayList<>();
		if (input == null || input.isEmpty()) {
			return output;
		}
		input.forEach(organizzazione -> {
			output.add(OrganizzazioneDto.mapper(organizzazione));
		});
		return output;
	}
	
	public static OrganizzazioneDto mapper(OrganizzazioneModel input) {
		var dto = new OrganizzazioneDto();
		dto.setId(input.getId());
		dto.setDenominazione(input.getDenominazione());
		return dto;
	}
}
