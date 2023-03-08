package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.PoliticaAgricolaComunitariaModel;

@Repository
public interface PoliticaAgricolaComunitariaDao extends JpaRepository<PoliticaAgricolaComunitariaModel, Long> {

//	@Query("select p from PoliticaAgricolaComunitariaModel p "
//			+ " where :currentYear between p.annoInizio and p.annoFine")
//	public PoliticaAgricolaComunitariaModel findByCurrentYear(Integer currentYear);
	
	public PoliticaAgricolaComunitariaModel findByCodicePac(String codicePac);
}
