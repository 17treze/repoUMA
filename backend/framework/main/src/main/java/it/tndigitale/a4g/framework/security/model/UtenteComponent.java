package it.tndigitale.a4g.framework.security.model;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.security.configuration.SecurityContextWrapper;

@Component
public class UtenteComponent {
    public static final String DEFAULT_ROLE_PREFIX = "ROLE_";
	
	private SecurityContextWrapper securityContext;
	
	@Autowired
    public UtenteComponent setComponents(
    		SecurityContextWrapper securityContext) {
		this.securityContext = securityContext;
		SecurityContextHolder.getContext().setAuthentication(securityContext.getAuthentication());
		return this;
	}
	
	public String utenza() {
		return autenticazione().getName();
	}
    
	public Authentication autenticazione(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

//  public Authentication autenticazione(){
//  return securityContext.getAuthentication();
//}
	
    public String username() {
        try {
            Authentication authentication = autenticazione();
            return (authentication!=null)? authentication.getName():null;
        } catch(Exception e) {
            return null;
        }
    }

    public Boolean checkUsernameFilter(String usernameFilter) {
	    String usernameAuthentication = username();
	    return usernameAuthentication!=null &&
               usernameFilter!=null &&
               usernameAuthentication.equals(usernameFilter);
    }
    
    public boolean haRuolo(Ruolo ruolo) {
        return haRuolo(ruolo.getCodiceRuolo());
    }

    public boolean haRuolo(String ruolo) {
        ruolo = DEFAULT_ROLE_PREFIX + ruolo;
        Authentication auth = autenticazione();

        if ((auth == null) || (auth.getPrincipal() == null)) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if (ruolo.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }

        return false;
    }
    
    public boolean haUnRuolo(Ruolo... ruolo) {
        if (ruolo == null || ruolo.length < 1) {
            return true;
        }
        boolean result = false;
        for (int i = 0; i < ruolo.length && !result; i++) {
            result = haRuolo(ruolo[i]);
        }
        return result;
    }
}
