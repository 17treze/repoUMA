package it.tndigitale.a4gistruttoria.service.businesslogic.domanda;

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
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.time.Clock;



@Component
public class AvvioIstruttorieDomandaScheduledComponent {
	
	private Logger logger = LoggerFactory.getLogger(AvvioIstruttorieDomandaScheduledComponent.class);

	@Value("${verificaperiodica.utenzatecnica.username}")
	private String utenzaTecnica;
	@Autowired
	private AvvioIstruttorieDomandeAnnualiService avvioService;
    @Autowired
    private Clock clock;

	@Scheduled(cron = "${avviosaldi.cron.expression}")
	public void avvioSaldi() {
		int annoCorrente = clock.today().getYear();
		logger.info("Avvio istruttorie per l'anno corrente {}", annoCorrente);		
		configuraUtenzaTecnica();
		try  {
			avvioService.avvioSaldi(annoCorrente);
		} finally {
			clearAutenticazione();
		}
		logger.info("Terminato avvio istruttorie per l'anno corrente {}", annoCorrente);		
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
	
	public static void clearAutenticazione() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }	
}
