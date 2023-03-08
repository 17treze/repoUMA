package it.tndigitale.a4gistruttoria.service.businesslogic.avvio;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreSuperficieModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;

@Component(InizializzaDatiIstruttoreSuperficieComponent.NOME_QUALIFICATORE)
public class InizializzaDatiIstruttoreSuperficieComponent implements InizializzaDatiIstruttore {

    public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "SUPERFICIE";
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
        istruttoria.setDatiIstruttoreSuperficie(clonaDatiSuperficiDaIstruttorePrecedente(istruttoria, istruttoriaPrecedente.getDatiIstruttoreSuperficie()));
    }

    protected DatiIstruttoreSuperficieModel clonaDatiSuperficiDaIstruttorePrecedente(IstruttoriaModel istruttoria, DatiIstruttoreSuperficieModel datiIstruttoriaPrecedente) {
        if (datiIstruttoriaPrecedente == null) {
            return null;
        }
        DatiIstruttoreSuperficieModel result = new DatiIstruttoreSuperficieModel();
        BeanUtils.copyProperties(datiIstruttoriaPrecedente, result, ID, VERSIONE);
        result.setIstruttoria(istruttoria);

        return result;
    }
}
