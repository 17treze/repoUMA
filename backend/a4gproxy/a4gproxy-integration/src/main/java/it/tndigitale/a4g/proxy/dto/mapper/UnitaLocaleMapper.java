package it.tndigitale.a4g.proxy.dto.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import it.tndigitale.a4g.proxy.dto.persona.UnitaLocaleDto;
import it.tndigitale.ws.wssanagraficaimprese.DATIIMPRESA;
import it.tndigitale.ws.wssanagraficaimprese.INDIRIZZO;
import it.tndigitale.ws.wssanagraficaimprese.LOCALIZZAZIONE;
import it.tndigitale.ws.wssanagraficaimprese.LOCALIZZAZIONI;
import it.tndigitale.ws.wssanagraficaimprese.RISPOSTA;

public class UnitaLocaleMapper {
	private static final Logger log = LoggerFactory.getLogger(UnitaLocaleMapper.class);

	public static List<UnitaLocaleDto> from(JAXBElement<RISPOSTA> dettaglioCompleto) throws Exception {
		if(dettaglioCompleto != null && dettaglioCompleto.getValue()!= null && dettaglioCompleto.getValue().getDATI() != null && dettaglioCompleto.getValue().getDATI().getDATIIMPRESA()!= null) {
			DATIIMPRESA datiImpresa = dettaglioCompleto.getValue().getDATI().getDATIIMPRESA();

			List<LOCALIZZAZIONI> localizzazioni = datiImpresa.getLOCALIZZAZIONI();
			
			List<UnitaLocaleDto> unitaLocali = new ArrayList<UnitaLocaleDto>();
			
			if(localizzazioni != null && localizzazioni.size() > 0) {
				localizzazioni.forEach(input -> {
					if (input.getLOCALIZZAZIONE() != null && input.getLOCALIZZAZIONE().size() > 0) {
						input.getLOCALIZZAZIONE().forEach(localizzazione -> {
							unitaLocali.add(UnitaLocaleMapper.from(localizzazione));
						});
					}
				});
			}
			return unitaLocali;
		}
		return null;
	}
	
	public static List<UnitaLocaleDto> from(DATIIMPRESA datiImpresa){
		if(datiImpresa != null) {

			List<LOCALIZZAZIONI> localizzazioni = datiImpresa.getLOCALIZZAZIONI();
			
			List<UnitaLocaleDto> unitaLocali = new ArrayList<UnitaLocaleDto>();
			
			if(localizzazioni != null && localizzazioni.size() > 0) {
				localizzazioni.forEach(input -> {
					if (input.getLOCALIZZAZIONE() != null && input.getLOCALIZZAZIONE().size() > 0) {
						input.getLOCALIZZAZIONE().forEach(localizzazione -> {
							unitaLocali.add(UnitaLocaleMapper.from(localizzazione));
						});
					}
				});
			}
			return unitaLocali;
		}
		return null;
	}
	
	
	
	public static UnitaLocaleDto from(LOCALIZZAZIONE localizzazione) {
		UnitaLocaleDto unitaLocale = new UnitaLocaleDto();
		
		unitaLocale.setIdentificativo(localizzazione.getNUMEROTIPO().getNUMERO());

		List<String> destinazioneUso = new ArrayList<String>();
		if(localizzazione.getNUMEROTIPO().getTIPO1() != null && localizzazione.getNUMEROTIPO().getTIPO1().trim().length() > 0) {
			destinazioneUso.add(localizzazione.getNUMEROTIPO().getTIPO1());
		}
		if(localizzazione.getNUMEROTIPO().getTIPO2() != null && localizzazione.getNUMEROTIPO().getTIPO2().trim().length() > 0) {
			destinazioneUso.add(localizzazione.getNUMEROTIPO().getTIPO2());
		}
		if(localizzazione.getNUMEROTIPO().getTIPO3() != null && localizzazione.getNUMEROTIPO().getTIPO3().trim().length() > 0) {
			destinazioneUso.add(localizzazione.getNUMEROTIPO().getTIPO3());
		}
		if(localizzazione.getNUMEROTIPO().getTIPO4() != null && localizzazione.getNUMEROTIPO().getTIPO4().trim().length() > 0) {
			destinazioneUso.add(localizzazione.getNUMEROTIPO().getTIPO4());
		}
		if(localizzazione.getNUMEROTIPO().getTIPO5() != null && localizzazione.getNUMEROTIPO().getTIPO5().trim().length() > 0) {
			destinazioneUso.add(localizzazione.getNUMEROTIPO().getTIPO5());
		}
		unitaLocale.setDestinazioniUso(destinazioneUso);
//		data apertura
		unitaLocale.setDataApertura(parseDate(localizzazione.getDTAPERTURA()));
		
//		eventuale cessazione
		if(localizzazione.getCESSAZIONELOC() != null) {
			unitaLocale.setDataCessazione(parseDate(localizzazione.getCESSAZIONELOC().getDTCESSAZIONE()));	
			unitaLocale.setDataDenunciaCessazione(parseDate(localizzazione.getCESSAZIONELOC().getDTDENUNCIACESS()));
			unitaLocale.setCausaleCessazione(localizzazione.getCESSAZIONELOC().getCAUSALE());
		}
		
//		attivita'
		unitaLocale.setAttivita(localizzazione.getATTIVITA());
		
//		commercio al dettaglio
		if(localizzazione.getCOMMERCIODETTAGLIO() != null) {
			unitaLocale.setDataDenunciaInizioAttivita(parseDate(localizzazione.getCOMMERCIODETTAGLIO().getDTDENUNCIA()));	
			unitaLocale.setSettoreMerceologico(localizzazione.getCOMMERCIODETTAGLIO().getSETTOREMERCEOLOGICO());
		}
		
		//Indirizzo, telefono, pec
		INDIRIZZO indirizzo = localizzazione.getINDIRIZZO();
		unitaLocale.setIndirizzo(IndirizzoMapper.from(indirizzo));
		unitaLocale.setIndirizzoPec(indirizzo.getINDIRIZZOPEC());
		unitaLocale.setTelefono(indirizzo.getTELEFONO());

		//Lista Attivita economiche Ateco
		if(localizzazione.getCODICEATECOUL() != null && localizzazione.getCODICEATECOUL().getATTIVITAISTAT() != null) {
			unitaLocale.setAttivitaAteco(AttivitaAtecoMapper.fromV2(localizzazione.getCODICEATECOUL().getATTIVITAISTAT()));	
		}
		
		return unitaLocale;
	}
	
	
	private static LocalDate parseDate(String dataInput) {
		
		try {
			if(StringUtils.hasLength(dataInput) && dataInput.trim().length() > 0) {
				return LocalDate.parse(dataInput, DateTimeFormatter.ofPattern("yyyyMMdd"));	
			}	
		}catch(DateTimeParseException e) {
			log.warn("Formato data {} invalido", dataInput);
		}
		return null;		
		
	}
	
}
