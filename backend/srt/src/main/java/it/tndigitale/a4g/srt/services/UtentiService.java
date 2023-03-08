package it.tndigitale.a4g.srt.services;

import java.util.List;

import it.tndigitale.a4g.srt.dto.Utente;
import it.tndigitale.a4g.srt.dto.UtenteFilter;

public interface UtentiService {

	List<Utente> findByFilter(UtenteFilter filter);
}
