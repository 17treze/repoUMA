package it.tndigitale.a4g.proxy.repository.sincronizzazione.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaantiTab;

@Repository
public interface DichiarazioneAntimafiaDao extends JpaRepository<AabaantiTab, Long> {

	public List<AabaantiTab> findByCuaa(String cuaa);
}
