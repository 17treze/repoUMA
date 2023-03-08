package it.tndigitale.a4g.ags;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.tndigitale.a4g.ags.repository.dao.DomandaDaoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4g.ags.dto.DomandaPsr;
import it.tndigitale.a4g.ags.model.DomandeCollegatePsrFilter;
import it.tndigitale.a4g.ags.repository.dao.DomandaDao;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class DomandaPSRSicurezzaIntegrationTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private DomandaDaoImpl daoDomanda;

	@Test
	public void getDatiStrutturaliNonAutenticato403() throws Exception {
		List<DomandaPsr> domandePsr = new ArrayList<>();
		DomandeCollegatePsrFilter domandeCollegatePsrFilter = new DomandeCollegatePsrFilter();
		domandeCollegatePsrFilter.setCuaa(Arrays.asList("TRRCST78B08C794X"));
		domandeCollegatePsrFilter.setAnniCampagna(Arrays.asList(2018));
		Calendar c = Calendar.getInstance();
		c.set(2017, 11, 19);
		domandeCollegatePsrFilter.setDataPresentazione(c.getTime());
		domandeCollegatePsrFilter.setImporto(BigDecimal.ZERO);

		domandePsr.add(new DomandaPsr("PSR_2018", 2018, "TRRCST78B08C794X", "TORRESANI CRISTIAN", 189479l, new Date(), "P22014", "PROTOCOLLATA", BigDecimal.TEN));
		Mockito.doReturn(domandePsr).when(daoDomanda).getDomandePsr(domandeCollegatePsrFilter);
		this.mockMvc.perform(get("/api/v1/domandePSR").param("params", "{\"cuaa\":[\"TRRCST78B08C794X\"],\"dataPresentazione\":\"19/11/2017\",\"importo\":0,\"campagna\":2018}"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "utenteAppag")
	public void getDatiStrutturaliNonAbilitato403() throws Exception {
		DomandeCollegatePsrFilter domandeCollegatePsrFilter = new DomandeCollegatePsrFilter();
		domandeCollegatePsrFilter.setCuaa(Arrays.asList("TRRCST78B08C794X"));
		domandeCollegatePsrFilter.setAnniCampagna(Arrays.asList(2018));
		Calendar c = Calendar.getInstance();
		c.set(2017, 11, 19);
		domandeCollegatePsrFilter.setDataPresentazione(c.getTime());
		domandeCollegatePsrFilter.setImporto(BigDecimal.ZERO);

		List<DomandaPsr> domandePsr = new ArrayList<>();
		domandePsr.add(new DomandaPsr("PSR_2018", 2018, "TRRCST78B08C794X", "TORRESANI CRISTIAN", 189479l, new Date(), "P22014", "PROTOCOLLATA", BigDecimal.TEN));
		Mockito.doReturn(domandePsr).when(daoDomanda).getDomandePsr(domandeCollegatePsrFilter);
		this.mockMvc.perform(get("/api/v1/domandePSR").param("params", "{\"cuaa\":[\"TRRCST78B08C794X\"],\"dataPresentazione\":\"19/11/2017\",\"importo\":0,\"campagna\":2018}"))
				.andExpect(status().isForbidden());
	}
}
