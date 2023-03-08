package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.AllegatoConsuntivoModel;

@Repository
public interface AllegatiConsuntivoDao extends JpaRepository<AllegatoConsuntivoModel, Long> {

	public void deleteByIdIn(List<Long> idsAllegati);
	public void deleteByConsuntivoModel_id(long id);
}
