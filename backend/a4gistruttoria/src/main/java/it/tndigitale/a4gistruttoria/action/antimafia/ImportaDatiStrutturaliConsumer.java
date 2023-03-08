package it.tndigitale.a4gistruttoria.action.antimafia;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.action.ImportoRichiestoImpresaConsumer;
import it.tndigitale.a4gistruttoria.dto.ImportaDatiStrutturaliHandler;

@Component
public class ImportaDatiStrutturaliConsumer implements Consumer<ImportaDatiStrutturaliHandler> {

	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static Logger logger = LoggerFactory.getLogger(ImportaDatiStrutturaliConsumer.class);

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ImportoRichiestoImpresaConsumer importoRichiestoImpresaConsumer;
	@Value("${a4gistruttoria.srt.imprese.importorichiesto.uri}")
	private String urlSrtImpreseImportoRichiesto;

	@Override
	public void accept(ImportaDatiStrutturaliHandler importaDatiStrutturaliHandler) {
		String dataPresentazione = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(importaDatiStrutturaliHandler.getDataPresentazione());
		String response = restTemplate.getForObject(urlSrtImpreseImportoRichiesto, String.class, importaDatiStrutturaliHandler.getCuaa(), dataPresentazione);

		try {
			if (Objects.nonNull(response)) {
				objectMapper.readTree(response).forEach(jsonNode -> {
					importaDatiStrutturaliHandler.setImportoRichiesto(jsonNode);
					importoRichiestoImpresaConsumer.accept(importaDatiStrutturaliHandler);
				});
			}
		} catch (IOException e) {
			logger.error("Errore nell' importazione dei dati strutturali", e);
			throw new RuntimeException();
		} catch (Exception e) {
			logger.error("Errore non previsto", e);
			throw new RuntimeException();
		}
	}

}
