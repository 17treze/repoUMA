package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.UsoSuoloModel;

@Repository
public interface UsoSuoloDao extends JpaRepository<UsoSuoloModel, Long> {

	@Query("select s from UsoSuoloModel s where dataFine =:dataFine and visualizzaBo =:visualizzaBo")
	List<UsoSuoloModel> findCodUsoSuoloValido(@Param("dataFine") LocalDateTime dataFine, @Param("visualizzaBo") Integer visualizzaBo);
	
	UsoSuoloModel findByCodUsoSuolo(String codUsoSuolo);
}
