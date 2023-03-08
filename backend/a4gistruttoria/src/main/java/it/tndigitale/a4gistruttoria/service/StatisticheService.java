package it.tndigitale.a4gistruttoria.service;

import java.util.List;

import it.tndigitale.a4gistruttoria.dto.StatisticaDu;
import it.tndigitale.a4gistruttoria.util.TipologiaStatistica;

public interface StatisticheService {
	
	void cancellaStatisticheEsistenti(TipologiaStatistica tipoStatistica, Integer annoCampagna, List<String> misure);

	void salvaStatistica(StatisticaDu statisticaDu, TipologiaStatistica tipoStatistica);

}