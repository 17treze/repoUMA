package it.tndigitale.a4gistruttoria.repository.dao;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.SostegnoModel;

@Repository
public interface SostegnoDao extends JpaRepository<SostegnoModel, Long> {

	SostegnoModel findByDomandaUnicaModel_numeroDomanda(BigDecimal numeroDomanda);
	
}
