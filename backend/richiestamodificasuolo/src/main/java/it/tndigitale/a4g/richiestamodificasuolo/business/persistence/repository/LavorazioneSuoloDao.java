package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.framework.repository.dao.A4GEntitaDominioJPARepository;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;

@Repository
public interface LavorazioneSuoloDao extends JpaRepository<LavorazioneSuoloModel, Long>, JpaSpecificationExecutor<LavorazioneSuoloModel>, A4GEntitaDominioJPARepository<LavorazioneSuoloModel> {

	Optional<LavorazioneSuoloModel> findByIdAndUtente(Long idLavorazione, String utente);

	@Query("select count(1) from LavorazioneSuoloModel l where utente=:utente AND stato=:stato " + "AND (" + " not exists ( select 1 from SuoloModel s where s.idLavorazioneInCorso=l.id ) " + "AND "
			+ "not exists (select 1 from SuoloDichiaratoModel d where d.lavorazioneSuolo = l.id))")
	long countExistLavorazioniEmpty(@Param("utente") String utente, @Param("stato") StatoLavorazioneSuolo stato);

	@Query("select distinct l from LavorazioneSuoloModel l " + "left join SuoloDichiaratoModel d " + "on l.id = d.lavorazioneSuolo.id " + "left join RichiestaModificaSuoloModel r "
			+ "on d.richiestaModificaSuolo.id = r.id " + "where " + "l.id = (case when :idLav = 0L then l.id else :idLav end) " + "and "
			+ "l.utente = (case when :utente is null then l.utente else :utente end) " + "and "
			+ "(case when l.titolo is null then ' ' else lower(l.titolo) end) like (case when :titolo is null then case when l.titolo is null then ' ' else lower(l.titolo) end else :titolo end) "
			+ "and " + "(case when r.cuaa is null then ' ' else r.cuaa end) = (case when :cuaa is null then case when r.cuaa is null then ' ' else r.cuaa end else :cuaa end) " + "and "
			+ "l.stato in :statiLav ")
	// mette in relazione le lavorazioni con i caa
	Page<LavorazioneSuoloModel> findbyLavorazioniSuoloFilter(@Param("idLav") Long idLav, @Param("utente") String utente, @Param("cuaa") String cuaa, @Param("titolo") String titolo,
			@Param("statiLav") List<StatoLavorazioneSuolo> statiLav, Pageable paginazione);

}
