package it.tndigitale.a4gistruttoria.strategy;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.AccoppiatoSuperficie;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Ignore("errore primary key da indagare")
public class DatiASDatiSuperficiImpegnateTests {
	
	@Autowired
	private DatiASDatiSuperficiImpegnate datiSupImpegnateComp;
	
	@Autowired
	private DomandaUnicaDao daoDomanda;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RestTemplate restTemplate;
	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/superficiImpegnate3InterventiOlio.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void conDomandaCon3MisureOlivo() throws Exception {
		String cuaa = "GRCNGL64E14B809_";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		AccoppiatoSuperficie as = new AccoppiatoSuperficie(domanda.getId(), StatoIstruttoria.CONTROLLI_CALCOLO_OK);
		datiSupImpegnateComp.recupera(as);
		String expected = "{\"supRichiesta\":25572,\"supRichiestaNetta\":25572,\"m8\":null,\"m9\":null,\"m10\":null,\"m11\":null,\"m14\":null,\"m15\":{\"supRichiesta\":8524,\"supRichiestaNetta\":8524,\"superficiImpegnate\":[{\"datiCatastali\":{\"idParticella\":3405620,\"comune\":\"CAROVIGNO (BR)\",\"codNazionale\":\"B809\",\"foglio\":43,\"particella\":\"00017\",\"sub\":\" \"},\"datiColtivazione\":{\"idPianoColture\":6113282,\"idColtura\":4713,\"codColtura3\":\"160-111-011\",\"codColtura5\":\"420-006-000-000-011\",\"codLivello\":\"121\",\"descrizioneColtura\":\"OLIVO - OLIVE DA OLIO - CASALIVA\",\"coefficienteTara\":1.0,\"superficieDichiarata\":8524,\"descMantenimento\":\"PRATICA ORDINARIA\"},\"riferimentiCartografici\":{\"idParcella\":266201,\"idIsola\":1204176,\"codIsola\":\"IT25/GRCNGL64E14B809_/AAA01\"},\"supRichiesta\":8524,\"supRichiestaNetta\":8524}]},\"m16\":{\"supRichiesta\":8524,\"supRichiestaNetta\":8524,\"superficiImpegnate\":[{\"datiCatastali\":{\"idParticella\":3405620,\"comune\":\"CAROVIGNO (BR)\",\"codNazionale\":\"B809\",\"foglio\":43,\"particella\":\"00017\",\"sub\":\" \"},\"datiColtivazione\":{\"idPianoColture\":6113282,\"idColtura\":4713,\"codColtura3\":\"160-111-011\",\"codColtura5\":\"420-006-000-000-011\",\"codLivello\":\"121\",\"descrizioneColtura\":\"OLIVO - OLIVE DA OLIO - CASALIVA\",\"coefficienteTara\":1.0,\"superficieDichiarata\":8524,\"descMantenimento\":\"PRATICA ORDINARIA\"},\"riferimentiCartografici\":{\"idParcella\":266204,\"idIsola\":1204176,\"codIsola\":\"IT25/GRCNGL64E14B809_/AAA01\"},\"supRichiesta\":8524,\"supRichiestaNetta\":8524}]},\"m17\":{\"supRichiesta\":8524,\"supRichiestaNetta\":8524,\"superficiImpegnate\":[{\"datiCatastali\":{\"idParticella\":3405620,\"comune\":\"CAROVIGNO (BR)\",\"codNazionale\":\"B809\",\"foglio\":43,\"particella\":\"00017\",\"sub\":\" \"},\"datiColtivazione\":{\"idPianoColture\":6113282,\"idColtura\":4713,\"codColtura3\":\"160-111-011\",\"codColtura5\":\"420-006-000-000-011\",\"codLivello\":\"121\",\"descrizioneColtura\":\"OLIVO - OLIVE DA OLIO - CASALIVA\",\"coefficienteTara\":1.0,\"superficieDichiarata\":8524,\"descMantenimento\":\"PRATICA ORDINARIA\"},\"riferimentiCartografici\":{\"idParcella\":266205,\"idIsola\":1204176,\"codIsola\":\"IT25/GRCNGL64E14B809_/AAA01\"},\"supRichiesta\":8524,\"supRichiestaNetta\":8524}]}}";
		String result = objectMapper.writeValueAsString(as.getDatiSuperficiImpegnate());
		assertEquals(expected, result);
	}

}
