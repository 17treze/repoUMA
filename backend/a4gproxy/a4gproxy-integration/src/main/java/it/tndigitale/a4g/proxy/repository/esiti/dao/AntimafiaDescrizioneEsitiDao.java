package it.tndigitale.a4g.proxy.repository.esiti.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.repository.esiti.model.AntimafiaDescrizioneEsitiModel;

@Repository
public interface AntimafiaDescrizioneEsitiDao extends JpaRepository<AntimafiaDescrizioneEsitiModel, String> {
	
	public AntimafiaDescrizioneEsitiModel findByCodice(String codice);
}
