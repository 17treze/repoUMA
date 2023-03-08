package it.tndigitale.a4gutente.repository.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

public class PersistenzaEntitaDominioRepositoryImpl<T extends EntitaDominio> implements PersistenzaEntitaDominioRepository<T> {

	@Autowired
    private EntityManager em;	

	public PersistenzaEntitaDominioRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public <S extends T> S cleanSave(S entity) {
		em.detach(entity);
		return em.merge(entity);
	}

}
