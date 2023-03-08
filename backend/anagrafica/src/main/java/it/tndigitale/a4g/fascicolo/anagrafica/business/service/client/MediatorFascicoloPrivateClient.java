package it.tndigitale.a4g.fascicolo.anagrafica.business.service.client;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.SchedaValidazioneFascicoloDto;
import it.tndigitale.a4g.mediator.client.api.ControlliCompletezzaControllerApi;
import it.tndigitale.a4g.mediator.client.api.FascicoloValidazioneControllerApi;
import it.tndigitale.a4g.mediator.client.model.InlineObject1;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Component
public class MediatorFascicoloPrivateClient extends AbstractClient {
	@Value("${it.tndigit.a4g.client.fascicolo.mediator.url}") private String url;

	private FascicoloValidazioneControllerApi getFascicoloValidazioneControllerApi() {
		return restClientProxy(FascicoloValidazioneControllerApi.class, url);
	}

	public void validazioneMandato (SchedaValidazioneFascicoloDto schedaValidazioneFascicoloDto) throws IOException {
		File schedaValidazioneFile = File.createTempFile("schedaValidazioneFirmataFirmatario", ".pdf");
		try {
			FileUtils.writeByteArrayToFile(schedaValidazioneFile, schedaValidazioneFascicoloDto.getReport().getByteArray());
			getFascicoloValidazioneControllerApi().validazioneMandato(schedaValidazioneFascicoloDto.getCodiceFiscale(), schedaValidazioneFascicoloDto.getNextIdValidazione(), schedaValidazioneFile);
		} finally {
			Files.delete(schedaValidazioneFile.toPath());
		}
	}

	public void validazioneDetenzioneAutonoma (SchedaValidazioneFascicoloDto schedaValidazioneFascicoloDto) throws IOException {
		File schedaValidazioneFile = File.createTempFile("schedaValidazioneDetenzioneAutonoma", ".pdf");
		try {
			FileUtils.writeByteArrayToFile(schedaValidazioneFile, schedaValidazioneFascicoloDto.getReport().getByteArray());
			getFascicoloValidazioneControllerApi().validazioneDetenzioneAutonoma(schedaValidazioneFascicoloDto.getCodiceFiscale(), schedaValidazioneFascicoloDto.getNextIdValidazione(), schedaValidazioneFile);
		} finally {
			Files.delete(schedaValidazioneFile.toPath());
		}
	}
}
