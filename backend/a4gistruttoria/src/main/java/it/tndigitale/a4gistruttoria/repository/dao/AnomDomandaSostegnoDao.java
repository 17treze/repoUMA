package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gtAnomDomandaSostegno;

@Repository
public interface AnomDomandaSostegnoDao extends JpaRepository<A4gtAnomDomandaSostegno, Long> {

}
