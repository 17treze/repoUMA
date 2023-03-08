package it.tndigitale.a4gutente.service;

import it.tndigitale.a4gutente.repository.model.AccettazioneInformativaGeneraleEntita;
import it.tndigitale.a4gutente.repository.model.PersonaEntita;

public interface IAccettazioneInformativaGeneraleService {

	AccettazioneInformativaGeneraleEntita ricercaAccettazionePersona(PersonaEntita persona);

	boolean esisteAccettazioniPersona(PersonaEntita persona);

}