package it.tndigitale.a4gistruttoria.processo.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ProcessoPublisher {
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public void handleEvent(final ProcessoEvent processoEvent) {
		ProcessoEventHandler processoEventHandler = new ProcessoEventHandler(this, processoEvent);
		applicationEventPublisher.publishEvent(processoEventHandler);
	}

}