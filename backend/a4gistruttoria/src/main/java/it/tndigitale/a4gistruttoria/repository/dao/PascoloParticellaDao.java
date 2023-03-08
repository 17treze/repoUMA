package it.tndigitale.a4gistruttoria.repository.dao;

import java.util.List;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gtPascoloParticella;

@Repository
public interface PascoloParticellaDao extends JpaRepository<A4gtPascoloParticella, Long> {

	List<A4gtPascoloParticella> findByDomandaUnicaModel(DomandaUnicaModel domandaUnicaModel);

}
