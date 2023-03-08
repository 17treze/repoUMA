package it.tndigitale.a4g.proxy.repository.esiti.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.repository.esiti.model.AntimafiaEsitiId;
import it.tndigitale.a4g.proxy.repository.esiti.model.AntimafiaEsitiModel;



@Repository
public interface AntimafiaEsitiDao extends JpaRepository<AntimafiaEsitiModel, AntimafiaEsitiId> {
	
	public List<AntimafiaEsitiModel> findByIdCuaa(String cuaa);
}
