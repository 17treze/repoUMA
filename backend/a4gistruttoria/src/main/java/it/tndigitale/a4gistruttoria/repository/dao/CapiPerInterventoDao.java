package it.tndigitale.a4gistruttoria.repository.dao;

import java.util.List;

import it.tndigitale.a4gistruttoria.dto.StatisticaZootecnia;

public interface CapiPerInterventoDao {
	
	/**
	 * recupera totali esiti capi raggruppati per interventi
	 * @param annoCampagna
	 * @return
	 */
	public List<StatisticaZootecnia> getCapiPerIntervento(Integer annoCampagna);

}
