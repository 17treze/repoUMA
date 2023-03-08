package it.tndigitale.a4g.framework.repository.dao;

import javax.persistence.EntityManager;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

public class A4GEntitaDominioJPARepositoryImpl<E extends EntitaDominio> implements A4GEntitaDominioJPARepository<E> {

	private EntityManager em;

	public A4GEntitaDominioJPARepositoryImpl(EntityManager em) {
		super();
		this.em = em;
	}

	@Override
	public <S extends E> S cleanSave(S entity) {
		em.detach(entity);
		return em.merge(entity);
	}	
}
