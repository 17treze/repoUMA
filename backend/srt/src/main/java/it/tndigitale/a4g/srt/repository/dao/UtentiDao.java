package it.tndigitale.a4g.srt.repository.dao;

import java.util.List;

import it.tndigitale.a4g.srt.dto.Utente;
import it.tndigitale.a4g.srt.dto.UtenteFilter;

public interface UtentiDao {

	List<Utente> findByFilter(UtenteFilter filter);

}
