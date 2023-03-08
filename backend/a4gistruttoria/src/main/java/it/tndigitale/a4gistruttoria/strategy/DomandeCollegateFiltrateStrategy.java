package it.tndigitale.a4gistruttoria.strategy;

import java.util.List;

import it.tndigitale.a4gistruttoria.dto.antimafia.DichiarazioneAntimafiaConEsiti;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandeCollegate;
import it.tndigitale.a4gistruttoria.util.StatoDichiarazioneEnum;

public interface DomandeCollegateFiltrateStrategy {

	public DichiarazioneAntimafiaConEsiti getDomanda(List<A4gtDomandeCollegate> a4gtDomandeCollegate, StatoDichiarazioneEnum stato);
}
