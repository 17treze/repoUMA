package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.business.persistence.repository.MacchinaAgsDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.MacchinaAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.MacchinaAgsFilter;

@Service
public class MacchineAgsService {
	
	@Autowired private MacchinaAgsDao macchinaAgsDao;

	public List<MacchinaAgsDto> getMacchine(MacchinaAgsFilter filter) throws Exception {
		return macchinaAgsDao.getMacchine(filter);
	}

}
