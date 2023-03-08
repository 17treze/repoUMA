package it.tndigitale.a4gutente.event;

import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;
import it.tndigitale.a4gutente.dto.InfoPrivacyDto;

public class InfoPrivacyEvent  extends AbstractWrapperEvent<InfoPrivacyDto> {
	private InfoPrivacyDto infoPrivacyDto;

	public InfoPrivacyEvent() {
	}
	
	public InfoPrivacyEvent(InfoPrivacyDto infoPrivacyDto) {
		this.infoPrivacyDto = infoPrivacyDto;
	}
	
	@Override
	public InfoPrivacyDto getData() {
		return this.infoPrivacyDto;
	}
	
	@Override
	public AbstractWrapperEvent<InfoPrivacyDto> setData(InfoPrivacyDto infoPrivacyDto) {
		this.infoPrivacyDto = infoPrivacyDto;
		return this;
	}
}
