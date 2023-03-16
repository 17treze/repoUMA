package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MappingAgsModel;

@Repository
public interface MappingAgsDao extends JpaRepository<MappingAgsModel, Long> {
	public List<MappingAgsModel> findByCodiceAgsAndSottoCodiceAgs(String codiceAgs, String sottoCodiceAgs);
	
	public List<MappingAgsModel> findByCodiceAgsAndSottoCodiceAgsIsNull(String codiceAgs);
	
	public List<MappingAgsModel> findByIdMacchina(Long idMacchina);
	
	public List<MappingAgsModel> findByIdTipoMacchina(Long idTipoMacchina);
	
}