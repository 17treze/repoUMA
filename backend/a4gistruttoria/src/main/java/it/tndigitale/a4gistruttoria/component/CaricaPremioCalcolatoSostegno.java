package it.tndigitale.a4gistruttoria.component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

public abstract class CaricaPremioCalcolatoSostegno extends CaricaPremioSostegno {

	public static final String PREFISSO_NOME_QUALIFICATORE = "CARICA_PREMIO_CALCOLATO_";

	@Override
	public Double getPremio(IstruttoriaModel istruttoria) {
		return getImportoPremioCalcolato(istruttoria);
	}

	@Override
	public Map<TipoVariabile, BigDecimal> getImportoPremioCalcolato(IstruttoriaModel istruttoria, TipoVariabile... variabili) {
		if (esisteCalcolo(istruttoria)) {
			return super.getImportoPremioCalcolato(istruttoria, variabili);
		}
		return null;
	}

	protected boolean esisteCalcolo(IstruttoriaModel istruttoria) {
		return !Arrays
				.asList(StatoIstruttoria.RICHIESTO, StatoIstruttoria.NON_AMMISSIBILE,
						StatoIstruttoria.NON_RICHIESTO, StatoIstruttoria.INTEGRATO)
				.contains(istruttoria.getStato());
	}
	
	public static String getNomeQualificatore(Sostegno sostegno) {
		return PREFISSO_NOME_QUALIFICATORE + sostegno.name();
	}
}
