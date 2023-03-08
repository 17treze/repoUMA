package it.tndigitale.a4g.uma.business.service.protocollo;

import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;
import it.tndigitale.a4g.uma.dto.protocollo.ProtocollaDocumentoUmaDto;


public class ProtocollaDocumentoUmaEvent extends AbstractWrapperEvent<ProtocollaDocumentoUmaDto> {

	private ProtocollaDocumentoUmaDto protocollaDocumentoUmaDto;

	public ProtocollaDocumentoUmaEvent() {
	}

	public ProtocollaDocumentoUmaEvent(ProtocollaDocumentoUmaDto protocollaDocumentoUmaDto) {
		this.protocollaDocumentoUmaDto = protocollaDocumentoUmaDto;
	}

	@Override
	public ProtocollaDocumentoUmaDto getData() {
		return this.protocollaDocumentoUmaDto;
	}

	@Override
	public AbstractWrapperEvent<ProtocollaDocumentoUmaDto> setData(ProtocollaDocumentoUmaDto protocollaDocumentoUmaDto) {
		this.protocollaDocumentoUmaDto = protocollaDocumentoUmaDto;
		return this;
	}

	@Override
	public int hashCode() {
		var prime = 31;
		int result = super.hashCode();
		result = prime * result + ((protocollaDocumentoUmaDto == null) ? 0 : protocollaDocumentoUmaDto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProtocollaDocumentoUmaEvent other = (ProtocollaDocumentoUmaEvent) obj;
		if (protocollaDocumentoUmaDto == null) {
			if (other.protocollaDocumentoUmaDto != null)
				return false;
		} else if (!protocollaDocumentoUmaDto.equals(other.protocollaDocumentoUmaDto)) {
			return false;
		}
		return true;
	}

}
