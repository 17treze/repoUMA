package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.EredeModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@Repository
public interface EredeDao extends JpaRepository<EredeModel, EntitaDominioFascicoloId>, JpaSpecificationExecutor<EredeModel> {

	Optional<EredeModel> findByFascicolo_CuaaAndPersonaFisica_CodiceFiscale(final String cuaa, final String codiceFiscale);
	List<EredeModel> findByFascicolo_Cuaa(final String cuaa);
	Optional<EredeModel> findById(final Long id);
}
