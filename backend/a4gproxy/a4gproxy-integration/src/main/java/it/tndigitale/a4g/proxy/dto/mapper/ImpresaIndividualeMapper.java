package it.tndigitale.a4g.proxy.dto.mapper;

import it.tndigitale.a4g.proxy.dto.persona.ImpresaIndividualeDto;
import it.tndigitale.ws.wssanagraficaimprese.DATIIMPRESA;

public class ImpresaIndividualeMapper {
	
	public static ImpresaIndividualeDto from(DATIIMPRESA datiImpresa) {
		ImpresaIndividualeDto impresaIndividualeDto = new ImpresaIndividualeDto();
		impresaIndividualeDto.setDenominazione(datiImpresa.getESTREMIIMPRESA().getDENOMINAZIONE());
		impresaIndividualeDto.setPartitaIva(datiImpresa.getESTREMIIMPRESA().getPARTITAIVA());
		impresaIndividualeDto.setOggettoSociale(datiImpresa.getOGGETTOSOCIALE());
		impresaIndividualeDto.setFormaGiuridica(datiImpresa.getESTREMIIMPRESA().getFORMAGIURIDICA().getDESCRIZIONE());
		
		impresaIndividualeDto.setSedeLegale(SedeMapper.from(datiImpresa));
		
//		arricchimento dati ute
		impresaIndividualeDto.setUnitaLocali(UnitaLocaleMapper.from(datiImpresa));
		
		return impresaIndividualeDto;
	}
}
