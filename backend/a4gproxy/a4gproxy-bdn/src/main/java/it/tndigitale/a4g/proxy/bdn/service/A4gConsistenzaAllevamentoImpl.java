package it.tndigitale.a4g.proxy.bdn.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAllevamentoDO;
import org.springframework.beans.factory.annotation.Autowired;

import it.tndigitale.a4g.proxy.bdn.repository.dao.ConsistenzaAllevamentoDAOImpl;

public class A4gConsistenzaAllevamentoImpl implements A4gConsistenzaAllevamento {

	@Autowired
	ConsistenzaAllevamentoDAOImpl daoConistenzaAllevamento;

	@Autowired
	
	
	public List<ConsistenzaAllevamentoDO> recuperaConsistenzaAllevamento(String codiceFiscale, BigDecimal annoCampagna) {
		// TODO 
		List<ConsistenzaAllevamentoDO> consistenzaAllevamento = daoConistenzaAllevamento.getConsAllevamento(codiceFiscale, annoCampagna);
		List<ConsistenzaAllevamentoDO> consistenzaAllevamentoCUAA = 
				consistenzaAllevamento.stream()
					.filter(c -> c.getCodiFiscDete().equals(c.getCodiFiscProp()))
					.collect(Collectors.toList());
		
		
		
		consistenzaAllevamentoCUAA.forEach(cons -> {
			
		});
		
		
		if (consistenzaAllevamentoCUAA != null && !consistenzaAllevamentoCUAA.isEmpty()) {
			return consistenzaAllevamentoCUAA;
		} else
			return null;

	}

}
