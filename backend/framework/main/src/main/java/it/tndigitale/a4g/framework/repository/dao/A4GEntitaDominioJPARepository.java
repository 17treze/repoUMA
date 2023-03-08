package it.tndigitale.a4g.framework.repository.dao;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

public interface A4GEntitaDominioJPARepository<E extends EntitaDominio> {

	/**
	 * Metodo che richiama la clean prima di eseguire il save
	 * dell'entita. Necessario per forzare il controllo del lock
	 * ottimistico
	 * 
	 * @param <E> Tipo implicito dell'entita da gestire
	 * @param entity Entita da salvare
	 * 
	 * @return L'entita aggiornata
	 */
	<S extends E> S cleanSave(S entity);
}
