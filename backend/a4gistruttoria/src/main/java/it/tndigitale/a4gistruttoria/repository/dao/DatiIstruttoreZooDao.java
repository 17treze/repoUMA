package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreZootecniaModel;

@Repository
public interface DatiIstruttoreZooDao  extends JpaRepository<DatiIstruttoreZootecniaModel, Long>, JpaSpecificationExecutor<DatiIstruttoreZootecniaModel> {


}
