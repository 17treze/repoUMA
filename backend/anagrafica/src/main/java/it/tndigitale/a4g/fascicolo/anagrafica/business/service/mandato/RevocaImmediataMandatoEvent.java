package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.RichiestaRevocaImmediataDto;
import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;

public class RevocaImmediataMandatoEvent extends AbstractWrapperEvent<RichiestaRevocaImmediataDto> {
	
	private RichiestaRevocaImmediataDto richiestaRevocaImmediataDto;

	public RevocaImmediataMandatoEvent() {
	}
	
	public RevocaImmediataMandatoEvent(RichiestaRevocaImmediataDto richiestaRevocaImmediataDto) {
		this.richiestaRevocaImmediataDto = richiestaRevocaImmediataDto;
	}
	
	@Override
	public RichiestaRevocaImmediataDto getData() {
		return this.richiestaRevocaImmediataDto;
	}

	@Override
	public AbstractWrapperEvent<RichiestaRevocaImmediataDto> setData(RichiestaRevocaImmediataDto richiestaRevocaImmediataDto) {
		this.richiestaRevocaImmediataDto = richiestaRevocaImmediataDto;
		return this;
	}
}
