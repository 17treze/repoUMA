package it.tndigitale.a4g.proxy.services;

import java.io.IOException;
import java.math.BigDecimal;

import it.tndigitale.a4g.proxy.dto.AgricoltoreSIAN;

public interface AgricoltoreService {

	// Recupero informazioni su agricoltore attivo

	public AgricoltoreSIAN recuperaAgricoltoreSian(String codFisc, BigDecimal annoCamp) throws IOException;

}
