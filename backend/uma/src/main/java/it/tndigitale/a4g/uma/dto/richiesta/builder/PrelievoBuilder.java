package it.tndigitale.a4g.uma.dto.richiesta.builder;

import it.tndigitale.a4g.uma.business.persistence.entity.DistributoreModel;
import it.tndigitale.a4g.uma.business.persistence.entity.PrelievoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.distributori.DistributoreDto;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;

public class PrelievoBuilder {

	private PrelievoDto prelievo;

	public PrelievoBuilder() {
		prelievo = new PrelievoDto();
	}

	public PrelievoBuilder newDto() {
		prelievo = new PrelievoDto();
		return this;
	}

	public PrelievoBuilder withDistributore(DistributoreModel distributore) {
		prelievo.setDistributore(new DistributoreDto()
				.setId(distributore.getId())
				.setComune(distributore.getComune())
				.setDenominazione(distributore.getDenominazione())
				.setIdentificativo(distributore.getIdentificativo())
				.setIndirizzo(distributore.getIndirizzo())
				.setProvincia(distributore.getProvincia()));
		return this;
	}

	public PrelievoBuilder withAzienda(RichiestaCarburanteModel richiesta) {
		prelievo
		.setCuaa(richiesta.getCuaa())
		.setDenominazione(richiesta.getDenominazione());
		return this;
	}

	public PrelievoBuilder withPrelievo(PrelievoModel prelievoModel) {
		prelievo
		.setId(prelievoModel.getId())
		.setData(prelievoModel.getData())
		.setIsConsegnato(prelievoModel.getConsegnato())
		.setEstremiDocumentoFiscale(prelievoModel.getEstremiDocumentoFiscale())
		.setCarburante(new CarburanteDto()
				.setBenzina(prelievoModel.getBenzina() != null ? prelievoModel.getBenzina() : 0)
				.setGasolio(prelievoModel.getGasolio() != null ? prelievoModel.getGasolio() : 0)
				.setGasolioSerre(prelievoModel.getGasolioSerre() != null ? prelievoModel.getGasolioSerre() : 0));
		return this;
	}

	public PrelievoDto build() {
		return this.prelievo;
	}
}
