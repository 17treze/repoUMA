package it.tndigitale.a4g.ags.service;

import java.util.List;

import it.tndigitale.a4g.ags.dto.Utente;
import it.tndigitale.a4g.ags.dto.UtenteFilter;

public interface UtenteService {
	public List<Utente> getUtenti(String codiceFiscale);
	public List<Utente> getUtenti(UtenteFilter filtri);
}
