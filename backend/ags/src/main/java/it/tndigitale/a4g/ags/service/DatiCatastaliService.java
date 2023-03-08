package it.tndigitale.a4g.ags.service;

import java.util.Date;

import it.tndigitale.a4g.ags.dto.RegioneCatastale;

public interface DatiCatastaliService {

	RegioneCatastale getRegioneByCodNazionaleAndDataValidita(String codNazionale, Date dataValidita);
}
