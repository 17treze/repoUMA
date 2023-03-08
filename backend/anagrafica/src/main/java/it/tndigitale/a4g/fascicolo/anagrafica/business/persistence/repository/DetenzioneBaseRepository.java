package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@NoRepositoryBean
public interface DetenzioneBaseRepository<T extends DetenzioneModel>  extends JpaRepository<T, EntitaDominioFascicoloId> {

}
