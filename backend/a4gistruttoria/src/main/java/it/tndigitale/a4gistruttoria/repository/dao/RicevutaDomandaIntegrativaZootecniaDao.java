package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gtRicevutaDomIntZoot;

@Repository
public interface RicevutaDomandaIntegrativaZootecniaDao extends JpaRepository<A4gtRicevutaDomIntZoot, Long> {

	A4gtRicevutaDomIntZoot findByDomandaUnicaModel(DomandaUnicaModel domanda);
	
	public A4gtRicevutaDomIntZoot findByDomandaUnicaModel_Id (Long idDomanda);

}
