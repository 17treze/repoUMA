package it.tndigitale.a4g.ags.service.support;

import it.tndigitale.a4g.ags.dto.domandaunica.DichiarazioniDomandaUnica;
import it.tndigitale.a4g.ags.repository.dao.DomandaDao;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SostegnoSupportTest {

    private DomandaDao domandaDao;
    private SostegnoSupport sostegnoSupport;
    private static final Long NUMERO_DOAMDA = 1L;
    private static final Integer CAMPAGNA = 1999;
    private static final String ID_CAMPO_PER_SOSTEGNO_GIOVANE_RICHIESTO = "DUGA02";

    public SostegnoSupportTest() {
        domandaDao = mock(DomandaDao.class);

        sostegnoSupport = new SostegnoSupport().setComponents(domandaDao);
    }

    @Test
    public void forGiovaneIsRichiestoIfDichiarazioniEmptyThenReturnFALSE() {
        when(domandaDao.getDichiarazioniDomanda(NUMERO_DOAMDA, CAMPAGNA)).thenReturn(Collections.emptyList());

        Boolean isGiovaneRIchiesto = sostegnoSupport.giovaneIsRichiesto(NUMERO_DOAMDA, CAMPAGNA);

        assertThat(isGiovaneRIchiesto).isFalse();
        verify(domandaDao).getDichiarazioniDomanda(NUMERO_DOAMDA, CAMPAGNA);
    }

    @Test
    public void forGiovaneIsRichiestoIfDichiarazioniNotContainIdCampoCorrectThenReturnFALSE() {
        when(domandaDao.getDichiarazioniDomanda(NUMERO_DOAMDA, CAMPAGNA)).thenReturn(moreDichiarazioniDomandaWithoutIdCampoCorrect());

        Boolean isGiovaneRIchiesto = sostegnoSupport.giovaneIsRichiesto(NUMERO_DOAMDA, CAMPAGNA);

        assertThat(isGiovaneRIchiesto).isFalse();
        verify(domandaDao).getDichiarazioniDomanda(NUMERO_DOAMDA, CAMPAGNA);
    }

    @Test
    public void forGiovaneIsRichiestoIfDichiarazioniContainIdCampoCorrectThenReturnTRUE() {
        when(domandaDao.getDichiarazioniDomanda(NUMERO_DOAMDA, CAMPAGNA)).thenReturn(moreDichiarazioniDomandaWithIdCampoCorrect());

        Boolean isGiovaneRIchiesto = sostegnoSupport.giovaneIsRichiesto(NUMERO_DOAMDA, CAMPAGNA);

        assertThat(isGiovaneRIchiesto).isTrue();
        verify(domandaDao).getDichiarazioniDomanda(NUMERO_DOAMDA, CAMPAGNA);
    }

    private List<DichiarazioniDomandaUnica> moreDichiarazioniDomandaWithoutIdCampoCorrect() {
        DichiarazioniDomandaUnica dichiarazioniDomandaUnica = new DichiarazioniDomandaUnica();
        dichiarazioniDomandaUnica.setIdCampo("XXXXX");
        return Arrays.asList(dichiarazioniDomandaUnica);
    }

    private List<DichiarazioniDomandaUnica> moreDichiarazioniDomandaWithIdCampoCorrect() {
        DichiarazioniDomandaUnica dichiarazioniDomandaUnica = new DichiarazioniDomandaUnica();
        dichiarazioniDomandaUnica.setIdCampo(ID_CAMPO_PER_SOSTEGNO_GIOVANE_RICHIESTO);
        return Arrays.asList(dichiarazioniDomandaUnica);
    }
}
