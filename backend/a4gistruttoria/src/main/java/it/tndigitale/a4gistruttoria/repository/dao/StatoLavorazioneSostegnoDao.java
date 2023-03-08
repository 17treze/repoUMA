package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatoLavorazioneSostegnoDao extends JpaRepository<A4gdStatoLavSostegno, Long>, JpaSpecificationExecutor<A4gdStatoLavSostegno> {

    Optional<A4gdStatoLavSostegno> findByIdentificativo(String indentificativo);

}



