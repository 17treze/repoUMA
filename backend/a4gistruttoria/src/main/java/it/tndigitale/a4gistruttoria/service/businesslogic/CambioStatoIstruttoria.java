package it.tndigitale.a4gistruttoria.service.businesslogic;

import static it.tndigitale.a4gistruttoria.service.businesslogic.validator.CambioDiStatoIstruttoriaValidatorFactory.createValidatorFrom;

import org.springframework.beans.factory.annotation.Autowired;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.service.businesslogic.iotialia.IoItaliaSenderService;
import it.tndigitale.a4gistruttoria.service.loader.StatoLavSostegnoLoader;

public abstract class CambioStatoIstruttoria {

    @Autowired
    private IstruttoriaComponent istruttoriaComponent;
    @Autowired
    private StatoLavSostegnoLoader statoLavSostegnoLoader;
    @Autowired
    private IstruttoriaDao istruttoriaDao;
    @Autowired
    private TransizioneIstruttoriaDao transizioneIstruttoriaDao;
    @Autowired
    private Clock clock;
    @Autowired
    private IoItaliaSenderService ioItaliaSenderService;

    protected void cambiaStato(Long idIstruttoria, StatoIstruttoria stato) throws ElaborazioneIstruttoriaException {
        IstruttoriaModel istruttoriaModel = istruttoriaComponent.load(idIstruttoria);
        TransizioneIstruttoriaModel transizione = avviaTransizione(istruttoriaModel);
        cambiaStato(transizione, stato);
    }
    
    protected void cambiaStato(TransizioneIstruttoriaModel transizione, StatoIstruttoria stato) throws ElaborazioneIstruttoriaException {
        IstruttoriaModel istruttoriaModel = transizione.getIstruttoria();
        createValidatorFrom(stato).valida(istruttoriaModel);
        A4gdStatoLavSostegno nuovoStato = loadNuovoStato(stato);
        aggiornaTransizione(transizione, nuovoStato);
        istruttoriaModel.setA4gdStatoLavSostegno(nuovoStato);
        istruttoriaModel.getTransizioni().add(transizione);
        IstruttoriaModel istruttoriaSalvata = istruttoriaDao.save(istruttoriaModel);
        //IOIT-DU-01-xx - invio messaggio IO Italia
        ioItaliaSenderService.recuperaFactoryEinviaMessaggio(istruttoriaSalvata);
    }

    protected TransizioneIstruttoriaModel avviaTransizione(IstruttoriaModel istruttoria) {
        // inizializzazione transizione
        TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
        transizione.setIstruttoria(istruttoria);
        //
        A4gdStatoLavSostegno a4gtStatoLavSostegno = istruttoria.getA4gdStatoLavSostegno();
        transizione.setA4gdStatoLavSostegno2(a4gtStatoLavSostegno);
        transizione.setDataEsecuzione(clock.nowDate());
        transizione = transizioneIstruttoriaDao.save(transizione);
        return transizione;
    }
    
    protected void aggiornaTransizione(TransizioneIstruttoriaModel transizione, A4gdStatoLavSostegno nuovoStato) {
        // aggiorna transizione
        transizione.setA4gdStatoLavSostegno1(nuovoStato);
        transizioneIstruttoriaDao.save(transizione);
    }

    protected A4gdStatoLavSostegno loadNuovoStato(StatoIstruttoria stato) {
        return statoLavSostegnoLoader.loadByIdentificativo(stato.getStatoIstruttoria());
    }
    
    protected IstruttoriaDao getIstruttoriaDao() {
    	return istruttoriaDao;
    }

}
