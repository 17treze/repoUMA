package it.tndigitale.a4g.fascicolo.antimafia.ioitalia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.framework.client.RestClientBuilder;
import it.tndigitale.a4g.proxy.client.api.IoItaliaControllerApi;
import it.tndigitale.a4g.proxy.client.model.ComunicationDto;

@Service
public class IoItaliaConsumerApi {

	@Value("${it.tndigit.a4g.client.proxy.url}")
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
