package it.tndigitale.a4g.srt.repository.dao;

import it.tndigitale.a4g.srt.dto.ImportoRichiesto;

import java.util.Date;
import java.util.List;

public interface ImpreseDao {
    /**
     * Metodo di calcolo degli importi richiesti da una impresa da una certa data in poi
     *
     * @param cuaa
     * @return Lista degli importi richiesti
     */
    List<ImportoRichiesto> getImportoRichiesto(String cuaa, Date dataDa);
}