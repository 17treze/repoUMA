package it.tndigitale.a4g.proxy.services.ioitalia;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4g.proxy.dto.ioitalia.ComunicationDto;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WithMockUser
public class IoItaliaServiceTest {
    @SpyBean
    private IoItaliaService ioItaliaService;
    
    @Autowired
	private MockMvc mockMvc;

    private final String OGGETTO_MESSAGGIO_OK = "Comunicazione da sistema informativo agricoltura";

    private final String TESTO_MESSAGGIO_OK = "Buongiorno questa è una prova di comunicazione che è staata inviata per conto dell'Agenzia provinciale per i pagamenti";

    private final String OGGETTO_MESSAGGIO_BREVE = "Breve";

    private final String TESTO_MESSAGGIO_BREVE = "Breve";

    private final String CF_WHITE_LIST = "MRTLNZ72M30L378I";

    private final String CF_NO_WHITE_LIST = "TLLGFL72L10H224D";

    private final String CF_BREVE = "MRTVLR";

    @Test
    public void INVIA_MESSAGGIO() throws Exception {
        ComunicationDto comunication = new ComunicationDto();
        comunication.setCodiceFiscale(CF_WHITE_LIST)
                .setOggetto(OGGETTO_MESSAGGIO_OK)
                .setMessaggio(TESTO_MESSAGGIO_OK);
//      this.mockMvc.perform(get("/api/v1/ioitalia/invia-messaggio"));
//		Mockito.when(ioItaliaService.sendComunication(comunication)).thenReturn(Mockito.anyString());
//
//      Mockito.verify(ioItaliaService, atLeastOnce()).sendComunication(comunication);
    }

    @Test(expected = RuntimeException.class)
    public void FALLISCE_MESSAGGIO_CODICE_FISCALE_NON_WHITELIST() {
        ComunicationDto comunication = new ComunicationDto();
        comunication.setCodiceFiscale(CF_NO_WHITE_LIST)
                .setOggetto(OGGETTO_MESSAGGIO_OK)
                .setMessaggio(TESTO_MESSAGGIO_OK);
        Mockito.when(ioItaliaService.sendComunication(comunication)).thenThrow(RuntimeException.class);
    }

    @Test(expected = RuntimeException.class)
    public void FALLISCE_MESSAGGIO_OGGETTO_BREVE() {
        ComunicationDto comunication = new ComunicationDto();
        comunication.setCodiceFiscale(CF_WHITE_LIST)
                .setOggetto(OGGETTO_MESSAGGIO_BREVE)
                .setMessaggio(TESTO_MESSAGGIO_OK);
        Mockito.when(ioItaliaService.sendComunication(comunication)).thenThrow(RuntimeException.class);
    }

    @Test(expected = RuntimeException.class)
    public void FALLISCE_MESSAGGIO_TESTO_BREVE() {
        ComunicationDto comunication = new ComunicationDto();
        comunication.setCodiceFiscale(CF_WHITE_LIST)
                .setOggetto(OGGETTO_MESSAGGIO_OK)
                .setMessaggio(TESTO_MESSAGGIO_BREVE);
        Mockito.when(ioItaliaService.sendComunication(comunication)).thenThrow(RuntimeException.class);
    }

    @Test(expected = RuntimeException.class)
    public void FALLISCE_MESSAGGIO_CODICE_FISCALE_ERRATO() {
        ComunicationDto comunication = new ComunicationDto();
        comunication.setCodiceFiscale(CF_BREVE)
                .setOggetto(OGGETTO_MESSAGGIO_OK)
                .setMessaggio(TESTO_MESSAGGIO_OK);
        Mockito.when(ioItaliaService.sendComunication(comunication)).thenThrow(RuntimeException.class);
    }

    @Test(expected = RuntimeException.class)
    public void FALLISCE_MESSAGGIO_CF_NOWHITELIST() {
        ComunicationDto comunication = new ComunicationDto();
        comunication.setCodiceFiscale(CF_NO_WHITE_LIST)
                .setOggetto(OGGETTO_MESSAGGIO_OK)
                .setMessaggio(TESTO_MESSAGGIO_OK);
        Mockito.when(ioItaliaService.sendComunication(comunication)).thenThrow(RuntimeException.class);
    }
}
