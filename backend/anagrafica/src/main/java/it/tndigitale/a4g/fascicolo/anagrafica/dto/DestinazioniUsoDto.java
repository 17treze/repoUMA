package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DestinazioneUsoModel;

@ApiModel(description = "Destinazione uso")
public class DestinazioniUsoDto  implements Serializable {

	
//	@ApiModelProperty(value = "Identificativo modo pagamento")

	    private String descrizione;
	   

		public String getDescrizione() {
			return descrizione;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}


	public static List<DestinazioniUsoDto> build(final List<DestinazioneUsoModel> input) {
		List<DestinazioniUsoDto> output = new ArrayList<DestinazioniUsoDto>();
		if(input == null || input.size() == 0) {
			return output;
		}
		
		input.forEach(uso -> {
			output.add(DestinazioniUsoDto.mapper(uso));
		});
		
		return output;
	}
	
	private static DestinazioniUsoDto mapper(DestinazioneUsoModel input) {
		if(input == null) {
			return null;
		}
		DestinazioniUsoDto dto = new DestinazioniUsoDto();
		
		dto.setDescrizione(input.getDescrizione());
		
		return dto;
	}
}
