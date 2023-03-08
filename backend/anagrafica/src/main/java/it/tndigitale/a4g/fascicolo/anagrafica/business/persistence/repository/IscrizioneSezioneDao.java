package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IscrizioneSezioneModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

public interface IscrizioneSezioneDao extends JpaRepository<IscrizioneSezioneModel, EntitaDominioFascicoloId>  {
	@Query("SELECT coalesce(max(obj.idValidazione), 0) + 1 FROM IscrizioneSezioneModel obj where obj.id=:id")
	Integer getNextIdValidazione(Long id);
}
