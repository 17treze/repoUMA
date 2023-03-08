package it.tndigitale.a4gistruttoria.service.businesslogic.iotialia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.framework.client.RestClientBuilder;
import it.tndigitale.a4g.proxy.client.api.IoItaliaControllerApi;
import it.tndigitale.a4g.proxy.client.model.ComunicationDto;

@Service
public class IoItaliaConsumerApi {
	
	@Value("${a4gistruttoria.proxy.client.uri}")
	private String urlProxy;
	@Autowired
	protected RestClientBuilder restClientBuilder;
	
	private IoItaliaControllerApi ioItaliaControllerApi;
	
	private IoItaliaControllerApi getIoItaliaControllerApi() {
		if (ioItaliaControllerApi == null) {
			ioItaliaControllerApi= restClientBuilder.from(IoItaliaControllerApi.class)
								      .setBasePath(urlProxy)
								      .newInstance();
		}
		return ioItaliaControllerApi;
	}
	
	public String inviaMessaggio(ComunicationDto comunication) {
		return getIoItaliaControllerApi().inviaMessaggioUsingPOST(comunication);
	}

}
