/**
 * 
 */
package it.tndigitale.a4gutente.config;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gutente.api.ApiUrls;
import it.tndigitale.a4gutente.codici.Ruoli;
import it.tndigitale.a4gutente.service.ProfiloUserDetailsService;

/**
 * Configurazione per la sicurezza. L'autenticazione e' delegata ad un altro sistema (siteMinder), ovvero ADD e ADC.
 * L'integrazione avviene tramite header.
 * 
 * @author it417
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private static final Logger log = LoggerFactory.getLogger(WebSecurityConfiguration.class);
	
	private static final String TOKEN_KEY_PORTALE = "Authorization";
	
	//	TODO ottiene la property del modo di inizializzazione del datasource.
	//	Se è embedded(locale) allora si disabiliterà il frameOption per poter 
	//	rendere correttamente disponibile la console h2 via browser.
	//	@Value("${spring.datasource.initialization-mode:notembedded}")
	//	private String springDatasourceInitializationMode;
	
	@Value("${wso2Host}")
	private String wso2Host;
	
	@Value("${verificaToken}")
	private boolean verificaToken;
	
	@Autowired
	private ProfiloUserDetailsService cuds;
	
	public WebSecurityConfiguration() {
		super();
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(preauthAuthProvider());
	}
	
	@Bean
	UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() {
		UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>();
		wrapper.setUserDetailsService(cuds);
		return wrapper;
	}
	
	@Bean
	public PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
		PreAuthenticatedAuthenticationProvider preauthAuthProvider = new PreAuthenticatedAuthenticationProvider();
		preauthAuthProvider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
		return preauthAuthProvider;
	}
	
	@Bean
	public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter(
			final AuthenticationManager authenticationManager) {
		RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter() {
			
			private static final String TOKEN_ADD = Costanti.HEADER_UPN;
			
			@Override
			protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
				// Se non è presente il codice fiscale uso quello di ADD
				if (!isTokenPresent(request)) {
					return request.getHeader(TOKEN_ADD);
				}
				return super.getPreAuthenticatedPrincipal(request);
			}
			
			//			protected boolean isCodiceFiscalePresent(HttpServletRequest request) {
			//				String cfUtente = request.getHeader(Costanti.HEADER_CF);
			//				return ((cfUtente != null) && (!cfUtente.isEmpty()));
			//			}
			
			protected boolean isTokenPresent(HttpServletRequest request) {
				log.info("Verifica token: " + verificaToken);
				if (verificaToken && this.verificaAccessToken(request)) {
					//			ResponseEntity<RestOutput> responseEntity = exceptionController
					//					.handleNotAuthorizedException(new NotAuthorizedException(
					//							messageSource.getMessage("error.NotAuthorizedNotValidSession", null, Locale.ITALIAN)));
					//			Gson gson = new Gson();
					//			String json = gson.toJson(responseEntity.getBody());
					//			response.getWriter().write(json);
					//			response.setStatus(responseEntity.getStatusCodeValue());
					//			response.setContentType("application/json");
					return true;
				}
				log.info("Token non trovato o non valido");
				return false;
			}
			
			/**
			 * <p>
			 * Verifica se l'access-token ricevuto da wso2 risulta valido
			 * </p>
			 *
			 * @param request
			 */
			public boolean verificaAccessToken(HttpServletRequest request) {
				
				try {
					String accessToken = request.getHeader(TOKEN_KEY_PORTALE);
					if (accessToken != null) {
						accessToken = accessToken.replace("Bearer ", "");
						
						StringBuilder command = new StringBuilder(
								"curl -k -u admin:admin -H 'Content-Type: application/x-www-form-urlencoded' -X POST --data token=");
						command.append(URLEncoder.encode(accessToken, StandardCharsets.UTF_8.toString()));
						command.append(" https://");
						command.append(wso2Host);
						command.append(":9443/oauth2/introspect");
						
						Process process = Runtime.getRuntime().exec(command.toString());
						
						InputStream input = process.getInputStream();
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						
						// UtilityFile.copyInputOutput(input, output);
						int nRead;
						byte[] data = new byte[4];
						while ((nRead = input.readNBytes(data, 0, data.length)) != 0) {
							output.write(data, 0, nRead);
						}
						output.flush();
						String strResponse = new String(output.toByteArray());
						if (strResponse.indexOf("</html>") > 0) {
							strResponse = strResponse.substring(strResponse.indexOf("</html>") + 7);
						}
						logger.info("oauth2Response: " + strResponse);
						
						ObjectMapper mapper = new ObjectMapper();
						Map<String, Object> oauth2Response = mapper.readValue(strResponse, Map.class);
						
						Boolean active = (Boolean) oauth2Response.get("active");
						
						return active != null && active;
					}
					return false;
				}
				catch (Exception e) {
					logger.error("Exception:", e);
					return false;
				}
			}
			
		};
		filter.setAuthenticationManager(authenticationManager);
		filter.setPrincipalRequestHeader(TOKEN_KEY_PORTALE);
		filter.setExceptionIfHeaderMissing(false);
		return filter;
	}
	
	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {
		return authenticationManager();
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().antMatchers(HttpMethod.POST, ApiUrls.UTENTI_V1)
				.hasRole(Ruoli.CREA_UTENTE.getCodiceRuolo()) //
				.antMatchers(HttpMethod.GET, ApiUrls.DOMANDE_V1).hasRole(Ruoli.VISUALIZZA_DOMANDE.getCodiceRuolo()) //
				.antMatchers(ApiUrls.UTENTI_V1 + "/log").permitAll().antMatchers(ApiUrls.API_V1 + "/**").authenticated()
				.anyRequest().permitAll().and()
				.addFilterAfter(requestHeaderAuthenticationFilter(customAuthenticationManager()),
						RequestHeaderAuthenticationFilter.class)
				.csrf().disable(); //Inserito per consentire l'upload dei file.
		
		//	    	TODO abilitazione della h2console(via browser) solo quando vengono effettuati i test in locale 
		//	    	if(StringUtils.hasLength(springDatasourceInitializationMode) && springDatasourceInitializationMode.trim().equals("embedded")) {
		//	    		httpSecurity.headers().frameOptions().disable(); //for h2console	
		//	    	}
		
	}
}
