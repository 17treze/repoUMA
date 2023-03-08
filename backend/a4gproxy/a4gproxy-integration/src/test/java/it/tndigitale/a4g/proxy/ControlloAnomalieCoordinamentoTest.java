package it.tndigitale.a4g.proxy;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import javax.annotation.Resource;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.ControlloAnomalieCoordinamentoDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@SqlGroup({ 
	@Sql({ "classpath:sincronizzazione/schema.sql" }),
	@Sql({ "classpath:sincronizzazione/data.sql" })
})
@WithMockUser
public class ControlloAnomalieCoordinamentoTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Resource
	private ControlloAnomalieCoordinamentoDao dao;

	@Test
	public void getAnomalieReturns0() throws IOException, Exception {
		String anomalie = this.mockMvc.perform(get("/api/v1/anomaliecoordinamento/1?annoCampagna=2017"))
				.andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		assertEquals("0", anomalie);
	}

	@Test
	public void getAnomalieReturns1() throws IOException, Exception {
		String anomalie = this.mockMvc.perform(get("/api/v1/anomaliecoordinamento/1?annoCampagna=2018"))
				.andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		assertEquals("1", anomalie);
	}
}
