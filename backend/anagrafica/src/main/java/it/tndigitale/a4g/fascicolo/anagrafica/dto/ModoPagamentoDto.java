package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.ModoPagamentoModel;

@ApiModel(description = "Dati a supporto della modifica della sede operativa del CAA mandatario  ")
public class ModoPagamentoDto  implements Serializable {
	
	@ApiModelProperty(value = "Identificativo modo pagamento")
	private Long id;
	@ApiModelProperty(value = "Codice IBAN")
	private String iban;
	@ApiModelProperty(value = "Codice BIC")
	private String bic;
	@ApiModelProperty(value = "Denominazione Istituto di credito")
	private String denominazioneIstituto;
	@ApiModelProperty(value = "Denominazione Filiale")
	private String denominazioneFiliale;
	@ApiModelProperty(value = "Sede della filiale")
	private String cittaFiliale;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getIban() {
		return iban;
	}
	
	public void setIban(String iban) {
		this.iban = iban;
	}
	
	public String getBic() {
		return bic;
	}
	
	public void setBic(String bic) {
		this.bic = bic;
	}
	
	public String getDenominazioneIstituto() {
		return denominazioneIstituto;
	}
	
	public void setDenominazioneIstituto(String denominazioneIstituto) {
		this.denominazioneIstituto = denominazioneIstituto;
	}
	
	public String getDenominazioneFiliale() {
		return denominazioneFiliale;
	}
	
	public void setDenominazioneFiliale(String denominazioneFiliale) {
		this.denominazioneFiliale = denominazioneFiliale;
	}
	
	public String getCittaFiliale() {
		return cittaFiliale;
	}
	
	public void setCittaFiliale(String cittaFiliale) {
		this.cittaFiliale = cittaFiliale;
	}
	
	public static List<ModoPagamentoDto> build(final List<ModoPagamentoModel> input) {
		List<ModoPagamentoDto> output = new ArrayList<>();
		if (input == null || input.isEmpty()) {
			return output;
		}
		input.forEach(modoPagamento -> {
			output.add(ModoPagamentoDto.mapper(modoPagamento));
		});
		return output;
	}
	
	private static ModoPagamentoDto mapper(ModoPagamentoModel input) {
		ModoPagamentoDto dto = new ModoPagamentoDto();
		dto.setId(input.getId());
		dto.setDenominazioneIstituto(input.getDenominazioneIstituto());
		dto.setDenominazioneFiliale(input.getDenominazioneFiliale());
		dto.setCittaFiliale(input.getCittaFiliale());
		dto.setIban(input.getIban());
		dto.setBic(input.getBic());
		return dto;
	}
}
