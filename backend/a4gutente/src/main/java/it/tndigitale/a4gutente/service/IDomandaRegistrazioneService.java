package it.tndigitale.a4gutente.service;

import java.util.List;

import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.CounterStato;
import it.tndigitale.a4gutente.dto.DatiAutenticazione;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtenteSintesi;
import it.tndigitale.a4gutente.dto.Distributore;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneCreataResponse;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneUtenteDto;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneUtenteFilter;
import it.tndigitale.a4gutente.dto.EnteCAA;
import it.tndigitale.a4gutente.dto.RichiestaDomandaApprovazione;
import it.tndigitale.a4gutente.dto.RichiestaDomandaRifiuta;
import it.tndigitale.a4gutente.dto.TipoDomandaRegistrazione;

public interface IDomandaRegistrazioneService {

	Long registraDomanda(DatiDomandaRegistrazioneUtente datiDomanda) throws Exception;
	
	Long aggiornaDomanda(DatiDomandaRegistrazioneUtente datiDomanda) throws Exception;
	
	DatiDomandaRegistrazioneUtente getDomanda(Long id) throws Exception;

	Boolean isUtenteRegistrabile() throws Exception;
	
	byte[] stampa(Long idDomanda) throws Exception;
	
	Long firma(Long idDomanda, DatiAutenticazione datiAutenticazione) throws Exception;

	/**
	 * Recupero CAA/Sedi
	 * 
	 * @return
	 */
	List<EnteCAA> getCAA(String params) throws Exception;

	/**
	 * Recupero Distributori
	 *
	 * @return
	 */
	List<Distributore> getDistributori(String params) throws Exception;
	
	List<String> getDipartimenti() throws Exception;
	
	public void protocollaDomanda(DomandaRegistrazioneUtenteDto domandaRegistrazioneUtenteDto);
	
	void protocolla(Long idDomanda) throws Exception;

//	TODO commentato perche' sembra inutilizzata
//	DatiDomandaRegistrazioneUtente domandaUtenteProtocollata(String identificativoUtente) throws Exception;
	
	List<DatiDomandaRegistrazioneUtente> ricerca(DomandaRegistrazioneUtenteFilter criteri) throws Exception;

	RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> ricercaDomande(DomandaRegistrazioneUtenteFilter criteri, Paginazione paginazione, Ordinamento ordinamento) throws Exception;

	List<CounterStato> contaDomande(DomandaRegistrazioneUtenteFilter filter) throws Exception;

	void presaInCarico(Long id) throws Exception;

	Long approva(RichiestaDomandaApprovazione richiesta) throws Exception;

	Long rifiuta(RichiestaDomandaRifiuta richiesta) throws Exception;

	DomandaRegistrazioneCreataResponse registrazioneFirmaDocumento(DatiDomandaRegistrazioneUtente domandaRegistrazioneUtente,
			DatiAutenticazione datiAutenticazione);

	public DatiDomandaRegistrazioneUtente ultimaDomandaUtenteCorrentePerDataProtocollazione(StatoDomandaRegistrazioneUtente statoDomandaRegistrazioneUtente, TipoDomandaRegistrazione tipoDomandaRegistrazione) throws Exception;
}
