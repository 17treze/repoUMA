package it.tndigitale.a4g.framework.security.configuration;

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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import it.tndigitale.a4g.framework.client.ClientServiceBuilder;
import it.tndigitale.a4g.framework.security.service.A4GUserDetailsService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@ConditionalOnProperty(prefix = "it.tndigit", name = "a4g.security.web", havingValue = "true")
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	public WebSecurityConfiguration() {
		super();
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
	}
	
	private static final Logger log = LoggerFactory.getLogger(WebSecurityConfiguration.class);
	
	private static final String TOKEN_KEY_PORTALE = "Authorization";
	
	@Value("${wso2Host}")
	private String wso2Host;
	
	@Value("${verificaToken}")
	private boolean verificaToken;
	
	@Value("${it.tndigit.a4g.security.web.version:/api/v1}")
	private String apiVersion;
	
	@Autowired
	private A4GUserDetailsService datiUtenteService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(preauthAuthProvider());
	}
	
	@Bean
	UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() {
		UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>();
		wrapper.setUserDetailsService(datiUtenteService);
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
			
			@Override
			protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
				// Se non è presente il codice fiscale uso quello di ADD
				if (!isTokenPresent(request)) {
					String tokenADD = request.getHeader(ClientServiceBuilder.HEADER_UPN);
					return tokenADD;
				}
				return super.getPreAuthenticatedPrincipal(request);
			}
			
			//			protected boolean isCodiceFiscalePresent(HttpServletRequest request) {
			//				String cfUtente = request.getHeader(ClientServiceBuilder.HEADER_CF);
			//				Boolean isPresent = (cfUtente != null) && (!cfUtente.isEmpty());
			//				return isPresent;
			//			}
			
			protected boolean isTokenPresent(HttpServletRequest request) {
				log.info("Verifica token: " + verificaToken);
				if (verificaToken && this.verificaAccessToken(request)) {
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
					log.info("Access token: " + accessToken);
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
		//		filter.setPrincipalRequestHeader(ClientServiceBuilder.HEADER_CF);
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
				.authorizeRequests().antMatchers(apiVersion + "/**").authenticated().and()
				.addFilterAfter(requestHeaderAuthenticationFilter(customAuthenticationManager()),
						RequestHeaderAuthenticationFilter.class)
				.csrf().disable();
	}
}
