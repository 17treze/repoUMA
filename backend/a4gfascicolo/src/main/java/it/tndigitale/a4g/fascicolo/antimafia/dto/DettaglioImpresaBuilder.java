package it.tndigitale.a4g.fascicolo.antimafia.dto;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.antimafia.A4gfascicoloConstants;
import it.tndigitale.a4g.fascicolo.antimafia.config.DateFormatConfig;

@Component
public class DettaglioImpresaBuilder {

	private static final String PROVINCIA = "provincia";

	private static final String COMUNE = "comune";

	private static final String NCIVICO = "ncivico";

	private static final String TOPONIMO = "toponimo";

	private static final String CAPITALI = "capitali";

	private static final String DURATASOCIETA = "duratasocieta";

	private static final String ESTREMIIMPRESA = "estremiimpresa";

	private static final String INDIRIZZO = "indirizzo";

	private static final String INFORMAZIONISEDE = "informazionisede";


	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DateFormatConfig dateFormatConfig;

	@Autowired
	private SoggettiImpresaBuilder soggettiImpresaBuilder;

	@Autowired
	private AziendeCollegateBuilder aziendeCollegateBuilder;



	public DettaglioImpresa build(JsonNode datiImpresa) throws Exception {
		return build(datiImpresa, false);
	}



	public DettaglioImpresa build(JsonNode datiImpresa, boolean skipException) throws Exception {
		DettaglioImpresa dettaglioImpresa = objectMapper.readerFor(DettaglioImpresa.class).readValue(datiImpresa);
		dettaglioImpresa.setFormaGiuridicaCodice(datiImpresa.path(ESTREMIIMPRESA).path("formagiuridica").path("cformagiuridica").textValue());
		dettaglioImpresa.setFormaGiuridicaDescrizione(datiImpresa.path(ESTREMIIMPRESA).path("formagiuridica").path("descrizione").textValue());
		dettaglioImpresa.setDenominazione(datiImpresa.path(ESTREMIIMPRESA).path("denominazione").textValue());
		dettaglioImpresa.setOggettoSociale(datiImpresa.path("oggettosociale").textValue());
		dettaglioImpresa.setCodiceFiscale(datiImpresa.path(ESTREMIIMPRESA).path("codicefiscale").textValue());
		dettaglioImpresa.setPartitaIva(datiImpresa.path(ESTREMIIMPRESA).path("partitaiva").textValue());

		dettaglioImpresa.setIndirizzoPEC(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("indirizzopec").textValue());

		StringBuilder sedeLegale = new StringBuilder();
		sedeLegale.append(!datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path(TOPONIMO).isNull() ? datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path(TOPONIMO).textValue().concat(" ") : "");
		sedeLegale.append(!datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("via").isNull() ? datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("via").textValue().concat(" ") : "");
		sedeLegale.append(!datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path(NCIVICO).isNull() ? datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path(NCIVICO).textValue().concat(" ") : "");
		sedeLegale.append(!datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("cap").isNull() ? datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("cap").textValue().concat(" ") : "");
		sedeLegale.append(!datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path(COMUNE).isNull() ? datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path(COMUNE).textValue().concat(" ") : "");
		sedeLegale.append(!datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path(PROVINCIA).isNull() ? datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path(PROVINCIA).textValue() : "");
		dettaglioImpresa.setSedeLegale(sedeLegale.toString());
		Sede sede=new Sede();
		sede.setAltreindicazioni(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("altreindicazioni").textValue());
		sede.setCap(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("cap").textValue());
		sede.setCcomune(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("ccomune").textValue());
		sede.setComune(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path(COMUNE).textValue());
		sede.setFax(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("fax").textValue());
		sede.setFrazione(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("frazione").textValue());
		sede.setNcivico(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path(NCIVICO).textValue());
		sede.setProvincia(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path(PROVINCIA).textValue());
		sede.setStato(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("stato").textValue());
		sede.setStradario(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("stradario").textValue());
		sede.setTelefono(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("telefono").textValue());
		sede.setToponimo(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path(TOPONIMO).textValue());
		sede.setVia(datiImpresa.path(INFORMAZIONISEDE).path(INDIRIZZO).path("via").textValue());
		dettaglioImpresa.setSede(sede);

		// SET SOGGETTI IMPRESA
		dettaglioImpresa.setSoggettiImpresa(soggettiImpresaBuilder.build(datiImpresa.path("personesede"),skipException));

		// SET AZIENDE COLLEGATE
		dettaglioImpresa.setAziendeCollegate(aziendeCollegateBuilder.build(datiImpresa.path("personesede")));


		// SET CCIAA
		JsonNode datiIscrizioneREA=null;
		JsonNode datiIscrizioneREAArray = datiImpresa.path(ESTREMIIMPRESA).path("datiiscrizionerea");
		if (!datiIscrizioneREAArray.elements().hasNext()) {
			throw new Exception("Impresa non trovata");
		} else {
			datiIscrizioneREA = StreamSupport.stream(datiIscrizioneREAArray.spliterator(), false)
					.filter(datiIscrizione -> "TN".equals(datiIscrizione.path("cciaa").textValue()))
					.findFirst()
					.orElseThrow(() -> new Exception("Nessuna iscrizione alla Camera di Commercio trovata"));
//			if (datiIscrizioneREAArray.has(1)) {
//				if (skipException) {
//					datiIscrizioneREA=datiIscrizioneREAArray.get( datiIscrizioneREAArray.size()-1);//prendo l'ultimo, cioè il più aggiornato
//				}
//			}

		}

		EstremiCCIAA estremiCCIAA = new EstremiCCIAA();
		estremiCCIAA.setSede(datiIscrizioneREA.path("cciaa").textValue());
		estremiCCIAA.setNumeroIscrizione(datiIscrizioneREA.path("nrea").textValue());
		estremiCCIAA.setDataIscrizione(dateFormatConfig.convertiDataParix(datiIscrizioneREA.path("data").textValue()));
		dettaglioImpresa.setEstremiCCIAA(estremiCCIAA);

		// SET DETTAGLIO PERSONA GIURIDICA
		// TODO indagare meglio sul motivo dell' introduzione dell' if sulle ditte individuali
		//if (!dettaglioImpresa.getFormaGiuridicaCodice().equals(A4gfascicoloConstants.DITTA_INDIVIDUALE)) {
		DettaglioPersonaGiuridica dettPG = new DettaglioPersonaGiuridica();
		dettPG.setFormaGiuridica(dettaglioImpresa.getFormaGiuridicaDescrizione());
		if (A4gfascicoloConstants.DURATA_ILLIMITATA.equals(datiImpresa.path(DURATASOCIETA).path("durataillimitata").textValue())) {
			dettPG.setDurata(A4gfascicoloConstants.DURATA_ILLIMITATA_AMF);
		} else {
			dettPG.setDurata(dateFormatConfig.convertiDataParix(datiImpresa.path(DURATASOCIETA).path("dttermine").textValue()));
		}

		dettPG.setEstremiCostituzione(dateFormatConfig.convertiDataParix((datiImpresa.path(DURATASOCIETA).path("dtcostituzione").textValue())));
		if (datiImpresa.path(CAPITALI).textValue() != null) {
			String valore = datiImpresa.path(CAPITALI).path("capitalesociale").path("deliberato").textValue() + " "
					+ datiImpresa.path(CAPITALI).path("capitalesociale").path("valuta").textValue();
			Double n = Double.parseDouble(valore);
			DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.ITALY);
			// Create a DecimalFormatSymbols for each locale and sets its new currency symbol.
			DecimalFormatSymbols symbol = new DecimalFormatSymbols(Locale.ITALY);
			symbol.setCurrencySymbol("");
			// Set the new DecimalFormatSymbols into formatter object.
			formatter.setDecimalFormatSymbols(symbol);
			dettPG.setCapitaleSociale(formatter.format(n)); // DATI_IMPRESA.CAPITALE_SOCIALE.DELIBERATO/SOTTOSCRITTO/VERSATO???
		} else {
			dettPG.setCapitaleSociale("Dato non disponibile");
		}
		dettaglioImpresa.setDettaglioPersonaGiuridica(dettPG);
		//}

		return dettaglioImpresa;
	}
}
