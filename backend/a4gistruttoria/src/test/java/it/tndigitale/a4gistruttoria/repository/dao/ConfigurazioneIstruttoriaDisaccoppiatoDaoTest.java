package it.tndigitale.a4gistruttoria.repository.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4gistruttoria.repository.model.ConfigurazioneIstruttoriaDisaccoppiatoModel;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureTestDatabase
public class ConfigurazioneIstruttoriaDisaccoppiatoDaoTest {

	@Autowired
	private ConfigurazioneIstruttoriaDisaccoppiatoDao confDao;

	@MockBean
	private RestTemplate restTemplate;

	@Test
	public void verificaDatiPresenti2018() {
		Integer campagna = 2018;
		Optional<ConfigurazioneIstruttoriaDisaccoppiatoModel> configurazione = confDao.findByCampagna(campagna);
		
		assertTrue(configurazione.isPresent());
		
		ConfigurazioneIstruttoriaDisaccoppiatoModel expected = new ConfigurazioneIstruttoriaDisaccoppiatoModel();
		expected.setId(1L);
		expected.setVersion(0);
		expected.setCampagna(campagna);
		expected.setPercentualeIncrementoGiovane(0.5);
		expected.setPercentualeIncrementoGreening(0.4979);
		expected.setLimiteGiovane(90.0);
		expected.setPercentualeRiduzioneLineareArt51Par2(0.05);
		expected.setPercentualeRiduzioneLineareArt51Par3(0.04);
		expected.setPercentualeRiduzioneLineareMassimaleNetto(0.8);
		expected.setPercentualeRiduzioneTitoli(0.09);
		
		assertTrue(equivalenti(expected, configurazione.get()));
		
	}

	@Test
	public void verificaDatiPresenti2019() {
		Integer campagna = 2019;
		Optional<ConfigurazioneIstruttoriaDisaccoppiatoModel> configurazione = confDao.findByCampagna(campagna);
		
		assertTrue(configurazione.isPresent());
		
		ConfigurazioneIstruttoriaDisaccoppiatoModel expected = new ConfigurazioneIstruttoriaDisaccoppiatoModel();
		expected.setId(2L);
		expected.setVersion(0);
		expected.setCampagna(campagna);
		expected.setPercentualeIncrementoGiovane(0.5);
		expected.setPercentualeIncrementoGreening(0.4979);
		expected.setLimiteGiovane(90.0);
		expected.setPercentualeRiduzioneLineareArt51Par2(0.05);
		expected.setPercentualeRiduzioneLineareArt51Par3(0.04);
		expected.setPercentualeRiduzioneLineareMassimaleNetto(0.8);
		expected.setPercentualeRiduzioneTitoli(0.09);
		
		assertTrue(equivalenti(expected, configurazione.get()));
		
	}

	@Test
	public void verificaDatiNONPresenti2017() {
		Integer campagna = 2017;
		Optional<ConfigurazioneIstruttoriaDisaccoppiatoModel> configurazione = confDao.findByCampagna(campagna);
		
		assertFalse(configurazione.isPresent());
		
	}
	
	protected boolean equivalenti(ConfigurazioneIstruttoriaDisaccoppiatoModel expected, ConfigurazioneIstruttoriaDisaccoppiatoModel actual) {
		return Optional.of(actual)
			.filter(a -> a.getId().equals(expected.getId()))
			.filter(a -> a.getVersion().equals(expected.getVersion()))
			.filter(a -> a.getCampagna().equals(expected.getCampagna()))
			.filter(a -> a.getPercentualeIncrementoGiovane().equals(expected.getPercentualeIncrementoGiovane()))
			.filter(a -> a.getPercentualeIncrementoGreening().equals(expected.getPercentualeIncrementoGreening()))
			.filter(a -> a.getPercentualeRiduzioneLineareArt51Par2().equals(expected.getPercentualeRiduzioneLineareArt51Par2()))			
			.filter(a -> a.getPercentualeRiduzioneLineareArt51Par3().equals(expected.getPercentualeRiduzioneLineareArt51Par3()))			
			.filter(a -> a.getPercentualeRiduzioneLineareMassimaleNetto().equals(expected.getPercentualeRiduzioneLineareMassimaleNetto()))			
			.filter(a -> a.getPercentualeRiduzioneTitoli().equals(expected.getPercentualeRiduzioneTitoli()))
			.filter(a -> a.getLimiteGiovane().equals(expected.getLimiteGiovane()))
			.isPresent();
	}
}
