package it.tndigitale.a4gistruttoria.api.configurazioneistruttoria;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.DatiConfigurazioneAccoppiati;
import it.tndigitale.a4gistruttoria.dto.InterventoDto;
import it.tndigitale.a4gistruttoria.dto.InterventoDuPremio;
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttoriaDisaccoppiatoDto;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
		"a4gistruttoria.pac.istruttoria.du.visualizza.utente", "a4gistruttoria.pac.istruttoria.edita" })
public class 	ConfigurazioneIstruttoriaSuperficieControllerTest {
	@Autowired private MockMvc mvc;
	@Autowired private MapperWrapper mapperWrapper;

	@Test
	public void getConfIstruttoriaAcs() throws Exception {
		mvc.perform(get(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/acs/2018")).andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.configurazioneInstruttoriaZootecnicaFilter").isEmpty())
				.andExpect(jsonPath("$.interventoDuPremi").isArray())
				.andExpect(jsonPath("$.interventoDuPremi", hasSize(9)))
				.andExpect(jsonPath("$.interventoDuPremi[*].intervento.codiceAgea",
						containsInAnyOrder("122", "124", "123", "125", "128", "129", "132", "138", "126")));
	}

	@Test
	public void saveConfIstruttoriaAcs() throws Exception {
		DatiConfigurazioneAccoppiati conf = new DatiConfigurazioneAccoppiati();
		conf.setConfigurazioneInstruttoriaZootecnicaFilter(null);
		List<InterventoDuPremio> interventi = this.dataPreparationAcs();
		conf.setInterventoDuPremi(interventi);
		mvc.perform(post(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/acs/2019").content(mapperWrapper.asJsonString(conf))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.interventoDuPremi[*].id").exists())
				.andExpect(jsonPath("$.configurazioneInstruttoriaZootecnicaFilter").isEmpty())
				.andExpect(jsonPath("$.interventoDuPremi").isArray())
				.andExpect(jsonPath("$.interventoDuPremi", hasSize(8)))
				.andExpect(jsonPath("$.interventoDuPremi[*].intervento.codiceAgea",
						containsInAnyOrder("122", "123", "124", "125", "128", "129", "132", "138")))
				.andExpect(jsonPath("$.interventoDuPremi[*].valoreUnitarioIntervento", hasItem(20.40)));
		;
	}

	@Test(expected = NestedServletException.class)
	public void saveConfIstruttoriaAcsError() throws Exception {
		ConfIstruttoriaDisaccoppiatoDto conf = new ConfIstruttoriaDisaccoppiatoDto();
		conf.setCampagna(2101);
		conf.setPercentualeIncrementoGiovane(0.1);
		conf.setPercentualeIncrementoGreening(0.2);
		conf.setLimiteGiovane(1.0);
		conf.setPercentualeRiduzioneLineareArt51Par2(1.0);
		conf.setPercentualeRiduzioneLineareArt51Par3(1.1);
		conf.setPercentualeRiduzioneLineareMassimaleNetto(1.2);
		conf.setPercentualeRiduzioneLineareMassimaleNetto(1.3);
		conf.setPercentualeRiduzioneTitoli(1.4);
		mvc.perform(post(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/disaccoppiato/2102").content(mapperWrapper.asJsonString(conf))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	private List<InterventoDuPremio> dataPreparationAcs() {
		InterventoDto interventoDto122 = new InterventoDto();
		interventoDto122.setCodiceAgea("122");
		InterventoDto interventoDto123 = new InterventoDto();
		interventoDto123.setCodiceAgea("123");
		InterventoDto interventoDto124 = new InterventoDto();
		interventoDto124.setCodiceAgea("124");
		InterventoDto interventoDto125 = new InterventoDto();
		interventoDto125.setCodiceAgea("125");
		// Intervento intervento126 = new Intervento();
		// intervento126.setCodiceAgea("126");
		InterventoDto interventoDto128 = new InterventoDto();
		interventoDto128.setCodiceAgea("128");
		InterventoDto interventoDto129 = new InterventoDto();
		interventoDto129.setCodiceAgea("129");
		InterventoDto interventoDto132 = new InterventoDto();
		interventoDto132.setCodiceAgea("132");
		InterventoDto interventoDto138 = new InterventoDto();
		interventoDto138.setCodiceAgea("138");

		InterventoDuPremio interventoDuPremio122 = new InterventoDuPremio();
		interventoDuPremio122.setIntervento(interventoDto122);
		interventoDuPremio122.setValoreUnitarioIntervento(BigDecimal.valueOf(20.40));
		InterventoDuPremio interventoDuPremio123 = new InterventoDuPremio();
		interventoDuPremio123.setIntervento(interventoDto123);
		interventoDuPremio123.setValoreUnitarioIntervento(BigDecimal.valueOf(40.21));
		InterventoDuPremio interventoDuPremio124 = new InterventoDuPremio();
		interventoDuPremio124.setIntervento(interventoDto124);
		interventoDuPremio124.setValoreUnitarioIntervento(BigDecimal.valueOf(60.42));
		InterventoDuPremio interventoDuPremio125 = new InterventoDuPremio();
		interventoDuPremio125.setIntervento(interventoDto125);
		interventoDuPremio125.setValoreUnitarioIntervento(BigDecimal.valueOf(80.24));
		// InterventoDuPremio interventoDuPremio126 = new InterventoDuPremio();
		// interventoDuPremio126.setIntervento(intervento126);
		// interventoDuPremio126.setValoreUnitarioIntervento(BigDecimal.valueOf(100.4));
		// interventoDuPremio126.setId(Long.parseLong("2970344"));
		InterventoDuPremio interventoDuPremio128 = new InterventoDuPremio();
		interventoDuPremio128.setIntervento(interventoDto128);
		interventoDuPremio128.setValoreUnitarioIntervento(BigDecimal.valueOf(120.2));
		InterventoDuPremio interventoDuPremio129 = new InterventoDuPremio();
		interventoDuPremio129.setIntervento(interventoDto129);
		interventoDuPremio129.setValoreUnitarioIntervento(BigDecimal.valueOf(140.63));
		InterventoDuPremio interventoDuPremio132 = new InterventoDuPremio();
		interventoDuPremio132.setIntervento(interventoDto132);
		interventoDuPremio132.setValoreUnitarioIntervento(BigDecimal.valueOf(160.22));
		InterventoDuPremio interventoDuPremio138 = new InterventoDuPremio();
		interventoDuPremio138.setIntervento(interventoDto138);
		interventoDuPremio138.setValoreUnitarioIntervento(BigDecimal.valueOf(110.11));

		List<InterventoDuPremio> interventi = new ArrayList<InterventoDuPremio>();
		interventi.add(interventoDuPremio122);
		interventi.add(interventoDuPremio123);
		interventi.add(interventoDuPremio124);
		interventi.add(interventoDuPremio125);
		// interventi.add(interventoDuPremio126);
		interventi.add(interventoDuPremio128);
		interventi.add(interventoDuPremio129);
		interventi.add(interventoDuPremio132);
		interventi.add(interventoDuPremio138);

		return interventi;
	}

}