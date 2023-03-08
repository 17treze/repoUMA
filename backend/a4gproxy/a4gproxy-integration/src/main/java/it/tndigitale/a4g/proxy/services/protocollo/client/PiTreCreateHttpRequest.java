package it.tndigitale.a4g.proxy.services.protocollo.client;


import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
class PiTreCreateHttpRequest {
	
	private static final String ROUTE_SERVICE="/RouteRestRequest";
	private static final String CODE_ADM_HEADER="CODE_ADM";
	private static final String ROUTED_ACTION_HEADER="ROUTED_ACTION";
	private static final String AUTH_HEADER="AuthToken";
	
	@Value("${pitre.rest.uri}")
	private String urlPtre;
	@Value("${pitre.codeadm}")
	protected String piTreCodeAdm;
	
	@Value("${client.ws.key-store}")
	private Resource keyStore;
	@Value("${client.ws.key-store-password}")
	private String keyStorePassword;
	@Value("${client.ws.trust-store}")
	private Resource trustStore;
	@Value("${client.ws.trust-store-password}")
	private String trustStorePassword;
	
	@Value("${pitre.rest.key.alias}")
	private String keyAlias;	
	

	private RestTemplate restTemplateForPiTre; 

	private static final Logger log = LoggerFactory.getLogger(PiTreCreateHttpRequest.class);
	
	@Autowired
	private PiTreSharedComponents sharedComponents;

	<T> T createRestCall(Object request, Class<T> responseType, String action, HttpMethod method) {
		HttpHeaders headers = createHeaders(action);
		ResponseEntity<T> respEntity = createRestRequest(request, responseType, method, headers);
		return respEntity.getBody();
	}
	
	@PostConstruct
	private void createRestTemplate() {		
		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(keyStore.getInputStream(),
					keyStorePassword.toCharArray());
			String aliasKey = getAliasKey();
			SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
					.loadKeyMaterial(ks, keyStorePassword.toCharArray(), (aliasKey != null) ? new AliasPrivateKeyStrategy(aliasKey) : null)
					.build();
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
			HttpClient httpClient = HttpClients.custom()
					.setSSLSocketFactory(socketFactory)
					.build();
			ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);		
			restTemplateForPiTre = new RestTemplate(factory);
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException | UnrecoverableKeyException e) {
			throw new RuntimeException("Errore nella creazione del RestTemplate utilizzando SSL",e);
		}
	}

	private <T> ResponseEntity<T> createRestRequest(Object request, Class<T> responseType, HttpMethod method, HttpHeaders headers) {
		ResponseEntity<T> respEntity;
		if (HttpMethod.GET.equals(method)) {
			//Per le richieste GET, è necessario inserire i parametri nella url
			HttpEntity entity = new HttpEntity(headers);
			respEntity = restTemplateForPiTre.exchange(buildQueryParam(urlPtre + ROUTE_SERVICE,request), method, entity, responseType);
		} else {
			//Mentre per i metodi PUT e POST, è necessario passare la stessa richiesta 
			//passata al metodo originale.
			HttpEntity entity = new HttpEntity(request, headers);
			respEntity = restTemplateForPiTre.exchange(urlPtre + ROUTE_SERVICE, method, entity, responseType);
		}
		return respEntity;
	}

	private String buildQueryParam(String url, Object request) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		((Map<String,String>) request).forEach((key,value) -> {
			builder.queryParam(key, value);
		});
		return builder.toUriString();
	}


	private HttpHeaders createHeaders(String action) {
		HttpHeaders headers = new HttpHeaders();
		//Il metodo RouteRestRequest richiede due header:
		//	-	ROUTED_ACTION: nome del metodo da invocare (ad esempio GetToken, oppure CreateDocument);
		//	-	CODE_ADM: codice dell’amministrazione verso la quale instradare la richiesta.
		headers.add(CODE_ADM_HEADER, piTreCodeAdm);
		headers.add(ROUTED_ACTION_HEADER, action);
		//nel caso venga eseguita la chiamata GetToken non è necessario aggiungere l'header
		if (!PiTreRestRoutedAction.GET_TOKEN.getAction().equals(action)) {
			//Al fine di garantire un corretto utilizzo, per chiamare ogni servizio sarà necessario 
			//l’inserimento di un header “AuthToken” contenente il token di autenticazione 
			//prelevato tramite il metodo GetToken
			headers.add(AUTH_HEADER, sharedComponents.getAuthenticationToken());
		}
		return headers;
	}


	private class AliasPrivateKeyStrategy implements PrivateKeyStrategy {

		private String alias;
	    
		public AliasPrivateKeyStrategy(String alias) {
			this.alias = alias;

		}		
		@Override
		public String chooseAlias(Map<String, PrivateKeyDetails> aliases, Socket socket) {
			log.debug("chooseClientAlias: alias {}", alias);
			boolean aliasFound = false;

			// Get all aliases from the key manager. If any matches with the managed alias,
			// then return it.
			// If the alias has not been found, return null (and let the API to handle it,
			// causing the handshake to fail).

			if (aliases != null && !aliases.isEmpty()) {
				Iterator<String> validAliases = aliases.keySet().iterator();
				while (!aliasFound && validAliases.hasNext()) {
					if (validAliases.next().equals(alias))
						aliasFound = true;
				}
			}

			if (aliasFound) {
				return alias;
			} else {
				log.warn("chooseClientAlias : alias {} NOT FOUND", alias);
				return null;
			}
		}
		
	}
	
	protected String getAliasKey() {
		return keyAlias;
	}

}
