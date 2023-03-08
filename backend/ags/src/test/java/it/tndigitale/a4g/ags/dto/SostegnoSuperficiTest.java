package it.tndigitale.a4g.ags.dto;

import org.junit.Test;

import static it.tndigitale.a4g.ags.dto.SostegnoSuperfici.fillWithSostegni;
import static it.tndigitale.a4g.ags.dto.SostegnoSuperfici.fillWithSuperificieTotaleRichiesta;
import static org.assertj.core.api.Assertions.assertThat;

public class SostegnoSuperficiTest {

    @Test
    public void itCalculateTotaleSuperficieRichiestaAndSostegni() {
        SostegnoSuperfici sostegnoSuperfici = sostegnoSuperfici();

        fillWithSuperificieTotaleRichiesta(sostegnoSuperfici);
        fillWithSostegni(sostegnoSuperfici);

        assertThat(sostegnoSuperfici.getSuperficieTotaleRichiesta()).isEqualTo(130L);
        assertThat(sostegnoSuperfici.getSostegnoFrumentoDuroRichiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoFrumentoLeguminoseRichiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoFrumentoProteaginoseRichiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoOlivoNazionareRichiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoOlivoQualitaRichiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoSoiaRichiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoOlivo75Richiesto()).isFalse();
    }

    @Test
    public void itTestDefaultSuperficieRichiesta() {
        SostegnoSuperfici sostegnoSuperfici =  new SostegnoSuperfici();

        fillWithSuperificieTotaleRichiesta(sostegnoSuperfici);

        assertThat(sostegnoSuperfici.getSuperficieTotaleRichiesta()).isEqualTo(0L);
        assertThat(sostegnoSuperfici.getSostegnoFrumentoDuroRichiesto()).isFalse();
        assertThat(sostegnoSuperfici.getSostegnoFrumentoLeguminoseRichiesto()).isFalse();
        assertThat(sostegnoSuperfici.getSostegnoFrumentoProteaginoseRichiesto()).isFalse();
        assertThat(sostegnoSuperfici.getSostegnoOlivoNazionareRichiesto()).isFalse();
        assertThat(sostegnoSuperfici.getSostegnoOlivoQualitaRichiesto()).isFalse();
        assertThat(sostegnoSuperfici.getSostegnoSoiaRichiesto()).isFalse();
        assertThat(sostegnoSuperfici.getSostegnoOlivo75Richiesto()).isFalse();
    }

    private SostegnoSuperfici sostegnoSuperfici() {
        return new SostegnoSuperfici().setSuperficieRichiestaFrumentoProteaginose(10L)
                                      .setSuperficieRichiestaFrumentoLeguminose(20L)
                                      .setSuperficieRichiestaFrumentoDuro(30L)
                                      .setSuperficieRichiestaSoia(30L)
                                      .setSuperficieRichiestaPomodoro(20L)
                                      .setSuperficieRichiestaOlivoQualita(10L)
                                      .setSuperficieRichiestaOlivoNazionale(10L);
    }
}
