package it.tndigitale.a4g.territorio.business.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.territorio.business.persistence.entity.ConduzioneModel;

@Repository
public interface ConduzioneDao extends JpaRepository<ConduzioneModel, EntitaDominioFascicoloId>, JpaSpecificationExecutor<ConduzioneModel> {
}
