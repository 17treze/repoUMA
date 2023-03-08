package it.tndigitale.a4g.ags.service;

import it.tndigitale.a4g.ags.dto.Cuaa;
import it.tndigitale.a4g.ags.dto.InfoDomandaDU;

public interface CuaaService {

	public Cuaa getInfoCuaa(String cuaa);

	InfoDomandaDU findByCuaaIntestatarioAndAnnoRiferimento(String cuaa, Integer anno);
}
