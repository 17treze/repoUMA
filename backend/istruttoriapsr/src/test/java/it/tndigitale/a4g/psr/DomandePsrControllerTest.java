package it.tndigitale.a4g.psr;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4g.psr.business.persistence.repository.DomandePsrDao;
import it.tndigitale.a4g.psr.dto.CodiceMisureIntervento;
import it.tndigitale.a4g.psr.dto.DomandaPsr;
import it.tndigitale.a4g.psr.dto.Operazione;
import it.tndigitale.a4g.psr.dto.StatoDomanda;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithMockUser(username = "utente", roles = { "a4g.ags.domanda.psr.visualizza" })
public class DomandePsrControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private DomandePsrDao daoDomandePsr;
	
	@Test
	public void getDomandePsr() throws Exception {
		DomandaPsr domanda1 = new DomandaPsr();
	    domanda1.setCampagna(2018);
	    domanda1.setCuaa("CRLMRC71B01H330G");
	    domanda1.setNumeroDomanda(102030L);
	    domanda1.setDataPresentazione(LocalDate.now());
	    domanda1.setStato(StatoDomanda.IN_ISTRUTTORIA);
	    DomandaPsr domanda2 = new DomandaPsr();
	    domanda2.setCampagna(2019);
	    domanda2.setCuaa("CRLMRC71B01H330G");
	    domanda2.setNumeroDomanda(203040L);
	    domanda2.setDataPresentazione(LocalDate.now());
	    domanda2.setStato(StatoDomanda.IN_ISTRUTTORIA);
	    List<DomandaPsr> listaDomande = new ArrayList<DomandaPsr>();
	    listaDomande.add(domanda1);
	    listaDomande.add(domanda2);
		Mockito.when(daoDomandePsr.recuperaDomandePsr(ArgumentMatchers.eq("CRLMRC71B01H330G"))).thenReturn(listaDomande);
		
		Operazione op1 = new Operazione();
		op1.setCodiceMisureIntervento(CodiceMisureIntervento.M10_O1_1);
		Operazione op2 = new Operazione();
		op2.setCodiceMisureIntervento(CodiceMisureIntervento.M11_O1_1);
	    List<Operazione> listaOperazioni1 = new ArrayList<Operazione>();
	    listaOperazioni1.add(op1);
        listaOperazioni1.add(op2);
        
        Operazione op3 = new Operazione();
		op3.setCodiceMisureIntervento(CodiceMisureIntervento.M10_O1_3);
		Operazione op4 = new Operazione();
		op4.setCodiceMisureIntervento(CodiceMisureIntervento.M13_O1_1);
	    List<Operazione> listaOperazioni2 = new ArrayList<Operazione>();
	    listaOperazioni2.add(op3);
        listaOperazioni2.add(op4);
		
		Mockito.when(daoDomandePsr.recuperaMisureInterventoDomandaPsr(ArgumentMatchers.eq(102030L))).thenReturn(listaOperazioni1);
		Mockito.when(daoDomandePsr.recuperaMisureInterventoDomandaPsr(ArgumentMatchers.eq(203040L))).thenReturn(listaOperazioni2);
		
		this.mockMvc.perform(get("/api/v1//domande-psr/CRLMRC71B01H330G/consultazione")).andExpect(status().isOk())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].numeroDomanda").value(102030L))
		.andExpect(jsonPath("$[0].cuaa").value("CRLMRC71B01H330G"))
		.andExpect(jsonPath("$[0].campagna").value(2018))
		.andExpect(jsonPath("$[0].operazioni[0].codiceMisureIntervento").value(CodiceMisureIntervento.M10_O1_1.toString()))
		.andExpect(jsonPath("$[0].operazioni[1].codiceMisureIntervento").value(CodiceMisureIntervento.M11_O1_1.toString()))
		.andExpect(jsonPath("$[1].numeroDomanda").value(203040L))
		.andExpect(jsonPath("$[1].cuaa").value("CRLMRC71B01H330G"))
		.andExpect(jsonPath("$[1].campagna").value(2019))
		.andExpect(jsonPath("$[1].operazioni[0].codiceMisureIntervento").value(CodiceMisureIntervento.M10_O1_3.toString()))
		.andExpect(jsonPath("$[1].operazioni[1].codiceMisureIntervento").value(CodiceMisureIntervento.M13_O1_1.toString()));

	}
}
