package it.tndigitale.a4g.framework.client;

public interface DefaultUrlMicroService {
	String PROXY_URL = "${it.tndigit.a4g.client.proxy.url:dummy_url_proxy}";
	String ANAGRAFICA_URL = "${it.tndigit.a4g.client.anagrafica.url:dummy_url_proxy}";
}
