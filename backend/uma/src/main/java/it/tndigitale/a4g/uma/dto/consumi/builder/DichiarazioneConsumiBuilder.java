package it.tndigitale.a4g.uma.dto.consumi.builder;

import java.time.LocalDateTime;
import java.util.List;

import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.PrelievoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;

public class DichiarazioneConsumiBuilder {

	private DichiarazioneConsumiDto dichiarazioneConsumiDto;

	public DichiarazioneConsumiBuilder() {
		dichiarazioneConsumiDto= new DichiarazioneConsumiDto();
	}

	public DichiarazioneConsumiBuilder from(DichiarazioneConsumiModel model) {
		dichiarazioneConsumiDto
		.setCfRichiedente(model.getCfRichiedente())
		.setDataConduzione(model.getDataConduzione())
		.setDataPresentazione(model.getDataPresentazione())
		.setDataProtocollazione(model.getDataProtocollazione())
		.setId(model.getId())
		.setProtocollo(model.getProtocollo())
		.setStato(model.getStato())
		.setMotivazioneAccisa(model.getMotivazioneAccisa());
		return this;
	}

	public DichiarazioneConsumiBuilder from(RichiestaCarburanteModel richiestaModel) {
		dichiarazioneConsumiDto
		.setCampagnaRichiesta(richiestaModel.getCampagna())
		.setCuaa(richiestaModel.getCuaa())
		.setDenominazione(richiestaModel.getDenominazione())
		.setIdRichiesta(richiestaModel.getId());
		return this;
	}

	public DichiarazioneConsumiBuilder withRimanenza(CarburanteCompletoDto rimanenza) {
		dichiarazioneConsumiDto.setRimanenza(rimanenza);
		return this;
	}
	
	public DichiarazioneConsumiBuilder withDatiPrelievi(LocalDateTime dataLimitePrelievi, List<PrelievoModel> prelievi) {
		dichiarazioneConsumiDto.setHaPrelieviOltreLimite(prelievi.stream().anyMatch(prelievo -> prelievo.getData().isAfter(dataLimitePrelievi)));
		dichiarazioneConsumiDto.setDataLimitePrelievi(dataLimitePrelievi);
		return this;
	}
	
	public DichiarazioneConsumiDto build() {
		return dichiarazioneConsumiDto;
	}
}
