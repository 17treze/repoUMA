package it.tndigitale.a4gutente.service.support;

import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.APPROVATA;
import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.IN_LAVORAZIONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.Test;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gutente.repository.dao.IDomandaRegistrazioneUtenteDao;
import it.tndigitale.a4gutente.repository.dao.IEnteDao;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;
import it.tndigitale.a4gutente.service.loader.DomandaLoader;

public class DomandaRegistrazioneUtenteSupportTest {

    private DomandaRegistrazioneUtenteSupport support;
    private DomandaLoader domandaLoader;
    private IDomandaRegistrazioneUtenteDao domandaRegistrazioneUtenteDao;
    private IEnteDao enteDao;
    private Clock clock;

    private static final Long ID_DOMANDA = 1L;
    private static final LocalDateTime NOW = LocalDateTime.of(2019,10,1,10,0);

    public DomandaRegistrazioneUtenteSupportTest() {
        domandaLoader = mock(DomandaLoader.class);
        domandaRegistrazioneUtenteDao = mock(IDomandaRegistrazioneUtenteDao.class);
        enteDao = mock(IEnteDao.class);
        clock = mock(Clock.class);

        support = new DomandaRegistrazioneUtenteSupport()
                .setComponents(domandaLoader,
                               domandaRegistrazioneUtenteDao,
                               enteDao,
                               clock);
    }

    @Test
    public void itChangeStateOfDomanda() throws Exception {
        when(domandaLoader.load(ID_DOMANDA)).thenReturn(aDomandaRegistrazioneUtente());
        when(domandaRegistrazioneUtenteDao.save(any())).thenReturn(aDomandaRegistrazioneUtente());

        DomandaRegistrazioneUtente domandaSalvata = support.cambiaStato(ID_DOMANDA, APPROVATA);

        assertThat(domandaSalvata).isEqualTo(aDomandaRegistrazioneUtente());
        verify(domandaLoader).load(ID_DOMANDA);
        verify(domandaRegistrazioneUtenteDao).save(any());
    }

    private DomandaRegistrazioneUtente aDomandaRegistrazioneUtente() {
        DomandaRegistrazioneUtente domanda = new DomandaRegistrazioneUtente().setStato(IN_LAVORAZIONE)
                                                                             .setIstruttoriaEntita(new IstruttoriaEntita());
        domanda.setId(ID_DOMANDA);
        return domanda;
    }

}
