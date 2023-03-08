package it.tndigitale.a4g.srt.services;

import it.tndigitale.a4g.srt.dto.ImportoRichiesto;

import java.util.Date;
import java.util.List;

public interface ImpreseService {

    List<ImportoRichiesto> getImportoRichiesto(String cuua, Date dataModifica);
}

