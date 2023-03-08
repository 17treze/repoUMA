package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;

@Repository
public interface StatoLavSostegnoDao extends JpaRepository<A4gdStatoLavSostegno, Long> {

	A4gdStatoLavSostegno findByIdentificativo(String identificativo);

}
