package it.tndigitale.a4g.ags.service;

import it.tndigitale.a4g.ags.dto.AnagraficaAzienda;

public interface FascicoloAziendaleService {

    AnagraficaAzienda findAnagraficaAziendaByCuaa(String cuaa);

}
