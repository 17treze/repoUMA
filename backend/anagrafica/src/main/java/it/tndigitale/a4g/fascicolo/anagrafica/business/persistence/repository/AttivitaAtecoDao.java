package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.AttivitaAtecoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.UnitaTecnicoEconomicheModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

public interface AttivitaAtecoDao extends JpaRepository<AttivitaAtecoModel, EntitaDominioFascicoloId> {
	List<AttivitaAtecoModel> findByPersonaModelAndIdValidazione(PersonaModel personaModel, Integer idValidazione);
	
	List<AttivitaAtecoModel> findByUnitaTecnicoEconomicheAndIdValidazione(UnitaTecnicoEconomicheModel ute, Integer idValidazione);
}
