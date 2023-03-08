package it.tndigitale.a4g.fascicolo.anagrafica.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.FascicoloAgsDao;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.FascicoloAgsDto;
import it.tndigitale.a4g.framework.support.PersonaSelector;
import it.tndigitale.a4g.framework.time.Clock;

@Service
public class SoggettoService {

	@Autowired
	private FascicoloAgsDao fascicoloAgsDao;

	@Autowired
	private Clock clock;

	public List<CaricaAgsDto> getSoggettiFascicoloAziendale(String cuaa) {
		return fascicoloAgsDao.getTitolariRappresentantiLegali(cuaa);
	}

	public List<CaricaAgsDto> getEredi(String cuaa) {

		// controlla che il fascicolo sia di persona fisica
		if (!PersonaSelector.isPersonaFisica(cuaa)) {
			return new ArrayList<>();
		}
		// controlla che il titolare sia effettivamente deceduto
		FascicoloAgsDto fascicolo = fascicoloAgsDao.getFascicolo(cuaa, clock.now());

		Assert.isTrue(fascicolo.getDataMorteTitolare() != null, "Non è possibile recuperare gli eredi. Il titolare non risulta deceduto.");

		// recupera gli eredi registrati nel fascicolo aziendale
		var eredi = fascicoloAgsDao.getEredi(cuaa);
		Assert.isTrue(!CollectionUtils.isEmpty(eredi), "Il campo data decesso risulta valorizzato. E' necessario inserire nel fascicolo un erede.");
		return eredi;
	}

}
