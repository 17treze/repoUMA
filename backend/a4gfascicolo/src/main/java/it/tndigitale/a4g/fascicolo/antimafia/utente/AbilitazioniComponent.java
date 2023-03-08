package it.tndigitale.a4g.fascicolo.antimafia.utente;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import it.tndigitale.a4g.fascicolo.antimafia.service.ext.ConsumeExternalRestApi4Anagrafica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.antimafia.Ruoli;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.Fascicolo;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.ParamsRicercaFascicolo;
import it.tndigitale.a4g.fascicolo.antimafia.repository.dao.DichiarazioneAntimafiaDao;
import it.tndigitale.a4g.fascicolo.antimafia.service.ConsultazioneService;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.security.service.UtenteClient;
import it.tndigitale.a4g.framework.support.ListSupport;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.DetenzioneAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto;

/**
 * Classe che si occupa di gestire i controlli autorizzativi ai servizi.
 * In una prima fase un livello 3 programmatico (controllo ruolo). E' stato fatto
 * cosi in ottica di estendere i controlli sui dati anche
 * 
 * @author it417
 *
 */
@Component("abilitazioniComponent")
public class AbilitazioniComponent {

	@Autowired
	private UtenteComponent utenteComponent;
	@Autowired
	private UtenteClient abilitazioniUtente;
	@Autowired
	private DichiarazioneAntimafiaDao daoDichiarazioneAntimafia;
	@Autowired
	private ConsultazioneService consultazioneService;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ConsumeExternalRestApi4Anagrafica apiAnagrafica;

	private static final Logger logger = LoggerFactory.getLogger(AbilitazioniComponent.class);

	public boolean checkLetturaFascicolo() {
		return utenteComponent.haUnRuolo(Ruoli.RICERCA_FASCICOLO_FILTRO_ENTE, Ruoli.RICERCA_FASCICOLO_FILTRO_UTENTE, Ruoli.RICERCA_FASCICOLO_NON_FILTRATA);
	}

	public boolean checkLetturaCuaa(String cuaa) throws Exception {
		if (utenteComponent.haUnRuolo(Ruoli.RICERCA_FASCICOLO_NON_FILTRATA)) return true;
		if (utenteComponent.haUnRuolo(Ruoli.RICERCA_FASCICOLO_FILTRO_ENTE) && isFascicoloAbilitatoEnte(cuaa)) {
			return true;
		}
		return (utenteComponent.haUnRuolo(Ruoli.RICERCA_FASCICOLO_FILTRO_UTENTE) && isFascicoloAbilitatoUtente(cuaa));
	}

	/********** ABILITAZIONI ANAGRAFICA **********/

	// l'utente connesso è un Titolare/Rappresentate Legale dell'azienda
	private boolean isFascicoloAbilitatoUtente(String cuaa) throws Exception {
		List<String> listaCuaa = abilitazioniUtente.getAziendeUtente();
		return listaCuaa.contains(cuaa);
	}

	// lo sportello del CAA connesso appartiene al fascicolo che si vuole reperire
	private boolean isFascicoloAbilitatoEnte(String cuaa) throws Exception {
		Optional<FascicoloAgsDto> fascicoloAgsOpt = Optional.ofNullable(apiAnagrafica.getFascicolo(cuaa));
		if (fascicoloAgsOpt.isPresent()) {
			List<Long> sportelliFascicolo = fascicoloAgsOpt.get().getDetenzioni()
					.stream()
					.map(DetenzioneAgsDto::getIdentificativoSportello)
					.collect(Collectors.toList());

			List<Long> sportelliUtente = abilitazioniUtente.getEntiUtente().stream().map(Long::valueOf).collect(Collectors.toList());
			List<Long> sportelliAbilitati = ListSupport.intersect(sportelliFascicolo, sportelliUtente);

			boolean isAbilitato = !CollectionUtils.isEmpty(sportelliAbilitati);
			
			if (!isAbilitato) {
				logger.warn("[ABILITAZIONE FALLITA ENTE] - Tentato Accesso al fascicolo {} da parte di {}" , cuaa, abilitazioniUtente.getEntiUtente());
			}
			return isAbilitato;
		}
		logger.warn("[ABILITAZIONE FALLITA ENTE] - Nessun fascicolo trovato per il cuaa {}" , cuaa);
		return false;
	}

	public boolean checkLetturaAntimafia() {
		return utenteComponent.haUnRuolo(Ruoli.RICERCA_ANTIMAFIA_ENTE, Ruoli.RICERCA_ANTIMAFIA_UTENTE, Ruoli.RICERCA_ANTIMAFIA_TUTTI);
	}

	public boolean checkEditaAntimafia(Dichiarazione dichiarazione, Long idDichiarazione) throws Exception {
		boolean canView =  false;
		if (utenteComponent.haUnRuolo(Ruoli.EDITA_ANTIMAFIA_TUTTI)) return true;

		List<String> cuaaAbilitati = null;
		if (utenteComponent.haUnRuolo(Ruoli.EDITA_ANTIMAFIA_UTENTE)) {
			cuaaAbilitati = abilitazioniUtente.getAziendeUtente();
			canView = cuaaAbilitati.contains(recuperaCuaaAntimafia(dichiarazione, idDichiarazione));
		}
		if (!canView && utenteComponent.haUnRuolo(Ruoli.EDITA_ANTIMAFIA_ENTE)) {
			//recupero sportelli utente
			//chiamare AGS e recuperare i CUAA relativi ai vari sportelli
			ParamsRicercaFascicolo ricercaFascicolo =  new ParamsRicercaFascicolo();
			ricercaFascicolo.setCuaa(recuperaCuaaAntimafia(dichiarazione, idDichiarazione));
			List<Fascicolo> fascicoliEnti = consultazioneService.getFascicoliEnti(objectMapper.writeValueAsString(ricercaFascicolo));
			canView = !CollectionUtils.isEmpty(fascicoliEnti) && fascicoliEnti.size() > 0;
		}
		return canView;
	}

	private String recuperaCuaaAntimafia(Dichiarazione dichiarazione, Long idDichiarazione) {
		if (dichiarazione !=null && 
				dichiarazione.getAzienda() !=null && 
				!StringUtils.isEmpty(dichiarazione.getAzienda().getCuaa())) {
			return dichiarazione.getAzienda().getCuaa();
		} else {
			return daoDichiarazioneAntimafia.getOne(idDichiarazione).getA4gtAziendaAgricola().getCuaa();
		}
	}

	public boolean checkCancellaAntimafia() {
		return utenteComponent.haUnRuolo(Ruoli.CANCELLA_ANTIMAFIA_ENTE, Ruoli.CANCELLA_ANTIMAFIA_UTENTE, Ruoli.CANCELLA_ANTIMAFIA_TUTTI);
	}
}
