package it.tndigitale.a4g.ags.service;

import it.tndigitale.a4g.ags.dto.SostegniSuperficie;
import it.tndigitale.a4g.ags.dto.SostegnoDisaccoppiato;
import it.tndigitale.a4g.ags.dto.SostegnoSuperfici;
import it.tndigitale.a4g.ags.repository.dao.DomandaDao;
import it.tndigitale.a4g.ags.service.support.SostegnoSupport;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class SostegnoServiceTest {

    private SostegnoService sostegnoService;
    private DomandaDao domandaDao;
    private SostegnoSupport sostegnoSupport;

    private static final Long NUMERO_DOMANDA = 1L;
    private static final Integer CAMPAGNA = 1999;
    public static final String SOSTEGNO_BPS = "BPS";
    public static final String SOSTEGNO_PACC = "PACC";
    public static final String COD_INTERVENTO_SOIA = "SOIA";
    public static final String COD_INTERVENTO_GDURO = "GDURO";
    public static final String COD_INTERVENTO_CPROT = "CPROT";
    public static final String COD_INTERVENTO_LEGUMIN = "LEGUMIN";
    public static final String COD_INTERVENTO_POMOD = "POMOD";
    public static final String COD_INTERVENTO_OLIO = "OLIO";
    public static final String COD_INTERVENTO_OLIVE_PEND75 = "OLIVE_PEND75";
    public static final String COD_INTERVENTO_OLIVE_QUAL = "OLIVE_DISC";

    public SostegnoServiceTest() {
        domandaDao = mock(DomandaDao.class);
        sostegnoSupport = mock(SostegnoSupport.class);

        sostegnoService = new SostegnoService().setComponents(domandaDao, sostegnoSupport);
    }

    @Test
    public void forDetailDisaccoppiatoitReturnDetailOfSostegnoDisaccoppiato() {
        when(sostegnoSupport.giovaneIsRichiesto(NUMERO_DOMANDA, CAMPAGNA)).thenReturn(Boolean.TRUE);
        when(domandaDao.getSostegniSuperficie(NUMERO_DOMANDA)).thenReturn(moreSostegniSuperficie());

        SostegnoDisaccoppiato sostegnoDisaccoppiato =  sostegnoService.detailDisaccoppiato(NUMERO_DOMANDA, CAMPAGNA);

        assertThat(sostegnoDisaccoppiato).isNotNull();
        assertThat(sostegnoDisaccoppiato.getGiovaneRichiesto()).isTrue();
        assertThat(sostegnoDisaccoppiato.getGreeningRichiesto()).isTrue();
        assertThat(sostegnoDisaccoppiato.getBpsRichiesto()).isTrue();
        assertThat(sostegnoDisaccoppiato.getSuperficieImpegnataLorda()).isEqualTo(30L);
        assertThat(sostegnoDisaccoppiato.getSuperficieImpegnataNetta()).isEqualTo(250F);
        verify(sostegnoSupport).giovaneIsRichiesto(NUMERO_DOMANDA, CAMPAGNA);
        verify(domandaDao).getSostegniSuperficie(NUMERO_DOMANDA);
    }

    @Test
    public void forDetailDisaccoppiatoIfExistSostegnoWithCoeffTaraNullThenThrowing() {
        when(sostegnoSupport.giovaneIsRichiesto(NUMERO_DOMANDA, CAMPAGNA)).thenReturn(Boolean.TRUE);
        when(domandaDao.getSostegniSuperficie(NUMERO_DOMANDA)).thenReturn(moreSostegniSuperficieWithCoeffTaraNull());

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> sostegnoService.detailDisaccoppiato(NUMERO_DOMANDA, CAMPAGNA));
        verify(sostegnoSupport).giovaneIsRichiesto(NUMERO_DOMANDA, CAMPAGNA);
        verify(domandaDao).getSostegniSuperficie(NUMERO_DOMANDA);
    }

    @Test
    public void forDetailDisaccoppiatoIfExistSostegnoWithSupImpegnataNullThenConsiderZeroAndReturnDetailOfSostegnoDisaccoppiato() {
        when(sostegnoSupport.giovaneIsRichiesto(NUMERO_DOMANDA, CAMPAGNA)).thenReturn(Boolean.TRUE);
        when(domandaDao.getSostegniSuperficie(NUMERO_DOMANDA)).thenReturn(moreSostegniSuperficieWithSupImpegnataNull());

        SostegnoDisaccoppiato sostegnoDisaccoppiato =  sostegnoService.detailDisaccoppiato(NUMERO_DOMANDA, CAMPAGNA);

        assertThat(sostegnoDisaccoppiato).isNotNull();
        assertThat(sostegnoDisaccoppiato.getGiovaneRichiesto()).isTrue();
        assertThat(sostegnoDisaccoppiato.getGreeningRichiesto()).isTrue();
        assertThat(sostegnoDisaccoppiato.getBpsRichiesto()).isTrue();
        assertThat(sostegnoDisaccoppiato.getSuperficieImpegnataLorda()).isEqualTo(20L);
        assertThat(sostegnoDisaccoppiato.getSuperficieImpegnataNetta()).isEqualTo(200F);
        verify(sostegnoSupport).giovaneIsRichiesto(NUMERO_DOMANDA, CAMPAGNA);
        verify(domandaDao).getSostegniSuperficie(NUMERO_DOMANDA);
    }

    @Test
    public void forDetailSuperficiIfExistSostegniThenReturnDetailOfSostegnoSuperfici() {
        when(domandaDao.getSostegniSuperficie(NUMERO_DOMANDA)).thenReturn(moreSostegniSuperficiePerDetailSostegnoSuperficie());

        SostegnoSuperfici sostegnoSuperfici = sostegnoService.detailSuperfici(NUMERO_DOMANDA);

        assertThat(sostegnoSuperfici).isNotNull();
        assertThat(sostegnoSuperfici.getSostegnoOlivo75Richiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoOlivoQualitaRichiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoOlivoNazionareRichiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoSoiaRichiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoPomorodoRichiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoFrumentoProteaginoseRichiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoFrumentoLeguminoseRichiesto()).isTrue();
        assertThat(sostegnoSuperfici.getSostegnoFrumentoDuroRichiesto()).isTrue();

        assertThat(sostegnoSuperfici.getSuperficieRichiestaSoia()).isEqualTo(30L);
        assertThat(sostegnoSuperfici.getSuperficieRichiestaPomodoro()).isEqualTo(60L);
        assertThat(sostegnoSuperfici.getSuperficieRichiestaFrumentoDuro()).isEqualTo(30L);
        assertThat(sostegnoSuperfici.getSuperficieRichiestaFrumentoLeguminose()).isEqualTo(50L);
        assertThat(sostegnoSuperfici.getSuperficieRichiestaFrumentoProteaginose()).isEqualTo(30L);
        assertThat(sostegnoSuperfici.getSuperficieRichiestaOlivo75()).isEqualTo(40L);
        assertThat(sostegnoSuperfici.getSuperficieRichiestaOlivoNazionale()).isEqualTo(40L);
        assertThat(sostegnoSuperfici.getSuperficieRichiestaOlivoQualita()).isEqualTo(60L);

        assertThat(sostegnoSuperfici.getSuperficieTotaleRichiesta()).isEqualTo(340L);
    }



    private List<SostegniSuperficie> moreSostegniSuperficie() {
        SostegniSuperficie sostegniSuperficie1 = new SostegniSuperficie();
        sostegniSuperficie1.setSostegno(SOSTEGNO_BPS);
        sostegniSuperficie1.setCodIntervento(SOSTEGNO_BPS);
        sostegniSuperficie1.setSupImpegnata(10L);
        sostegniSuperficie1.setCoeffTara(5F);

        SostegniSuperficie sostegniSuperficie2 = new SostegniSuperficie();
        sostegniSuperficie2.setSostegno(SOSTEGNO_BPS);
        sostegniSuperficie2.setCodIntervento(SOSTEGNO_BPS);
        sostegniSuperficie2.setSupImpegnata(20L);
        sostegniSuperficie2.setCoeffTara(10F);

        return Arrays.asList(sostegniSuperficie1, sostegniSuperficie2);
    }

    private List<SostegniSuperficie> moreSostegniSuperficieWithCoeffTaraNull() {
        SostegniSuperficie sostegniSuperficie = new SostegniSuperficie();
        sostegniSuperficie.setSostegno(SOSTEGNO_BPS);
        sostegniSuperficie.setCodIntervento(SOSTEGNO_BPS);
        sostegniSuperficie.setSupImpegnata(10L);
        sostegniSuperficie.setCoeffTara(null);
        return Arrays.asList(sostegniSuperficie);
    }

    private List<SostegniSuperficie> moreSostegniSuperficieWithSupImpegnataNull() {
        SostegniSuperficie sostegniSuperficie1 = new SostegniSuperficie();
        sostegniSuperficie1.setSostegno(SOSTEGNO_BPS);
        sostegniSuperficie1.setCodIntervento(SOSTEGNO_BPS);
        sostegniSuperficie1.setSupImpegnata(null);
        sostegniSuperficie1.setCoeffTara(1.2F);

        SostegniSuperficie sostegniSuperficie2 = new SostegniSuperficie();
        sostegniSuperficie2.setSostegno(SOSTEGNO_BPS);
        sostegniSuperficie2.setCodIntervento(SOSTEGNO_BPS);
        sostegniSuperficie2.setSupImpegnata(20L);
        sostegniSuperficie2.setCoeffTara(10F);

        return Arrays.asList(sostegniSuperficie1, sostegniSuperficie2);
    }

    private List<SostegniSuperficie> moreSostegniSuperficiePerDetailSostegnoSuperficie() {
        SostegniSuperficie sostegno1 = aSostegnoPacc(COD_INTERVENTO_SOIA, 10L);
        SostegniSuperficie sostegno2 = aSostegnoPacc(COD_INTERVENTO_SOIA, 20L);

        SostegniSuperficie sostegno3 = aSostegnoPacc(COD_INTERVENTO_GDURO, 10L);
        SostegniSuperficie sostegno4 = aSostegnoPacc(COD_INTERVENTO_GDURO, 20L);

        SostegniSuperficie sostegno5 = aSostegnoPacc(COD_INTERVENTO_CPROT, 30L);

        SostegniSuperficie sostegno6 = aSostegnoPacc(COD_INTERVENTO_POMOD, 10L);
        SostegniSuperficie sostegno7 = aSostegnoPacc(COD_INTERVENTO_POMOD, 20L);
        SostegniSuperficie sostegno8 = aSostegnoPacc(COD_INTERVENTO_POMOD, 30L);

        SostegniSuperficie sostegno9 = aSostegnoPacc(COD_INTERVENTO_OLIO, 10L);
        SostegniSuperficie sostegno10 = aSostegnoPacc(COD_INTERVENTO_OLIO, 30L);

        SostegniSuperficie sostegno11 = aSostegnoPacc(COD_INTERVENTO_OLIVE_PEND75, 20L);
        SostegniSuperficie sostegno12 = aSostegnoPacc(COD_INTERVENTO_OLIVE_PEND75, 20L);

        SostegniSuperficie sostegno13 = aSostegnoPacc(COD_INTERVENTO_OLIVE_QUAL, 10L);
        SostegniSuperficie sostegno14 = aSostegnoPacc(COD_INTERVENTO_OLIVE_QUAL, 20L);
        SostegniSuperficie sostegno15 = aSostegnoPacc(COD_INTERVENTO_OLIVE_QUAL, 30L);

        SostegniSuperficie sostegno16 = aSostegnoPacc(COD_INTERVENTO_LEGUMIN, 30L);
        SostegniSuperficie sostegno17 = aSostegnoPacc(COD_INTERVENTO_LEGUMIN, 20L);

        SostegniSuperficie sostegno18 = aSostegnoPacc(COD_INTERVENTO_LEGUMIN + "X", 20L);
        SostegniSuperficie sostegno19 = aSostegnoPacc(COD_INTERVENTO_OLIVE_QUAL + "X", 20L);
        SostegniSuperficie sostegno20 = aSostegnoPacc(COD_INTERVENTO_OLIVE_PEND75 + "X", 20L);
        SostegniSuperficie sostegno21 = aSostegnoPacc(COD_INTERVENTO_OLIO + "X", 20L);
        SostegniSuperficie sostegno22 = aSostegnoPacc(COD_INTERVENTO_POMOD + "X", 20L);
        SostegniSuperficie sostegno23 = aSostegnoPacc(COD_INTERVENTO_CPROT + "X", 20L);
        SostegniSuperficie sostegno24 = aSostegnoPacc(COD_INTERVENTO_GDURO + "X", 20L);
        SostegniSuperficie sostegno25 = aSostegnoPacc(COD_INTERVENTO_SOIA + "X", 20L);


        return Arrays.asList(sostegno1, sostegno2,
                             sostegno3, sostegno4,
                             sostegno5,
                             sostegno6, sostegno7, sostegno8,
                             sostegno9, sostegno10,
                             sostegno11, sostegno12,
                             sostegno13, sostegno14, sostegno15,
                             sostegno16, sostegno17,
                             sostegno18, sostegno19, sostegno20, sostegno21, sostegno22, sostegno23, sostegno24, sostegno25);
    }

    private SostegniSuperficie aSostegnoPacc(String codiceIntervento, Long supImpegnata) {
        SostegniSuperficie sostegniSuperficie = new SostegniSuperficie();
        sostegniSuperficie.setSostegno(SOSTEGNO_PACC);
        sostegniSuperficie.setCodIntervento(codiceIntervento);
        sostegniSuperficie.setSupImpegnata(supImpegnata);
        return sostegniSuperficie;
    }

}
