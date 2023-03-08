package it.tndigitale.a4g.srt.repository.dao;

import java.util.List;

public interface RuoliDao {
		/**
		 * Metodo di recupero dei ruoli associati all'utente identificato su SRTrento dal codice fiscale
		 * 
		 * @param codiceFiscale
		 *            Codice fiscale dell'utente di SRTrento
		 * @return Lista dei ruoli, formato stringa
		 */
		public List<String> getRuoliPerUtente(String codiceFiscale);
}
