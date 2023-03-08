package it.tndigitale.a4g.zootecnia.business.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.zootecnia.business.persistence.entity.FascicoloModel;

@Repository
public interface FascicoloDao extends JpaRepository<FascicoloModel, EntitaDominioFascicoloId> {
	Optional<FascicoloModel> findByCuaaAndIdValidazione(String cuaa, Integer idValidazione);
}
