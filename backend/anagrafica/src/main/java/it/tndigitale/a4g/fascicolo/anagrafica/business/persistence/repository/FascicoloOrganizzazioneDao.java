package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloOrganizzazioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.OrganizzazioneModel;

@Repository
public interface FascicoloOrganizzazioneDao extends JpaRepository<FascicoloOrganizzazioneModel, Long> {
	public List<FascicoloOrganizzazioneModel> findByCuaa(String cuaa);
	public List<FascicoloOrganizzazioneModel> findByCuaaAndOrganizzazione(String cuaa, OrganizzazioneModel organizzazione);
}

