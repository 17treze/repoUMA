package it.tndigitale.a4g.ags.repository.dao;

import it.tndigitale.a4g.ags.dto.AnagraficaAziendaBaseData;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaDocumentData;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaEnteData;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaFascicoloData;

import java.util.Map;

public interface FascicoloAziendaleDao {

    long findPkCuaaByCuaa(String cuaa);

    AnagraficaAziendaDocumentData loadDocumentData(long pkCuaa);

    AnagraficaAziendaBaseData loadAnagraficaBaseData(long pkCuaa);

    AnagraficaAziendaEnteData loadEnteData(long pkCuaa);

    AnagraficaAziendaFascicoloData loadFascicoloData(long pkCuaa);

    Map<Integer, Integer> loadDocumentTypeMapping();
}
