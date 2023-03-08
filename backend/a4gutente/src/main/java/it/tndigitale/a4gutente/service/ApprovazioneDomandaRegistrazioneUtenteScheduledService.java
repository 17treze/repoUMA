package it.tndigitale.a4gutente.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApprovazioneDomandaRegistrazioneUtenteScheduledService implements IApprovazioneDomandaRegistrazioneUtenteScheduledService {

	private static final Logger logger = LoggerFactory.getLogger(ApprovazioneDomandaRegistrazioneUtenteScheduledService.class);

	@Value("${automazioneApprovazione.utenzatecnica.username}")
	private String utenzaTecnica;
	@Autowired
	private ApprovazioneDomandeRegistrazioneUtenteService service;
	
	@Scheduled(cron = "${cron.expression.automazioneApprovazione}")
	@Override
	@Transactional
	public void automazioneApprovazione() throws Exception {
		configuraUtenzaTecnica();
		try {
			service.automazioneApprovazione();	
		} finally {
			clearAutenticazione();
		}
	}
	
	protected void configuraUtenzaTecnica() {
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
