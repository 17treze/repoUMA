package it.tndigitale.a4g.proxy.repository.sincronizzazione.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.Aabaall2Tab;

@Repository
public interface Allegato2AntimafiaDao  extends JpaRepository<Aabaall2Tab, Long> {

}
