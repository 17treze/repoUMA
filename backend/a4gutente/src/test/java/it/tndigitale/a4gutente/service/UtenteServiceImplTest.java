package it.tndigitale.a4gutente.service;

import static it.tndigitale.a4gutente.dto.MotivazioneDisattivazione.PENSIONAMENTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import it.tndigitale.a4gutente.repository.dao.IDistributoreDao;
import it.tndigitale.a4gutente.repository.model.*;
import org.junit.Test;

import it.tndigitale.a4g.framework.security.configuration.SecurityContextWrapper;
import it.tndigitale.a4gutente.dto.MotivazioneDisattivazione;
import it.tndigitale.a4gutente.dto.Utente;
import it.tndigitale.a4gutente.repository.dao.IPersonaDao;
import it.tndigitale.a4gutente.repository.dao.IUtenteCompletoDao;
import it.tndigitale.a4gutente.repository.dao.RuoloDao;
import it.tndigitale.a4gutente.repository.dao.custom.UtenteCompletoCustomDao;
import it.tndigitale.a4gutente.service.loader.IstruttoriaLoader;

public class UtenteServiceImplTest {

    private UtenteServiceImpl utenteService;

    private IUtenteCompletoDao utenteCompletoDao;
    private RuoloDao ruoloDao;
    private UtenteCompletoCustomDao utenteCompletoCustomDao;
    private SecurityContextWrapper securityContextWrapper;
    private IPersonaDao personaDao;
    private IDistributoreDao distributoreDao;
    private IstruttoriaLoader istruttoriaLoader;

    private static final String IDENTIFICATIVO = "IDENTIFICATIVO";
    private static final String COGNOME = "COGNOME";
    private static final String NOME= "NOME";
    private static final MotivazioneDisattivazione MOTIVO_DISATTIVAZIONE= PENSIONAMENTO;
    private static final Long ID_UTENTE = 1L;
    private static final Set<A4gtProfilo> profili = new HashSet<A4gtProfilo>() {{
        A4gtProfilo profilo = new A4gtProfilo();
        profilo.setId(12L);
        profilo.setIdentificativo("Identificativo");
        add(profilo);
    }};
    private static final List<A4gtProfilo> profiliDisabilitati = new ArrayList<A4gtProfilo>() {{
        A4gtProfilo profilo = new A4gtProfilo();
        profilo.setId(14L);
        profilo.setIdentificativo("Identificativo 2");
        add(profilo);
    }};
    private static final Set<A4gtEnte> sedi = new HashSet<A4gtEnte>() {{
        A4gtEnte sede = new A4gtEnte();
        sede.setId(14L);
        sede.setIdentificativo(2930L);
        add(sede);
    }};
    private static final Set<A4grUtenteAzienda> aziende = new HashSet<A4grUtenteAzienda>() {{
        A4grUtenteAzienda azienda = new A4grUtenteAzienda();
        azienda.setId(17L);
        azienda.setIdCarica(1L);
        azienda.setCuaa(IDENTIFICATIVO);
        add(azienda);
    }};
    private static final Set<A4gtDistributore> distributori = new HashSet<A4gtDistributore>() {{
        A4gtDistributore distributore = new A4gtDistributore();
        distributore.setId(17L);
        distributore.setDenominazioneAzienda("AGIP TRENTO");
        add(distributore);
    }};

    public UtenteServiceImplTest() {
        utenteCompletoDao = mock(IUtenteCompletoDao.class);
        ruoloDao = mock(RuoloDao.class);
        utenteCompletoCustomDao = mock(UtenteCompletoCustomDao.class);
        securityContextWrapper = mock(SecurityContextWrapper.class);
        personaDao = mock(IPersonaDao.class);
        distributoreDao = mock(IDistributoreDao.class);
        istruttoriaLoader = mock(IstruttoriaLoader.class);

        utenteService = new UtenteServiceImpl()
                .setUtenteCompletoDao(utenteCompletoDao,
                                      ruoloDao,
                                      utenteCompletoCustomDao,
                                      securityContextWrapper,
                                      personaDao,
                                      istruttoriaLoader,
                                      distributoreDao);
    }

    @Test
    public void forCaricaByIdentificativoIfNotExistThenThrowing() {
        when(utenteCompletoDao.findByIdentificativo(IDENTIFICATIVO)).thenReturn(null);

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> utenteService.carica(IDENTIFICATIVO));
        verify(utenteCompletoDao).findByIdentificativo(IDENTIFICATIVO);
        verify(personaDao, never()).findOneByCodiceFiscale(any());
        verify(istruttoriaLoader, never()).loadLastIstruttoriaByIdentificativoUtente(IDENTIFICATIVO);
    }

    @Test
    public void forCaricaByIdentificativoIfExistWithoutProfiliDisabilitatiThenReturnUtente() throws Exception {
        when(utenteCompletoDao.findByIdentificativo(IDENTIFICATIVO)).thenReturn(utente());
        when(personaDao.findOneByCodiceFiscale(IDENTIFICATIVO)).thenReturn(persona());
        when(istruttoriaLoader.loadLastIstruttoriaByIdentificativoUtente(IDENTIFICATIVO)).thenReturn(null);

        Utente utente = utenteService.carica(IDENTIFICATIVO);

        assertThat(utente).isNotNull();
        assertThat(utente.getIdentificativo()).isEqualTo(IDENTIFICATIVO);
        assertThat(utente.getId()).isEqualTo(ID_UTENTE);
        assertThat(utente.getCognome()).isEqualTo(COGNOME);
        assertThat(utente.getNome()).isEqualTo(NOME);
        assertThat(utente.getMotivazioneDisattivazione()).isNull();
        assertThat(utente.getProfili()).hasSize(1);
        assertThat(utente.getProfili().get(0).getDisabled()).isFalse();
        assertThat(utente.getSedi()).hasSize(1);
        assertThat(utente.getAziende()).hasSize(1);
        verify(utenteCompletoDao).findByIdentificativo(IDENTIFICATIVO);
        verify(personaDao).findOneByCodiceFiscale(IDENTIFICATIVO);
        verify(istruttoriaLoader).loadLastIstruttoriaByIdentificativoUtente(IDENTIFICATIVO);

    }

    @Test
    public void forCaricaByIdentificativoIfExistWithProfiliDisabilitatiThenReturnUtente() throws Exception {
        when(utenteCompletoDao.findByIdentificativo(IDENTIFICATIVO)).thenReturn(utente());
        when(personaDao.findOneByCodiceFiscale(IDENTIFICATIVO)).thenReturn(persona());
        when(istruttoriaLoader.loadLastIstruttoriaByIdentificativoUtente(IDENTIFICATIVO)).thenReturn(istruttoria());

        Utente utente = utenteService.carica(IDENTIFICATIVO);

        assertThat(utente).isNotNull();
        assertThat(utente.getIdentificativo()).isEqualTo(IDENTIFICATIVO);
        assertThat(utente.getId()).isEqualTo(ID_UTENTE);
        assertThat(utente.getCognome()).isEqualTo(COGNOME);
        assertThat(utente.getNome()).isEqualTo(NOME);
        assertThat(utente.getMotivazioneDisattivazione()).isEqualTo(MOTIVO_DISATTIVAZIONE);
        assertThat(utente.getProfili()).hasSize(2);
        assertThat(utente.getProfili().get(0).getDisabled()).isFalse();
        assertThat(utente.getProfili().get(0).getId()).isEqualTo(12L);
        assertThat(utente.getProfili().get(1).getDisabled()).isTrue();
        assertThat(utente.getProfili().get(1).getId()).isEqualTo(14L);
        assertThat(utente.getSedi()).hasSize(1);
        assertThat(utente.getAziende()).hasSize(1);
        verify(utenteCompletoDao).findByIdentificativo(IDENTIFICATIVO);
        verify(personaDao).findOneByCodiceFiscale(IDENTIFICATIVO);
        verify(istruttoriaLoader).loadLastIstruttoriaByIdentificativoUtente(IDENTIFICATIVO);
    }

    @Test
    public void forCaricaByIdIfUtenteNotExistThenThrowing() {
        when(utenteCompletoDao.findById(ID_UTENTE)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> utenteService.carica(ID_UTENTE));
        verify(utenteCompletoDao).findById(ID_UTENTE);
        verify(personaDao, never()).findOneByCodiceFiscale(any());
        verify(istruttoriaLoader, never()).loadLastIstruttoriaByIdentificativoUtente(any());
    }

    @Test
    public void forCaricaByIdIfUtenteExistThenReturnUtente() throws Exception {
        when(utenteCompletoDao.findById(ID_UTENTE)).thenReturn(Optional.of(utente()));
        when(personaDao.findOneByCodiceFiscale(IDENTIFICATIVO)).thenReturn(persona());
        when(istruttoriaLoader.loadLastIstruttoriaByIdentificativoUtente(IDENTIFICATIVO)).thenReturn(istruttoria());

        Utente utente = utenteService.carica(ID_UTENTE);

        assertThat(utente).isNotNull();
        assertThat(utente.getIdentificativo()).isEqualTo(IDENTIFICATIVO);
        assertThat(utente.getId()).isEqualTo(ID_UTENTE);
        assertThat(utente.getCognome()).isEqualTo(COGNOME);
        assertThat(utente.getNome()).isEqualTo(NOME);
        assertThat(utente.getMotivazioneDisattivazione()).isEqualTo(MOTIVO_DISATTIVAZIONE);
        assertThat(utente.getProfili()).hasSize(2);
        assertThat(utente.getProfili().get(0).getDisabled()).isFalse();
        assertThat(utente.getProfili().get(0).getId()).isEqualTo(12L);
        assertThat(utente.getProfili().get(1).getDisabled()).isTrue();
        assertThat(utente.getProfili().get(1).getId()).isEqualTo(14L);
        assertThat(utente.getSedi()).hasSize(1);
        assertThat(utente.getAziende()).hasSize(1);
        verify(utenteCompletoDao).findById(ID_UTENTE);
        verify(personaDao).findOneByCodiceFiscale(IDENTIFICATIVO);
        verify(istruttoriaLoader).loadLastIstruttoriaByIdentificativoUtente(IDENTIFICATIVO);
    }

    private A4gtUtente utente() {
        A4gtUtente utente = new A4gtUtente();
        utente.setIdentificativo(IDENTIFICATIVO);
        utente.setCodiceFiscale(IDENTIFICATIVO);
        utente.setId(ID_UTENTE);
        utente.setProfili(profili);
        utente.setA4gtEntes(sedi);
        utente.setA4grUtenteAziendas(aziende);
        utente.setA4gtDistributori(distributori);
        return utente;
    }

    private PersonaEntita persona() {
        PersonaEntita personaEntita = new PersonaEntita();
        personaEntita.setCodiceFiscale(IDENTIFICATIVO);
        personaEntita.setCognome(COGNOME);
        personaEntita.setNome(NOME);
        return personaEntita;
    }

    private IstruttoriaEntita istruttoria() {
        return new IstruttoriaEntita().setMotivazioneDisattivazione(MOTIVO_DISATTIVAZIONE)
                                      .setProfiliDisabilitati(profiliDisabilitati);
    }
}
