package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.RegistroOlioDOPModel;

import java.util.Optional;

@Repository
public interface RegistroOlioDOPDao extends JpaRepository<RegistroOlioDOPModel, Long> {
	
    Optional<RegistroOlioDOPModel>
	    findByCuaaIntestatarioAndInizioCampagnaLessThanEqualAndFineCampagnaGreaterThanEqual(
	            String cuaaIntestatario, Integer inizioCampagna, Integer fineCampagna);
}