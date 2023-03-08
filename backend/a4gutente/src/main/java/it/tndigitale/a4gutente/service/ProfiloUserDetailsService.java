/**
 * 
 */
package it.tndigitale.a4gutente.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gutente.config.Costanti;
import it.tndigitale.a4gutente.repository.model.Ruolo;

/**
 * @author it417
 *
 */
@Service
public class ProfiloUserDetailsService implements UserDetailsService {

	@Autowired
	private IUtenteService utenteService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String utenza) throws UsernameNotFoundException {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		List<Ruolo> ruoli = utenteService.caricaRuoliUtente(utenza);
		if (ruoli != null) {
			ruoli.stream().forEach(ruolo -> authorities.add(new SimpleGrantedAuthority(Costanti.DEFAULT_ROLE_PREFIX + ruolo.getIdentificativo())));
		}
		UserDetails user = new UserDetails() {

			private static final long serialVersionUID = 6477216455468967827L;

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
				return utenza;
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
