package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TipoRicercaModificaSuoloTest {

	@Test
	public void tipoRicercaModificaSuolo() {
		assertThat(TipoRicercaModificaSuolo.RICHIESTE_DI_MODIFICA_SUOLO.toString()).isEqualTo("RICHIESTE_DI_MODIFICA_SUOLO");
	}
}
