package it.tndigitale.a4gistruttoria.service.businesslogic.domanda;

import java.util.List;

import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneDomandaException;

public interface ElaborazioneDomanda {

	public void elabora(Long idDomanda) throws ElaborazioneDomandaException;
	
	public List<Long> caricaIdDaElaborare(Integer annoCampagna) throws ElaborazioneDomandaException;
}
