package it.tndigitale.a4g.fascicolo.antimafia.repository.dao;

import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtNota;
import org.springframework.data.jpa.repository.JpaRepository;

import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtNota;

public interface NotaDao extends JpaRepository<A4gtNota, Long> {

}
