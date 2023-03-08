package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreSuperficieModel;

@Repository
public interface DatiIstruttoreSupDao  extends JpaRepository<DatiIstruttoreSuperficieModel, Long>, JpaSpecificationExecutor<DatiIstruttoreSuperficieModel> {


}
