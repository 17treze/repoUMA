package it.tndigitale.a4g.proxy.repository.sincronizzazione.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.Aabaall3Tab;

@Repository
public interface Allegato3AntimafiaDao  extends JpaRepository<Aabaall3Tab, Long> {

}
