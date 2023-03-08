package it.tndigitale.a4gistruttoria.service.businesslogic.avvio;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreZootecniaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;

@Component(InizializzaDatiIstruttoreZootecniaComponent.NOME_QUALIFICATORE)
public class InizializzaDatiIstruttoreZootecniaComponent implements InizializzaDatiIstruttore {

    public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "ZOOTECNIA";
    private static final String ID = "id";
    private static final String VERSIONE = "versione";

    @Autowired
    private IstruttoriaDao istruttoriaDao;

    @Override
    public void inizializzaDati(IstruttoriaModel istruttoria) {
        List<IstruttoriaModel> istruttorieSostegno =
                istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(istruttoria.getDomandaUnicaModel().getId(), istruttoria.getSostegno());
        IstruttoriaModel istruttoriaPrecedente = istruttorieSostegno.stream()
                .filter(i -> !istruttoria.getId().equals(i.getId()))
                .max(Comparator.comparingLong(IstruttoriaModel::getId))
                .orElse(null);
        if (istruttoriaPrecedente != null) {
            clonaDatiDaIstruttoriaPrecedente(istruttoria, istruttoriaPrecedente);
        }
    }

    protected void clonaDatiDaIstruttoriaPrecedente(IstruttoriaModel istruttoria, IstruttoriaModel istruttoriaPrecedente) {
        istruttoria.setDatiIstruttoreZootecnia(clonaDatiZootecniaDaIstruttorePrecedente(istruttoria, istruttoriaPrecedente.getDatiIstruttoreZootecnia()));
    }

    protected DatiIstruttoreZootecniaModel clonaDatiZootecniaDaIstruttorePrecedente(IstruttoriaModel istruttoria, DatiIstruttoreZootecniaModel datiIstruttoriaPrecedente) {
        if (datiIstruttoriaPrecedente == null) {
            return null;
        }
        DatiIstruttoreZootecniaModel result = new DatiIstruttoreZootecniaModel();
        BeanUtils.copyProperties(datiIstruttoriaPrecedente, result, ID, VERSIONE);
        result.setIstruttoria(istruttoria);

        return result;
    }
}
