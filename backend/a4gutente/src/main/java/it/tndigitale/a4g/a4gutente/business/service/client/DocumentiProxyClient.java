package it.tndigitale.a4g.a4gutente.business.service.client;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.client.DefaultUrlMicroService;
import it.tndigitale.a4g.proxy.client.api.ProtocolloControllerApi;

@Component
public class DocumentiProxyClient extends AbstractClient {

	@Value(DefaultUrlMicroService.PROXY_URL)
	private String urlProxy;
	
	private ProtocolloControllerApi getProtocolloControllerApi() {
		return restClientProxy(ProtocolloControllerApi.class, urlProxy);
	}
	
	public String createIncomingDocument(String infoIn, File documento, List<File> allegati) {
		return getProtocolloControllerApi().createIncomingDocumentRESTUsingPOST(infoIn, documento, allegati);
	}
}
