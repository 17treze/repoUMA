package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.builder;

import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaMotorizzataModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.MacchinaDto;

@Component
public class MacchinaDtoBuilder {
	private MacchinaDto macchinaDto;

	public MacchinaDtoBuilder() {
		macchinaDto = new MacchinaDto();
	}

	public MacchinaDtoBuilder newDto() {
		macchinaDto = new MacchinaDto();
		return this;
	}

	public MacchinaDtoBuilder from(MacchinaModel model) {
		macchinaDto.setId(model.getId()).setModello(model.getModello()).setTarga(model.getTarga()).setTipologia(model.getSottotipoMacchinario().getClassefunzionale().getTipologia().getDescrizione());

		return this;
	}

	public MacchinaDtoBuilder withAlimentazione(MacchinaModel model) {
		if (model instanceof MacchinaMotorizzataModel) {
			macchinaDto.setAlimentazione(((MacchinaMotorizzataModel) model).getAlimentazione());
		}
		return this;
	}

	public MacchinaDto build() {
		return macchinaDto;
	}
}
