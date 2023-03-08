package it.tndigitale.a4g.proxy.services.protocollo.rest;

import org.springframework.util.StringUtils;

import it.tndigitale.a4g.proxy.services.protocollo.PiTreException;

public class ErrorMessageEvaluation {
	
	public  static void checkPiTreResponse(Integer code, String errorMessage) throws PiTreException {
		if (!StringUtils.isEmpty(errorMessage)) {
			throw new PiTreException(errorMessage);
		}
	}


}
