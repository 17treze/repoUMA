package it.tndigitale.a4g.proxy.bdn.service.manager;

import java.util.List;

import it.tndigitale.a4g.proxy.bdn.dto.ErroreDO;

public interface UsciteDaPascoloPartiteOviniManager {

	public List<ErroreDO> inserimentoMassivo(Integer annoCampagna);

	ErroreDO inserimentoUsciteDaPascoloPartiteOvini(String cuaa, Integer annoCampagna);

}
