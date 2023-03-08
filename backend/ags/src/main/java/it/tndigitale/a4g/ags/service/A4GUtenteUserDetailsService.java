/**
 * 
 */
package it.tndigitale.a4g.ags.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.security.service.UtenteClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class A4GUtenteUserDetailsService implements UserDetailsService {
	@Autowired
	private UtenteClient a4gClient;

	private static Logger logger = LoggerFactory.getLogger(A4GUtenteUserDetailsService.class);
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		List<String> ruoli = null;
		try {
			ruoli = a4gClient.getRuoliUtente(username);
		} catch (Exception e) {
			logger.error("Errore caricando i ruoli per l'utente {}", username, e);
			//throw new UsernameNotFoundException("Problemi nel caricamento dei ruoli per l'utente " + username);
		}
		if (ruoli != null) {
			ruoli.stream().forEach(ruolo -> authorities.add(new SimpleGrantedAuthority(UtenteComponent.DEFAULT_ROLE_PREFIX + ruolo)));
		}
		UserDetails user = new UserDetails() {

			private static final long serialVersionUID = 1977956860379506497L;

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}

			@Override
			public boolean isAccountNonLocked() {
				return true;
			}

			@Override
			public boolean isAccountNonExpired() {
				return true;
			}

			@Override
			public String getUsername() {
				return username;
			}

			@Override
			public String getPassword() {
				return "***";
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return authorities;
			}
		};
		return user;
	}
}
