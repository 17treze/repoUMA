package it.tndigitale.a4g.proxy;

import it.tndigitale.a4g.proxy.dto.catasto.TipologiaParticellaCatastale;
import it.tndigitale.a4g.proxy.services.catasto.ParticellaService;

public interface GeneralFactory {
	
	ParticellaService getParticellaService(TipologiaParticellaCatastale tipo);
	
}