package it.tndigitale.a4g.proxy.repository.sincronizzazione.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaazcoTab;

@Repository
public interface AziendeCollegateAntimafiaDao  extends JpaRepository<AabaazcoTab, Long> {

}
