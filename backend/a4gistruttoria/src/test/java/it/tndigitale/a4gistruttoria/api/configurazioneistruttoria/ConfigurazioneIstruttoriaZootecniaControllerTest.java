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
public class ConfigurazioneIstruttoriaZootecniaControllerTest {
	@Autowired private MockMvc mvc;
	@Autowired private MapperWrapper mapperWrapper;

	@Test
	public void getConfIstruttoriaAcz() throws Exception {
		mvc.perform(get(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/acz/2019")).andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.configurazioneInstruttoriaZootecnicaFilter").isEmpty())
				.andExpect(jsonPath("$.interventoDuPremi").isArray())
				.andExpect(jsonPath("$.interventoDuPremi", hasSize(11)))
				.andExpect(jsonPath("$.interventoDuPremi[*].intervento.codiceAgea", containsInAnyOrder("310", "311",
						"312", "313", "314", "315", "316", "318", "320", "321", "322")));
	}

	@Test
	public void saveConfIstruttoriaAcz() throws Exception {
		DatiConfigurazioneAccoppiati conf = new DatiConfigurazioneAccoppiati();
		conf.setConfigurazioneInstruttoriaZootecnicaFilter(null);
		List<InterventoDuPremio> interventi = this.dataPreparationAcz();
		conf.setInterventoDuPremi(interventi);
		mvc.perform(post(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/acz/2019").content(mapperWrapper.asJsonString(conf))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.interventoDuPremi[*].id").exists())
				.andExpect(jsonPath("$.configurazioneInstruttoriaZootecnicaFilter").isEmpty())
				.andExpect(jsonPath("$.interventoDuPremi").isArray())
				.andExpect(jsonPath("$.interventoDuPremi", hasSize(11)))
				.andExpect(jsonPath("$.interventoDuPremi[*].intervento.codiceAgea", containsInAnyOrder("310", "311",
						"312", "313", "314", "315", "316", "318", "320", "321", "322")))
				.andExpect(jsonPath("$.interventoDuPremi[*].valoreUnitarioIntervento", hasItem(40.21)))
				.andExpect(jsonPath("$.interventoDuPremi[*].priorita", hasItem(1)));
	}

	@Test(expected = NestedServletException.class)
	public void saveConfIstruttoriaAczError() throws Exception {
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

	private List<InterventoDuPremio> dataPreparationAcz() {
		InterventoDto interventoDto310 = new InterventoDto();
		interventoDto310.setCodiceAgea("310");
		InterventoDto interventoDto311 = new InterventoDto();
		interventoDto311.setCodiceAgea("311");
		InterventoDto interventoDto312 = new InterventoDto();
		interventoDto312.setCodiceAgea("312");
		InterventoDto interventoDto313 = new InterventoDto();
		interventoDto313.setCodiceAgea("313");
		InterventoDto interventoDto314 = new InterventoDto();
		interventoDto314.setCodiceAgea("314");
		InterventoDto interventoDto315 = new InterventoDto();
		interventoDto315.setCodiceAgea("315");
		InterventoDto interventoDto316 = new InterventoDto();
		interventoDto316.setCodiceAgea("316");
		InterventoDto interventoDto318 = new InterventoDto();
		interventoDto318.setCodiceAgea("318");
		InterventoDto interventoDto320 = new InterventoDto();
		interventoDto320.setCodiceAgea("320");
		InterventoDto interventoDto321 = new InterventoDto();
		interventoDto321.setCodiceAgea("321");
		InterventoDto interventoDto322 = new InterventoDto();
		interventoDto322.setCodiceAgea("322");

		InterventoDuPremio interventoDuPremio310 = new InterventoDuPremio();
		interventoDuPremio310.setIntervento(interventoDto310);
		interventoDuPremio310.setValoreUnitarioIntervento(new BigDecimal("40.21"));
		interventoDuPremio310.setPriorita(new Integer("1"));
		InterventoDuPremio interventoDuPremio311 = new InterventoDuPremio();
		interventoDuPremio311.setIntervento(interventoDto311);
		interventoDuPremio311.setValoreUnitarioIntervento(BigDecimal.valueOf(40.21));
		InterventoDuPremio interventoDuPremio312 = new InterventoDuPremio();
		interventoDuPremio312.setIntervento(interventoDto312);
		interventoDuPremio312.setValoreUnitarioIntervento(BigDecimal.valueOf(60.42));
		InterventoDuPremio interventoDuPremio314 = new InterventoDuPremio();
		interventoDuPremio314.setIntervento(interventoDto314);
		interventoDuPremio314.setValoreUnitarioIntervento(BigDecimal.valueOf(80.24));
		InterventoDuPremio interventoDuPremio315 = new InterventoDuPremio();
		interventoDuPremio315.setIntervento(interventoDto315);
		interventoDuPremio315.setValoreUnitarioIntervento(BigDecimal.valueOf(100.4));
		InterventoDuPremio interventoDuPremio316 = new InterventoDuPremio();
		interventoDuPremio316.setIntervento(interventoDto316);
		interventoDuPremio316.setValoreUnitarioIntervento(BigDecimal.valueOf(120.2));
		InterventoDuPremio interventoDuPremio318 = new InterventoDuPremio();
		interventoDuPremio318.setIntervento(interventoDto318);
		interventoDuPremio318.setValoreUnitarioIntervento(BigDecimal.valueOf(140.63));
		InterventoDuPremio interventoDuPremio320 = new InterventoDuPremio();
		interventoDuPremio320.setIntervento(interventoDto320);
		interventoDuPremio320.setValoreUnitarioIntervento(BigDecimal.valueOf(160.22));
		InterventoDuPremio interventoDuPremio321 = new InterventoDuPremio();
		interventoDuPremio321.setIntervento(interventoDto321);
		interventoDuPremio321.setValoreUnitarioIntervento(BigDecimal.valueOf(110.11));
		InterventoDuPremio interventoDuPremio322 = new InterventoDuPremio();
		interventoDuPremio322.setIntervento(interventoDto322);
		interventoDuPremio322.setValoreUnitarioIntervento(BigDecimal.valueOf(122.11));
		InterventoDuPremio interventoDuPremio313 = new InterventoDuPremio();
		interventoDuPremio313.setIntervento(interventoDto313);
		interventoDuPremio313.setValoreUnitarioIntervento(BigDecimal.valueOf(142.11));

		List<InterventoDuPremio> interventi = new ArrayList<InterventoDuPremio>();
		interventi.add(interventoDuPremio310);
		interventi.add(interventoDuPremio311);
		interventi.add(interventoDuPremio312);
		interventi.add(interventoDuPremio313);
		interventi.add(interventoDuPremio314);
		interventi.add(interventoDuPremio315);
		interventi.add(interventoDuPremio316);
		interventi.add(interventoDuPremio318);
		interventi.add(interventoDuPremio320);
		interventi.add(interventoDuPremio321);
		interventi.add(interventoDuPremio322);

		return interventi;
	}

}
