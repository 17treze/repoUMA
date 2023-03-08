package it.tndigitale.a4g.fascicolo.antimafia.repository.dao;

import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gdStatoDicAntimafia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatoDichiarazioneAntimafiaDao extends JpaRepository<A4gdStatoDicAntimafia, Long> {

}
