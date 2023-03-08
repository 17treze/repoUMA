package it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.util.CustomCollectors;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Component
class JsonTranslator {
    private static final Logger logger = LoggerFactory.getLogger(JsonTranslator.class);

    @Autowired
    private ObjectMapper objectMapper;

    protected Optional<VariabileCalcolo> getVariabile(TipoVariabile tipoVariabile, List<PassoTransizioneModel> passi, TipologiaPassoTransizione codicePasso) throws Exception {
        // Se non riesco a recuperare il valore univoco di una variabile, di default
        // la considero "non bloccante" (vado avanti con l'elaborazione della riga per la domanda)
        // return getVariabile(tipoVariabile, passi, codicePasso, false);
        try {
            PassoTransizioneModel passo = passi.stream()
                    .filter(p -> p.getCodicePasso().equals(codicePasso))
                    .collect(CustomCollectors.toSingleton());
            DatiInput input = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
            DatiOutput output = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

            return Optional.ofNullable(Stream.concat(input.getVariabiliCalcolo().stream(), output.getVariabiliCalcolo().stream())
                    .filter(p -> p.getTipoVariabile().equals(tipoVariabile))
                    .collect(CustomCollectors.toSingleton()));

        } catch (Exception e) {
            logger.debug("Errore recupero variabile: {} (passo: {})", tipoVariabile.name(), codicePasso);
        }
        return Optional.empty();
    }

    protected <T> T getValoreVariabilePrecaricata(Map<TipoVariabile, VariabileCalcolo> variabili, TipoVariabile tipoVariabile, Class<T> type) {
        return getValoreVariabilePrecaricata(variabili, tipoVariabile, type, true);
    }

    protected <T> T getValoreVariabilePrecaricata(Map<TipoVariabile, VariabileCalcolo> variabili, TipoVariabile tipoVariabile, Class<T> type, boolean isDefault) {
        if (variabili.get(tipoVariabile) == null && !isDefault)
            return null;

        if (Float.class.isAssignableFrom(type)) {
            Float value = variabili.get(tipoVariabile) != null && variabili.get(tipoVariabile).getValNumber() != null
                    ? variabili.get(tipoVariabile).getValNumber().floatValue() : 0;
            return type.cast(value);
        }
        if (Boolean.class.isAssignableFrom(type)) {
            Boolean value = variabili.get(tipoVariabile) != null && variabili.get(tipoVariabile).getValBoolean();
            return type.cast(value);
        }
        if (String.class.isAssignableFrom(type)) {
            String value = variabili.get(tipoVariabile) != null ? variabili.get(tipoVariabile).getValString() : "";
            return type.cast(value);
        }
        if (BigDecimal.class.isAssignableFrom(type)) {
            BigDecimal value = variabili.get(tipoVariabile) != null ? variabili.get(tipoVariabile).getValNumber() : BigDecimal.ZERO;
            return type.cast(value);
        }
        return null;
    }
}
