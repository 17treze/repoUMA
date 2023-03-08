package it.tndigitale.a4g.territorio.business.persistence.repository;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import it.tndigitale.a4g.territorio.business.persistence.entity.TipoDocumentoConduzioneModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoDocumentoConduzioneDao extends JpaRepository<TipoDocumentoConduzioneModel, EntitaDominio> {
	Optional<TipoDocumentoConduzioneModel> findById(Long id);
	List<TipoDocumentoConduzioneModel> findBySottotipo_Id(Long id);
}
