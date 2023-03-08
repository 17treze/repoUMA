/**
 * 
 */
package it.tndigitale.a4g.proxy.config;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

/**
 * Classe da estendere per le chiamate a WS che utilizzano KeyStore e TrustStore
 * per l'autenticazione.<br>
 * <br>
 * Estendendo questa classe, si ritroveranno tutte le utility per le invocazioni
 * dei servizi della {@link WebServiceGatewaySupport}.<br>
 * Per il suo corretto utilizzo, bisogna estendere questa classe e nella classe
 * figlia bisogna creare un metodo annotato <b>@PostContruct</b> nel quale si
 * invoca il metodo {@link #buildWebTemplate(String, String)}.
 * 
 * @author S.DeLuca
 *
 */
public abstract class WSKSTSSupport extends WSBasicSupport {

	private static final String KEYSTORE_TYPE = "JKS";
	private static final String TRUSTSTORE_TYPE = "JKS";

	@Autowired
	private SoapMessageFactory messageFactory;


	private static final Logger log = LoggerFactory.getLogger(WSKSTSSupport.class);

	@Value("${client.ws.key-store}")
	private Resource keyStore;
	@Value("${client.ws.key-store-password}")
	private String keyStorePassword;
	@Value("${client.ws.trust-store}")
	private Resource trustStore;
	@Value("${client.ws.trust-store-password}")
	private String trustStorePassword;
	
	/**
	 * Metodo da invocare nelle classi figlie per la costruzione del WebTemplate.
	 * 
	 * @param context il qualified name del package usato per le classi generate da
	 *                JAXB.
	 * @param wsUri   la URL del servizio, generalmente configurata in un property
	 *                ed iniettata tramite {@link Value}.
	 * @throws KeyStoreException         se non trova implementazioni per il tipo
	 *                                   indicato.
	 * @throws NoSuchAlgorithmException  se non trova l'algoritmo indicato.
	 * @throws CertificateException      se non riesce a caricare i certificati per
	 *                                   KeyStore o TrustStore.
	 * @throws IOException               se c'è un problema di I/O caricando il file
	 *                                   KeyStore o TrustStore.
	 * @throws UnrecoverableKeyException se la chiave non può essere recuperata
	 *                                   (es.: password errata).
	 */
	@Override
	protected void buildWebTemplate(String context, String wsUri) throws Exception {
		super.buildWebTemplate(context, wsUri);
		setMessageSender(buildMessageSender());
	}	
	
	// questo metodo non utilizza il keystore. Ma solo il trustore
	protected void buildWebTemplate(String context, String wsUri, String wsUsername, String wsPassword) throws Exception {
		buildWebTemplate(context, wsUri);
		this.setMessageFactory(messageFactory);
		// sovrascrivi build web template con il solo utilizzo del trustore
		setMessageSender(buildMessageSenderOnlyTrustStore());
		setInterceptors(new ClientInterceptor[] { new BasicAuthenticationPreemptiveConfig(wsUsername, wsPassword) });
	}

	private HttpsUrlConnectionMessageSender buildMessageSender() throws KeyStoreException, NoSuchAlgorithmException,
	CertificateException, IOException, UnrecoverableKeyException {
		KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
		ks.load(keyStore.getInputStream(), keyStorePassword.toCharArray());
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(ks, keyStorePassword.toCharArray());
		KeyStore ts = KeyStore.getInstance(TRUSTSTORE_TYPE);
		ts.load(trustStore.getInputStream(), trustStorePassword.toCharArray());
		trustStore.getInputStream().close();
		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(ts);
		KeyManager[] kms = keyManagerFactory.getKeyManagers();
		KeyManager km = null;
		for (int i = 0; i < kms.length; i++) {
			km = kms[i];
			if (km instanceof X509KeyManager) {
				kms[i] = new AliasSelectorKeyManager((X509KeyManager)km, getAlias());
			}
		}
		km = null;
		HttpsUrlConnectionMessageSender messageSender = new HttpsUrlConnectionMessageSender();
		messageSender.setKeyManagers(kms);
		messageSender.setTrustManagers(trustManagerFactory.getTrustManagers());
		// otherwise: java.security.cert.CertificateException: No name matching
		// localhost found
		messageSender.setHostnameVerifier((hostname, sslSession) -> hostname.equals("localhost"));
		return messageSender;
	}
	// prova effettuata per vedere se è sufficiente solo il truststore per il catasto
	private HttpsUrlConnectionMessageSender buildMessageSenderOnlyTrustStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ts = KeyStore.getInstance(TRUSTSTORE_TYPE);
		ts.load(trustStore.getInputStream(), trustStorePassword.toCharArray());
		trustStore.getInputStream().close();
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(ts);
		HttpsUrlConnectionMessageSender messageSender = new HttpsUrlConnectionMessageSender();
		messageSender.setTrustManagers(trustManagerFactory.getTrustManagers());
		// otherwise: java.security.cert.CertificateException: No name matching
		// localhost found
		messageSender.setHostnameVerifier((hostname, sslSession) -> hostname.equals("localhost"));
		return messageSender;
	}

	protected abstract String getAlias();

	private class AliasSelectorKeyManager implements X509KeyManager{

		private X509KeyManager sourceKeyManager;
		private String alias;

		public AliasSelectorKeyManager(X509KeyManager keyManager, String alias) {
			this.sourceKeyManager = keyManager;
			this.alias = alias;

		}

		@Override
		public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
			log.debug("chooseClientAlias: alias {}", alias);
			boolean aliasFound = false;

			// Get all aliases from the key manager. If any matches with the managed alias,
			// then return it.
			// If the alias has not been found, return null (and let the API to handle it,
			// causing the handshake to fail).

			for (int i = 0; i < keyType.length && !aliasFound; i++) {
				String[] validAliases = sourceKeyManager.getClientAliases(keyType[i], issuers);
				if (validAliases != null) {
					for (int j = 0; j < validAliases.length && !aliasFound; j++) {
						if (validAliases[j].equals(alias))
							aliasFound = true;
					}
				}
			}

			if (aliasFound) {
				return alias;
			} else {
				log.warn("chooseClientAlias : alias {} NOT FOUND", alias);
				return null;
			}
		}

		@Override
		public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
			return sourceKeyManager.chooseServerAlias(keyType, issuers, socket);
		}

		@Override
		public X509Certificate[] getCertificateChain(String alias) {
			log.debug("getCertificateChain alias {}", alias);
			return sourceKeyManager.getCertificateChain(alias);
		}

		@Override
		public String[] getClientAliases(String keyType, Principal[] issuers) {
			return sourceKeyManager.getClientAliases(keyType, issuers);
		}

		@Override
		public PrivateKey getPrivateKey(String alias) {
			return sourceKeyManager.getPrivateKey(alias);
		}

		@Override
		public String[] getServerAliases(String keyType, Principal[] issuers) {
			return sourceKeyManager.getServerAliases(keyType, issuers);
		}

	}

}