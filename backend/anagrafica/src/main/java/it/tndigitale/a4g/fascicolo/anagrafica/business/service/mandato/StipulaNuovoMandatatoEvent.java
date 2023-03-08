package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.ApriFascicoloDto;
import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;

public class StipulaNuovoMandatatoEvent extends AbstractWrapperEvent<ApriFascicoloDto> {
	
	private ApriFascicoloDto apriFascicoloDto;

	public StipulaNuovoMandatatoEvent() {
	}
	
	public StipulaNuovoMandatatoEvent(ApriFascicoloDto apriFascicoloDto) {
		this.apriFascicoloDto = apriFascicoloDto;
	}
	
	@Override
	public ApriFascicoloDto getData() {
		return this.apriFascicoloDto;
	}

	@Override
	public AbstractWrapperEvent<ApriFascicoloDto> setData(ApriFascicoloDto apriFascicoloDto) {
		this.apriFascicoloDto = apriFascicoloDto;
		return this;
	}
}
