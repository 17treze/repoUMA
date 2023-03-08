package it.tndigitale.a4g.ags.service;

import it.tndigitale.a4g.ags.dto.SostegniSuperficie;
import it.tndigitale.a4g.ags.dto.SostegnoDisaccoppiato;
import it.tndigitale.a4g.ags.dto.SostegnoSuperfici;
import it.tndigitale.a4g.ags.repository.dao.DomandaDao;
import it.tndigitale.a4g.ags.service.support.SostegnoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.tndigitale.a4g.ags.service.support.SostegnoSupport.*;

@Service
public class SostegnoService {

    private DomandaDao domandaDao;
    private SostegnoSupport sostegnoSupport;

    @Autowired
    public SostegnoService setComponents(DomandaDao domandaDao, SostegnoSupport sostegnoSupport) {
        this.domandaDao = domandaDao;
        this.sostegnoSupport = sostegnoSupport;
        return this;
    }

    public SostegnoDisaccoppiato detailDisaccoppiato(Long numeroDomanda, Integer campagna) {
        Boolean giovaneIsRichiesto = sostegnoSupport.giovaneIsRichiesto(numeroDomanda, campagna);
        List<SostegniSuperficie> sostegniSuperficie = domandaDao.getSostegniSuperficie(numeroDomanda);
        return new SostegnoDisaccoppiato().setGiovaneRichiesto(giovaneIsRichiesto)
                                          .setSuperficieImpegnataLorda(calcolaSuperficieImpegnataLorda(sostegniSuperficie))
                                          .setSuperficieImpegnataNetta(calcolaSuperficieImpegnataNetta(sostegniSuperficie));
    }

    public SostegnoSuperfici detailSuperfici(Long numeroDomanda) {
        List<SostegniSuperficie> sostegniSuperficie = domandaDao.getSostegniSuperficie(numeroDomanda);

        SostegnoSuperfici sostegno =  new SostegnoSuperfici().setSuperficieRichiestaFrumentoDuro(calcolaSuperficieRichiestaFrumentoDuro(sostegniSuperficie))
                                                             .setSuperficieRichiestaFrumentoLeguminose(calcolaSuperficieRichiestaFrumentoLeguminose(sostegniSuperficie))
                                                             .setSuperficieRichiestaFrumentoProteaginose(calcolaSuperficieRichiestaFrumentoProteaginose(sostegniSuperficie))
                                                             .setSuperficieRichiestaOlivo75(calcolaSuperficieRichiestaOlivo75(sostegniSuperficie))
                                                             .setSuperficieRichiestaOlivoNazionale(calcolaSuperficieRichiestaOlivoNazionale(sostegniSuperficie))
                                                             .setSuperficieRichiestaOlivoQualita(calcolaSuperficieRichiestaOlivoQualita(sostegniSuperficie))
                                                             .setSuperficieRichiestaPomodoro(calcolaSuperficieRichiestaPomodoro(sostegniSuperficie))
                                                             .setSuperficieRichiestaSoia(calcolaSuperficieRichiestaSoia(sostegniSuperficie));
        SostegnoSuperfici.fillWithSostegni(sostegno);
        SostegnoSuperfici.fillWithSuperificieTotaleRichiesta(sostegno);
        return sostegno;
    }

}
