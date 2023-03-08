package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

public interface FabbricatiDao extends JpaRepository<FabbricatoModel, EntitaDominioFascicoloId> {

	public List<FabbricatoModel> findByFascicolo_cuaaAndFascicolo_idValidazione(String cuaa, Integer idValidazione);

}
