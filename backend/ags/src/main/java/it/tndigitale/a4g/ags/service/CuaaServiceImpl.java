package it.tndigitale.a4g.ags.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.ags.dto.Cuaa;
import it.tndigitale.a4g.ags.repository.dao.CuaaDao;
import it.tndigitale.a4g.ags.dto.InfoDomandaDU;

@Service
public class CuaaServiceImpl implements CuaaService {

	@Autowired
	CuaaDao daoCuaa;

	@Override
	public Cuaa getInfoCuaa(String cuaa) {

		return daoCuaa.getInfoCuaa(cuaa);
	}
	
	@Override
	public InfoDomandaDU findByCuaaIntestatarioAndAnnoRiferimento(String cuaa, Integer anno) {

		return daoCuaa.getInfoDomandaDU(cuaa, anno);
	}

}
