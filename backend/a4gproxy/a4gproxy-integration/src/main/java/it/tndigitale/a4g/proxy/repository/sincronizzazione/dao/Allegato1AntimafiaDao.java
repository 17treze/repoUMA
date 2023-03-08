package it.tndigitale.a4g.proxy.repository.sincronizzazione.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.Aabaall1Tab;

@Repository
public interface Allegato1AntimafiaDao extends JpaRepository<Aabaall1Tab, Long> {

}
