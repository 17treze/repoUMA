package it.tndigitale.a4gistruttoria.action;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

/**
 * @author a.fiori
 *
 */
@Component
public class IstruttoriaComponent {
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	
	/**
	 * Recupera l'istruttoria più recente da una domanda unica utilizzando le relazioni JPA.
	 * Deve essere utilizzata in un contesto transazionale per il corretto funzionamento
	 * 
	 * @param domanda unica
	 * @param sostegno da ricercare
	 * @return
	 */
	public IstruttoriaModel getUltimaIstruttoria(DomandaUnicaModel domanda, Sostegno sostegno) {
		List<IstruttoriaModel> istruttorie = domanda.getA4gtLavorazioneSostegnos().stream()
				.filter(istr -> sostegno.equals(istr.getSostegno()))
				.collect(Collectors.toList());

		return !CollectionUtils.isEmpty(istruttorie) 
					? istruttorie.stream()
						.max(Comparator.comparingLong(IstruttoriaModel::getId))
						.get() 
					: null;
	}
	
	/**
	 * Esclude tutte le istruttorie in stato debiti
	 * 
	 * @param domanda
	 * @param sostegno
	 * @return
	 */
	public IstruttoriaModel getUltimaIstruttoriaPagamenti(DomandaUnicaModel domanda, Sostegno sostegno) {
		List<IstruttoriaModel> istruttorie = domanda.getA4gtLavorazioneSostegnos().stream()
				.filter(istr -> sostegno.equals(istr.getSostegno()))
				.filter(istr -> !StatoIstruttoria.DEBITI.equals(istr.getStato()))
				.collect(Collectors.toList());

		return !CollectionUtils.isEmpty(istruttorie) 
					? istruttorie.stream()
						.max(Comparator.comparingLong(IstruttoriaModel::getId))
						.get() 
					: null;
	}

	public IstruttoriaModel load(Long idIstruttoria) {
		return istruttoriaDao.findById(idIstruttoria)
				.orElseThrow(() ->
						new EntityNotFoundException("Istruttoria[" + idIstruttoria + "] non trovata"));
	}

}
