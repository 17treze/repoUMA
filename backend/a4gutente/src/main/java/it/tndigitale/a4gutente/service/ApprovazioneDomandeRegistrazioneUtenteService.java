package it.tndigitale.a4gutente.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.a4gutente.business.service.client.DetenzioneAutonomaClient;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.DatiAnagrafici;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtente.ServiziType;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtenteSintesi;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneUtenteFilter;
import it.tndigitale.a4gutente.dto.Istruttoria;
import it.tndigitale.a4gutente.dto.Profilo;
import it.tndigitale.a4gutente.dto.RichiestaDomandaApprovazione;
import it.tndigitale.a4gutente.dto.TipoDomandaRegistrazione;
import it.tndigitale.a4gutente.repository.dao.IDomandaRegistrazioneUtenteDao;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.utility.EmailSupport;
import it.tndigitale.a4gutente.utility.ListSupport;

@Service
public class ApprovazioneDomandeRegistrazioneUtenteService implements IApprovazioneDomandaRegistrazioneUtenteScheduledService {

	private static final Logger logger = LoggerFactory.getLogger(ApprovazioneDomandeRegistrazioneUtenteService.class);

	@Value("${automazioneApprovazione.utenzatecnica.username}")
	private String utenzaTecnica;
	@Autowired
	private DomandaRegistrazioneUtenteService domandaRegService;
	@Autowired
	private IDomandaRegistrazioneUtenteDao domandaUtenteRep;
	@Autowired
	private IstruttoriaService istruttoriaService;
	@Autowired
	private ProfiloService profiloService;
	@Autowired
	private DetenzioneAutonomaClient detenzioneAutonomaClient;
	@PersistenceContext
	protected EntityManager entityManager;

	public ApprovazioneDomandeRegistrazioneUtenteService setComponents(
														   IstruttoriaService istruttoriaService,
														   EntityManager entityManager,
														   DomandaRegistrazioneUtenteService domandaRegService) {
		this.istruttoriaService = istruttoriaService;
		this.entityManager = entityManager;
		this.domandaRegService = domandaRegService;
		
		return this;
	}
	
	@Override
	@Transactional
	public void automazioneApprovazione() throws Exception {
		RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> domandeProtocollate = getDomandeProtocollate();
		if (CollectionUtils.isEmpty(domandeProtocollate.getRisultati())) {
			logger.debug("Non è presente nessuna domanda in stato PROTOCOLLATA");
			return;
		}
		for (DatiDomandaRegistrazioneUtenteSintesi domanda: domandeProtocollate.getRisultati()) {
			try {
				DatiDomandaRegistrazioneUtente datiDomanda = domandaRegService.getDomanda(domanda.getId());
				if(verificaCondizioniAutomazioneApprovazione(datiDomanda)) {
					String noteEMotivazione = getNoteEMotivazione(datiDomanda.getTipoDomandaRegistrazione());
					domandaRegService.presaInCarico(datiDomanda.getId());
					creaIstruttoria(datiDomanda.getId(), noteEMotivazione);
					DomandaRegistrazioneUtente domandaIn = domandaUtenteRep.getOne(datiDomanda.getId());
					entityManager.detach(domandaIn);
					approvaDomanda(datiDomanda.getId(), datiDomanda.getDatiAnagrafici(), noteEMotivazione);
					if(datiDomanda.getTipoDomandaRegistrazione().equals(TipoDomandaRegistrazione.RIDOTTA_AZIENDA_ANAGRAFICO)) {
						detenzioneAutonomaClient.apri(datiDomanda.getResponsabilitaRichieste().getResponsabilitaLegaleRappresentante().get(0).getCuaa());
					}
				}
			} catch (Exception e) {
				logger.error("Errore durante l'elaborazione della domanda {}", domanda.getId(), e);
				throw e;
			}
		}
	}
	
	protected RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> getDomandeProtocollate () throws Exception {
		DomandaRegistrazioneUtenteFilter filter = new DomandaRegistrazioneUtenteFilter();
		filter.setStato(StatoDomandaRegistrazioneUtente.PROTOCOLLATA);
		return domandaRegService.ricercaDomande(filter, Paginazione.PAGINAZIONE_DEFAULT, Ordinamento.DEFAULT_ORDER_BY);
	}
	
	protected boolean verificaCondizioniAutomazioneApprovazione(DatiDomandaRegistrazioneUtente datiDomanda) throws Exception {
		return checkIfServiziContainsOnlyA4G(datiDomanda.getServizi()) && datiDomanda.getResponsabilitaRichieste() != null &&
				ListSupport.isNullOrEmpty(datiDomanda.getResponsabilitaRichieste().getResponsabilitaAltriEnti()) &&
				ListSupport.isNullOrEmpty(datiDomanda.getResponsabilitaRichieste().getResponsabilitaCaa()) &&
				ListSupport.isNullOrEmpty(datiDomanda.getResponsabilitaRichieste().getResponsabilitaConsulente()) &&
				ListSupport.isNullOrEmpty(datiDomanda.getResponsabilitaRichieste().getResponsabilitaPat()) &&
				(ListSupport.isNotEmpty(datiDomanda.getResponsabilitaRichieste().getResponsabilitaLegaleRappresentante()) ||
				ListSupport.isNotEmpty(datiDomanda.getResponsabilitaRichieste().getResponsabilitaTitolare()));
 	}
	
	protected void creaIstruttoria(Long id, String motivazione) throws Exception {
		Istruttoria istruttoria = new Istruttoria()
				.setIdDomanda(id)
				.setProfili(getIdProfiloAzienda())
				.setVariazioneRichiesta(motivazione);
		istruttoriaService.crea(istruttoria);
		entityManager.flush();
	}
	
	protected void approvaDomanda(Long id, DatiAnagrafici datiAnagrafici, String note) throws Exception {
		RichiestaDomandaApprovazione richiesta = new RichiestaDomandaApprovazione()
				.setIdDomanda(id)
				.setNote(note)
				.setTestoMail(EmailSupport.getTestoMail(datiAnagrafici));
		domandaRegService.approva(richiesta);
	}
	
	protected List<Long> getIdProfiloAzienda() throws Exception {
		List<Profilo> profili = profiloService.ricercaProfiliUtente();
		return profili
				.stream()
				.filter(x -> x.getIdentificativo().equals("azienda"))
				.map(x -> x.getId())
				.collect(Collectors.toList());
	}
	
	private static Boolean checkIfServiziContainsOnlyA4G(Set<ServiziType> servizi) {
		return servizi != null && servizi.size() == 1 && servizi.contains(ServiziType.A4G);
	}
	
	private static String getNoteEMotivazione(TipoDomandaRegistrazione tipoDomandaRegistrazione) {
		if (tipoDomandaRegistrazione.equals(TipoDomandaRegistrazione.COMPLETA)) {
			return "approvazione gestita in automatico dal sistema per richiesta da a4g";
		}
		if (tipoDomandaRegistrazione.equals(TipoDomandaRegistrazione.RIDOTTA_AZIENDA) || tipoDomandaRegistrazione.equals(TipoDomandaRegistrazione.RIDOTTA_AZIENDA_ANAGRAFICO)) {
			return "approvazione gestita in automatico dal sistema per richiesta da myappag";
		}
		return "";
	}

}
