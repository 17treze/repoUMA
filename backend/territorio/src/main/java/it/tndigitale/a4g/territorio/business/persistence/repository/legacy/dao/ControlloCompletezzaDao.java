package it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dao;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import it.tndigitale.a4g.territorio.business.persistence.entity.ControlloCompletezzaModel;
// import it.tndigitale.a4g.territorio.business.persistence.entity.FascicoloModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ControlloCompletezzaDao extends JpaRepository<ControlloCompletezzaModel, EntitaDominio> {
	List<ControlloCompletezzaModel> findByCuaa(String cuaa);
	Optional<ControlloCompletezzaModel> findByCuaaAndTipoControllo(String cuaa, String tipoControllo);
}
