package it.tndigitale.a4g.framework.security.model;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.security.configuration.SecurityContextWrapper;

@Component
public class UtenteComponent {
	public static final String DEFAULT_ROLE_PREFIX = "ROLE_";
	
	private static final Logger logger = LoggerFactory.getLogger(UtenteComponent.class);
	
	private static final String TOKEN_KEY_PORTALE = "Authorization";
	
	@Value("${wso2Host}")
	private String wso2Host;
	
	private SecurityContextWrapper securityContext;
	
	@Autowired
	public UtenteComponent setComponents(SecurityContextWrapper securityContext) {
		this.securityContext = securityContext;
		SecurityContextHolder.getContext().setAuthentication(securityContext.getAuthentication());
		return this;
	}
	
	public String utenza() {
		return autenticazione().getName();
	}
	
	public Authentication autenticazione() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	//  public Authentication autenticazione(){
	//  return securityContext.getAuthentication();
	//}
	
	public String username() {
		try {
			// Authentication authentication = autenticazione();
			// return (authentication!=null)? authentication.getName():null;
			return this.getWso2Username();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public Boolean checkUsernameFilter(String usernameFilter) {
		String usernameAuthentication = username();
		return usernameAuthentication != null && usernameFilter != null
				&& usernameAuthentication.equals(usernameFilter);
	}
	
	public boolean haRuolo(Ruolo ruolo) {
		return haRuolo(ruolo.getCodiceRuolo());
	}
	
	public boolean haRuolo(String ruolo) {
		ruolo = DEFAULT_ROLE_PREFIX + ruolo;
		Authentication auth = autenticazione();
		logger.info("Authentication: " + auth);
		
		String utente = username();
		logger.info("Utente: " + utente);
		if (utente == null) {
			return false;
		}
		
		//		Authentication auth = autenticazione();
		//		
		//		if ((auth == null) || (auth.getPrincipal() == null)) {
		//			return false;
		//		}
		
		return true;
		//        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		//
		//        if (authorities == null) {
		//            return false;
		//        }
		//
		//        for (GrantedAuthority grantedAuthority : authorities) {
		//            if (ruolo.equals(grantedAuthority.getAuthority())) {
		//                return true;
		//            }
		//        }
		//
		//        return false;
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
	
	/**
	 * <p>
	 * Verifica se l'access-token ricevuto da wso2 risulta valido
	 * </p>
	 *
	 * @param request
	 */
	public String getWso2Username() {
		
		try {
			StringBuilder command = new StringBuilder(
					"curl -k -u admin:admin -H 'Content-Type: application/x-www-form-urlencoded' -X POST --data token=");
			command.append(URLEncoder.encode(utenza(), StandardCharsets.UTF_8.toString()));
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
			
			if (active) {
				return (String) oauth2Response.get("username");
			}
			return null;
		}
		catch (Exception e) {
			logger.error("Exception:", e);
			return null;
		}
	}
}
