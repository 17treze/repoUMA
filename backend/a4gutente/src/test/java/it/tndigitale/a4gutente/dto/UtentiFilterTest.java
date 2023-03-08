package it.tndigitale.a4gutente.dto;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtentiFilterTest {

    @Test
    public void forGetOrDefault() {
        UtentiFilter filter = UtentiFilter.getOrDefault(new UtentiFilter().setCodiceFiscale("  ")
                                                                          .setNome("")
                                                                          .setCognome("cognome"));

        assertThat(filter.getCodiceFiscale()).isNull();
        assertThat(filter.getNome()).isNull();
        assertThat(filter.getCognome()).isEqualTo("cognome");
    }

}
