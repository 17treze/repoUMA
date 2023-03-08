package it.tndigitale.a4g.proxy.config;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;
/**
 * Inner class per la Preemptive basic authorization.<br>
 * Il client aggiunge l'header <b>"Authorization: Basic BASE_64_ENCODED_STRING"</b> alle request in uscita.
 */

public final class BasicAuthenticationPreemptiveConfig implements ClientInterceptor {

	private static final String HEADER_AUTHORIZATION = "Authorization";
	private static final String BASIC_AUTH = "Basic ";
	private static final char BASIC_AUTH_SEPARATOR = ':';
	private static final boolean CONTINUE_PROCESSING = true;
	private final Logger logger = LoggerFactory.getLogger(BasicAuthenticationPreemptiveConfig.class);
	private final String userName;
	private final String password;

	@Override
	public boolean handleRequest(MessageContext messageContext) {
		return getConnection(TransportContextHolder.getTransportContext().getConnection()).map(this::appendAuthorization).orElseGet(() -> CONTINUE_PROCESSING);
	}

	/**
	 * Get connection from context
	 *
	 * @param connection current SOAP connection
	 * @return optional connection
	 */
	private Optional<HttpUrlConnection> getConnection(WebServiceConnection connection) {
		if (connection instanceof HttpUrlConnection) {
			return Optional.of((HttpUrlConnection) connection);
		} else {
			logger.error("Preemptive auth skipped due to SOAP connection that doesn't implement: {}", HttpUrlConnection.class);
			return Optional.empty();
		}
	}

	/**
	 * Append basic auth header to given connection
	 *
	 * @param connection connection where header should be appended to
	 * @return flag indicating whether processing should be continued
	 */
	private boolean appendAuthorization(HttpUrlConnection connection) {
		logger.debug("Preemptive auth - applying auth - user: {}, pass length: {}", userName, password.length());
		connection.getConnection().addRequestProperty(HEADER_AUTHORIZATION, BASIC_AUTH + encodeBase64String(getCredentials().getBytes()));
		return CONTINUE_PROCESSING;
	}

	/**
	 * Get credentials
	 *
	 * @return non-empty string
	 */
	private String getCredentials() {
		return userName + BASIC_AUTH_SEPARATOR + password;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) {
		return CONTINUE_PROCESSING;
	}

	@Override
	public boolean handleFault(MessageContext messageContext) {
		return CONTINUE_PROCESSING;
	}

	@Override
	public void afterCompletion(MessageContext messageContext, Exception e) {
		// empty
	}

	public BasicAuthenticationPreemptiveConfig(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
}
