package it.tndigitale.a4g.ags.config;

import javax.servlet.http.HttpServletRequest;

import static it.tndigitale.a4g.framework.client.ClientServiceBuilder.HEADER_CF;
import static it.tndigitale.a4g.framework.client.ClientServiceBuilder.HEADER_UPN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import it.tndigitale.a4g.ags.Costanti;
import it.tndigitale.a4g.ags.api.ApiUrls;
import it.tndigitale.a4g.ags.service.A4GUtenteUserDetailsService;

/**
 * Configurazione per  la sicurezza.
 * L'autenticazione e' delegata ad un altro sistema (siteMinder),
 * ovvero ADD e ADC. L'integrazione avviene tramite header.
 * @author it417
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	// private static final String HEADER_CF = "codicefiscale";
	// private static final String HEADER_UPN = "upn";

	@Autowired
	private A4GUtenteUserDetailsService datiUtenteService;
	
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
		PreAuthenticatedAuthenticationProvider preauthAuthProvider = 
				new PreAuthenticatedAuthenticationProvider();
		preauthAuthProvider.setPreAuthenticatedUserDetailsService(
				userDetailsServiceWrapper()
		);
		return preauthAuthProvider;
	}
	
	@Bean
	public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter(
			final AuthenticationManager authenticationManager) {
		RequestHeaderAuthenticationFilter filter = 
				new RequestHeaderAuthenticationFilter() {
			
				private static final String TOKEN_ADD = HEADER_UPN;

					@Override
					protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
						// Se non è presente il codice fiscale uso quello di ADD
						if (!isCodiceFiscalePresent(request)) {
							return request.getHeader(TOKEN_ADD);
						}
						return super.getPreAuthenticatedPrincipal(request);
					}
					
					
					protected boolean isCodiceFiscalePresent(HttpServletRequest request) {
						String cfUtente = request.getHeader(HEADER_CF);
						return ((cfUtente != null) && (!cfUtente.isEmpty()));
					}
			
		};
		filter.setAuthenticationManager(authenticationManager);
		filter.setPrincipalRequestHeader(HEADER_CF);
		filter.setExceptionIfHeaderMissing(false);
		return filter;
	}
	
	@Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }	

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
	    	httpSecurity
	    	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    	.and()
	    	.authorizeRequests()
	    	.antMatchers(ApiUrls.DOMANDE_PSR_V1 + "/**").authenticated() //
	    	.antMatchers(ApiUrls.DOMANDE_DU_V1 + "/**").authenticated() //
	    	.antMatchers(ApiUrls.FASCICOLI_V1 + "/**").authenticated() //
	    	.antMatchers(ApiUrls.ESITI_ANTIMAFIA_V1 + "/**").authenticated() //
	    	//.antMatchers(ApiUrls.PIANI_COLTURALI_V1 + "/**").authenticated() //
	    	.antMatchers(ApiUrls.UTENTI_V1 + "/**").authenticated() //
	    	.anyRequest().permitAll()
	        .and()
	        .addFilterAfter(requestHeaderAuthenticationFilter(customAuthenticationManager()), RequestHeaderAuthenticationFilter.class)
	        .csrf().disable();	//Inserito per consentire l'upload dei file.    	
	    }
}