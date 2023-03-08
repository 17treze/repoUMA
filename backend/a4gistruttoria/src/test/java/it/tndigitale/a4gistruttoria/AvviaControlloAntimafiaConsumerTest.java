package it.tndigitale.a4gistruttoria;

import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4gistruttoria.action.AvviaControlloAntimafiaConsumer;
import it.tndigitale.a4gistruttoria.dto.CustomThreadLocal;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.util.EmailUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureTestDatabase
public class AvviaControlloAntimafiaConsumerTest {

	@SpyBean
	private AvviaControlloAntimafiaConsumer consumer;
	@MockBean
	private EmailUtils emailService;
	@Autowired
	private ProcessoDao processoDao;
	@MockBean
	private RestTemplate restTemplate;
	@Value("${a4gistruttoria.a4gfascicolo.uri}")
	private String urlA4gfascicolo;

	
	@Test
	@WithMockUser("Scaccia")
	public void testRetryServizio() throws Exception {
		Mockito.doNothing().when(emailService).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		A4gtProcesso a4gprocesso = new A4gtProcesso();
		a4gprocesso.setDatiElaborazione("{\"totaleDomandeGestite\":\"0\",\"domandeGestite\":[],\"domandeConProblemi\":[],\"numeroDomandeDaElaborare\":\"1\"}");
		a4gprocesso.setStato(StatoProcesso.RUN); // 104 stato PROCESSO_RUN
		a4gprocesso.setTipo(TipoProcesso.CALCOLO_ACCOPPIATO_ZOOTECNIA);  // 109 tipo CLC_ACCOPPIATO_ZOOTENIA
		a4gprocesso.setDtInizio(new Date());
		a4gprocesso.setPercentualeAvanzamento(new BigDecimal(0));
		a4gprocesso=processoDao.saveAndFlush(a4gprocesso);
		CustomThreadLocal.addVariable("idProcesso", a4gprocesso.getId());
		long idDichiarazioneAntimafia = 1010;
		mockFascicoloGetDichiarazione(idDichiarazioneAntimafia);
		a4gprocesso.setDatiElaborazione("{\"totaleDomandeGestite\":\"0\",\"domandeGestite\":[],\"domandeConProblemi\":[],\"numeroDomandeDaElaborare\":\"1\"}");
		CustomThreadLocal.addVariable("idProcesso", a4gprocesso.getId());
		consumer.accept(idDichiarazioneAntimafia);
		Mockito.verify(this.consumer, times(3)).accept(idDichiarazioneAntimafia);
		
	}
	
	private void mockFascicoloGetDichiarazione(Long idDichiarazione) throws Exception {
		String fascicoloGetDichiarazione = urlA4gfascicolo.concat("antimafia").concat("/").concat(idDichiarazione.toString());
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(fascicoloGetDichiarazione)), Mockito.eq(String.class))).thenThrow(new RestClientException("Error"));
	}

}
