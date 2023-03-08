package it.tndigitale.a4gutente.api;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4gutente.dto.Profilo;
import it.tndigitale.a4gutente.utility.JsonSupport;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfiloControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;

	@Test
    @WithMockUser("UTENTEAPPAG")
	public void listaProfiliRestituisceElencoNonVuoto() throws Exception {
		
		ResultActions resultActions = performGetProfili();
		
		final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk())
					 							 .andExpect(content().string(containsString("appag")))
					 							 .andReturn();

		List<Profilo> profili = JsonSupport.toList(mvcResult.getResponse().getContentAsString(), Profilo[].class);

		assertThat(profili).isNotEmpty();
		assertThat(profili).extracting("haRuoli").contains(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
	}
	
	protected ResultActions performGetProfili() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(ApiUrls.PROFILI_V1));
		return resultActions;
	}

}
