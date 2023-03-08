package it.tndigitale.a4g.fascicolo.antimafia.repository.dao;

import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtProcedimentoAmf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedimentoDao extends JpaRepository<A4gtProcedimentoAmf, Long> {
}
