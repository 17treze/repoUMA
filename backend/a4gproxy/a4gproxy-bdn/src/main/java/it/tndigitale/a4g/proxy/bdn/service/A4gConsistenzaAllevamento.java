package it.tndigitale.a4g.proxy.bdn.service;

import java.math.BigDecimal;
import java.util.List;

import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAllevamentoDO;

public interface A4gConsistenzaAllevamento {

	// Recupero le informazioni dalla tabella a4gt_bdn_cons_allevamento
	List<ConsistenzaAllevamentoDO> recuperaConsistenzaAllevamento(String codiceFiscale, BigDecimal annoCampagna);
}
