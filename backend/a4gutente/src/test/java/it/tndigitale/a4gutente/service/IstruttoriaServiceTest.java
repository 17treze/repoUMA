package it.tndigitale.a4gutente.service;

import static it.tndigitale.a4gutente.dto.Responsabilita.DIPENDENTE_PAT;
import static it.tndigitale.a4gutente.dto.Responsabilita.TITOLARE_AZIENDA_AGRICOLA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gutente.dto.Istruttoria;
import it.tndigitale.a4gutente.dto.IstruttoriaPerStorico;
import it.tndigitale.a4gutente.dto.IstruttoriaSenzaDomanda;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.TitolareImpresa;
import it.tndigitale.a4gutente.dto.RichiestaDomandaApprovazione;
import it.tndigitale.a4gutente.dto.RichiestaDomandaRifiuta;
import it.tndigitale.a4gutente.dto.StoricoIstruttorie;
import it.tndigitale.a4gutente.dto.Utente;
import it.tndigitale.a4gutente.repository.dao.IDomandaRegistrazioneUtenteDao;
import it.tndigitale.a4gutente.repository.dao.IIstruttoriaDao;
import it.tndigitale.a4gutente.repository.model.A4gtEnte;
import it.tndigitale.a4gutente.repository.model.A4gtProfilo;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;
import it.tndigitale.a4gutente.service.loader.EnteLoader;
import it.tndigitale.a4gutente.service.loader.IstruttoriaLoader;
import it.tndigitale.a4gutente.service.loader.ProfiloLoader;
import it.tndigitale.a4gutente.service.loader.UtenteLoader;
import it.tndigitale.a4gutente.service.support.IstruttoriaSupport;
import it.tndigitale.a4gutente.service.support.UtenteSupport;

public class IstruttoriaServiceTest {

    private IIstruttoriaDao istruttoriaDao;
    private IstruttoriaLoader istruttoriaLoader;
    private IstruttoriaService istruttoriaService;
    private IDomandaRegistrazioneUtenteDao domandaRegistrazioneUtenteDao;
    private IUtenteService utenteService;
    private IstruttoriaSupport istruttoriaSupport;
    private UtenteLoader utenteLoader;
    private ProfiloLoader profiloLoader;
    private EnteLoader enteLoader;
    private UtenteSupport utenteSupport;
    private Clock clock;

    private static final LocalDateTime NOW = LocalDateTime.of(2019,1,1,10,1);
    private static final Long ID_DOMANDA = 1L;
    private static final Long ID_ISTRUTTORIA = 10L;
    private static final Long ID_UTENTE = 100L;
    private static final String NOTE = "NOTE";
    private static final String MOTIVAZIONE_RIFIUTO = "MOTIVAZIONE_RIFIUTO";
    private static final List<Long> IDS_PROFILI = Arrays.asList(377L);
    private static final List<Long> IDS_SEDI = Arrays.asList(390L);
    private static final List<A4gtEnte> ENTI = Arrays.asList(new A4gtEnte());
    private static final List<A4gtProfilo> PROFILI_SENZA_UTENTE_AZIENDA_AGRICOLA =
            Arrays.asList(new A4gtProfilo().setResponsabilita(DIPENDENTE_PAT));
    private static final List<A4gtProfilo> PROFILI_CON_UTENTE_AZIENDA_AGRICOLA =
            Arrays.asList(new A4gtProfilo().setResponsabilita(TITOLARE_AZIENDA_AGRICOLA));

    public IstruttoriaServiceTest() {
        istruttoriaDao = mock(IIstruttoriaDao.class);
        istruttoriaLoader = mock(IstruttoriaLoader.class);
        domandaRegistrazioneUtenteDao = mock(IDomandaRegistrazioneUtenteDao.class);
        utenteService = mock(IUtenteService.class);
        istruttoriaSupport = mock(IstruttoriaSupport.class);
        utenteLoader = mock(UtenteLoader.class);
        profiloLoader = mock(ProfiloLoader.class);
        enteLoader = mock(EnteLoader.class);
        utenteSupport = mock(UtenteSupport.class);
        clock = mock(Clock.class);

        istruttoriaService = new IstruttoriaService()
                .setComponents(istruttoriaDao,
                               istruttoriaLoader,
                               domandaRegistrazioneUtenteDao,
                               utenteService,
                               istruttoriaSupport,
                               utenteLoader,
                               enteLoader,
                               profiloLoader,
                               utenteSupport,
                               clock);
    }

    @Test
    public void IfIstruttoriaByIdDomandaExistThenReturnIstruttoria() throws Exception {
        when(istruttoriaLoader.loadByIdDomanda(ID_DOMANDA)).thenReturn(aIstruttoria());

        Istruttoria istruttoria = istruttoriaService.findByIdDomanda(ID_DOMANDA);

        assertThat(istruttoria).isNotNull();
        verify(istruttoriaLoader).loadByIdDomanda(ID_DOMANDA);
    }

    @Test
    public void itCreateIstruttoria() {
        when(istruttoriaLoader.loadForCreate(any())).thenReturn(new IstruttoriaEntita());
        when(istruttoriaDao.save(new IstruttoriaEntita())).thenReturn(aIstruttoria());

        Long idIstruttoria = istruttoriaService.crea(new Istruttoria());

        assertThat(idIstruttoria).isEqualTo(ID_ISTRUTTORIA);
        verify(istruttoriaLoader).loadForCreate(any());
        verify(istruttoriaDao).save(new IstruttoriaEntita());
        verify(domandaRegistrazioneUtenteDao).save(aDomandaOfIstruttoria());
    }

    @Test
    public void itUpdateIstruttoria() {
        when(istruttoriaLoader.loadForUpdate(any())).thenReturn(new IstruttoriaEntita());
        when(istruttoriaDao.save(new IstruttoriaEntita())).thenReturn(aIstruttoria());

        Long idIstruttoria = istruttoriaService.aggiorna(new Istruttoria());

        assertThat(idIstruttoria).isEqualTo(ID_ISTRUTTORIA);
        verify(istruttoriaLoader).loadForUpdate(any());
        verify(istruttoriaDao).save(new IstruttoriaEntita());
        verify(domandaRegistrazioneUtenteDao, never()).save(any());
    }

    @Test
    public void itAggiornaNotePerApprovazioneDomanda() {
        when(istruttoriaLoader.loadByIdDomanda(ID_DOMANDA)).thenReturn(new IstruttoriaEntita());
        when(istruttoriaDao.save(getIstruttoriaConNote())).thenReturn(getIstruttoriaConNote());

        IstruttoriaEntita istruttoriaEntita = istruttoriaService.aggiornaIstruttoriaPerApprovazioneDomanda(
                new RichiestaDomandaApprovazione().setIdDomanda(ID_DOMANDA)
                                                  .setNote(NOTE));

        assertThat(istruttoriaEntita).isEqualTo(getIstruttoriaConNote());
        verify(istruttoriaLoader).loadByIdDomanda(ID_DOMANDA);
        verify(istruttoriaDao).save(getIstruttoriaConNote());
    }

    @Test
    public void creaOAggiornaUtenzaPerApprovazioneDomandaConProfiloNonPresente() throws Exception {
        when(utenteService.createOrGetUtente(aDomandaRegistrazioneUtenteXApprovazione())).thenReturn(new A4gtUtente());

        A4gtUtente utente = istruttoriaService
                .creaOAggiornaUtenzaPerApprovazioneDomanda(
                        aIstruttoriaEntitaPerAggiornamentoUtenzaSenzaProfiloAziendaAgricola());

        assertThat(utente).isNotNull();
        verify(utenteService).createOrGetUtente(aDomandaRegistrazioneUtenteXApprovazione());
        verify(utenteService).addEnti(any(), any());
        verify(utenteService).addProfili(any(), any());
        verify(istruttoriaSupport, never()).extraiTitolatiImpresaDaDomandaIstruttoria(ID_DOMANDA);
        verify(utenteService, never()).addAziende(any(), any());
    }

    @Test
    public void creaOAggiornaUtenzaPerApprovazioneDomandaConProfiloPresente() throws Exception {
        when(utenteService.createOrGetUtente(aDomandaRegistrazioneUtenteXApprovazione())).thenReturn(new A4gtUtente());
        when(istruttoriaSupport.extraiTitolatiImpresaDaDomandaIstruttoria(ID_DOMANDA)).thenReturn(Collections.singletonList(new TitolareImpresa()));

        A4gtUtente utente = istruttoriaService
                .creaOAggiornaUtenzaPerApprovazioneDomanda(
                        aIstruttoriaEntitaPerAggiornamentoUtenzaConProfiloAziendaAgricola());

        assertThat(utente).isNotNull();
        verify(utenteService).createOrGetUtente(aDomandaRegistrazioneUtenteXApprovazione());
        verify(utenteService).addEnti(any(), any());
        verify(utenteService).addProfili(any(), any());
        verify(istruttoriaSupport).extraiTitolatiImpresaDaDomandaIstruttoria(ID_DOMANDA);
        verify(utenteService).addAziende(any(), any());
    }



    @Test
    public void aggiornaIstruttoriaPerRifiutoDomanda() {
        when(clock.now()).thenReturn(NOW);
        when(utenteLoader.loadUtenteConnesso()).thenReturn(new A4gtUtente());
        when(istruttoriaLoader.loadByIdOrCreate(ID_DOMANDA)).thenReturn(new IstruttoriaEntita());
        when(istruttoriaDao.save(any())).thenReturn(istruttoriaEntitaConMotivazioneRifiuto());

        IstruttoriaEntita istruttoriaEntita = istruttoriaService.aggiornaIstruttoriaPerRifiutoDomanda(richiestaDomandaRifiuta());

        assertThat(istruttoriaEntita).isEqualTo(istruttoriaEntitaConMotivazioneRifiuto());
        verify(istruttoriaLoader).loadByIdOrCreate(ID_DOMANDA);
        verify(istruttoriaDao).save(any());
    }

    @Test
    public void creaIstruttoriaSenzaDomandaPerModificaAbilitazioniUtenteConCancellareAziende() {
        when(utenteLoader.load(ID_UTENTE)).thenReturn(utenteDaModificare());
        when(istruttoriaLoader.loadForCreateSenzaDomanda(any(), any())).thenReturn(istruttoriaSalvataSenzaDomanda());
        when(istruttoriaDao.save(istruttoriaSalvataSenzaDomanda())).thenReturn(istruttoriaSalvataSenzaDomanda());
        when(enteLoader.load(IDS_SEDI)).thenReturn(ENTI);
        when(profiloLoader.load(IDS_PROFILI)).thenReturn(PROFILI_SENZA_UTENTE_AZIENDA_AGRICOLA);


        Long idIstruttoria = istruttoriaService.creaSenzaDomanda(istruttoriaSenzaDomanda());

        assertThat(idIstruttoria).isEqualTo(ID_ISTRUTTORIA);
        verify(utenteLoader).load(ID_UTENTE);
        verify(istruttoriaLoader).loadForCreateSenzaDomanda(any(), any());
        verify(istruttoriaDao).save(istruttoriaSalvataSenzaDomanda());
        verify(enteLoader).load(IDS_SEDI);
        verify(utenteService).addEnti(any(), anyList());
        verify(profiloLoader).load(IDS_PROFILI);
        verify(utenteService).addProfili(any(), anyList());
        verify(utenteService).removeAziende(any());
    }


    @Test
    public void creaIstruttoriaSenzaDomandaPerModificaAbilitazioniUtenteSenzaCancellareAziende() {
        when(utenteLoader.load(ID_UTENTE)).thenReturn(utenteDaModificare());
        when(istruttoriaLoader.loadForCreateSenzaDomanda(any(), any())).thenReturn(istruttoriaSalvataSenzaDomanda());
        when(istruttoriaDao.save(istruttoriaSalvataSenzaDomanda())).thenReturn(istruttoriaSalvataSenzaDomanda());
        when(enteLoader.load(IDS_SEDI)).thenReturn(ENTI);
        when(profiloLoader.load(IDS_PROFILI)).thenReturn(PROFILI_CON_UTENTE_AZIENDA_AGRICOLA);


        Long idIstruttoria = istruttoriaService.creaSenzaDomanda(istruttoriaSenzaDomanda());

        assertThat(idIstruttoria).isEqualTo(ID_ISTRUTTORIA);
        verify(utenteLoader).load(ID_UTENTE);
        verify(istruttoriaLoader).loadForCreateSenzaDomanda(any(), any());
        verify(istruttoriaDao).save(istruttoriaSalvataSenzaDomanda());
        verify(enteLoader).load(IDS_SEDI);
        verify(utenteService).addEnti(any(), anyList());
        verify(profiloLoader).load(IDS_PROFILI);
        verify(utenteService).addProfili(any(), anyList());
        verify(utenteService, never()).removeAziende(any());
    }

    @Test
    public void forAggiungiUtenteAIstruttoriaItAddUserToIstruttoriaAndSave() {
        when(istruttoriaDao.save(any())).thenReturn(new IstruttoriaEntita());

        IstruttoriaEntita istruttoriaEntita =
                istruttoriaService.aggiungiUtenteAIstruttoria(new A4gtUtente(), new IstruttoriaEntita());

        assertThat(istruttoriaEntita).isEqualTo(new IstruttoriaEntita());
        verify(istruttoriaDao).save(any());
    }

    @Test
    public void forListaStoricoItReturnStoricoUtente() {
        when(utenteSupport.caricaUtenteConAnagrafica(ID_UTENTE)).thenReturn(utente());
        when(istruttoriaSupport.caricaIstruttorieXStorico(ID_UTENTE)).thenReturn(istruttoriePerStorico());

        StoricoIstruttorie storico = istruttoriaService.listaStorico(ID_UTENTE);

        assertThat(storico).isNotNull();
        assertThat(storico.getUtente()).isEqualTo(utente());
        assertThat(storico.getIstruttorie()).isEqualTo(istruttoriePerStorico());
        verify(utenteSupport).caricaUtenteConAnagrafica(ID_UTENTE);
        verify(istruttoriaSupport).caricaIstruttorieXStorico(ID_UTENTE);
    }



    private Utente utente() {
        return new Utente().setId(ID_UTENTE);
    }

    private List<IstruttoriaPerStorico> istruttoriePerStorico() {
        return Arrays.asList(new IstruttoriaPerStorico());
    }

    private IstruttoriaEntita aIstruttoriaEntitaPerAggiornamentoUtenzaSenzaProfiloAziendaAgricola() {
        return new IstruttoriaEntita().setDomanda(aDomandaRegistrazioneUtenteXApprovazione())
                                      .setProfili(Collections.singletonList(new A4gtProfilo().setResponsabilita(DIPENDENTE_PAT)));
    }

    private IstruttoriaEntita aIstruttoriaEntitaPerAggiornamentoUtenzaConProfiloAziendaAgricola() {
        return new IstruttoriaEntita().setDomanda(aDomandaRegistrazioneUtenteXApprovazione())
                                      .setProfili(Collections.singletonList(new A4gtProfilo().setResponsabilita(TITOLARE_AZIENDA_AGRICOLA)));
    }

    private DomandaRegistrazioneUtente aDomandaRegistrazioneUtenteXApprovazione() {
        DomandaRegistrazioneUtente domanda = new DomandaRegistrazioneUtente();
        domanda.setId(ID_DOMANDA);
        return domanda;
    }

    private IstruttoriaEntita getIstruttoriaConNote() {
        return new IstruttoriaEntita().setNote(NOTE);
    }


    private DomandaRegistrazioneUtente aDomandaOfIstruttoria() {
        return aIstruttoria().getDomanda().setConfigurato(Boolean.TRUE);
    }

    private IstruttoriaEntita aIstruttoria() {
        IstruttoriaEntita entita = new IstruttoriaEntita();
        entita.setDomanda(new DomandaRegistrazioneUtente());
        entita.setId(ID_ISTRUTTORIA);
        return entita;
    }

    private RichiestaDomandaRifiuta richiestaDomandaRifiuta() {
        return new RichiestaDomandaRifiuta().setMotivazioneRifiuto(MOTIVAZIONE_RIFIUTO)
                                            .setIdDomanda(ID_DOMANDA);
    }

    private IstruttoriaEntita istruttoriaEntitaConMotivazioneRifiuto() {
        return new IstruttoriaEntita().setMotivazioneRifiuto(MOTIVAZIONE_RIFIUTO);
    }

    private A4gtUtente utenteDaModificare() {
        A4gtUtente utente = new A4gtUtente();
        utente.setId(ID_UTENTE);
        return utente;
    }

    private IstruttoriaSenzaDomanda istruttoriaSenzaDomanda() {
        return new IstruttoriaSenzaDomanda().setIdUtente(ID_UTENTE)
                                            .setProfili(IDS_PROFILI)
                                            .setSedi(IDS_SEDI);
    }

    private IstruttoriaEntita istruttoriaSalvataSenzaDomanda() {
        IstruttoriaEntita istruttoriaEntita = new IstruttoriaEntita();
        istruttoriaEntita.setId(ID_ISTRUTTORIA);
        return istruttoriaEntita;
    }
}
