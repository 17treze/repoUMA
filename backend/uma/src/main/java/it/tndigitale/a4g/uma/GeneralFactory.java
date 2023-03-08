package it.tndigitale.a4g.uma;

import it.tndigitale.a4g.uma.business.service.elenchi.ElenchiTemplate;
import it.tndigitale.a4g.uma.business.service.lavorazioni.RecuperaLavorazioniStrategy;
import it.tndigitale.a4g.uma.business.service.protocollo.ProtocollazioneStrategy;

public interface GeneralFactory {

	RecuperaLavorazioniStrategy getLavorazioniStrategy(String ambito);
	
	ProtocollazioneStrategy getProtocollazioneStrategy(String tipoDocumento);
	
	ElenchiTemplate getElenchiTemplate(String tipoElenco);
}