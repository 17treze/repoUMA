package it.tndigitale.a4g.uma.dto.clienti.builder;

import it.tndigitale.a4g.uma.business.persistence.entity.ClienteModel;
import it.tndigitale.a4g.uma.dto.clienti.ClienteDto;

public class ClienteBuilder {
	private ClienteDto clienteDto;

	public ClienteBuilder() {
		clienteDto = new ClienteDto();
	}

	public ClienteBuilder from(ClienteModel model) {
		clienteDto
		.setId(model.getId())
		.setCuaa(model.getCuaa())
		.setDenominazione(model.getDenominazione())
		.setIdDichiarazioneConsumi(model.getDichiarazioneConsumi().getId())
		.setIdFascicolo(model.getIdFascicolo());
		return this;
	}

	public ClienteDto build() {
		return clienteDto;
	}
}

