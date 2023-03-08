package it.tndigitale.a4g.framework.client;

import java.util.function.Supplier;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClientServiceBuilder {
	public static final String HEADER_UPN = "upn";
	public static final String HEADER_CF = "codicefiscale";

	protected ClientHttpRequestInterceptor buildUsernameHeader(Supplier<String> username) {
		return (request, body, execution) -> {
			String currentUser = username.get();
			if (isADDAuthentication(currentUser)) {
				request.getHeaders().set(HEADER_UPN, currentUser);
			} else {
				request.getHeaders().set(HEADER_CF, currentUser);
			}
			return execution.execute(request, body);
		};
	}
	
	public RestTemplate buildWith(Supplier<String> username) {
		return new RestTemplateBuilder()
		.additionalInterceptors(buildUsernameHeader(username))
		.build();
	}
	
	protected boolean isADDAuthentication(String username) {
		return username.indexOf("@") > 0;
	}

}
