package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiErede;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;

@Repository
public interface DatiEredeDao extends JpaRepository<A4gtDatiErede, Long> {
	
	public A4gtDatiErede findByDomandaUnicaModel(DomandaUnicaModel domanda);
}
