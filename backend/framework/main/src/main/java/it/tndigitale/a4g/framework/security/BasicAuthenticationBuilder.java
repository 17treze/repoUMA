package it.tndigitale.a4g.framework.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BasicAuthenticationBuilder {
	private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationBuilder.class);

	public void configureAutentication(String userName) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		Authentication authentication = new UsernamePasswordAuthenticationToken(userName, "",	authorities);
		logger.info("UsernamePasswordAuthenticationToken for [{}] : {}", userName, authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public static void clearAutentication() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
}
