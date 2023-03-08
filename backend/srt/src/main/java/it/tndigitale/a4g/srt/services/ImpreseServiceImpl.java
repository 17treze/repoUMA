package it.tndigitale.a4g.srt.services;

import it.tndigitale.a4g.srt.dto.ImportoRichiesto;
import it.tndigitale.a4g.srt.repository.dao.ImpreseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ImpreseServiceImpl implements ImpreseService {

    @Autowired
    private ImpreseDao impreseDao;

    @Override
    public List<ImportoRichiesto> getImportoRichiesto(String cuaa, Date dataModifica) {
        return impreseDao.getImportoRichiesto(cuaa, dataModifica);
    }
}
