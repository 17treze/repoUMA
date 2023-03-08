package it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.ReportValidazioneDto;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.TerritorioPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.UtentePrivateClient;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;

import it.tndigitale.a4g.fascicolo.mediator.business.service.client.AnagraficaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.DotazioneTecnicaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.ProxyClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.ZootecniaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.exception.FascicoloInvalidConditionException;
import it.tndigitale.a4g.fascicolo.mediator.dto.ReportValidazioneFascicoloDto;
import it.tndigitale.a4g.fascicolo.mediator.dto.TipoSchedaValidazioneEnum;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;

@Service
public class SchedaValidazioneFascicoloService {

	private static final String TEMPLATE_PATH = "template/scheda_validazione_fascicolo.docx";
	private static final Logger logger = LoggerFactory.getLogger(SchedaValidazioneFascicoloService.class);

	@Autowired 
	private AnagraficaPrivateClient anagraficaPrivateClient;
	@Autowired 
	private ZootecniaPrivateClient zootecniaPrivateClient;
	@Autowired
	private TerritorioPrivateClient territorioPrivateClient;
	@Autowired
	private DotazioneTecnicaPrivateClient dotazioneTecnicaPrivateClient;
	@Autowired
	private UtentePrivateClient utentePrivateClient;
	@Autowired
	private ProxyClient proxyClient;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	UtenteComponent utenteComponent;

	public Resource stampaSchedaValidazioneFascicolo(final String cuaa) throws FascicoloInvalidConditionException, IOException {

		// esegui controlli di validazione
//		TODO se il fascicolo è in stato CONTROLLATO_OK passa, altrimenti deve ripetere i controlli asincroni
		var fascicoloLive = anagraficaPrivateClient.getByCuaaUsingGET1(cuaa, 0);
		if (!fascicoloLive.getStato().equals(FascicoloDto.StatoEnum.CONTROLLATO_OK)
				&& !fascicoloLive.getStato().equals(FascicoloDto.StatoEnum.ALLA_FIRMA_CAA)
				&& !fascicoloLive.getStato().equals(FascicoloDto.StatoEnum.FIRMATO_CAA)
				&& !fascicoloLive.getStato().equals(FascicoloDto.StatoEnum.ALLA_FIRMA_AZIENDA)
				&& !fascicoloLive.getStato().equals(FascicoloDto.StatoEnum.VALIDATO)) {
			throw new FascicoloInvalidConditionException(FascicoloDto.StatoEnum.CONTROLLATO_OK.toString());
		}
		Long idSchedaValidazione;
		Resource report;
		if(fascicoloLive.getSchedaValidazione() == null) {
			idSchedaValidazione = anagraficaPrivateClient.getNewIdSchedaValidazioneUsingGET();
			report = getDatiPerSchedaValidazioneFascicolo(cuaa, TipoSchedaValidazioneEnum.DEFINITIVA, idSchedaValidazione);
		} else {
			idSchedaValidazione = fascicoloLive.getIdSchedaValidazione();
			report = new ByteArrayResource(fascicoloLive.getSchedaValidazione());
		}
		File schedaValidazioneFile = null;
        try {
        	schedaValidazioneFile = File.createTempFile("schedaValidazione", ".pdf");
            FileUtils.writeByteArrayToFile(schedaValidazioneFile, report.getInputStream().readAllBytes());
        } catch (Exception ex) {
        	
        } finally {
        	if (schedaValidazioneFile != null) {
        		try {
        			// aggiorna stato fascicolo solo da CONTROLLATO_OK
        			if (fascicoloLive.getStato().equals(FascicoloDto.StatoEnum.CONTROLLATO_OK)) {
    					anagraficaPrivateClient.aggiornaStatoFascicoloAllaFirmaCaa(cuaa, idSchedaValidazione, schedaValidazioneFile);
        			}
					Files.delete(schedaValidazioneFile.toPath());
				} catch (IOException e) {
					
				}	
        	}
        }
		return report;
	}

	public Resource stampaSchedaValidazioneFascicoloBozza(final String cuaa) throws IOException {
		// reperisce i dati e stampa il report
		return getDatiPerSchedaValidazioneFascicolo(cuaa, TipoSchedaValidazioneEnum.BOZZA, new Long(0));
	}

	private Resource getDatiPerSchedaValidazioneFascicolo(final String cuaa, final TipoSchedaValidazioneEnum tipoSchedaValidazione, Long idSchedaValidazione) throws IOException {
		ReportValidazioneDto anagraficaReportValidazione = anagraficaPrivateClient.getReportValidazione(cuaa);
		
		anagraficaReportValidazione.setIdentificativoScheda(
				tipoSchedaValidazione == TipoSchedaValidazioneEnum.DEFINITIVA ? idSchedaValidazione.toString() : "BOZZA");
		
		ReportValidazioneFascicoloDto reportValidazioneFascicoloDto = new ReportValidazioneFascicoloDto()
				.setTipoSchedaValidazione(tipoSchedaValidazione)
				.setReportValidazioneAnagrafica(anagraficaReportValidazione)
				.setReportValidazioneZootecnia(zootecniaPrivateClient.getReportValidazione(cuaa))
				.setReportValidazioneTerreni(territorioPrivateClient.getReportValidazione(cuaa))
				.setReportValidazioneDotazioneTecnica(dotazioneTecnicaPrivateClient.getReportValidazione(cuaa))
				.setReportValidazioneUtente(utentePrivateClient.getReportValidazione(utenteComponent.utenza()));

		if (logger.isDebugEnabled()) {
			logger.debug(objectMapper.writeValueAsString(reportValidazioneFascicoloDto));
		}

		return new ByteArrayResource(proxyClient.stampaPdfA(TEMPLATE_PATH, objectMapper.writeValueAsString(reportValidazioneFascicoloDto)));
	}

}
