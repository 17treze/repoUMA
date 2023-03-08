package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gtAllevMontagna;

@Repository
public interface AllevMontagnaDao extends JpaRepository<A4gtAllevMontagna, Long> {

}
