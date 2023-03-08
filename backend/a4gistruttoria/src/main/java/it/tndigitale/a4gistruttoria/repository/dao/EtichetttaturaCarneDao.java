package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gtEtichettaturaCarne;

@Repository
public interface EtichetttaturaCarneDao extends JpaRepository<A4gtEtichettaturaCarne, Long> {

}
