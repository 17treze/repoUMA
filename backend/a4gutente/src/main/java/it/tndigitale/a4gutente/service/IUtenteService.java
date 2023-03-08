package it.tndigitale.a4gutente.service;

import java.util.List;

import it.tndigitale.a4gutente.dto.*;
import it.tndigitale.a4gutente.dto.UtenteProfiloA4gDto;

import org.springframework.web.multipart.MultipartFile;

import it.tndigitale.a4gutente.repository.model.A4gtEnte;
import it.tndigitale.a4gutente.repository.model.A4gtProfilo;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.repository.model.Ruolo;

public interface IUtenteService {

	List<Profilo> caricaProfiliUtente();

	List<Profilo> caricaProfiliUtente(String utente);

	List<Ruolo> caricaRuoliUtente();

	List<Ruolo> caricaRuoliUtente(String utente);

	List<String> caricaEntiUtente();

	List<String> caricaAziendeUtente();
	
	List<Distributore> caricaDistributoriUtente();
	
	A4gtUtente getUtente(String utenza);
	
	A4gtUtente salvaUtente(A4gtUtente u);

	A4gtUtente createOrGetUtente(DomandaRegistrazioneUtente domanda);

	void addEnti(A4gtUtente utente, List<A4gtEnte> enti);

	void addProfili(A4gtUtente utente, List<A4gtProfilo> profili);

	void addAziende(A4gtUtente utente, List<ResponsabilitaRichieste.TitolareImpresa> aziende);

	void removeAziende(A4gtUtente utente);

	void addDistributori(A4gtUtente utente, List<ResponsabilitaRichieste.RuoloDistributore> ruoliDistributore);

	List<Utente> ricerca(UtentiFilter filter) throws Exception;

	Utente carica(String identificativo) throws Exception;
	
	Utente carica(Long id);

	void protocollaPrivacy(String richiedenteCodiceFiscale, String richiedenteNome, String richiedenteCognome,
			String infoIn, MultipartFile documento, List<MultipartFile> allegati) throws Exception;

	void protocollaInformativaPrivacy(InfoPrivacyDto infoPrivacyDto);
	
	CsvFile getUtentiCsv() throws Exception;

	Distributore caricaDistributoreUtente(Long id);

	ReportValidazioneDto getReportValidazione(String cuaa) throws Exception;

	List<UtenteProfiloA4gDto> getAllUtentiProfilo (List<ProfiloUtente> profili) throws Exception;

	}
