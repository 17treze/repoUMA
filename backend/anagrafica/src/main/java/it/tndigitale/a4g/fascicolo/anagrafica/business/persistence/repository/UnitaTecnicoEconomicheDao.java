package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.UnitaTecnicoEconomicheModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

public interface UnitaTecnicoEconomicheDao extends JpaRepository<UnitaTecnicoEconomicheModel, EntitaDominioFascicoloId> {
	
	List<UnitaTecnicoEconomicheModel> findByPersona_CodiceFiscaleAndIdValidazione(final String cuaa, final Integer idValidazione);
}
