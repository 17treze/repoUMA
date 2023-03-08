package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.OrganizzazioneModel;

@Repository
public interface OrganizzazioneDao extends JpaRepository<OrganizzazioneModel, Long> {
}

