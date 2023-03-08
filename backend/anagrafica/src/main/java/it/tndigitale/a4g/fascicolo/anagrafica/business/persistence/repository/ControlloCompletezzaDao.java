package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.ControlloCompletezzaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DocumentoIdentitaModel;
import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ControlloCompletezzaDao extends JpaRepository<ControlloCompletezzaModel, EntitaDominio> {
	List<ControlloCompletezzaModel> findByFascicolo_CuaaAndFascicolo_IdValidazione(final String cuaa, final Integer idValidazione);
	Optional<ControlloCompletezzaModel> findByFascicolo_CuaaAndFascicolo_IdValidazioneAndTipoControllo(final String cuaa, final Integer idValidazione, String tipoControllo);
}
