package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreDisPascoliModel;

@Repository
public interface DatiIstruttoreDisPascoliDao extends JpaRepository<DatiIstruttoreDisPascoliModel, Long>{

}
