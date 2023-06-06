package it.tndigitale.a4g.framework.client;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClientServiceBuilder {
	public static final String HEADER_UPN = "upn";
	public static final String HEADER_CF = "codicefiscale";
	public static final String TOKEN_KEY_PORTALE = "Authorization";
	
	private final static Logger log = LoggerFactory.getLogger(ClientServiceBuilder.class);
	
	protected ClientHttpRequestInterceptor buildUsernameHeader(Supplier<String> username) {
		return (request, body, execution) -> {
			String currentUser = username.get();
			log.info("buildUsernameHeader: " + currentUser);
			if (isADDAuthentication(currentUser)) {
				request.getHeaders().set(HEADER_UPN, currentUser);
			}
			else {
				// request.getHeaders().set(HEADER_CF, currentUser);
				request.getHeaders().set(TOKEN_KEY_PORTALE, currentUser);
			}
			return execution.execute(request, body);
		};
	}
	
	public RestTemplate buildWith(Supplier<String> username) {
		return new RestTemplateBuilder().additionalInterceptors(buildUsernameHeader(username)).build();
	}
	
	protected boolean isADDAuthentication(String username) {
		return username.indexOf("@") > 0;
	}
	
}
