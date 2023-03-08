package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.ConsuntivoConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburanteConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoConsuntivo;

@Repository
public interface ConsuntiviConsumiDao extends JpaRepository<ConsuntivoConsumiModel, Long> {

	public List<ConsuntivoConsumiModel> findByDichiarazioneConsumi_id(long idConsumi);
	
	// a db esiste constraint unique su questi campi 
	public Optional<ConsuntivoConsumiModel> findByDichiarazioneConsumiAndTipoCarburanteAndTipoConsuntivo(DichiarazioneConsumiModel dichiarazioneConsumi, TipoCarburanteConsuntivo tipoCarburanteConsuntivo, TipoConsuntivo tipoConsuntivo);

	public void deleteByDichiarazioneConsumi_id(long idDichiarazioneConsumi);
}
