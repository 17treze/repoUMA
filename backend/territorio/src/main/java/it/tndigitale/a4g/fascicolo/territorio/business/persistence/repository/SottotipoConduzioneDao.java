package it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.territorio.business.persistence.entity.SottotipoConduzioneModel;
import it.tndigitale.a4g.fascicolo.territorio.business.persistence.entity.TipoConduzioneModel;

@Repository
public interface SottotipoConduzioneDao extends JpaRepository<SottotipoConduzioneModel, Long> {
	List<SottotipoConduzioneModel> findByTipoConduzione(TipoConduzioneModel tipo);
}
