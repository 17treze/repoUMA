package it.tndigitale.a4gistruttoria.processo.events;

import org.springframework.context.ApplicationEvent;

public class ProcessoEventHandler extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private ProcessoEvent processoEvent;

	public ProcessoEventHandler(Object source, ProcessoEvent processoEvent) {
		super(source);
		this.processoEvent = processoEvent;
	}

	public ProcessoEvent geProcessoEvent() {
		return this.processoEvent;
	}
}