package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.business.persistence.repository.FabbricatiAgsDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.FabbricatoAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.FabbricatoAgsFilter;
import it.tndigitale.a4g.framework.time.Clock;

@Service
public class FabbricatiAgsService {

	@Autowired private FabbricatiAgsDao fabbricatiAgsDao;
	
	@Autowired private Clock clock;

	public List<FabbricatoAgsDto> getFabbricati(FabbricatoAgsFilter filter) {
		if (filter.getData() == null) {
			filter.setData(clock.now());
		}
		return fabbricatiAgsDao.getFabbricati(filter.getCuaa(), filter.getData(), filter.getProvince(), filter.getTitoliConduzione());
	}

}
