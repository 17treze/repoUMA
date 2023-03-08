/**
 * 
 */
package it.tndigitale.a4g.proxy.config;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;

/**
 * Classe da estendere per le chiamate a WS che utilizzano Basic Authentication per l'autenticazione.<br>
 * <br>
 * Estendendo questa classe, si ritroveranno tutte le utility per le invocazioni dei servizi della {@link WebServiceGatewaySupport}.<br>
 * Per il suo corretto utilizzo, bisogna estendere questa classe e nella classe figlia bisogna creare un metodo annotato <b>@PostContruct</b> nel quale si invoca il metodo
 * {@link #buildWebTemplate(String, String)}.
 * 
 * @author S.DeLuca
 *
 */
public class WSBasicAuthenticationPreemptiveSupport extends WSBasicSupport {

	@Autowired
	private SoapMessageFactory messageFactory;

	/**
	 * Metodo da invocare nelle classi figlie per la costruzione del WebTemplate.
	 * 
	 * @param context il qualified name del package usato per le classi generate da JAXB.
	 * @param wsUri la URL del servizio, generalmente configurata in un property ed iniettata tramite {@link Value}.
	 * @param wsUsername lo username del servizio, generalmente configurata in un property ed iniettata tramite {@link Value}.
	 * @param wsPassword la password del servizio, generalmente configurata in un property ed iniettata tramite {@link Value}.
	 */
	protected void buildWebTemplate(String context, String wsUri, String wsUsername, String wsPassword) throws Exception {
		super.buildWebTemplate(context, wsUri);
		this.setMessageFactory(messageFactory);
		setInterceptors(new ClientInterceptor[] { new BasicAuthenticationPreemptiveConfig(wsUsername, wsPassword) });
	}

}
