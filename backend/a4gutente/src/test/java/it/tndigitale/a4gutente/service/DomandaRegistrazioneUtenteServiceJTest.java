package it.tndigitale.a4gutente.service;

import it.tndigitale.a4gutente.dto.RichiestaDomandaApprovazione;
import it.tndigitale.a4gutente.dto.RichiestaDomandaRifiuta;
import it.tndigitale.a4gutente.dto.TipoDomandaRegistrazione;
import it.tndigitale.a4gutente.exception.ValidationException;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;
import it.tndigitale.a4gutente.service.support.DomandaRegistrazioneUtenteSupport;
import it.tndigitale.a4gutente.utility.EmailSupport;

import org.junit.Test;

import javax.persistence.EntityManager;

import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.APPROVATA;
import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.RIFIUTATA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class DomandaRegistrazioneUtenteServiceJTest {

    private DomandaRegistrazioneUtenteService domandaRegistrazioneUtenteService;
    private DomandaRegistrazioneUtenteSupport domandaRegistrazioneUtenteSupport;
    private EmailSupport emailSupport;
    private IstruttoriaService istruttoriaService;
    private EntityManager entityManager;

    private static final Long ID_DOMANDA = 1L;
    private static final String MAIL = "mail@mail.com";
    private static final String NOTE = "NOTE";
    private static final String TESTO_MAIL = "TESTO_MAIL";
    private static final String MOTIVAZIONE_RIFIUTO = "MOTIVAZIONE_RIFIUTO";
    private static final String APPAG_FIRMA = "\n\nUnità Informatizzazione e Sviluppo piattaforme Informatiche\n\n" +
            "PROVINCIA AUTONOMA DI TRENTO \n" +
            "AGENZIA PROVINCIALE PER I PAGAMENTI\n" +
            "IT - Via G.B. Trener, 3 - 38121 Trento\n" +
            "T. +39 0461 494909\n" +
            "F. +39 0461 495810\n" +
            "@ siap@provincia.tn.it\n" +
            "@ appag@provincia.tn.it";

    public DomandaRegistrazioneUtenteServiceJTest() {
        domandaRegistrazioneUtenteSupport = mock(DomandaRegistrazioneUtenteSupport.class);
        emailSupport = mock(EmailSupport.class);
        istruttoriaService = mock(IstruttoriaService.class);
        entityManager = mock(EntityManager.class);

        domandaRegistrazioneUtenteService= new DomandaRegistrazioneUtenteService()
                .setComponents(domandaRegistrazioneUtenteSupport,
                               emailSupport,
                               istruttoriaService,
                                entityManager);
    }

    @Test
    public void approvaDomandaRichiestaUtente() throws Exception {
        when(domandaRegistrazioneUtenteSupport.cambiaStato(ID_DOMANDA, APPROVATA)).thenReturn(aDomandaRegistrazioneUtente());
        when(istruttoriaService.aggiornaIstruttoriaPerApprovazioneDomanda(aRichiestaDomandaApprovazione())).thenReturn(new IstruttoriaEntita());
        when(istruttoriaService.creaOAggiornaUtenzaPerApprovazioneDomanda(any())).thenReturn(new A4gtUtente());
        when(istruttoriaService.aggiungiUtenteAIstruttoria(new A4gtUtente(), new IstruttoriaEntita())).thenReturn(new IstruttoriaEntita());

        Long idDomanda = domandaRegistrazioneUtenteService.approva(aRichiestaDomandaApprovazione());

        assertThat(idDomanda).isEqualTo(ID_DOMANDA);
        verify(domandaRegistrazioneUtenteSupport).cambiaStato(ID_DOMANDA, APPROVATA);
        verify(istruttoriaService).aggiornaIstruttoriaPerApprovazioneDomanda(aRichiestaDomandaApprovazione());
        verify(istruttoriaService).creaOAggiornaUtenzaPerApprovazioneDomanda(new IstruttoriaEntita());
        verify(emailSupport).sendSimpleMessage(MAIL,
                                       "APPAG - Approvazione della domanda di richiesta accesso utente",
                                          TESTO_MAIL + APPAG_FIRMA);
        verify(entityManager).flush();
        verify(istruttoriaService).aggiungiUtenteAIstruttoria(any(), any());

    }

    @Test
    public void approvaDomandaKOForInvalidRequest() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> domandaRegistrazioneUtenteService.approva(new RichiestaDomandaApprovazione()));
        verify(domandaRegistrazioneUtenteSupport, never()).cambiaStato(any(), any());
        verify(istruttoriaService, never()).aggiornaIstruttoriaPerApprovazioneDomanda(any());
        verify(istruttoriaService, never()).creaOAggiornaUtenzaPerApprovazioneDomanda(any());
        verify(emailSupport, never()).sendSimpleMessage(any(), any(), any());
        verify(entityManager, never()).flush();
    }

    @Test
    public void approvaDomandaKOForInvalidRequest_TestoMailConSoliSpazi_() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                domandaRegistrazioneUtenteService.approva(new RichiestaDomandaApprovazione().setTestoMail("  ").setIdDomanda(ID_DOMANDA)));
        verify(domandaRegistrazioneUtenteSupport, never()).cambiaStato(any(), any());
        verify(istruttoriaService, never()).aggiornaIstruttoriaPerApprovazioneDomanda(any());
        verify(istruttoriaService, never()).creaOAggiornaUtenzaPerApprovazioneDomanda(any());
        verify(emailSupport, never()).sendSimpleMessage(any(), any(), any());
        verify(entityManager, never()).flush();
    }

    @Test
    public void approvaDomandaKOForInvalidRequest_TestoMailVuoto_() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                domandaRegistrazioneUtenteService.approva(new RichiestaDomandaApprovazione().setTestoMail("").setIdDomanda(ID_DOMANDA)));
        verify(domandaRegistrazioneUtenteSupport, never()).cambiaStato(any(), any());
        verify(istruttoriaService, never()).aggiornaIstruttoriaPerApprovazioneDomanda(any());
        verify(istruttoriaService, never()).creaOAggiornaUtenzaPerApprovazioneDomanda(any());
        verify(emailSupport, never()).sendSimpleMessage(any(), any(), any());
        verify(entityManager, never()).flush();
    }

    @Test
    public void approvaDomandaKOForInvalidRequest_TestoMailNull_() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                domandaRegistrazioneUtenteService.approva(new RichiestaDomandaApprovazione().setTestoMail(null).setIdDomanda(ID_DOMANDA)));
        verify(domandaRegistrazioneUtenteSupport, never()).cambiaStato(any(), any());
        verify(istruttoriaService, never()).aggiornaIstruttoriaPerApprovazioneDomanda(any());
        verify(istruttoriaService, never()).creaOAggiornaUtenzaPerApprovazioneDomanda(any());
        verify(emailSupport, never()).sendSimpleMessage(any(), any(), any());
        verify(entityManager, never()).flush();
    }

    @Test
    public void approvaDomandaKOForInvalidRequest_IdDomandaNULL_() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                domandaRegistrazioneUtenteService.approva(new RichiestaDomandaApprovazione().setTestoMail("Testo mail").setIdDomanda(null)));
        verify(domandaRegistrazioneUtenteSupport, never()).cambiaStato(any(), any());
        verify(istruttoriaService, never()).aggiornaIstruttoriaPerApprovazioneDomanda(any());
        verify(istruttoriaService, never()).creaOAggiornaUtenzaPerApprovazioneDomanda(any());
        verify(emailSupport, never()).sendSimpleMessage(any(), any(), any());
        verify(entityManager, never()).flush();
    }

    @Test
    public void rifiutaDomandaRichiestaUtente() throws Exception {
        when(domandaRegistrazioneUtenteSupport.cambiaStato(ID_DOMANDA, RIFIUTATA)).thenReturn(aDomandaRegistrazioneUtenteRifiutata());
        when(istruttoriaService.aggiornaIstruttoriaPerRifiutoDomanda(aRichiestaDomandaRifiuta())).thenReturn(new IstruttoriaEntita());

        Long idDomanda = domandaRegistrazioneUtenteService.rifiuta(aRichiestaDomandaRifiuta());

        assertThat(idDomanda).isEqualTo(ID_DOMANDA);
        verify(domandaRegistrazioneUtenteSupport).cambiaStato(ID_DOMANDA, RIFIUTATA);
        verify(istruttoriaService).aggiornaIstruttoriaPerRifiutoDomanda(aRichiestaDomandaRifiuta());
        verify(emailSupport).sendSimpleMessage(MAIL,
                                       "APPAG - Rifiuto della domanda di richiesta accesso utente",
                                               TESTO_MAIL + APPAG_FIRMA);
        verify(entityManager).flush();
    }

    @Test
    public void rifiutaDomandaRichiestaUtenteKOForInvalidrequest() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> domandaRegistrazioneUtenteService.rifiuta(new RichiestaDomandaRifiuta()));
        verify(domandaRegistrazioneUtenteSupport, never()).cambiaStato(any(), any());
        verify(istruttoriaService, never()).aggiornaIstruttoriaPerRifiutoDomanda(any());
        verify(emailSupport, never()).sendSimpleMessage(any(), any(), any());
        verify(entityManager, never()).flush();
    }

    @Test
    public void rifiutaDomandaRichiestaUtenteKOForInvalidrequest_TestoMailConSoloSpazi() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                domandaRegistrazioneUtenteService.rifiuta(new RichiestaDomandaRifiuta().setIdDomanda(ID_DOMANDA)
                                                                                       .setTestoMail("  ")
                                                                                       .setMotivazioneRifiuto("Motivazione")));
        verify(domandaRegistrazioneUtenteSupport, never()).cambiaStato(any(), any());
        verify(istruttoriaService, never()).aggiornaIstruttoriaPerRifiutoDomanda(any());
        verify(emailSupport, never()).sendSimpleMessage(any(), any(), any());
        verify(entityManager, never()).flush();
    }

    @Test
    public void rifiutaDomandaRichiestaUtenteKOForInvalidrequest_TestoMailVuoto() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                domandaRegistrazioneUtenteService.rifiuta(new RichiestaDomandaRifiuta().setIdDomanda(ID_DOMANDA)
                                                                                       .setTestoMail("")
                                                                                       .setMotivazioneRifiuto("Motivazione")));
        verify(domandaRegistrazioneUtenteSupport, never()).cambiaStato(any(), any());
        verify(istruttoriaService, never()).aggiornaIstruttoriaPerRifiutoDomanda(any());
        verify(emailSupport, never()).sendSimpleMessage(any(), any(), any());
        verify(entityManager, never()).flush();
    }

    @Test
    public void rifiutaDomandaRichiestaUtenteKOForInvalidrequest_TestoMailNull() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                domandaRegistrazioneUtenteService.rifiuta(new RichiestaDomandaRifiuta().setIdDomanda(ID_DOMANDA)
                                                                                       .setTestoMail(null)
                                                                                       .setMotivazioneRifiuto("Motivazione")));
        verify(domandaRegistrazioneUtenteSupport, never()).cambiaStato(any(), any());
        verify(istruttoriaService, never()).aggiornaIstruttoriaPerRifiutoDomanda(any());
        verify(emailSupport, never()).sendSimpleMessage(any(), any(), any());
        verify(entityManager, never()).flush();
    }

    @Test
    public void rifiutaDomandaRichiestaUtenteKOForInvalidrequest_MotivazioneConSoloSpazi() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                domandaRegistrazioneUtenteService.rifiuta(new RichiestaDomandaRifiuta().setIdDomanda(ID_DOMANDA)
                                                                                       .setTestoMail("Testo mail")
                                                                                       .setMotivazioneRifiuto("  ")));
        verify(domandaRegistrazioneUtenteSupport, never()).cambiaStato(any(), any());
        verify(istruttoriaService, never()).aggiornaIstruttoriaPerRifiutoDomanda(any());
        verify(emailSupport, never()).sendSimpleMessage(any(), any(), any());
        verify(entityManager, never()).flush();
    }

    @Test
    public void rifiutaDomandaRichiestaUtenteKOForInvalidrequest_MotivazioneNull() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                domandaRegistrazioneUtenteService.rifiuta(new RichiestaDomandaRifiuta().setIdDomanda(ID_DOMANDA)
                                                                                       .setTestoMail("Testo mail")
                                                                                       .setMotivazioneRifiuto(null)));
        verify(domandaRegistrazioneUtenteSupport, never()).cambiaStato(any(), any());
        verify(istruttoriaService, never()).aggiornaIstruttoriaPerRifiutoDomanda(any());
        verify(emailSupport, never()).sendSimpleMessage(any(), any(), any());
        verify(entityManager, never()).flush();
    }

    @Test
    public void rifiutaDomandaRichiestaUtenteKOForInvalidrequest_IdDomandaNull() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                domandaRegistrazioneUtenteService.rifiuta(new RichiestaDomandaRifiuta().setIdDomanda(null)
                                                                                       .setTestoMail("Testo mail")
                                                                                       .setMotivazioneRifiuto("Motivazione")));
        verify(domandaRegistrazioneUtenteSupport, never()).cambiaStato(any(), any());
        verify(istruttoriaService, never()).aggiornaIstruttoriaPerRifiutoDomanda(any());
        verify(emailSupport, never()).sendSimpleMessage(any(), any(), any());
        verify(entityManager, never()).flush();
    }


    private DomandaRegistrazioneUtente aDomandaRegistrazioneUtente() {
        DomandaRegistrazioneUtente domanda =  new DomandaRegistrazioneUtente().setEmail(MAIL)
                                                                              .setStato(APPROVATA);
        domanda.setId(ID_DOMANDA);
        domanda.setTipoDomandaRegistrazione(TipoDomandaRegistrazione.COMPLETA);
        return domanda;
    }

    private DomandaRegistrazioneUtente aDomandaRegistrazioneUtenteRifiutata() {
        DomandaRegistrazioneUtente domanda =  new DomandaRegistrazioneUtente().setEmail(MAIL)
                                                                              .setStato(RIFIUTATA);
        domanda.setId(ID_DOMANDA);
        domanda.setTipoDomandaRegistrazione(TipoDomandaRegistrazione.COMPLETA);
        return domanda;
    }

    private RichiestaDomandaApprovazione aRichiestaDomandaApprovazione() {
        return new RichiestaDomandaApprovazione().setIdDomanda(ID_DOMANDA)
                                                 .setNote(NOTE)
                                                 .setTestoMail(TESTO_MAIL);
    }

    private RichiestaDomandaRifiuta aRichiestaDomandaRifiuta() {
        return new RichiestaDomandaRifiuta().setIdDomanda(ID_DOMANDA)
                                            .setTestoMail(TESTO_MAIL)
                                            .setMotivazioneRifiuto(MOTIVAZIONE_RIFIUTO);
    }

}
