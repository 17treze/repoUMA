/**
 * 
 */
package it.tndigitale.a4g.proxy.bdn.wsbdninterrogazioni.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

/**
 * Classe da estendere per le chiamate BASIC a WS.<br>
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
public class WSBasicSupport extends WebServiceGatewaySupport {

	protected Jaxb2Marshaller jaxb2Marshaller;

	/**
	 * Metodo da invocare nelle classi figlie per la costruzione del WebTemplate.
	 * 
	 * @param context il qualified name del package usato per le classi generate da
	 *                JAXB.
	 * @param wsUri   la URL del servizio, generalmente configurata in un property
	 *                ed iniettata tramite {@link Value}.
	 */
	protected void buildWebTemplate(String context, String wsUri) throws Exception {
		jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setContextPath(context);
		setMarshaller(jaxb2Marshaller);
		setUnmarshaller(jaxb2Marshaller);
		setDefaultUri(wsUri);
	}

}