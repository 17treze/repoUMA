package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoColtModel;

@Repository
public interface StatoColtDao extends JpaRepository<StatoColtModel, Long> {
	@Query("select s from StatoColtModel s where dataFine =:dataFine")
	List<StatoColtModel> findStatoColtValido(@Param("dataFine") LocalDateTime dataFine);
	StatoColtModel findByStatoColt(String statoColt);
}