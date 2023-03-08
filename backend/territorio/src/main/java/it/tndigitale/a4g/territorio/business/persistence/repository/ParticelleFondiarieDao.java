package it.tndigitale.a4g.territorio.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.territorio.business.persistence.entity.ParticelleFondiarieModel;

import java.util.List;

@Repository
public interface ParticelleFondiarieDao extends JpaRepository<ParticelleFondiarieModel, EntitaDominioFascicoloId>, JpaSpecificationExecutor<ParticelleFondiarieModel> {
    List<ParticelleFondiarieModel> findByConduzione_Fascicolo_CuaaAndConduzione_Fascicolo_IdValidazione(String cuaa, Integer idValidazione);
}
