package it.tndigitale.a4g.ags.service.support;

import it.tndigitale.a4g.ags.dto.SostegniSuperficie;
import it.tndigitale.a4g.ags.dto.domandaunica.DichiarazioniDomandaUnica;
import it.tndigitale.a4g.ags.repository.dao.DomandaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Component
public class SostegnoSupport {

    public static final String ID_CAMPO_PER_SOSTEGNO_GIOVANE_RICHIESTO = "DUGA02";
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

    private DomandaDao domandaDao;

    @Autowired
    public SostegnoSupport setComponents(DomandaDao domandaDao) {
        this.domandaDao = domandaDao;
        return this;
    }

    public Boolean giovaneIsRichiesto(Long idDomanda, Integer campagna) {
        List<DichiarazioniDomandaUnica> dichiarazioniDomandaUnica = domandaDao.getDichiarazioniDomanda(idDomanda, campagna);
        return Optional.ofNullable(dichiarazioniDomandaUnica)
                       .orElse(emptyList())
                       .stream()
                       .filter(dichiarazione -> dichiarazione.getIdCampo().equals(ID_CAMPO_PER_SOSTEGNO_GIOVANE_RICHIESTO))
                       .findFirst()
                       .isPresent();
    }

    public static Long calcolaSuperficieImpegnataLorda(List<SostegniSuperficie> sostegniSuperficie) {
        List<?> elements = filtraElementiBPSRichiesto(sostegniSuperficie).stream().collect(Collectors.toList());

        return filtraElementiBPSRichiesto(sostegniSuperficie).stream()
                                                             .map(sostegno -> sostegno.getSupImpegnata())
                                                             .reduce(0L, (a,b) -> getSupImpegnataOrDefault(a) + getSupImpegnataOrDefault(b));
    }

    public static Float calcolaSuperficieImpegnataNetta(List<SostegniSuperficie> sostegniSuperficie) {
        return filtraElementiBPSRichiesto(sostegniSuperficie).stream()
                                                             .map(sostegno -> prodotto(sostegno.getSupImpegnata(), sostegno.getCoeffTara()))
                                                             .reduce(0F, (a,b) -> a + b);
    }

    public static Long calcolaSuperficieRichiestaSoia(List<SostegniSuperficie> sostegniSuperficie) {
        return sommaSuperficieImpegnataElementiPACC_ConCodIntervento(sostegniSuperficie, COD_INTERVENTO_SOIA);
    }

    public static Long calcolaSuperficieRichiestaFrumentoDuro(List<SostegniSuperficie> sostegniSuperficie) {
        return sommaSuperficieImpegnataElementiPACC_ConCodIntervento(sostegniSuperficie, COD_INTERVENTO_GDURO);
    }

    public static Long calcolaSuperficieRichiestaFrumentoProteaginose(List<SostegniSuperficie> sostegniSuperficie) {
        return sommaSuperficieImpegnataElementiPACC_ConCodIntervento(sostegniSuperficie, COD_INTERVENTO_CPROT);
    }

    public static Long calcolaSuperficieRichiestaFrumentoLeguminose(List<SostegniSuperficie> sostegniSuperficie) {
        return sommaSuperficieImpegnataElementiPACC_ConCodIntervento(sostegniSuperficie, COD_INTERVENTO_LEGUMIN);
    }

    public static Long calcolaSuperficieRichiestaPomodoro(List<SostegniSuperficie> sostegniSuperficie) {
        return sommaSuperficieImpegnataElementiPACC_ConCodIntervento(sostegniSuperficie, COD_INTERVENTO_POMOD);
    }

    public static Long calcolaSuperficieRichiestaOlivoNazionale(List<SostegniSuperficie> sostegniSuperficie) {
        return sommaSuperficieImpegnataElementiPACC_ConCodIntervento(sostegniSuperficie, COD_INTERVENTO_OLIO);
    }

    public static Long calcolaSuperficieRichiestaOlivo75(List<SostegniSuperficie> sostegniSuperficie) {
        return sommaSuperficieImpegnataElementiPACC_ConCodIntervento(sostegniSuperficie, COD_INTERVENTO_OLIVE_PEND75);
    }

    public static Long calcolaSuperficieRichiestaOlivoQualita(List<SostegniSuperficie> sostegniSuperficie) {
        return sommaSuperficieImpegnataElementiPACC_ConCodIntervento(sostegniSuperficie, COD_INTERVENTO_OLIVE_QUAL);
    }



    private static List<SostegniSuperficie> filtraElementiBPSRichiesto(List<SostegniSuperficie> sostegni) {
        return Optional.ofNullable(sostegni)
                       .orElse(Collections.emptyList())
                       .stream()
                       .filter(sostegno -> isSostegnoBPS(sostegno))
                       .collect(Collectors.toList());
    }

    private static Long sommaSuperficieImpegnataElementiPACC_ConCodIntervento(List<SostegniSuperficie> sostegni, String codIntervento) {
        return Optional.ofNullable(sostegni)
                       .orElse(Collections.emptyList())
                       .stream()
                       .filter(sostegno -> isPACC_ConCodIntervento(sostegno, codIntervento))
                       .map(sostegno -> sostegno.getSupImpegnata())
                       .reduce(0L, (a,b) -> getSupImpegnataOrDefault(a) + getSupImpegnataOrDefault(b));

    }

    private static Boolean isPACC_ConCodIntervento(SostegniSuperficie sostegno, String codIntervento) {
        return SOSTEGNO_PACC.equals(sostegno.getSostegno()) && codIntervento.equals(sostegno.getCodIntervento());
    }

    private static Boolean isSostegnoBPS(SostegniSuperficie sostegno) {
        return SOSTEGNO_BPS.equals(sostegno.getSostegno()) &&
               SOSTEGNO_BPS.equals(sostegno.getCodIntervento());
    }

    private static Float prodotto(Long supImpegnata, Float coeffTara) {
        if (coeffTara == null) {
            throw new RuntimeException("Prodotto non eseguito per coefficiente Tara Nullo");
        }
        return coeffTara * getSupImpegnataOrDefault(supImpegnata);
    }

    private static Long getSupImpegnataOrDefault(Long supImpegnata) {
        return (supImpegnata != null)? supImpegnata:0L;
    }

}
