package it.tndigitale.a4g.ags.repository.dao;

import it.tndigitale.a4g.ags.dto.Cuaa;
import it.tndigitale.a4g.ags.dto.InfoDomandaDU;

public interface CuaaDao {

	public Cuaa getInfoCuaa(String cuaa);
	
	public InfoDomandaDU getInfoDomandaDU(String cuaa, Integer anno);

}
