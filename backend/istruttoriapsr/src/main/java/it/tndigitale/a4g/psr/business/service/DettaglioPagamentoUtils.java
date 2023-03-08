package it.tndigitale.a4g.psr.business.service;

import it.tndigitale.a4g.psr.dto.DettaglioPagametoPsrRow;

import java.util.Collection;
import java.util.NoSuchElementException;

public class DettaglioPagamentoUtils {
    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static DettaglioPagametoPsrRow findValoreByRowValue(Collection<DettaglioPagametoPsrRow> dettaglioPagametoPsrRows, String rowName) {
        return dettaglioPagametoPsrRows.stream().filter(c -> c.getVariabile().equals(rowName)).findFirst().orElseThrow();
    }

    public static DettaglioPagametoPsrRow findValoreByRowValueNotRequired(Collection<DettaglioPagametoPsrRow> dettaglioPagametoPsrRows, String rowName) {
        try {
            return findValoreByRowValue(dettaglioPagametoPsrRows, rowName);
        } catch (NoSuchElementException ex) {
            return new DettaglioPagametoPsrRow();
        }
    }
}
