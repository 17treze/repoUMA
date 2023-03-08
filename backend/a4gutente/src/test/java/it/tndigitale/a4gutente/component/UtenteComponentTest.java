package it.tndigitale.a4gutente.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import it.tndigitale.a4g.framework.security.configuration.SecurityContextWrapper;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4gutente.codici.Ruoli;

class ImplGrantedAuthority implements GrantedAuthority {
	
	public String authority;
	
	public ImplGrantedAuthority(final String auth) {
		this.authority = auth;
	}

	@Override
	public String getAuthority() {
		return authority;
	}
}

class UtenteComponentTest {
	private SecurityContextWrapper securityContextWrapper;
	private UtenteComponent utenteComponent;
	
	public UtenteComponentTest() {
		securityContextWrapper = mock(SecurityContextWrapper.class);
		Authentication authentication = aAuthentication();
		doReturn(authentication).when(securityContextWrapper).getAuthentication();
		
		utenteComponent = new UtenteComponent();
//		TODO: al momento setComponents è stato integrato in frameworkk.UtenteComponent
		utenteComponent.setComponents(securityContextWrapper);
	}
	
	private Authentication aAuthentication() {
		Authentication authentication = mock(Authentication.class);
		Collection<ImplGrantedAuthority> grantedAuthorities = new ArrayList<ImplGrantedAuthority>();
		grantedAuthorities.add(new ImplGrantedAuthority("ROLE_auth1"));
		grantedAuthorities.add(new ImplGrantedAuthority("ROLE_a4gutente.utenti.importazione"));
		
		doReturn(grantedAuthorities).when(authentication).getAuthorities();
		doReturn(new Object()).when(authentication).getPrincipal();
		doReturn("name1").when(authentication).getName();
		return authentication;
    }	

	@Test
	void haRuolo() {
		Authentication auth = securityContextWrapper.getAuthentication();
		assertThat(auth.getName()).isEqualTo("name1");
		assertThat(utenteComponent.haRuolo("auth1")).isTrue();
		assertThat(utenteComponent.haRuolo(
				Ruoli.IMPORTAZIONE_MASSIVA_UTENTI)).isTrue();
		assertThat(utenteComponent.haUnRuolo(
				Ruoli.IMPORTAZIONE_MASSIVA_UTENTI, Ruoli.CREA_UTENTE)).isTrue();
		
	}

}
