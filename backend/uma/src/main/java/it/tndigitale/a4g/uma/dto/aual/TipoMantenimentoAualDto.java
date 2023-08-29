package it.tndigitale.a4g.uma.dto.aual;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TipoMantenimentoAualDto {
	
	private String tipoMantenimento;
}
