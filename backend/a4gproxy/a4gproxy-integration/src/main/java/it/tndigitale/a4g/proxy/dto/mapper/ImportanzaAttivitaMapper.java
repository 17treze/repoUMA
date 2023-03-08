package it.tndigitale.a4g.proxy.dto.mapper;

import it.tndigitale.a4g.proxy.dto.persona.ImportanzaAttivita;

public class ImportanzaAttivitaMapper {

	public static ImportanzaAttivita fromAnagraficaImpresa(String sigla) throws Exception {
		switch (sigla) {
		case "P":
			return ImportanzaAttivita.PRIMARIO;
		case "S":
			return ImportanzaAttivita.SECONDARIO;
		case "I":
			return ImportanzaAttivita.PRIMARIO_IMPRESA;
		default:
			throw new Exception(String.format("ImportanzaAttivita Missing mapping for code '%s'", sigla));
		}
	}
}
