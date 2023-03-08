package it.tndigitale.a4gistruttoria.api.configurazioneistruttoria;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttoriaDisaccoppiatoDto;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
@WithMockUser(username = "istruttore", roles = { 
		"a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
		"a4gistruttoria.pac.istruttoria.du.visualizza.utente",
		"a4gistruttoria.pac.istruttoria.edita"})
public class ConfigurazioneIstruttoriaDisaccoppiatoControllerTest {
	@Autowired private MockMvc mvc;
	@Autowired private MapperWrapper mapperWrapper;
	
	@Test
	public void getConfIstruttoriaDisaccoppiato() throws Exception {
		mvc.perform(
			      	get(ApiUrls.ISTRUTTORIE_DU_CONF_V1+"/disaccoppiato/2018"))
			      .andExpect(status().is2xxSuccessful())
			      .andExpect(jsonPath("$.id").isNumber())
			      .andExpect(jsonPath("$.campagna").value(2018))
			      .andExpect(jsonPath("$.percentualeIncrementoGiovane").value(0.5));
	}
	
	@Test
	public void getConfIstruttoriaDisaccoppiatoNoContent() throws Exception {
		mvc.perform(
			      	get(ApiUrls.ISTRUTTORIE_DU_CONF_V1+"/disaccoppiato/2000"))
			      .andExpect(status().is2xxSuccessful())
			      .andExpect(status().is(204));
	}
	
	@Test
	public void saveConfIstruttoriaDisaccoppiato() throws Exception {
		ConfIstruttoriaDisaccoppiatoDto conf = new ConfIstruttoriaDisaccoppiatoDto();
		conf.setCampagna(2100);
		conf.setPercentualeIncrementoGiovane(0.1);
		conf.setPercentualeIncrementoGreening(0.2);
		conf.setLimiteGiovane(1.0);
		conf.setPercentualeRiduzioneLineareArt51Par2(1.0);
		conf.setPercentualeRiduzioneLineareArt51Par3(1.1);
		conf.setPercentualeRiduzioneLineareMassimaleNetto(1.2);
		conf.setPercentualeRiduzioneLineareMassimaleNetto(1.3);
		conf.setPercentualeRiduzioneTitoli(1.4);
		mvc.perform( 
			      post(ApiUrls.ISTRUTTORIE_DU_CONF_V1+"/disaccoppiato/2100")
			      .content(mapperWrapper.asJsonString(conf))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().is2xxSuccessful())
			      .andExpect(jsonPath("$.id").exists())
			      .andExpect(jsonPath("$.percentualeIncrementoGiovane").value(0.1))
			      .andExpect(jsonPath("$.percentualeIncrementoGreening").value(0.2));
	}
	
	@Test(expected = NestedServletException.class)
	public void saveConfIstruttoriaDisaccoppiatoError() throws Exception {
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
		mvc.perform( 
			      post(ApiUrls.ISTRUTTORIE_DU_CONF_V1+"/disaccoppiato/2102")
			      .content(mapperWrapper.asJsonString(conf))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().is5xxServerError());
	}
	
}
