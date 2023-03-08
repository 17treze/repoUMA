package it.tndigitale.a4g.proxy.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import it.tndigitale.a4g.proxy.dto.persona.AttivitaDto;
import it.tndigitale.a4g.proxy.dto.persona.ImportanzaAttivita;
import it.tndigitale.a4g.proxy.utils.DateFormatUtils;
import it.tndigitale.ws.isvalidazioneanagrafe.ISAttivitaPro2P;
import it.tndigitale.ws.wssanagraficaimprese.ATTIVITAISTAT;

public class AttivitaAtecoMapper {
	
	private AttivitaAtecoMapper() {}
	
	public static List<AttivitaDto> fromAgenziaEntrateV2(List<ISAttivitaPro2P> listAttivita) {
		return listAttivita.stream().map(attivita -> {
			AttivitaDto attivitaAteco = new AttivitaDto();
			attivitaAteco.setCodice(attivita.getCodice().getValue());
			attivitaAteco.setDescrizione(attivita.getDescrizione().getValue());
			return attivitaAteco;
		}).collect(Collectors.toList());
	}
	
	public static List<AttivitaDto> fromV2(List<ATTIVITAISTAT> listAttivitaParix) {
		return listAttivitaParix.stream().map(attivita -> {
			AttivitaDto attivitaAteco = new AttivitaDto();
			attivitaAteco.setCodice(attivita.getCATTIVITA());
			attivitaAteco.setDescrizione(attivita.getDESCATTIVITA());
			
			ImportanzaAttivita importanza;
			try {
				importanza = ImportanzaAttivitaMapper.fromAnagraficaImpresa(attivita.getCIMPORTANZA());
			} catch (Exception e) {
				importanza = null;
			}
			attivitaAteco.setImportanza(importanza);
			return attivitaAteco;
		}).collect(Collectors.toList());
	}

}
