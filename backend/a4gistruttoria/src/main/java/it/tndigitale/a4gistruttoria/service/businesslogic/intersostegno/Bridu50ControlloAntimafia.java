package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;

public abstract class Bridu50ControlloAntimafia<T> {

	@Autowired
	private VerificaEsitoDichiarazioneAntimafia verificaEsito;

	protected boolean isDocumentazioneAntimafiaInRegola(IstruttoriaModel istruttoria) {
		T datiIstruttore = getA4gtDatiIstruttoriaPerCheckAntimafia(istruttoria);
		return Optional.ofNullable(datiIstruttore)
			.map(checkAntimafia())
			.filter(Boolean::booleanValue)
			.orElseGet(() -> verificaEsito.apply(istruttoria));		
	}
	protected abstract T getA4gtDatiIstruttoriaPerCheckAntimafia(IstruttoriaModel istruttoria); 

	protected abstract Function<T, Boolean> checkAntimafia();
}
