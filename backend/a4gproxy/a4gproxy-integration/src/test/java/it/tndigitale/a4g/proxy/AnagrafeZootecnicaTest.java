package it.tndigitale.a4g.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4g.proxy.dto.zootecnia.*;
import it.tndigitale.a4g.proxy.services.AnagrafeZootecnicaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ws.test.client.MockWebServiceServer;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.FileReader;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class AnagrafeZootecnicaTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnagrafeZootecnicaService anagrafeService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockWebServiceServer mockServer;

    private final String MOCK_REPO_DIR = "src/test/resources/bdn/";

    private final String CONTEXT_CONTROLLER = "/api/v1/zootecnia";

    @Before
    public void init() {
        mockServer = MockWebServiceServer.createServer(anagrafeService);
    }


    @Test
    public void vaccheLatteTest() throws Exception {
        // MockServer
        Source requestPayload = new StreamSource(new FileReader(MOCK_REPO_DIR + "VACCA_LATTE_MONTANA_Request.xml"));
        Source responsePayload = new StreamSource(new FileReader(MOCK_REPO_DIR + "VACCA_LATTE_MONTANA_Response.xml"));
        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        // Mock API
        CapiAziendaPerInterventoFilter filter = new CapiCarneLattePerInterventoFilter();
        filter.setCampagna(2019);
        filter.setIntervento(InterventoCarneLatte.VACCA_LATTE_MONTANA);
        filter.setCuaa("PDRTTR69M30C794R");
        filter.setIdAllevamento(729337);

        String url = CONTEXT_CONTROLLER + "/" + filter.getCuaa() + "/" + filter.getCampagna();

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/capi/bovini/lattecarne")
                .param("intervento", filter.getIntervento().name())
                .param("idAllevamento", filter.getIdAllevamento().toString())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mockServer.verify();
    }

    @Test
    public void capiMacellatiTest() throws Exception {
        // MockServer
        Source requestPayload = new StreamSource(new FileReader(MOCK_REPO_DIR + "BOVINO_MACELLATO_Request.xml"));
        Source responsePayload = new StreamSource(new FileReader(MOCK_REPO_DIR + "BOVINO_MACELLATO_Response.xml"));
        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        // Mock API
        CapiAziendaPerInterventoFilter filter = new CapiMacellazionePerInterventoFilter();
        filter.setCampagna(2019);
        filter.setIntervento(InterventoMacellazione.BOVINO_MACELLATO);
        filter.setCuaa("PDRTTR69M30C794R");
        filter.setIdAllevamento(729337);

        String url = CONTEXT_CONTROLLER + "/" + filter.getCuaa() + "/" + filter.getCampagna();

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/capi/bovini/macellati")
                .param("intervento", filter.getIntervento().name())
                .param("idAllevamento", filter.getIdAllevamento().toString())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mockServer.verify();
    }

    @Test
    public void ovicapriniTest() throws Exception {
        // MockServer
        Source requestPayload = new StreamSource(new FileReader(MOCK_REPO_DIR + "AGNELLA_Request.xml"));
        Source responsePayload = new StreamSource(new FileReader(MOCK_REPO_DIR + "AGNELLA_Response.xml"));
        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        // Mock API
        CapiAziendaPerInterventoFilter filter = new CapiOvicapriniPerInterventoFilter();
        filter.setCampagna(2019);
        filter.setIntervento(InterventoOvicaprino.AGNELLA);
        filter.setCuaa("PDRTTR69M30C794R");
        filter.setIdAllevamento(729337);

        String url = CONTEXT_CONTROLLER + "/" + filter.getCuaa() + "/" + filter.getCampagna();

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/capi/ovicaprini")
                .param("intervento", filter.getIntervento().name())
                .param("idAllevamento", filter.getIdAllevamento().toString())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mockServer.verify();
    }
}
