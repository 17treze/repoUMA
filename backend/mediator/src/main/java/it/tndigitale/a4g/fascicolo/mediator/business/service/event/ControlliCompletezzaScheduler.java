package it.tndigitale.a4g.fascicolo.mediator.business.service.event;

import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.ControlliCompletezzaFascicoloService;
import it.tndigitale.a4g.fascicolo.mediator.business.service.scheduler.AbilitazioneUtenzaTecnica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ControlliCompletezzaScheduler {

	@Autowired
    private ControlliCompletezzaFascicoloService controlliCompletezzaFascicoloService;

    @Autowired
    private AbilitazioneUtenzaTecnica abilitazioneUtenzaTecnica;
    

    @Scheduled(cron = "${it.tndigit.mediator.scheduler.fascicolo.controlli.completezza.cron}")
    public void resubmitEvents() {
        abilitazioneUtenzaTecnica.configuraUtenzaTecnica();
        controlliCompletezzaFascicoloService.gestisciFascicoloControlloCompletezza();
    }
}
