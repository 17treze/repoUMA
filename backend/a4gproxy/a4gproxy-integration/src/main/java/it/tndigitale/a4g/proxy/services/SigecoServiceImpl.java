package it.tndigitale.a4g.proxy.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.ControlloSigecoDao;

@Service
public class SigecoServiceImpl implements SigecoService {
	
	private static final Logger logger = LoggerFactory.getLogger(SigecoService.class);

	@Autowired
	ControlloSigecoDao daoControlloSigeco;

	public Long recuperaEsitoControlloSigeco(Long anno, String numeroDomanda) {
		BigDecimal esito;
		Long inviate = daoControlloSigeco.findByNumeroDomandaAndAnno(anno, numeroDomanda);
		// if (inviate != null) {
		// esito = inviate.getCodiEsit();
		// } else {
		// return null;
		// }
		// return esito.longValue();
		return inviate;
	}

	@Override
	public Optional<Boolean> recuperaFlagConv(Long annoCampagna, String numeroDomanda, String cuaa) {
		List<String> results = daoControlloSigeco.findFlagConvByNumeroDomandaAndAnnoCampagnaAndCuaa(numeroDomanda, annoCampagna, cuaa);
		if (!results.isEmpty()) {
			return Optional.of(results.get(0).equalsIgnoreCase("S"));
		}
		return Optional.empty();
	};

}
