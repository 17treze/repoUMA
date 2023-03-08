package it.tndigitale.a4gistruttoria.service;

import java.io.IOException;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;

public interface ElencoPascoliService {

	public void estraiParticellePascolo(Long idDomanda) throws IOException;
	public void estraiPascoliAziendali(DomandaUnicaModel domanda) throws IOException;
}
