package it.tndigitale.a4g.proxy.services;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import it.tndigitale.a4g.proxy.dto.persona.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.services.AnagrafeTributariaServiceImpl.AnagrafeTributariaWS;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.AopTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ws.test.client.MockWebServiceServer;
import static org.springframework.ws.test.client.RequestMatchers.anything;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class AnagrafeTributariaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AnagrafeTributariaServiceImpl anagrafeTributariaService;
    @SpyBean
    private AnagrafeTributariaWS anagrafeTributariaWS;

    private MockWebServiceServer mockServer;

    @Before
    public void init() {
        //mockServer = MockWebServiceServer.createServer(anagrafeTributariaWS);
        //https://github.com/spring-projects/spring-boot/issues/11920
        mockServer = MockWebServiceServer.createServer((AnagrafeTributariaWS) AopTestUtils.getUltimateTargetObject(anagrafeTributariaWS));
    }

    @Test
    public void ricercaAnagrafeTributaria() throws Exception {
        Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagrafetributaria/ricercaAnagrafeTributariaRequest_PersonaFisica.xml"));
        Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagrafetributaria/ricercaAnagrafeTributariaResponse_PersonaFisica.xml"));
        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
        mockMvc.perform(get("/api/v1/anagrafetributaria/TRRCST78B08C794X")).andExpect(content().string(containsString("TRRCST78B08C794X")));
        mockServer.verify();
    }
    
    @Test
    public void testRetryServizioAnagraficaTributaria() throws Exception {
        String codiceFiscale = "BBBNTA00A01Z109Y";

        for(int i = 0; i < 11; i++) {
            mockServer
                .expect(anything())
                .andRespond(withPayload(new StreamSource(new FileReader("src/test/resources/anagrafetributaria/ricercaAnagrafeTributariaResponseKO.xml"))));
        }

        mockMvc.perform(get("/api/v1/anagrafetributaria/BBBNTA00A01Z109Y"));

        Mockito
            .verify(anagrafeTributariaWS, times(10))
            .retryableCaricaAnagrafica(codiceFiscale);
        Mockito
            .verify(anagrafeTributariaWS, atLeastOnce())
            .recoverCaricaAnagrafica(any(Exception.class), eq(codiceFiscale));
    }
    
    @Test
    public void getAnagrafeTributaria_PersonaFisica() throws Exception {
        final String CODICE_FISCALE = "TRRCST78B08C794X";
        Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagrafetributaria/ricercaAnagrafeTributariaRequest_PersonaFisica.xml"));
        Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagrafetributaria/ricercaAnagrafeTributariaResponse_PersonaFisica.xml"));
        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
        
        String jsonResponseContent = new String(Files.readAllBytes(Paths.get("src/test/resources/anagrafetributaria/expectedGetPersonaFisica.json")));
        mockMvc.perform(get("/api/v2/anagrafetributaria/personafisica/" + CODICE_FISCALE))
        .andExpect(status().isOk())
        .andExpect(content().json(jsonResponseContent));
    }

    @Test
    public void ricercaAnagrafeTributaria_Deceduto() throws Exception {
        final String CODICE_FISCALE = "MRCRSN13B54B389G";
        Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagrafetributaria/deceduto/ricercaAnagrafeTributariaRequest_PersonaFisica_Deceduta.xml"));
        Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagrafetributaria/deceduto/ricercaAnagrafeTributariaResponse_PersonaFisica_Deceduta.xml"));
        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        PersonaFisicaDto persona = anagrafeTributariaService.getPersonaFisica(CODICE_FISCALE);
        assertEquals(CODICE_FISCALE, persona.getCodiceFiscale());
        assertEquals(Boolean.TRUE, persona.getDeceduta());
    }

    @Test
    public void ricercaAnagrafeTributaria_ImpresaIndividualeChiusa() throws Exception {
        final String CODICE_FISCALE = "RGNPLA67A63E783V";
        Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagrafetributaria/impresaindividuale/ricercaAnagrafeTributariaRequest_ImpresaIndividualeChiusa.xml"));
        Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagrafetributaria/impresaindividuale/ricercaAnagrafeTributariaResponse_ImpresaIndividualeChiusa.xml"));
        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        PersonaFisicaDto persona = anagrafeTributariaService.getPersonaFisica(CODICE_FISCALE);
        assertEquals(CODICE_FISCALE, persona.getCodiceFiscale());
        assertNull(persona.getImpresaIndividuale());
    }

    @Test
    public void ricercaAnagrafeTributaria_NonDeceduto() throws Exception {
        final String CODICE_FISCALE = "TRRCST78B08C794X";

        Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagrafetributaria/ricercaAnagrafeTributariaRequest_PersonaFisica.xml"));
        Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagrafetributaria/ricercaAnagrafeTributariaResponse_PersonaFisica.xml"));
        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        PersonaFisicaDto persona = anagrafeTributariaService.getPersonaFisica(CODICE_FISCALE);
        assertEquals(CODICE_FISCALE, persona.getCodiceFiscale());
        assertEquals(Boolean.FALSE, persona.getDeceduta());
    }
    
    @Test
    public void getAnagrafeTributaria_PersonaGiuridica() throws Exception {
        Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagrafetributaria/ricercaAnagrafeTributariaRequest_PersonaGiuridica.xml"));
        Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagrafetributaria/ricercaAnagrafeTributariaResponse_PersonaGiuridica.xml"));
        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
        
        String jsonResponseContent = new String(Files.readAllBytes(Paths.get("src/test/resources/anagrafetributaria/expectedGetPersonaGiuridica.json")));
        mockMvc.perform(get("/api/v2/anagrafetributaria/personagiuridica/00123890220"))
        .andExpect(status().isOk())
        .andExpect(content().json(jsonResponseContent))
        .andReturn();
        mockServer.verify();
    }

}
