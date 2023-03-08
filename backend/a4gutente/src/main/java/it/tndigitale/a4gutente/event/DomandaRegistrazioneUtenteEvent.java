package it.tndigitale.a4gutente.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneUtenteDto;

/**
 * @author ite3279
 */
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
public class DomandaRegistrazioneUtenteEvent extends AbstractWrapperEvent<DomandaRegistrazioneUtenteDto> {
	private DomandaRegistrazioneUtenteDto domandaRegistrazioneUtenteDto;
	
	public DomandaRegistrazioneUtenteEvent() {
	}
	
	public DomandaRegistrazioneUtenteEvent(DomandaRegistrazioneUtenteDto domandaRegistrazioneUtente) {
		this.domandaRegistrazioneUtenteDto = domandaRegistrazioneUtente;
	}
	
	@Override
	public DomandaRegistrazioneUtenteDto getData() {
		return this.domandaRegistrazioneUtenteDto;
	}

	@Override
	public AbstractWrapperEvent<DomandaRegistrazioneUtenteDto> setData(DomandaRegistrazioneUtenteDto domandaRegistrazioneUtente) {
		this.domandaRegistrazioneUtenteDto = domandaRegistrazioneUtente;
		return this;
	}
}
