package it.tndigitale.a4g.framework.event.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "it.tndigit", name="event.scheduler", havingValue="true")
public class EventStoreScheduler {

	@Autowired
    private EventStoreService eventStoreService;
    

    @Scheduled(cron = "${it.tndigit.event.cron}")
    public void resubmitEvents() {
        List<EventStoredModel> events = eventStoreService.findAll();
        if (events == null || events.isEmpty())
            return;

		for(EventStoredModel event : events) {
        		eventStoreService.reprocessEvent(event.getId());
		}
    }
}
