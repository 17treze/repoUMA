package it.tndigitale.a4gutente.service;

import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.DatiAnagrafici;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtente.ServiziType;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtenteSintesi;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste;
import it.tndigitale.a4gutente.dto.TipoDomandaRegistrazione;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.TitolareImpresa;
import javassist.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApprovazioneDomandaRegistrazioneUtenteScheduledServiceJTest {

    @Autowired
    private ApprovazioneDomandaRegistrazioneUtenteScheduledService service;

    @MockBean
    private DomandaRegistrazioneUtenteService domandaRegService;

    @MockBean
    private IstruttoriaService istruttoriaService;

    @Test
    public void domandaProtocollataOk() throws Exception {
        doReturn(getRisultatiPaginatiDomandaResigtrazioneUtente()).when(domandaRegService).ricercaDomande(any(), any(), any());
        doReturn(getDatiDomandaRegistrazioneUtente_verificaCondizioniAutomazioneApprovazione_OK()).when(domandaRegService).getDomanda(any());
        doReturn(1L).when(istruttoriaService).crea(any());

        Assertions.assertThatCode(() -> service.automazioneApprovazione()).doesNotThrowAnyException();

        verify(domandaRegService).ricercaDomande(any(), any(), any());
        verify(domandaRegService).getDomanda(any());
        verify(domandaRegService).presaInCarico(any());
        verify(istruttoriaService).crea(any());
        verify(domandaRegService).approva(any());
    }

    @Test
    public void domandaProtocollataKo() throws Exception {
        doReturn(getRisultatiPaginatiDomandaResigtrazioneUtente()).when(domandaRegService).ricercaDomande(any(), any(), any());
        doReturn(getDatiDomandaRegistrazioneUtente_verificaCondizioniAutomazioneApprovazione_OK()).when(domandaRegService).getDomanda(any());
        doThrow(NotFoundException.class).when(domandaRegService).presaInCarico(any());

        Assertions.assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> service.automazioneApprovazione());
        verify(domandaRegService).ricercaDomande(any(), any(), any());
        verify(domandaRegService).getDomanda(any());
        verify(domandaRegService).presaInCarico(any());
        verify(istruttoriaService, never()).crea(any());
        verify(domandaRegService, never()).approva(any());
    }

    @Test
    public void domandaNonProtocollata() throws Exception {
        doReturn(getRicercaDomandeEmpty()).when(domandaRegService).ricercaDomande(any(), any(), any());

        service.automazioneApprovazione();

        verify(domandaRegService).ricercaDomande(any(), any(), any());
        verify(domandaRegService, never()).getDomanda(any());
        verify(domandaRegService, never()).presaInCarico(any());
        verify(istruttoriaService, never()).crea(any());
        verify(domandaRegService, never()).approva(any());
    }

    @Test
    public void verificaCondizioniAutomazioneApprovazioneFalse() throws Exception {
        doReturn(getRisultatiPaginatiDomandaResigtrazioneUtente()).when(domandaRegService).ricercaDomande(any(), any(), any());
        doReturn(getDatiDomandaRegistrazioneUtente_verificaCondizioniAutomazioneApprovazione_KO()).when(domandaRegService).getDomanda(any());

        service.automazioneApprovazione();

        verify(domandaRegService).ricercaDomande(any(), any(), any());
        verify(domandaRegService).getDomanda(any());
        verify(domandaRegService, never()).presaInCarico(any());
        verify(istruttoriaService, never()).crea(any());
        verify(domandaRegService, never()).approva(any());
    }

    private RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> getRicercaDomandeEmpty() {
        RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> listaDomande = new RisultatiPaginati<>();
        listaDomande.setCount(0L);
        listaDomande.setRisultati(Collections.emptyList());
        return listaDomande;
    }

    private RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> getRisultatiPaginatiDomandaResigtrazioneUtente() {
        RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> domandeProtocollate = new RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi>()
                .setCount(1L)
                .setRisultati(getDatiDomandaRegistrazioneUtenteSintesi());
        return domandeProtocollate;
    }

    private List<DatiDomandaRegistrazioneUtenteSintesi> getDatiDomandaRegistrazioneUtenteSintesi() {
        DatiDomandaRegistrazioneUtenteSintesi datiDomanda = new DatiDomandaRegistrazioneUtenteSintesi();
        datiDomanda.setConfigurato(false);
        datiDomanda.setDataProtocollazione(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        datiDomanda.setDatiAnagrafici(getDatiAnagrafici());
        datiDomanda.setId(1L);
        datiDomanda.setIdProtocollo("1234");
        datiDomanda.setStato(StatoDomandaRegistrazioneUtente.PROTOCOLLATA);

        List<DatiDomandaRegistrazioneUtenteSintesi> listDatiDomanda = Arrays.asList(datiDomanda);

        return listDatiDomanda;
    }

    private DatiDomandaRegistrazioneUtente getDatiDomandaRegistrazioneUtente_verificaCondizioniAutomazioneApprovazione_OK() {
        DatiDomandaRegistrazioneUtente datiDomanda = getDatiDomandaRegistrazioneUtente();

        Set<ServiziType> servizi = new HashSet<ServiziType>();
        servizi.add(ServiziType.A4G);

        datiDomanda.setServizi(servizi);
        datiDomanda.setTipoDomandaRegistrazione(TipoDomandaRegistrazione.COMPLETA);
        return datiDomanda;
    }

    private DatiDomandaRegistrazioneUtente getDatiDomandaRegistrazioneUtente_verificaCondizioniAutomazioneApprovazione_KO() {
        DatiDomandaRegistrazioneUtente datiDomanda = getDatiDomandaRegistrazioneUtente();

        Set<ServiziType> servizi = new HashSet<ServiziType>();
        servizi.add(ServiziType.AGS);

        datiDomanda.setServizi(servizi);
        datiDomanda.setTipoDomandaRegistrazione(TipoDomandaRegistrazione.COMPLETA);
        return datiDomanda;
    }

    private DatiDomandaRegistrazioneUtente getDatiDomandaRegistrazioneUtente() {
        DatiDomandaRegistrazioneUtente datiDomanda = new DatiDomandaRegistrazioneUtente();

        datiDomanda.setId(1L);
        datiDomanda.setIdProtocollo("1234");
        datiDomanda.setStato(StatoDomandaRegistrazioneUtente.PROTOCOLLATA);
        datiDomanda.setDatiAnagrafici(getDatiAnagrafici());
        datiDomanda.setResponsabilitaRichieste(getResponsabilitaRichieste());

        datiDomanda.setLuogo("Trento");
        datiDomanda.setData("25/05/2020");
        datiDomanda.setDataProtocollazione(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        datiDomanda.setTipoDomandaRegistrazione(TipoDomandaRegistrazione.COMPLETA);

        return datiDomanda;
    }

    private ResponsabilitaRichieste getResponsabilitaRichieste() {
        ResponsabilitaRichieste responsabilitaRichieste = new ResponsabilitaRichieste();
        responsabilitaRichieste.setResponsabilitaTitolare(getResponsabilitaTitolare());
        return responsabilitaRichieste;

    }

    private List<TitolareImpresa> getResponsabilitaTitolare() {
        TitolareImpresa responsabilitaTitolare = new TitolareImpresa();
        responsabilitaTitolare.setCuaa("AAABBB11C22D333E");
        responsabilitaTitolare.setDenominazione("Pippo Pluto");
        List<TitolareImpresa> listResponsabilitaTitolare = Arrays.asList(responsabilitaTitolare);
        return listResponsabilitaTitolare;

    }

    private DatiAnagrafici getDatiAnagrafici() {
        DatiAnagrafici datiAnagrafici = new DatiAnagrafici();
        datiAnagrafici.setCodiceFiscale("AAABBB11C22D333E");
        datiAnagrafici.setCognome("Pippo");
        datiAnagrafici.setEmail("pippo@test.com");
        datiAnagrafici.setNome("Pluto");
        datiAnagrafici.setTelefono("111222");
        return datiAnagrafici;
    }

}
