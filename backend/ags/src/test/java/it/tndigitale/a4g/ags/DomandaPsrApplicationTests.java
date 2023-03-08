package it.tndigitale.a4g.ags;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.ags.dto.DomandaPsr;
import it.tndigitale.a4g.ags.model.DomandeCollegatePsrFilter;
import it.tndigitale.a4g.ags.repository.dao.DomandaDao;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase
public class DomandaPsrApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private DomandaDao daoDomanda;
	@Autowired
	private ObjectMapper objectMapper;

	// @Test
	// @WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_PSR_COD })
	public void getDatiStrutturali() throws Exception {
		List<DomandaPsr> domandePsr = new ArrayList<>();
		domandePsr.add(new DomandaPsr("PSR_2018", 2018, "TRRCST78B08C794X", "TORRESANI CRISTIAN", 189479l, new Date(), "P22014", "PROTOCOLLATA", BigDecimal.TEN));
		DomandeCollegatePsrFilter domandeCollegatePsrFilter = new DomandeCollegatePsrFilter();
		ArrayList<String> cuaaList = new ArrayList<>();
		ArrayList<Integer> anni = new ArrayList<>();
		cuaaList.add("TRRCST78B08C794X");
		anni.add(2018);
		domandeCollegatePsrFilter.setCuaa(cuaaList);
		domandeCollegatePsrFilter.setAnniCampagna(anni);
		String params = objectMapper.writeValueAsString(domandeCollegatePsrFilter);
		Mockito.doReturn(domandePsr).when(daoDomanda).getDomandePsr(domandeCollegatePsrFilter);
		this.mockMvc.perform(get("/api/v1/domandePSR").param("params", "{\"cuaa\":[\"TRRCST78B08C794X\"],\"dataPresentazione\":\"19/11/2017\",\"importo\":0,\"campagna\":2018}"))
				.andExpect(status().isOk()).andExpect(content().string(containsString("TRRCST78B08C794X")));
	}
}
