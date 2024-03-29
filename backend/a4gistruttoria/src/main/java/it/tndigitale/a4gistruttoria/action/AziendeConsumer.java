/**
 * 
 */
package it.tndigitale.a4gistruttoria.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4gistruttoria.config.DateFormatConfig;
import it.tndigitale.a4gistruttoria.dto.IstruttoriaAntimafiaChainHandler;

/**
 * @author S.DeLuca
 *
 */
@Component
public class AziendeConsumer implements Consumer<IstruttoriaAntimafiaChainHandler> {

	private static final Logger logger = LoggerFactory.getLogger(AziendeConsumer.class);

	@Autowired
	private DateFormatConfig dateFormatConfig;

	@Override
	public void accept(IstruttoriaAntimafiaChainHandler istruttoriaAntimafiaChain) {
		try {
			if (istruttoriaAntimafiaChain.getIsParixOK()) {
				// aziende
				JsonNode aziendeImpresa = istruttoriaAntimafiaChain.getDettaglioImpresa().path("aziendeCollegate"); // è un array
				JsonNode soggettiParixArray = istruttoriaAntimafiaChain.getSoggettiParix();
				String cuaa = istruttoriaAntimafiaChain.getCuaa();

				for (JsonNode azienda : aziendeImpresa) {
					String cfSoggetto = azienda.path("codiceFiscale").textValue();
					JsonNode caricheSoggetto = azienda.path("carica"); // è un array

					// soggetti
					for (JsonNode caricaSoggetto : caricheSoggetto) {

						List<JsonNode> soggettiParix = StreamSupport.stream(soggettiParixArray.spliterator(), false).filter(soggettoParix -> 
							// TODO: solo per persone fisiche o anche per aziende collegate?
							cfSoggetto.equals(soggettoParix.path("personagiuridica").path("codicefiscale").textValue())
						).collect(Collectors.toList());

						String tipologiaCaricaSoggetto = caricaSoggetto.path("tipologia").textValue();
						if (soggettiParix != null && soggettiParix.size() == 1) {
							JsonNode caricheSoggettoParix = soggettiParix.get(0).path("cariche").path("carica");

							List<JsonNode> caricheSoggettiParixFiltrate = StreamSupport.stream(caricheSoggettoParix.spliterator(), false).filter(caricaSoggettoParix -> {
								// TODO: check se cccarica o carica sono null oppure anche codice fiscale
								String codiceCaricaSoggettoParix = caricaSoggettoParix.path("ccarica").textValue();
								String codiceCaricaSoggetto = caricaSoggetto.path("codice").textValue();
								return codiceCaricaSoggettoParix.equals(codiceCaricaSoggetto);
							}).collect(Collectors.toList());
							if (caricheSoggettiParixFiltrate != null && caricheSoggettiParixFiltrate.size() == 1) {
								JsonNode caricaSoggettoParix = caricheSoggettiParixFiltrate.get(0).path("dtfine");
								String dtFineCaricaSoggettoParix = caricaSoggettoParix.isNull() ? "" : dateFormatConfig.convertiDataParix(caricaSoggettoParix.textValue());
								if (!dtFineCaricaSoggettoParix.isEmpty()) {
									DateFormat formatterAMF = new SimpleDateFormat(DateFormatConfig.DATE_FORMAT_AMF);
									Date dtFine;
									try {
										dtFine = formatterAMF.parse(dtFineCaricaSoggettoParix);
										if (dtFine.compareTo(new Date()) < 0) {
											// setta carica scaduta
											logger.debug("soggetto ".concat(cfSoggetto).concat(" non ricopre la carica ").concat(tipologiaCaricaSoggetto));
											istruttoriaAntimafiaChain.addSoggettiErrorMessage(
													"soggetto ".concat(cfSoggetto).concat(" non ricopre la carica ").concat(tipologiaCaricaSoggetto).concat(" per L'azienda ").concat(cuaa));
										}
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										throw new Exception("istruttoria antimafia - Parse data fallito ", e);
									}
								}
							} else {
								istruttoriaAntimafiaChain.addSoggettiErrorMessage(
										"soggetto ".concat(cfSoggetto).concat(" non ricopre la carica ").concat(tipologiaCaricaSoggetto).concat(" per L'azienda ").concat(cuaa));
							}
						} else {
							istruttoriaAntimafiaChain.addSoggettiErrorMessage("il soggetto ".concat(cfSoggetto).concat(" non è presente in parix").concat(" per l'azienda ").concat(cuaa));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("istruttoria antimafia - errore durante lo step di recupero dati da soggetti", e);
			throw new RuntimeException("istruttoria antimafia - errore durante lo step di recupero dati da soggetti", e);
		}

	}

}
