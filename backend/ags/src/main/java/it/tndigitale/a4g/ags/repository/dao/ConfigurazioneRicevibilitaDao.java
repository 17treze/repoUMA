package it.tndigitale.a4g.ags.repository.dao;

import it.tndigitale.a4g.ags.dto.ScadenziarioRicevibilitaDto;


public interface ConfigurazioneRicevibilitaDao {
	public ScadenziarioRicevibilitaDto getScandenziario(Integer campagna);
}
