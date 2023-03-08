package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.AttivitaAtecoModel;

@ApiModel(description = "Attivita ateco")
public class AttivitaAtecoDto  implements Serializable {

	
//	@ApiModelProperty(value = "Identificativo modo pagamento")

	 	private String codice;

	    private String descrizione;
	   
	    private String importanza;

		private String fonte;
	
	

		public String getCodice() {
			return codice;
		}

		public void setCodice(String codice) {
			this.codice = codice;
		}

		public String getDescrizione() {
			return descrizione;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}

		public String getImportanza() {
			return importanza;
		}

		public void setImportanza(String importanza) {
			this.importanza = importanza;
		}

		public String getFonte() {
			return fonte;
		}

		public void setFonte(String fonte) {
			this.fonte = fonte;
		}

	public static List<AttivitaAtecoDto> build(final List<AttivitaAtecoModel> input) {
		List<AttivitaAtecoDto> output = new ArrayList<AttivitaAtecoDto>();
		if(input == null || input.size() == 0) {
			return output;
		}
		
		input.forEach(ateco -> {
			output.add(AttivitaAtecoDto.mapper(ateco));
		});
		
		return output;
	}
	
	private static AttivitaAtecoDto mapper(AttivitaAtecoModel input) {
		if(input == null) {
			return null;
		}
		AttivitaAtecoDto dto = new AttivitaAtecoDto();
		
		dto.setCodice(input.getCodice());
		dto.setDescrizione(input.getDescrizione());
		dto.setFonte(input.getFonte().toString());
		dto.setImportanza(input.getImportanza().toString());
		
		return dto;
	}
}
