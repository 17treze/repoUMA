package it.tndigitale.a4g.framework.event.store;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 
 * @author ITE3279
 * 
 * Questa classe gestisce in modo multi-evento la tabella A4GT_EVENTSTORED rendendo possibile il filtro per tipo di evento fallito:
 * 1. per l'attivazione di questo scheduler si usa il property "it.tndigit.event.scheduler.multievent = true"; di conseguenza 
 *    si dovra' disattivare quello legacy (it.tndigit.event.scheduler) eliminandolo o impostandolo a false (it.tndigit.event.scheduler = false)
 * 2. per attivare un cron specifico per un tipo di evento si usa la sintassi "FQN.CLASSNAME + .multievent.cron.expr";
 *    ad es. per impostare un cron per il tipo di evento it.tndigitale.a4g.fascicolo.anagrafica.business.event.EndValidazioneFascicoloEvent
 *    si imposta "it.tndigitale.a4g.fascicolo.anagrafica.business.event.EndValidazioneFascicoloEvent.multievent.cron.expr=0 0/30 * * * ?"
 * 3. e' facoltativamente possibile anche impostare un tempo massimo (in ore) di retry. La regola e' simile a quella del
 *    punto 2, con l'eccezione che il suffisso sara' ".cron.max.hours" ("FQN.CLASSNAME + .cron.max.hours"); 
 *    ad esempio "it.tndigitale.a4g.fascicolo.anagrafica.business.event.EndValidazioneFascicoloEvent.multievent.cron.max.hours=12" 
 * 4. per applicare il cron generale si usa il property "it.tndigit.event.scheduler.multievent.cron = 00 00 07 * * *"; questo cron generale
 *    non considera i tipi di evento impostati al passo 2
 *    
 *  Esempio completo:
 *  #cron multi event filtered
 *  it.tndigit.event.scheduler.multievent = true
 *  it.tndigit.event.scheduler.multievent.cron = 00 00 07 * * *
 *  it.tndigitale.a4g.fascicolo.anagrafica.business.event.EndValidazioneFascicoloEvent.multievent.cron.expr=0 0/30 * * * ?
 *  it.tndigitale.a4g.fascicolo.anagrafica.business.event.EndValidazioneFascicoloEvent.multievent.cron.max.hours=12
 *  it.tndigitale.a4g.fascicolo.anagrafica.business.event.StartValidazioneFascicoloEvent.multievent.cron.expr=0 0/20 * * * ?
 */
@Component
@ConditionalOnProperty(prefix = "it.tndigit", name="event.scheduler.multievent", havingValue="true")
public class EventStoreSchedulerMultiEvent {
	static final Logger log = LoggerFactory.getLogger(EventStoreSchedulerMultiEvent.class);

	private static final String MAX_HOURS_SUFFIX = ".multievent.cron.max.hours";
	private static final String CRON_EXPR_SUFFIX = ".multievent.cron.expr";
	
	@Autowired
    private EventStoreService eventStoreService;
	
	@Autowired
	ThreadPoolTaskScheduler eventStoreSchedulerMultiEventPool;
	
	@Autowired
	private Environment env;
	
	private Map<String, Object> map = new HashMap<>();
	private List<String> filtroEventi;
	
	private void  getEventiDaFiltrare(){
		for(Iterator<PropertySource<?>> it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
	        var propertySource = it.next();
	        if (propertySource instanceof MapPropertySource) {
	            map.putAll(((MapPropertySource) propertySource).getSource());
	        }
	    }
		filtroEventi = map.keySet().stream().filter( key -> key.endsWith(CRON_EXPR_SUFFIX)).collect(Collectors.toList());
		filtroEventi.replaceAll(s -> s.replaceFirst(CRON_EXPR_SUFFIX + "$", ""));
	}
	
	@PostConstruct
	public void init() {
//		ottengo elenco classi personalizzate
		getEventiDaFiltrare();
		
		if (filtroEventi != null && !filtroEventi.isEmpty()) {
//				ottengo properties per classe
			filtroEventi.forEach(evento -> {
				String cronExprP = env.getProperty(evento + CRON_EXPR_SUFFIX);
				CronTrigger cronTrigger = null;
				try {
					cronTrigger = new CronTrigger(cronExprP);	
				}catch(Exception e) {
					log.error("Errore (il cron [" + cronExprP + "] non verra' attivato per l'evento" + evento + " ):", e);
					return;
				}
				
				String maxTimeP = env.getProperty(evento + MAX_HOURS_SUFFIX);
				if (StringUtils.hasLength(cronExprP)) {
					var task = new RunnableTask(evento);
					if (StringUtils.hasLength(maxTimeP)) {
						var maxTime = Long.parseLong(maxTimeP); 
						task = new RunnableTask(evento, cronExprP, maxTime);
					}
					eventStoreSchedulerMultiEventPool.schedule(task, cronTrigger);
				}
			});
		}
	}
    
    @Scheduled(cron = "${it.tndigit.event.scheduler.multievent.cron}")
    public void resubmitEvents() {

        List<EventStoredModel> events = eventStoreService.findAll();
        if (events == null || events.isEmpty())
            return;

//    	escludere cron personalizzati identificati da suffisso ".cronext.fixedrate.minutes"
        List<EventStoredModel> eventsFiltered = new ArrayList<>();
        if (filtroEventi != null && !filtroEventi.isEmpty()) {
        	eventsFiltered = events.stream().filter( eventoSingolo -> !filtroEventi.contains(eventoSingolo.getEvent())).collect(Collectors.toList());
        }
		for (EventStoredModel eventFiltered : eventsFiltered) {
			log.debug("tipo evento: {} - id{}", eventFiltered.getEvent(), eventFiltered.getId());
        	eventStoreService.reprocessEvent(eventFiltered.getId());
		}
    }
    
    class RunnableTask implements Runnable{
        private String evento;
        private String cronExpr; 
        private Long maxHoursInMillisec; // ore in millisecondi
        
        public RunnableTask(String evento){
        	this(evento, null, null);
        }
        
        public RunnableTask(String evento, String cronExpr, Long maxHours){
            this.evento = evento;
            this.cronExpr = cronExpr;
            this.maxHoursInMillisec = maxHours == null ? null : maxHours * 3600 * 1000;  // ore in millisecondi
        }
        
    	private LocalDateTime convertToLocalDateTimeViaSqlTimestamp(Date dateToConvert) {
            return new java.sql.Timestamp(
            	      dateToConvert.getTime()).toLocalDateTime();
    	}

        @Override
        public void run() {
        	var now = new Date();
        	List<EventStoredModel> events = eventStoreService.findAll();
            if (events == null || events.isEmpty())
                return;
            List<EventStoredModel> resultList = events.stream().filter( eventoSingolo -> eventoSingolo.getEvent().equalsIgnoreCase(evento)).collect(Collectors.toList());
            if (resultList != null && !resultList.isEmpty()) {
            	resultList.forEach( eventToProcess ->  {
            		if (maxHoursInMillisec != null) {
            			var generator = new CronSequenceGenerator(cronExpr);
            			var nowLocalDateTime = convertToLocalDateTimeViaSqlTimestamp(now);
            	        Date next = generator.next(now);
            	        var nextLocalDateTime = convertToLocalDateTimeViaSqlTimestamp(next);
            	        long diff = ChronoUnit.MILLIS.between(nowLocalDateTime, nextLocalDateTime);
            	        
            	        log.debug("date      now:{} - next {}",now, next);
            	        log.debug("diff {}", diff);
            	        if (eventToProcess.getNumberOfRetry() * diff > maxHoursInMillisec) {
            	        	return;
            	        }
            		}
            		log.debug("[RUN] tipo evento: {} - id{}", eventToProcess.getEvent(), eventToProcess.getId());            		
            		eventStoreService.reprocessEvent(eventToProcess.getId());
            	});
            }
        }
    }
}
