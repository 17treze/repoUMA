package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoLavorazioneModel;

@Repository
public interface SuoloDichiaratoLavorazioneDao extends JpaRepository<SuoloDichiaratoLavorazioneModel, Long>, JpaSpecificationExecutor<SuoloDichiaratoLavorazioneModel> {

	List<SuoloDichiaratoLavorazioneModel> findByIdLavorazioneAndStatoRichiestaNot(Long lavorazione, StatoRichiestaModificaSuolo statoRichiesta);
}
