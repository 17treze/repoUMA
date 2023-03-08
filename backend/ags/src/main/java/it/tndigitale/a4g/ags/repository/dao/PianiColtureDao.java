package it.tndigitale.a4g.ags.repository.dao;

import java.util.Date;
import java.util.List;

import it.tndigitale.a4g.ags.dto.CatastoPianoColture;
import it.tndigitale.a4g.ags.dto.DatiAggiuntiviParticella;
import it.tndigitale.a4g.ags.dto.InfoParticella;

public interface PianiColtureDao {

	public boolean eseguiCaricaPiani(Date dateFrom);

	List<InfoParticella> getParticelleFromPianiColture(Integer anno);

	List<CatastoPianoColture> getPianiColtureByInfoParticella(InfoParticella info, Integer anno);

	Integer getParticelleDistinctCount(Integer anno);

	DatiAggiuntiviParticella getDatiAggiuntiviParticella(InfoParticella info, Integer anno);
}
