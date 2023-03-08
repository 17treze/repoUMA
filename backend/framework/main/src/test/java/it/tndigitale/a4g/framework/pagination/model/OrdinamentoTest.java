package it.tndigitale.a4g.framework.pagination.model;


import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrdinamentoTest {

    private static final String MY_PROPERTY = "MY_PROPERTY";
    private static final Ordinamento DEFAULT_ORDINAMENTO = new Ordinamento("id", Ordine.ASC);
    private static final Ordine MY_ORDINE = Ordine.DESC;

    @Test
    public void ifOrdinamentoNullThenReturnOrdinamentoDefault() throws Exception {
        Ordinamento ordinamento = Ordinamento.getOrDefault(null);

        assertThat(ordinamento).isEqualTo(DEFAULT_ORDINAMENTO);
    }

    @Test
    public void ifOrdinamentoNotNullWithProprietaNullThenReturnOrdinamentoWithProprietaDefault() throws Exception {
        Ordinamento ordinamento = Ordinamento.getOrDefault(new Ordinamento(null, Ordine.ASC));

        assertThat(ordinamento.getProprieta()).isEqualTo("id");
        assertThat(ordinamento.getOrdine()).isEqualTo(Ordine.ASC);
    }

    @Test
    public void ifOrdinamentoNotNullWithProprietaIsEmptyThenReturnOrdinamentoWithProprietaDefault() throws Exception {
        Ordinamento ordinamento = Ordinamento.getOrDefault(new Ordinamento("", Ordine.ASC));

        assertThat(ordinamento.getProprieta()).isEqualTo("id");
        assertThat(ordinamento.getOrdine()).isEqualTo(Ordine.ASC);
    }

    @Test
    public void ifOrdinamentoNotNullWithOrderNullThenReturnOrdinamentoWithOrdineDefault() throws Exception {
        Ordinamento ordinamento = Ordinamento.getOrDefault(new Ordinamento(MY_PROPERTY, null));

        assertThat(ordinamento.getProprieta()).isEqualTo(MY_PROPERTY);
        assertThat(ordinamento.getOrdine()).isEqualTo(Ordine.ASC);
    }

    @Test
    public void ifOrdinamentoNotNullWithOrderAndProprietaNotNullReturnOrdinamento() throws Exception {
        Ordinamento ordinamento = Ordinamento.getOrDefault(new Ordinamento(MY_PROPERTY, MY_ORDINE));

        assertThat(ordinamento.getProprieta()).isEqualTo(MY_PROPERTY);
        assertThat(ordinamento.getOrdine()).isEqualTo(MY_ORDINE);
    }

}
