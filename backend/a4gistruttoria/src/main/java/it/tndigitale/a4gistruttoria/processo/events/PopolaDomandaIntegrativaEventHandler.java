package it.tndigitale.a4gistruttoria.processo.events;

import java.util.List;

import org.springframework.context.ApplicationEvent;

public class PopolaDomandaIntegrativaEventHandler extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private List<Long> domandaUnicaList;

	public PopolaDomandaIntegrativaEventHandler(Object source, List<Long> domandaUnicaList) {
		super(source);
		this.domandaUnicaList = domandaUnicaList;
	}

	public List<Long> getDomandaUnicaList() {
		return domandaUnicaList;
	}

	public void setDomandaUnicaList(List<Long> domandaUnicaList) {
		this.domandaUnicaList = domandaUnicaList;
	}

}