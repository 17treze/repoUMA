package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;

@Repository
public interface MandatoDao extends DetenzioneBaseRepository<MandatoModel> {
	public List<MandatoModel> findByFascicoloAndDataFineIsNull(FascicoloModel fascicoloModel);
	public List<MandatoModel> findByFascicolo(FascicoloModel fascicoloModel);
	public List<MandatoModel> findBySportelloIdentificativoIn(List<Long> identificativi);
	
	@Query("SELECT coalesce(max(fm.idValidazione), 0) + 1 FROM MandatoModel fm where fm.id=:id")
	Integer getNextIdValidazione(Long id);
}
