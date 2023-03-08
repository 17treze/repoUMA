package it.tndigitale.a4gutente.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import it.tndigitale.a4gutente.repository.model.Ruolo;

public interface RuoloDao extends Repository<Ruolo, String> {

	@Query(nativeQuery = true, name = "findRuoliUtente")
	public List<Ruolo> findByUtente(@Param("utente") String utente);
	
}
