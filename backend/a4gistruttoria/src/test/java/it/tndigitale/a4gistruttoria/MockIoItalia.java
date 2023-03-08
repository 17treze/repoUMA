package it.tndigitale.a4gistruttoria;

import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import it.tndigitale.a4gistruttoria.service.businesslogic.iotialia.IoItaliaSenderService;

public class MockIoItalia {

	@MockBean
	private  IoItaliaSenderService ioItaliaSenderService;
	@Before 
	public void mockIoItalia() {
		Mockito.doNothing().when(ioItaliaSenderService).recuperaFactoryEinviaMessaggio(Mockito.any());
	}

}
