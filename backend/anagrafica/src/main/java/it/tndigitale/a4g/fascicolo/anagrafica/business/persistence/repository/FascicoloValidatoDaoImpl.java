package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel_;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@Repository
public class FascicoloValidatoDaoImpl {

	@PersistenceContext private EntityManager entityManager;
	@Autowired private FascicoloDao fascicoloDao;
	
	public Optional<Long> getIdFascicoloByCuaaAndIdValidazione(final String cuaa, final Integer idValidazione) {
		var cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<FascicoloModel> root = q.from(FascicoloModel.class);
		q.select(root.get(FascicoloModel_.id)).where(cb.and(
				cb.equal(root.get(FascicoloModel_.cuaa), cuaa),
				cb.equal(root.get(FascicoloModel_.idValidazione), idValidazione)));
		return Optional.of(entityManager.createQuery(q).getSingleResult());
	}
	
	public Optional<FascicoloModel> findByCuaaAndIdValidazione(String cuaa, int idValidazione) {
		Optional<Long> id = getIdFascicoloByCuaaAndIdValidazione(cuaa, idValidazione);
		if (!id.isPresent()) {
			return Optional.empty();
		}
		return Optional.of(fascicoloDao.getOne(new EntitaDominioFascicoloId(id.get(), idValidazione)));
	}
}
