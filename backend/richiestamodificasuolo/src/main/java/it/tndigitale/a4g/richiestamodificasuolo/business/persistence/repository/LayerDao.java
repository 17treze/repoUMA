package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LayerModel;

@Repository
public interface LayerDao extends JpaRepository<LayerModel, Long> {

    List<LayerModel> findByListLayerProfilo_ProfiloUtenteIgnoreCase(final String profiloutente);
    List<LayerModel> findByListLayerProfilo_ProfiloUtenteInIgnoreCase(final List<String> profiliutente);
}