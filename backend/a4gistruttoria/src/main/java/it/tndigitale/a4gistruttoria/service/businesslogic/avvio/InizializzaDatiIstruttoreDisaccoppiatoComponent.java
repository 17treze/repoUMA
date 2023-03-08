package it.tndigitale.a4gistruttoria.service.businesslogic.avvio;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreDisModel;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreDisPascoliModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;

@Component(InizializzaDatiIstruttoreDisaccoppiatoComponent.NOME_QUALIFICATORE)
public class InizializzaDatiIstruttoreDisaccoppiatoComponent implements InizializzaDatiIstruttore {
	
	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "DISACCOPPIATO";
	
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
		istruttoria.setDatiIstruttoreDisModel(clonaDatiDaIstruttorePrecedente(istruttoria, istruttoriaPrecedente.getDatiIstruttoreDisModel()));
		istruttoria.setDatiIstruttoreDisPascoli(clonaDatiPascoliDaIstruttorePrecedente(istruttoria, istruttoriaPrecedente.getDatiIstruttoreDisPascoli()));
		
	}

	protected DatiIstruttoreDisModel clonaDatiDaIstruttorePrecedente(IstruttoriaModel istruttoria, DatiIstruttoreDisModel datiIstruttoriaPrecedente) {
		if (datiIstruttoriaPrecedente == null) {
			return null;
		}
		DatiIstruttoreDisModel result = new DatiIstruttoreDisModel();
		BeanUtils.copyProperties(datiIstruttoriaPrecedente, result, "id", "versione");
		result.setIstruttoria(istruttoria);
		return result;
	}

	protected Set<DatiIstruttoreDisPascoliModel> clonaDatiPascoliDaIstruttorePrecedente(IstruttoriaModel istruttoria, Set<DatiIstruttoreDisPascoliModel> datiIstruttoriaPrecedente) {
		if (datiIstruttoriaPrecedente == null) {
			return null;
		}
		Set<DatiIstruttoreDisPascoliModel> pascoli = new HashSet<DatiIstruttoreDisPascoliModel>();
		for (DatiIstruttoreDisPascoliModel pascolo : datiIstruttoriaPrecedente) {
			pascoli.add(clonaDatiPascoloDaIstruttorePrecedente(istruttoria,pascolo));
		}

		return pascoli;
	}
	protected DatiIstruttoreDisPascoliModel clonaDatiPascoloDaIstruttorePrecedente(IstruttoriaModel istruttoria, DatiIstruttoreDisPascoliModel datiIstruttoriaPrecedente) {
		if (datiIstruttoriaPrecedente == null) {
			return null;
		}
		DatiIstruttoreDisPascoliModel result = new DatiIstruttoreDisPascoliModel();
		BeanUtils.copyProperties(datiIstruttoriaPrecedente, result, "id", "versione");
		result.setIstruttoria(istruttoria);
		return result;
	}
}
