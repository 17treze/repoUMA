package it.tndigitale.a4g.proxy.dto.mapper;

import it.tndigitale.a4g.proxy.dto.zootecnia.AnagraficaAllevamentoDto;
import it.tndigitale.a4g.proxy.dto.zootecnia.StrutturaAllevamentoDto;
import it.tndigitale.a4g.proxy.utils.DateFormatUtils;
import it.tndigitale.a4g.proxy.ws.bdn.allevamenti.ArrayOfRootDatiANAGRAFICAALLEVAMENTO;

public class AnagraficaAllevamentoMapper {
	
	private AnagraficaAllevamentoMapper() {}
	
	

	public static AnagraficaAllevamentoDto from(ArrayOfRootDatiANAGRAFICAALLEVAMENTO.ANAGRAFICAALLEVAMENTO anagraficaAllevamento) {
		StrutturaAllevamentoDto struttura = new StrutturaAllevamentoDto();
		struttura.setIdentificativo(anagraficaAllevamento.getAZIENDACODICE());
		struttura.setCap(anagraficaAllevamento.getCAP());
		struttura.setComune(anagraficaAllevamento.getCOMUNE());
		struttura.setFoglioCatastale(anagraficaAllevamento.getFOGLIOCATASTALE());
		struttura.setIndirizzo(anagraficaAllevamento.getINDIRIZZO());
		struttura.setLatitudine(anagraficaAllevamento.getLATITUDINE());
		struttura.setLocalita(anagraficaAllevamento.getLOCALITA());
		struttura.setLongitudine(anagraficaAllevamento.getLONGITUDINE());
		struttura.setParticella(anagraficaAllevamento.getPARTICELLA());
		struttura.setSezione(anagraficaAllevamento.getSEZIONE());
		struttura.setSubalterno(anagraficaAllevamento.getSUBALTERNO());
		
		AnagraficaAllevamentoDto anagrafica = new AnagraficaAllevamentoDto();
		anagrafica.setStruttura(struttura);
		anagrafica.setIdentificativo(anagraficaAllevamento.getALLEVID()); 
		anagrafica.setAutorizzazioneLatte(anagraficaAllevamento.getAUTORIZZAZIONELATTE());
		anagrafica.setCapiTotali(anagraficaAllevamento.getCAPITOTALI());
		anagrafica.setCfDetentore(anagraficaAllevamento.getCODFISCALEDETEN());
		anagrafica.setCfProprietario(anagraficaAllevamento.getCODFISCALEPROP());
		anagrafica.setDataCalcoloCapi(DateFormatUtils.convertiDataBdnAnagraficaAllevamentiLocalDate(anagraficaAllevamento.getDATACALCOLOCAPI())); // formato "2003-01-01"?
		anagrafica.setDenominazioneDetentore(anagraficaAllevamento.getDENOMDETENTORE());
		anagrafica.setDenominazione(anagraficaAllevamento.getDENOMINAZIONE());
		anagrafica.setDenominazioneProprietario(anagraficaAllevamento.getDENOMPROPRIETARIO());
		anagrafica.setDtFineAttivita(DateFormatUtils.convertiDataBdnAnagraficaAllevamentiLocalDate(anagraficaAllevamento.getDTFINEATTIVITA())); // formato "2003-01-01"
		anagrafica.setDtFineDetentore(DateFormatUtils.convertiDataBdnAnagraficaAllevamentiLocalDate(anagraficaAllevamento.getDTFINEDETENTORE())); // formato "2003-01-01"
		anagrafica.setDtInizioAttivita(DateFormatUtils.convertiDataBdnAnagraficaAllevamentiLocalDate(anagraficaAllevamento.getDTINIZIOATTIVITA())); // formato "2003-01-01"
		anagrafica.setDtInizioDetentore(DateFormatUtils.convertiDataBdnAnagraficaAllevamentiLocalDate(anagraficaAllevamento.getDTINIZIODETENTORE())); // formato "2003-01-01"
		anagrafica.setOrientamentoProduttivo(anagraficaAllevamento.getORIENTAMENTOPRODUTTIVO()); // "I"
		anagrafica.setSoccida(anagraficaAllevamento.getSOCCIDA()); // "N"
		anagrafica.setSpecie(anagraficaAllevamento.getSPECODICE());
		anagrafica.setTipologiaAllevamentoCodice(anagraficaAllevamento.getTIPOALLEVCOD()); // "ST"
		anagrafica.setTipoAllevamentoDescrizione(anagraficaAllevamento.getTIPOALLEVDESCR()); // "STABULATO"
		anagrafica.setTipoProduzione(anagraficaAllevamento.getTIPOPRODUZIONE()); // "AL"		
		
		return anagrafica;
	}
	

}
