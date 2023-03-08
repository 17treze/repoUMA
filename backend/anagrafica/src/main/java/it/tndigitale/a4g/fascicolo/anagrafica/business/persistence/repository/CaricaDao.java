package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.List;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaConCaricaModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaConCaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@Repository
public interface CaricaDao extends JpaRepository<CaricaModel, EntitaDominioFascicoloId> {
	
	List<CaricaModel> findByPersonaGiuridicaModel(PersonaGiuridicaModel personaGiuridicaModel);

	List<CaricaModel> findByPersonaFisicaModel(PersonaFisicaModel personaFisicaModel);

	List<CaricaModel> findByPersonaGiuridicaModelAndFirmatario(PersonaGiuridicaModel personaGiuridicaModel, boolean firmatario);

	List<CaricaModel> findByPersonaFisicaModelAndFirmatario(PersonaFisicaModel personaFisicaModel, boolean firmatario);
	
	List<CaricaModel> findByPersonaGiuridicaModelAndPersonaFisicaConCaricaModel(PersonaGiuridicaModel personaGiuridicaModel, PersonaFisicaConCaricaModel personaFisicaConCaricaModel);
	
	List<CaricaModel> findByPersonaFisicaConCaricaModel(PersonaFisicaConCaricaModel personaFisicaModel);
	
	List<CaricaModel> findByPersonaGiuridicaConCaricaModel(PersonaGiuridicaConCaricaModel personaFisicaModel);
}
