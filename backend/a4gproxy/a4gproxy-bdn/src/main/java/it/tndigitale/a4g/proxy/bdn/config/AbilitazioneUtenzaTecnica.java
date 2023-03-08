package it.tndigitale.a4g.proxy.bdn.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AbilitazioneUtenzaTecnica {

	@Value("${bdn.scheduler.utenzatecnica}")
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
}