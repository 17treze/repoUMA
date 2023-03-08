package it.tndigitale.a4gistruttoria.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.dto.Paginazione;

public class PaginazioneUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Paginazione of(String paginazione) throws Exception {
        Paginazione criteriPaginazione = Paginazione.of();
        if (paginazione != null && !paginazione.isEmpty()) {
            criteriPaginazione = objectMapper.readValue(paginazione, Paginazione.class);
        } else {
            criteriPaginazione.setPagina(0);
        }
        return criteriPaginazione;
    }

}
