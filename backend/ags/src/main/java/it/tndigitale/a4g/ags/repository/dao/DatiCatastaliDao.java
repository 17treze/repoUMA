package it.tndigitale.a4g.ags.repository.dao;

import java.util.Date;

import it.tndigitale.a4g.ags.dto.RegioneCatastale;

public interface DatiCatastaliDao {

	RegioneCatastale getRegioneByCodNazionaleAndDataValidita(String codNazionale, Date dataValidita);

}
