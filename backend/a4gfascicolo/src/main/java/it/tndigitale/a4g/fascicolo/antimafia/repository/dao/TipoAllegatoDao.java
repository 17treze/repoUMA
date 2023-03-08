package it.tndigitale.a4g.fascicolo.antimafia.repository.dao;

import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gdTipoAllegato;
import org.springframework.data.jpa.repository.JpaRepository;

import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gdTipoAllegato;

public interface TipoAllegatoDao extends JpaRepository<A4gdTipoAllegato, Long> {
}
