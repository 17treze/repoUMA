package it.tndigitale.a4g.uma.dto.richiesta.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.uma.business.persistence.entity.ConsumiClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbisognoModel;
import it.tndigitale.a4g.uma.dto.richiesta.FabbisognoDto;

public class FabbisogniBuilder {

	private List<FabbisognoDto> fabbisogni;

	public FabbisogniBuilder() {
		fabbisogni = new ArrayList<>();
	}

	public FabbisogniBuilder withFabbisogni(List<FabbisognoModel> fabbisogni) {
		if (CollectionUtils.isEmpty(fabbisogni)) { return this; }
		fabbisogni.forEach(f -> 
		this.fabbisogni.add(new FabbisognoDto()
				.setCarburante(f.getCarburante())
				.setQuantita(f.getQuantita())));
		return this;
	}

	public FabbisogniBuilder withFabbisogniClienti(List<ConsumiClienteModel> consumi) {
		if (CollectionUtils.isEmpty(consumi)) { return this; }
		consumi.forEach(c -> 
		this.fabbisogni.add(new FabbisognoDto()
				.setCarburante(c.getCarburante())
				.setQuantita(c.getQuantita())));
		return this;
	}

	public List<FabbisognoDto> build() {
		return this.fabbisogni;
	}
}
