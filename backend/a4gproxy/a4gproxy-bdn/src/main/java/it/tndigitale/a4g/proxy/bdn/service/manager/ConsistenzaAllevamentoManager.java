package it.tndigitale.a4g.proxy.bdn.service.manager;

import java.util.List;

import it.tndigitale.a4g.proxy.bdn.dto.ErroreDO;

public interface ConsistenzaAllevamentoManager {

	public List<ErroreDO> inserimentoMassivo(Integer annoCampagna);

	public ErroreDO inserimentoConsistenzaAllevamento(String cuaa, Integer annoCampagna);

	public void sincronizzaAzienda(String codiceAzienda);
}
