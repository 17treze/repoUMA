package it.tndigitale.a4g.proxy.dto.mapper;

import java.time.LocalDateTime;

import it.tndigitale.a4g.proxy.dto.persona.IscrizioneRepertorioEconomicoDto;
import it.tndigitale.a4g.proxy.utils.DateFormatUtils;
import it.tndigitale.ws.wssanagraficaimprese.DATIISCRIZIONEREA;
import it.tndigitale.ws.wssanagraficaimprese.ESTREMIIMPRESA;

public class IscrizioneRegistroImpreseMapper {
	
	public static IscrizioneRepertorioEconomicoDto from(ESTREMIIMPRESA estremiImpresa) {
		IscrizioneRepertorioEconomicoDto iscrizioneRepertorioEconomico = new IscrizioneRepertorioEconomicoDto();
		
		//Prendo i dati iscrizione rea corrispodenti alla sede dell'impresa
		DATIISCRIZIONEREA iscrizioneReaSedeImpresa = null;
		for (DATIISCRIZIONEREA iscrizione : estremiImpresa.getDATIISCRIZIONEREA()) {
			if (iscrizione.getFLAGSEDE().equals("SI")) {
				iscrizioneReaSedeImpresa = iscrizione;
				break;
			}
		}

		iscrizioneRepertorioEconomico.setDataIscrizione(DateFormatUtils.convertiDataParixLocalDate(iscrizioneReaSedeImpresa.getDATA()));
		iscrizioneRepertorioEconomico.setNumero(iscrizioneReaSedeImpresa.getNREA());
		
		Long codiceRea = Long.valueOf(iscrizioneReaSedeImpresa.getNREA());
		iscrizioneRepertorioEconomico.setCodiceRea(codiceRea);
		iscrizioneRepertorioEconomico.setProvinciaRea(iscrizioneReaSedeImpresa.getCCIAA());
		iscrizioneRepertorioEconomico.setCessata((iscrizioneReaSedeImpresa.getCESSAZIONE() != null) ? true : false);
		return iscrizioneRepertorioEconomico;
	}

}
