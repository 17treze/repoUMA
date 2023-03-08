package it.tndigitale.a4gistruttoria.service.businesslogic.processo;

import it.tndigitale.a4gistruttoria.dto.Processo;

public interface ElaborazioneProcessoAsyncStrategyFactory {

	public ElaboraProcesso<Processo, ?> getElaborazioneAsync(String name);
}
