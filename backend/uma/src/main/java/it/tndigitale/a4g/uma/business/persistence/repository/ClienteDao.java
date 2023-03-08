package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.ClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;

@Repository
public interface ClienteDao extends JpaRepository<ClienteModel, Long> {

	public List<ClienteModel> findByDichiarazioneConsumi(DichiarazioneConsumiModel dichiarazioneConsumi);
	public void deleteByDichiarazioneConsumi_id(long idDichiarazioneConsumi);

}
