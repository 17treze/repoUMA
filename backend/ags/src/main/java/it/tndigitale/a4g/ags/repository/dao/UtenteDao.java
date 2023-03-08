package it.tndigitale.a4g.ags.repository.dao;

import java.util.List;

import it.tndigitale.a4g.ags.dto.Utente;
import it.tndigitale.a4g.ags.dto.UtenteFilter;

public interface UtenteDao {

	List<Utente> getUtenti(String codiceFiscale);
	public List<Utente> getUtenti(UtenteFilter filtri);
}
