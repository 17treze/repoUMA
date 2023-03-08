package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.CampioneModel;
import it.tndigitale.a4gistruttoria.repository.model.AmbitoCampione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampioneDao extends JpaRepository<CampioneModel, Long> {

	CampioneModel findByCuaaAndAmbitoCampioneAndAnnoCampagna(String cuaa, AmbitoCampione ambitoCampione, Integer annoCampagna);
	
	List<CampioneModel> findByCuaaAndAnnoCampagna(String cuaa, Integer annoCampagna);
}
