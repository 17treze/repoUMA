package it.tndigitale.a4gistruttoria;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

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
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.istruttoria.IstruttoriaDomandaUnica;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.specification.RiservaNazionaleEnum;
import it.tndigitale.a4gistruttoria.repository.specification.YesNoEnum;
import it.tndigitale.a4gistruttoria.util.JsonSupport;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.istruttoria.du.visualizza.tutti"})
public class IstruttoriaDomandaUnicaRestTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private RestTemplate restTemplate;

	@Test
	public void getIstruttoriePerTipoOk() throws Exception {
		TipoIstruttoria tipo = TipoIstruttoria.SALDO;
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("tipo", tipo.name()))
			//.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*].tipo", everyItem(is(tipo.name()))));
	}

	@Test
	public void getIstruttoriePerStatoRichiesto() throws Exception {
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("stato", StatoIstruttoria.RICHIESTO.name()))
			//.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*].stato", everyItem(is(StatoIstruttoria.RICHIESTO.name()))));
	}

	@Test
	public void getIstruttoriePerCampagnaOk() throws Exception {
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", "2018"))
			//.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*].domanda.campagna", everyItem(is(2018))));
	}

	@Test
	public void getIstruttoriePerSostegnoOk() throws Exception {
		Sostegno sostegno = Sostegno.DISACCOPPIATO;
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("sostegno", sostegno.name()))
			//.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*].sostegno", everyItem(is(sostegno.name()))));
	}

	@Test
	public void getIstruttorieConElencoLiquidazione() throws Exception {
		String cuaa = "CODELENC11111ACZ";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("cuaa", cuaa))
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*].domanda.cuaaIntestatario", everyItem(is(cuaa))))
			.andExpect(jsonPath("$.risultati[*].elencoLiquidazione").isNotEmpty())
			.andExpect(jsonPath("$.risultati[*].elencoLiquidazione.codElenco").isNotEmpty());
	}

	@Test
	public void getIstruttorie_Campagna_Sostegno_StatoTipo_Results() throws Exception {
		Sostegno sostegno = Sostegno.DISACCOPPIATO;
		TipoIstruttoria tipo = TipoIstruttoria.SALDO;
		Short campagna = 2018;
		StatoIstruttoria stato = StatoIstruttoria.LIQUIDABILE;
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", campagna.toString())
				.param("sostegno", sostegno.name())
				.param("stato", stato.name())
				.param("tipo", tipo.name())
			)
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*].domanda.campagna", everyItem(is(2018))))
			.andExpect(jsonPath("$.risultati[*].sostegno", everyItem(is(sostegno.name()))))
			.andExpect(jsonPath("$.risultati[*].stato", everyItem(is(stato.name()))))
			.andExpect(jsonPath("$.risultati[*].tipo", everyItem(is(tipo.name()))));
	}

	@Test
	public void getIstruttorie_Campagna_Sostegno_StatoRichiestoPaginataTipo_Results() throws Exception {
		Sostegno sostegno = Sostegno.DISACCOPPIATO;
		TipoIstruttoria tipo = TipoIstruttoria.SALDO;
		Short campagna = 2018;
		StatoIstruttoria stato = StatoIstruttoria.RICHIESTO;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", campagna.toString())
				.param("sostegno", sostegno.name())
				.param("stato", stato.name())
				.param("tipo", tipo.name())
				.param("numeroElementiPagina", count)
			)
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*].domanda.campagna", everyItem(is(2018))))
			.andExpect(jsonPath("$.risultati[*].sostegno", everyItem(is(sostegno.name()))))
			.andExpect(jsonPath("$.risultati[*].stato", everyItem(is(stato.name()))))
			.andExpect(jsonPath("$.risultati[*].tipo", everyItem(is(tipo.name()))))
			.andExpect(jsonPath("$.risultati[*]", hasSize(5)));
	}

	@Test
	public void getIstruttorie_Campagna_SostegnoStatoRichiesto_Paginata_OrdinataTipo_Results() throws Exception {
		Sostegno sostegno = Sostegno.DISACCOPPIATO;
		TipoIstruttoria tipo = TipoIstruttoria.SALDO;
		Short campagna = 2018;
		StatoIstruttoria stato = StatoIstruttoria.RICHIESTO;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", campagna.toString())
				.param("sostegno", sostegno.name())
				.param("stato", stato.name())
				.param("tipo", tipo.name())
				.param("numeroElementiPagina", count)
				.param("proprieta", "domandaUnicaModel.cuaaIntestatario")
				.param("ordine", "DESC")
			)
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*].domanda.campagna", everyItem(is(2018))))
			.andExpect(jsonPath("$.risultati[*].sostegno", everyItem(is(sostegno.name()))))
			.andExpect(jsonPath("$.risultati[*].stato", everyItem(is(stato.name()))))
			.andExpect(jsonPath("$.risultati[*].tipo", everyItem(is(tipo.name()))))
			.andExpect(jsonPath("$.risultati[*]", hasSize(5)));
	}

	@Test
	public void getIstruttorie_Cuaa_NumeroDomanda_Pascoli_NoResults() throws Exception {
		String numeroDomanda = "183338";
		YesNoEnum isPascolo = YesNoEnum.YES;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("pascoli", isPascolo.toString())
				.param("numeroElementiPagina", count)
				.param("proprieta", "domandaUnicaModel.cuaaIntestatario")
				.param("ordine", "DESC")
			)
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(0)));
	}

	@Test
	public void getIstruttorie_Cuaa_NumeroDomanda_Pascoli_Results() throws Exception {
		String numeroDomanda = "1183430";
		Short campagna = 2018;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", campagna.toString())
				.param("numeroDomanda", numeroDomanda)
				.param("pascoli", YesNoEnum.YES.toString())
				.param("numeroElementiPagina", count)
				.param("proprieta", "domandaUnicaModel.cuaaIntestatario")
				.param("ordine", "DESC")
			)
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}
	
	@Test
	public void getIstruttorie_Istruttoria_NoPascoli_Results() throws Exception {
		String numeroDomanda = "183338";
		Short campagna = 2018;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", campagna.toString())
				.param("numeroDomanda", numeroDomanda)
				.param("pascoli", YesNoEnum.NO.toString())
				.param("numeroElementiPagina", count)
				.param("proprieta", "domandaUnicaModel.cuaaIntestatario")
				.param("ordine", "DESC")
			)
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}
	
	@Test
	public void getIstruttorie_Cuaa_NumeroDomanda_RagioneSociale_Results() throws Exception {
		String cuaaIntestatario = "MNGDNE71D07L781V";
		String numeroDomanda = "183175";
		String count = "5";
		String ragioneSociale = "MENGARDA DENI";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("cuaa", cuaaIntestatario)
				.param("numeroDomanda", numeroDomanda)
				.param("numeroElementiPagina", count)
				.param("ragioneSociale", ragioneSociale)
				.param("proprieta", "domandaUnicaModel.cuaaIntestatario")
				.param("sostegno", Sostegno.DISACCOPPIATO.name())
				.param("ordine", "DESC")
			)
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}
	
	@Test
	public void getIstruttorie_Cuaa_NumeroDomanda_RagioneSocialeLike_Results() throws Exception {
		String cuaaIntestatario = "NGDNE71D07L781";
		String numeroDomanda = "183175";
		String count = "5";
		String ragioneSociale = "ENGARDA DEN";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("cuaa", cuaaIntestatario)
				.param("numeroDomanda", numeroDomanda)
				.param("numeroElementiPagina", count)
				.param("ragioneSociale", ragioneSociale)
				.param("proprieta", "domandaUnicaModel.cuaaIntestatario")
				.param("sostegno", Sostegno.DISACCOPPIATO.name())
				.param("ordine", "DESC")
			)
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}
	
	@Test
	public void getIstruttorie_Cuaa_NumeroDomanda_RagioneSociale_NoResults() throws Exception {
		String cuaaIntestatario = "MNGDNE71D07L781V 1";
		String numeroDomanda = "183175";
		String count = "5";
		String ragioneSociale = "MENGARDA DENI";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("cuaa", cuaaIntestatario)
				.param("numeroDomanda", numeroDomanda)
				.param("numeroElementiPagina", count)
				.param("ragioneSociale", ragioneSociale)
				.param("proprieta", "domandaUnicaModel.cuaaIntestatario")
				.param("ordine", "DESC")
			)
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(0)));
	}
	
	@Test
	public void getIstruttorie_Cuaa_NumeroDomanda_Bloccata_Results() throws Exception {
		String cuaaIntestatario = "MNGDNE71D07L781V";
		String numeroDomanda = "183175";
		YesNoEnum isBloccata = YesNoEnum.NO;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("cuaa", cuaaIntestatario)
				.param("numeroDomanda", numeroDomanda)
				.param("istruttoriaBloccata", isBloccata.toString())
				.param("numeroElementiPagina", count)
				.param("sostegno", Sostegno.DISACCOPPIATO.name())
				.param("proprieta", "domandaUnicaModel.cuaaIntestatario")
				.param("ordine", "DESC")
			)
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}
	
	@Test
	public void getIstruttorie_Cuaa_NumeroDomanda_Bloccata_NoResults() throws Exception {
		String cuaaIntestatario = "MNGDNE71D07L781V";
		String numeroDomanda = "183175";
		YesNoEnum isBloccata = YesNoEnum.YES;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("cuaa", cuaaIntestatario)
				.param("numeroDomanda", numeroDomanda)
				.param("istruttoriaBloccata", isBloccata.toString())
				.param("numeroElementiPagina", count)
				.param("proprieta", "domandaUnicaModel.cuaaIntestatario")
				.param("ordine", "DESC")
			)
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(0)));
	}
	
	@Test
	public void getIstruttorie_Cuaa_NumeroDomanda_ErroreCalcolo_Results() throws Exception {
		String cuaaIntestatario = "MNGDNE71D07L781V";
		String numeroDomanda = "183175";
		YesNoEnum isErroreCalcolo = YesNoEnum.NO;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("cuaa", cuaaIntestatario)
				.param("numeroDomanda", numeroDomanda)
				.param("erroreCalcolo", isErroreCalcolo.toString())
				.param("numeroElementiPagina", count)
				.param("sostegno", Sostegno.DISACCOPPIATO.name())
				.param("proprieta", "domandaUnicaModel.cuaaIntestatario")
				.param("ordine", "DESC")
			)
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}
	
	@Test
	public void getIstruttorie_Cuaa_NumeroDomanda_ErroreCalcolo_NoResults() throws Exception {
		String cuaaIntestatario = "MNGDNE71D07L781V";
		String numeroDomanda = "183175";
		YesNoEnum isErroreCalcolo = YesNoEnum.YES;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("cuaa", cuaaIntestatario)
				.param("numeroDomanda", numeroDomanda)
				.param("erroreCalcolo", isErroreCalcolo.toString())
				.param("numeroElementiPagina", count)
				.param("proprieta", "domandaUnicaModel.cuaaIntestatario")
				.param("ordine", "DESC"))
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(0)));
	}

	@Test
	public void getIstruttorie_Giovane_Results() throws Exception {
		String numeroDomanda = "183175";
		YesNoEnum isGiovane = YesNoEnum.YES;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("giovane", isGiovane.toString())
				.param("sostegno", Sostegno.DISACCOPPIATO.name())
				.param("numeroElementiPagina", count))
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}
	
	@Test
	public void getIstruttorie_NotGiovane_Empty_Results() throws Exception {
		String numeroDomanda = "183175";
		YesNoEnum isGiovane = YesNoEnum.NO;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("giovane", isGiovane.toString())
				.param("numeroElementiPagina", count))
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(0)));
	}
	
	@Test
	public void getIstruttorie_Campione_Results() throws Exception {
		String numeroDomanda = "183175";
		YesNoEnum isCampione = YesNoEnum.YES;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("sostegno", Sostegno.DISACCOPPIATO.name())
				.param("campione", isCampione.toString())
				.param("numeroElementiPagina", count))
			// .andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}
	
	@Test
	public void getIstruttorie_NotCampione_NoResults() throws Exception {
		String numeroDomanda = "183175";
		YesNoEnum isCampione = YesNoEnum.NO;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("campagna", "2019")
				.param("campione", isCampione.toString())
				.param("numeroElementiPagina", count))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(0)));
	}
	
	@Test
	public void getIstruttorie_NotCampione_Results() throws Exception {
		String numeroDomanda = "183109";
		YesNoEnum isCampione = YesNoEnum.NO;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("campione", isCampione.toString())
				.param("numeroElementiPagina", count))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(2))); //c'è una istruttoria di saldo e una di anticipo
	}

	@Test
	public void getIstruttorie_RiservaNazionale_ProvvedimentiAmministrativi_Results() throws Exception {
		String numeroDomanda = "183175";
		RiservaNazionaleEnum riservaNazionaleFilter = RiservaNazionaleEnum.B_NUOVO_AGRICOLTORE;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("riservaNazionale", riservaNazionaleFilter.toString())
				.param("sostegno", Sostegno.DISACCOPPIATO.name())
				.param("numeroElementiPagina", count))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}
	
	@Test
	public void getIstruttorie_RiservaNazionale_GiovaneAgricoltore_Results() throws Exception {
		String numeroDomanda = "183338";
		RiservaNazionaleEnum riservaNazionaleFilter = RiservaNazionaleEnum.A_GIOVANE_AGRICOLTORE;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("riservaNazionale", riservaNazionaleFilter.toString())
				.param("numeroElementiPagina", count))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}

	@Test
	public void getIstruttorie_RiservaNazionale_NoResults() throws Exception {
		String numeroDomanda = "183175";
		RiservaNazionaleEnum riservaNazionaleFilter = RiservaNazionaleEnum.A_GIOVANE_AGRICOLTORE;
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("riservaNazionale", riservaNazionaleFilter.toString())
				.param("numeroElementiPagina", count))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(0)));
	}
	
	@Test
	public void getIstruttorie_Istruttoria_Bloccata_Results() throws Exception {
		String numeroDomanda = "180375";
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("istruttoriaBloccata", YesNoEnum.YES.toString())
				.param("numeroElementiPagina", count))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}
	
	@Test
	public void getIstruttorie_Istruttoria_NoBloccata_NoResults() throws Exception {
		String numeroDomanda = "183175";
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("istruttoriaBloccata", YesNoEnum.YES.toString())
				.param("numeroElementiPagina", count))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(0)));
	}
	
	@Test
	public void getIstruttorie_Istruttoria_NoBloccata_Results() throws Exception {
		String numeroDomanda = "180375";
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("istruttoriaBloccata", YesNoEnum.NO.toString())
				.param("numeroElementiPagina", count))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(0)));
	}
	
	
	@Test
	public void getIstruttorie_Istruttoria_Per_Intervento_Results() throws Exception {
		String numeroDomanda = "1805239";
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("interventi", "026, 027") //M10 proteaginose
				.param("numeroElementiPagina", count))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}
	
	@Test
	public void getIstruttorie_Istruttoria_Per_Intervento_NoResults() throws Exception {
		String numeroDomanda = "1805239";
		String count = "5";
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("numeroDomanda", numeroDomanda)
				.param("interventi", "027") //M10 proteaginose
				.param("numeroElementiPagina", count))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(0)));
	}
	
	
	@Test
	public void countIstruttorie() throws Exception {
		TipoIstruttoria tipo = TipoIstruttoria.SALDO;
		final MvcResult mvcResult = this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("tipo", tipo.name()))
			.andExpect(status().isOk())
			.andReturn();
		RisultatiPaginati<IstruttoriaDomandaUnica> results = JsonSupport.toObject(
				mvcResult.getResponse().getContentAsString(), RisultatiPaginati.class);
		this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1 + "/count")				
				.param("tipo", tipo.name()))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(results.getCount().toString())));
	}
	
	@Test
	public void getIstruttorieById() throws Exception {
		TipoIstruttoria tipo = TipoIstruttoria.SALDO;
		final MvcResult mvcResult = this.mockMvc.perform(
				get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("tipo", tipo.name()))
			.andExpect(status().isOk())
			.andReturn();
		RisultatiPaginati<HashMap<String, Object>> results = JsonSupport.toObject(mvcResult.getResponse().getContentAsString(), (new RisultatiPaginati<IstruttoriaDomandaUnica>()).getClass());
		results.getRisultati().forEach(istr -> {
			try {
				this.mockMvc.perform(
						get(ApiUrls.ISTRUTTORIE_DU_V1 + "/" +istr.get("id")))			
					.andExpect(status().is(200));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
}
