package it.tndigitale.a4g.fascicolo.mediator.business.service.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AbilitazioneUtenzaTecnica {

	@Value("${it.tndigit.mediator.scheduler.fascicolo.controlli.completezza.username}")
	private String utenzaTecnica;

	public void configuraUtenzaTecnica() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				utenzaTecnica,
				"",
				authorities
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	protected void clearAutenticazione() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

}
