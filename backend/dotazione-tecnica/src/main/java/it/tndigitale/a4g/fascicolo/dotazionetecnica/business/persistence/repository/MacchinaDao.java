package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;


@Repository
public interface MacchinaDao extends JpaRepository<MacchinaModel, EntitaDominioFascicoloId> {
	public List<MacchinaModel> findByFascicolo_cuaaAndFascicolo_idValidazione(String cuaa, Integer idValidazione);
}
