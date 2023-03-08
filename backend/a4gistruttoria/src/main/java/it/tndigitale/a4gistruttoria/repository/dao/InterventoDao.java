package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.InterventoModel;

@Repository
public interface InterventoDao extends JpaRepository<InterventoModel, Long> {

	InterventoModel findByIdentificativoIntervento(CodiceInterventoAgs codiceInterventoAgs);
	InterventoModel findByCodiceAgea(String codiceAgea);

}
