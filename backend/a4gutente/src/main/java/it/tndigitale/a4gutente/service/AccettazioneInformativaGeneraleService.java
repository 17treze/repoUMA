package it.tndigitale.a4gutente.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gutente.repository.dao.IAccettazioneInformativaGeneraleDao;
import it.tndigitale.a4gutente.repository.model.AccettazioneInformativaGeneraleEntita;
import it.tndigitale.a4gutente.repository.model.PersonaEntita;

@Service
public class AccettazioneInformativaGeneraleService implements IAccettazioneInformativaGeneraleService {
	
	@Autowired
	private IAccettazioneInformativaGeneraleDao accettazioneRepository;

	/* (non-Javadoc)
	 * @see it.tndigitale.a4gutente.service.IAccettazioneInformativaGeneraleService#ricercaAccettazioniPersona(java.lang.String)
	 */
	@Override
	public AccettazioneInformativaGeneraleEntita ricercaAccettazionePersona(PersonaEntita persona) {
		return accettazioneRepository.findByPersona(persona);
	}
	
	/* (non-Javadoc)
	 * @see it.tndigitale.a4gutente.service.IAccettazioneInformativaGeneraleService#esistonoAccettazioniPersona(java.lang.String)
	 */
	@Override
	public boolean esisteAccettazioniPersona(PersonaEntita persona) {
		AccettazioneInformativaGeneraleEntita accettazione = ricercaAccettazionePersona(persona);
		return (accettazione != null) && (accettazione.getNumeroProtocollazione() != null)
				&& (!accettazione.getNumeroProtocollazione().isEmpty());
	}
}
