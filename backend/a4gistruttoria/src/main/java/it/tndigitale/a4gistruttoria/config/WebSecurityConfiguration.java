package it.tndigitale.a4gistruttoria.config;

/**
 * 
 */
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import it.tndigitale.a4g.framework.security.service.A4GUserDetailsService;
import it.tndigitale.a4gistruttoria.A4gIstruttoriaConstants;
import it.tndigitale.a4gistruttoria.api.ApiUrls;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	public WebSecurityConfiguration() {
		super();
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
	}

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
	public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter(final AuthenticationManager authenticationManager) {
		RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter() {

			@Override
			protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
				// Se non Ã¨ presente il codice fiscale uso quello di ADD
				if (!isCodiceFiscalePresent(request)) {
					String tokenADD = request.getHeader(A4gIstruttoriaConstants.HEADER_UPN);
					return tokenADD;
				}
				return super.getPreAuthenticatedPrincipal(request);
			}

			protected boolean isCodiceFiscalePresent(HttpServletRequest request) {
				String cfUtente = request.getHeader(A4gIstruttoriaConstants.HEADER_CF);
				Boolean isPresent = (cfUtente != null) && (!cfUtente.isEmpty());
				return isPresent;
			}

		};
		filter.setAuthenticationManager(authenticationManager);
		filter.setPrincipalRequestHeader(A4gIstruttoriaConstants.HEADER_CF);
		filter.setExceptionIfHeaderMissing(false);
		return filter;
	}

	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {
		return authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().authorizeRequests()
		.antMatchers(ApiUrls.API_V1 + "/**").authenticated() //
				// .antMatchers("/api/v1/domande/**")
				// .hasAnyRole(Ruoli.GET_ISTRUTTORIA.getCodiceRuolo(), Ruoli.EDIT_ISTRUTTORIA.getCodiceRuolo())
				// .anyRequest().permitAll()
				.and().addFilterAfter(requestHeaderAuthenticationFilter(customAuthenticationManager()), RequestHeaderAuthenticationFilter.class).csrf().disable();
	}
}
