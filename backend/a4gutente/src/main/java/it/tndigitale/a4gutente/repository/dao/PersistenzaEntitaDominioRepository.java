package it.tndigitale.a4gutente.repository.dao;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

public interface PersistenzaEntitaDominioRepository<T extends EntitaDominio> {

	/**
	 * Metodo che richiama la clean prima di eseguire il save
	 * dell'entita. Necessario per forzare il controllo del lock
	 * ottimistico
	 * 
	 * @param <S> Tipo implicito dell'entita da gestire
	 * @param entity Entita da salvare
	 * 
	 * @return L'entita aggiornata
	 */
	<S extends T> S cleanSave(S entity);
}
