package it.tndigitale.a4gistruttoria.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
@WithMockUser(username = "istruttore", roles = {
        "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
        "a4gistruttoria.pac.istruttoria.du.visualizza.utente", "a4gistruttoria.pac.istruttoria.edita"})
@Sql(scripts = {"/DomandaUnica/istruttoria/dati-istruttore_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/DomandaUnica/istruttoria/dati-istruttore_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ElencoLiquidazioneControllerTest extends TestCase {

    @Autowired
    private MockMvc mvc;

    @Test
    @Sql(scripts = {"/DomandaUnica/intersostegno/domanda_verbale_liquidazione_acs.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/intersostegno/domanda_verbale_liquidazione_acs_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testCaricaVerbaleLiquidazioneAccoppiatoSuperficie() throws Exception {
        String idElencoLiquidazione = "9999999";
        byte[] expectedContent = Files.readAllBytes(Paths.get("./src/test/resources/DomandaUnica/intersostegno/stampa/output/verbaleLiquidazioneAcs.pdf"));
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(ApiUrls.ELENCO_LIQUIDAZIONE_ISTRUTTORIA_DU_V1 + "/" + idElencoLiquidazione + "/verbale"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(expectedContent))
                
                .andReturn();
    }

    @Test
    @Sql(scripts = "/DomandaUnica/intersostegno/domanda_verbale_liquidazione_disaccoppiato_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/intersostegno/domanda_verbale_liquidazione_disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void testCaricaVerbaleLiquidazioneDisaccoppiato() throws Exception {
    	String idElencoLiquidazione = "9999999";
    	String url = ApiUrls.ELENCO_LIQUIDAZIONE_ISTRUTTORIA_DU_V1 + "/" + idElencoLiquidazione + ElencoLiquidazioneController.VERBALE_PATH;
    	byte[] expectedContent = Files.readAllBytes(Paths.get("./src/test/resources/DomandaUnica/intersostegno/stampa/output/verbaleLiquidazioneDisaccoppiato.pdf"));
    	this.mvc.perform(get(url))
    	        .andExpect(status().isOk())
    	        .andExpect(content().bytes(expectedContent))
    	        
    	        .andReturn();
    }

    @Test
    public void testCaricaVerbaleLiquidazioneAcz() throws Exception {
        //TODO test per stampa verbale acz Marco Dalla Torre
    }
}