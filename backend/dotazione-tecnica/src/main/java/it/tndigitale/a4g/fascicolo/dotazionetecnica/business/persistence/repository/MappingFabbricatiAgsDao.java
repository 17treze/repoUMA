package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MappingFabbricatiAgsModel;

@Repository
public interface MappingFabbricatiAgsDao extends JpaRepository<MappingFabbricatiAgsModel, Long> {

	public List<MappingFabbricatiAgsModel> findByCodiceAgs(String codiceAgs);

}
