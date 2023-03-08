package it.tndigitale.a4g.fascicolo.anagrafica.legacy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.Carica;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.CaricaAgsDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;


import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.FascicoloAgsDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.PersonaService;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class PersonaControllerTest {

	@MockBean
	private FascicoloAgsDao fascicoloAgsDao;
	@Autowired
	private PersonaService personaService;

	@Test
	void getResponsabilita() {
		String CODICE_FISCALE = "MSTFBA79L10H612L";
		List<CaricaAgsDto> respList = new ArrayList<>();
		CaricaAgsDto resp = new CaricaAgsDto();
		resp.setCuaa(CODICE_FISCALE);
		resp.setDenominazione("FABIO MAESTRANZI");
		resp.setCarica(Carica.TITOLARE);
		respList.add(resp);
		Mockito.when(fascicoloAgsDao.getCariche(Mockito.any(),Mockito.any())).thenReturn(respList);

		List<CaricaAgsDto> fascicoloList = personaService.getCariche(Mockito.eq(CODICE_FISCALE), Mockito.any());
		CaricaAgsDto carica = fascicoloList.get(0);
		assertEquals(carica.getCuaa(), CODICE_FISCALE);
	}
}
