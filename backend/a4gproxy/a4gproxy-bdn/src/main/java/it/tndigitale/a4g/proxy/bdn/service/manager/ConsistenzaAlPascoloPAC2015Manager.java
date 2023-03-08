package it.tndigitale.a4g.proxy.bdn.service.manager;

import java.util.List;

import org.springframework.web.client.RestClientException;

import it.tndigitale.a4g.proxy.bdn.dto.ErroreDO;

public interface ConsistenzaAlPascoloPAC2015Manager {

	public List<ErroreDO> inserimentoMassivo(Integer annoCampagna);

	public List<ErroreDO> inserimentoConsistenzaAlPascoloPAC2015PerCUAA(String cuaa, Integer annoCampagna);

	public Long aggiornaListaCuaaDaSincronizzare(Integer annoCampagna) throws RestClientException;
}
