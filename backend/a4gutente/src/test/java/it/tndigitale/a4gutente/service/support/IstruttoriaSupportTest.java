package it.tndigitale.a4gutente.service.support;

import it.tndigitale.a4gutente.codici.CAA;
import it.tndigitale.a4gutente.dto.IstruttoriaPerStorico;
import it.tndigitale.a4gutente.dto.MotivazioneDisattivazione;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.TitolareImpresa;
import it.tndigitale.a4gutente.dto.Utente;
import it.tndigitale.a4gutente.repository.dao.IDomandaRegistrazioneUtenteDao;
import it.tndigitale.a4gutente.repository.dao.IIstruttoriaDao;
import it.tndigitale.a4gutente.repository.model.*;
import org.junit.Test;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.CHIUSA;
import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.IN_LAVORAZIONE;
import static it.tndigitale.a4gutente.dto.MotivazioneDisattivazione.PENSIONAMENTO;
import static it.tndigitale.a4gutente.dto.Responsabilita.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class IstruttoriaSupportTest {

    private IstruttoriaSupport istruttoriaSupport;

    private IDomandaRegistrazioneUtenteDao domandaRegistrazioneUtenteDao;
    private IIstruttoriaDao istruttoriaDao;
    private UtenteSupport utenteSupport;

    private static final Long ID_UTENTE = 10L;
    private static final Long ID_ISTRUTTORIA = 100L;
    private static final Long ID_OPERATORE = 1000L;
    private static final Long ID_DOMANDA = 10000L;
    private static final MotivazioneDisattivazione MOTIVO_DISATTIVAZIONE = PENSIONAMENTO;
    private static final String CODICE_FISCALE_UTENTE = "CODICE_FISCALE_UTENTE";
    private static final String CODICE_FISCALE_OPERATORE = "CODICE_FISCALE_OPERATORE";
    private static final String TESTO_MAIL_INVIATA = "TESTO_MAIL_INVIATA";
    private static final String TESTO_COMUNICAZIONE = "TESTO_COMUNICAZIONE";
    private static final LocalDateTime DATA_TERMINE_ISTRUTTORIE = LocalDateTime.of(2019, 1,1 ,10,0);
    private static final List<A4gtProfilo> PROFILI_ATTIVI = new ArrayList<A4gtProfilo>() {{
        add(new A4gtProfilo().setResponsabilita(TITOLARE_AZIENDA_AGRICOLA).setDescrizione("XXXXXX"));
        add(new A4gtProfilo().setResponsabilita(DIPENDENTE_PAT).setDescrizione("YYYYYY"));
    }};
    private static final List<A4gtProfilo> PROFILI_DISABILITATI = new ArrayList<A4gtProfilo>() {{
        add(new A4gtProfilo().setResponsabilita(DIPENDENTE_TNDIGIT).setDescrizione("XXXXXX"));
    }};
    private static final List<A4gtEnte> ENTI = new ArrayList<A4gtEnte>() {{
        A4gtEnte ENTE = mock(A4gtEnte.class);
        when(ENTE.getCAA()).thenReturn(CAA.ACLI);
        when(ENTE.getDescrizione()).thenReturn("CAA ACLI - TRENTO - 001");
        when(ENTE.getIdentificativo()).thenReturn(13L);
        add(ENTE);
    }};

    public IstruttoriaSupportTest() {
        domandaRegistrazioneUtenteDao = mock(IDomandaRegistrazioneUtenteDao.class);
        istruttoriaDao = mock(IIstruttoriaDao.class);
        utenteSupport = mock(UtenteSupport.class);

        istruttoriaSupport = new IstruttoriaSupport()
                .setComponents(domandaRegistrazioneUtenteDao,
                               istruttoriaDao,
                               utenteSupport);
    }

    @Test
    public void forExtraiTitolatiImpresaDaDomandaIstruttoriaIfNotExistDomandaThenReturnEmptyList() throws Exception {
        when(domandaRegistrazioneUtenteDao.getOne(ID_DOMANDA)).thenReturn(null);

        List<TitolareImpresa> titolari = istruttoriaSupport.extraiTitolatiImpresaDaDomandaIstruttoria(ID_DOMANDA);

        assertThat(titolari).isEmpty();
        verify(domandaRegistrazioneUtenteDao).getOne(ID_DOMANDA);
    }

    @Test
    public void forExtraiTitolatiImpresaDaDomandaIstruttoriaIfExistDomandaWithoutResponsabilitaThenReturnNotEmptyList() throws Exception {
        when(domandaRegistrazioneUtenteDao.getOne(ID_DOMANDA)).thenReturn(new DomandaRegistrazioneUtente());

        List<TitolareImpresa> titolari = istruttoriaSupport.extraiTitolatiImpresaDaDomandaIstruttoria(ID_DOMANDA);

        assertThat(titolari).isEmpty();
        verify(domandaRegistrazioneUtenteDao).getOne(ID_DOMANDA);
    }

    @Test
    public void forExtraiTitolatiImpresaDaDomandaIstruttoriaIfExistDomandaWithResponsabilitaThenReturnNotEmptyList() throws Exception {
        when(domandaRegistrazioneUtenteDao.getOne(ID_DOMANDA)).thenReturn(domandaRegistrazioneUtente());

        List<TitolareImpresa> titolari = istruttoriaSupport.extraiTitolatiImpresaDaDomandaIstruttoria(ID_DOMANDA);

        assertThat(titolari).isNotEmpty();
        assertThat(titolari).hasSize(2);
        verify(domandaRegistrazioneUtenteDao).getOne(ID_DOMANDA);
    }

    @Test
    public void forCaricaIstruttorieXStoricoIfIstruttorieNullThenReturnEmptyList() {
        when(istruttoriaDao.findIstruttorieByIdUtente(ID_UTENTE)).thenReturn(null);

        List<IstruttoriaPerStorico> istruttorie = istruttoriaSupport.caricaIstruttorieXStorico(ID_UTENTE);

        assertThat(istruttorie).isEmpty();
        verify(istruttoriaDao).findIstruttorieByIdUtente(ID_UTENTE);
        verify(utenteSupport, never()).caricaUtenteConAnagrafica(any());
    }

    @Test
    public void forCaricaIstruttorieXStoricoIfIstruttorieEmptyThenReturnEmptyList() {
        when(istruttoriaDao.findIstruttorieByIdUtente(ID_UTENTE)).thenReturn(emptyList());

        List<IstruttoriaPerStorico> istruttorie = istruttoriaSupport.caricaIstruttorieXStorico(ID_UTENTE);

        assertThat(istruttorie).isEmpty();
        verify(istruttoriaDao).findIstruttorieByIdUtente(ID_UTENTE);
        verify(utenteSupport, never()).caricaUtenteConAnagrafica(any());
    }

    @Test
    public void forCaricaIstruttorieXStoricoIfIstruttorieNotEmptyThenReturnNotEmptyList() {
        when(istruttoriaDao.findIstruttorieByIdUtente(ID_UTENTE)).thenReturn(Arrays.asList(istruttoriaEntita()));
        when(utenteSupport.caricaUtenteConAnagrafica(ID_OPERATORE)).thenReturn(operatore());

        List<IstruttoriaPerStorico> istruttorie = istruttoriaSupport.caricaIstruttorieXStorico(ID_UTENTE);

        assertThat(istruttorie).isNotEmpty();
        assertThat(istruttorie).hasSize(1);
        assertThat(istruttorie.get(0).getId()).isEqualTo(ID_ISTRUTTORIA);
        assertThat(istruttorie.get(0).getMotivazioneDisattivazione()).isEqualTo(MOTIVO_DISATTIVAZIONE);
        assertThat(istruttorie.get(0).getTestoComunicazione()).isEqualTo(TESTO_COMUNICAZIONE);
        assertThat(istruttorie.get(0).getTestoMailInviata()).isEqualTo(TESTO_MAIL_INVIATA);
        assertThat(istruttorie.get(0).getIstruttore()).isNotNull();
        assertThat(istruttorie.get(0).getIstruttore().getId()).isEqualTo(ID_OPERATORE);
        assertThat(istruttorie.get(0).getAziende()).hasSize(2);
        assertThat(istruttorie.get(0).getProfili()).hasSize(3);
        assertThat(istruttorie.get(0).getSedi()).hasSize(1);
        verify(istruttoriaDao).findIstruttorieByIdUtente(ID_UTENTE);
        verify(utenteSupport).caricaUtenteConAnagrafica(ID_OPERATORE);
    }

    @Test
    public void forExistProfiloWithResponsabilitaIfProfiliNullThenReturnOptionalEmpty() {
        Optional<A4gtProfilo> result = IstruttoriaSupport.existProfiloWithResponsabilita(null, DIPENDENTE_TNDIGIT);

        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void forExistProfiloWithResponsabilitaIfProfiliNotNullAndNotMatchThenReturnOptionalEmpty() {
        Optional<A4gtProfilo> result = IstruttoriaSupport
                .existProfiloWithResponsabilita(Arrays.asList(new A4gtProfilo().setResponsabilita(TITOLARE_AZIENDA_AGRICOLA)),
                                                DIPENDENTE_TNDIGIT);

        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void forExistProfiloWithResponsabilitaIfProfiliNotNullAndMatchThenReturnOptionalNotEmpty() {
        Optional<A4gtProfilo> result = IstruttoriaSupport
                .existProfiloWithResponsabilita(Arrays.asList(new A4gtProfilo().setResponsabilita(DIPENDENTE_TNDIGIT)),
                        DIPENDENTE_TNDIGIT);

        assertThat(result.get().getResponsabilita()).isEqualTo(DIPENDENTE_TNDIGIT);
    }

    @Test
    public void forExistProfiloWithResponsabilitaIfProfiliNEmptyThenReturnOptionalEmpty() {
        Optional<A4gtProfilo> result = IstruttoriaSupport.existProfiloWithResponsabilita(emptyList(), DIPENDENTE_TNDIGIT);

        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void forIsNotUpdatableIfDomandaNullThenReturnFalse() {
        Boolean isNotUpdatable = IstruttoriaSupport.isNotUpdatable(new IstruttoriaEntita());

        assertThat(isNotUpdatable).isFalse();
    }

    @Test
    public void forIsNotUpdatableIfDomandaNotNullAndStatoNullThenReturnFalse() {
        Boolean isNotUpdatable = IstruttoriaSupport.isNotUpdatable(new IstruttoriaEntita().setDomanda(new DomandaRegistrazioneUtente()));

        assertThat(isNotUpdatable).isTrue();
    }

    @Test
    public void forIsNotUpdatableIfStatoNotNullAndEqualInLavorazioneIThenReturnFalse() {
        Boolean isNotUpdatable = IstruttoriaSupport
                .isNotUpdatable(new IstruttoriaEntita().setDomanda(new DomandaRegistrazioneUtente().setStato(IN_LAVORAZIONE)));

        assertThat(isNotUpdatable).isFalse();
    }

    @Test
    public void forIsNotUpdatableIfStatoNotNullAndNotEqualInLavorazioneIThenReturnTrue() {
        Boolean isNotUpdatable = IstruttoriaSupport
                .isNotUpdatable(new IstruttoriaEntita().setDomanda(new DomandaRegistrazioneUtente().setStato(CHIUSA)));

        assertThat(isNotUpdatable).isTrue();
    }

    @Test
    public void forIsUpdatableOrThrowIfNotUpdatableThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() ->
                        IstruttoriaSupport
                                .isUpdatableOrThrow(
                                        new IstruttoriaEntita().setDomanda(new DomandaRegistrazioneUtente().setStato(CHIUSA))));
    }

    @Test
    public void forIsUpdatableOrThrowIfUpdatableThenNotThrowing() {
        IstruttoriaSupport
                .isUpdatableOrThrow(
                        new IstruttoriaEntita().setDomanda(new DomandaRegistrazioneUtente().setStato(IN_LAVORAZIONE)));
    }

    @Test
    public void forGetTitolariImpresaFromIfResponsabilitaNullThenReturnEmptyList() {
        List<TitolareImpresa> titolareImpresa = IstruttoriaSupport.getTitolariImpresaFrom(null);

        assertThat(titolareImpresa).isEmpty();
    }

    @Test
    public void forGetTitolariImpresaFromIfResponsabilitaNotNullAndLegaleTitolareNullThenReturnEmptyList() {
        List<TitolareImpresa> titolareImpresa = IstruttoriaSupport.getTitolariImpresaFrom(new ResponsabilitaRichieste());

        assertThat(titolareImpresa).isEmpty();
    }

    @Test
    public void forGetTitolariImpresaFromIfResponsabilitaNotNullAndLegaleTitolareEmptyListThenReturnEmptyList() {
        List<TitolareImpresa> titolareImpresa = IstruttoriaSupport
                .getTitolariImpresaFrom(responsabilitaRichiesteWithEmptyList());

        assertThat(titolareImpresa).isEmpty();
    }

    @Test
    public void forGetTitolariImpresaFromIfResponsabilitaNotNullAndLegaleTitolareNotEmptyListThenReturnNotEmptyList() {
        List<TitolareImpresa> titolareImpresa = IstruttoriaSupport
                .getTitolariImpresaFrom(responsabilitaRichiesteWithNotEmptyList());

        assertThat(titolareImpresa).isNotEmpty();
        assertThat(titolareImpresa).hasSize(3);
    }

    private ResponsabilitaRichieste responsabilitaRichiesteWithEmptyList() {
        ResponsabilitaRichieste responsabilitaRichieste = new ResponsabilitaRichieste();
        responsabilitaRichieste.setResponsabilitaLegaleRappresentante(emptyList());
        responsabilitaRichieste.setResponsabilitaTitolare(emptyList());
        return responsabilitaRichieste;
    }

    private ResponsabilitaRichieste responsabilitaRichiesteWithNotEmptyList() {
        ResponsabilitaRichieste responsabilitaRichieste = new ResponsabilitaRichieste();
        List<TitolareImpresa> titolari1 = new ArrayList<>();
        titolari1.add(new TitolareImpresa());
        responsabilitaRichieste.setResponsabilitaLegaleRappresentante(titolari1);
        List<TitolareImpresa> titolari2 = new ArrayList<>();
        titolari2.add(new TitolareImpresa());
        titolari2.add(new TitolareImpresa());
        responsabilitaRichieste.setResponsabilitaTitolare(titolari2);
        return responsabilitaRichieste;
    }

    private IstruttoriaEntita istruttoriaEntita() {
        DomandaRegistrazioneUtente domanda = new DomandaRegistrazioneUtente()
                .setResponsabilita(responsabilita());

        A4gtUtente utente = new A4gtUtente().setCodiceFiscale(CODICE_FISCALE_UTENTE);
        utente.setId(ID_UTENTE);

        A4gtUtente istruttore = new A4gtUtente().setCodiceFiscale(CODICE_FISCALE_OPERATORE)
                                                .setIdentificativo(CODICE_FISCALE_OPERATORE);
        istruttore.setId(ID_OPERATORE);

        IstruttoriaEntita istruttoriaEntita = new IstruttoriaEntita();
        istruttoriaEntita.setId(ID_ISTRUTTORIA);

        return istruttoriaEntita.setDomanda(domanda)
                                .setMotivazioneDisattivazione(MOTIVO_DISATTIVAZIONE)
                                .setUtente(utente)
                                .setIstruttore(istruttore)
                                .setDataTermineIstruttoria(DATA_TERMINE_ISTRUTTORIE)
                                .setTestoMailInviata(TESTO_MAIL_INVIATA)
                                .setTestoComunicazione(TESTO_COMUNICAZIONE)
                                .setProfili(PROFILI_ATTIVI)
                                .setProfiliDisabilitati(PROFILI_DISABILITATI)
                                .setEnti(ENTI);
    }

    private String responsabilita() {
        return "{\"responsabilitaTitolare\":[{\"cuaa\":\"TRRCST78B08C794X\"}],\"responsabilitaLegaleRappresentante\":[{\"cuaa\":\"92009950228\"}],\"responsabilitaCaa\":null}";
    }

    private Utente operatore() {
        return new Utente().setId(ID_OPERATORE)
                           .setCodiceFiscale(CODICE_FISCALE_OPERATORE)
                           .setNome("Fabio")
                           .setCodiceFiscale("Cometa")
                           .setIdentificativo(CODICE_FISCALE_OPERATORE)
                           .setProfili(emptyList());
    }

    private DomandaRegistrazioneUtente domandaRegistrazioneUtente() {
        return new DomandaRegistrazioneUtente().setResponsabilita(responsabilita());
    }
}
