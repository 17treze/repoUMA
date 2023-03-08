package it.tndigitale.a4gutente.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gutente.codici.CodResponsabilita;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneUtenteFilter;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.CollaboratoreAltriEnti;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.Consulente;
import it.tndigitale.a4gutente.exception.UtenteException;
import it.tndigitale.a4gutente.repository.dao.IAllegatoResponsabilitDao;
import it.tndigitale.a4gutente.repository.dao.IDomandaRegistrazioneUtenteDao;
import it.tndigitale.a4gutente.repository.model.A4gtAllegatoResponsabilita;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DomandaRegistrazioneUtenteServiceTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private DomandaRegistrazioneUtenteService service;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private IDomandaRegistrazioneUtenteDao domandaUtenteRep;

	@Autowired
	private IAllegatoResponsabilitDao allegatoResponsabilitDao;

	@Test
	@WithMockUser("utenteCompleto")
	public void conDomandaCompletaRegistraUnaNuovaDomanda() throws Exception {
		File inputDataFile = new File(
				"src/test/resources/domandaRegistrazioneUtente/registrazioneNuovaDomanda/nuovaDomandaRegistrazioneUtenteCompleto.json");

		DatiDomandaRegistrazioneUtente dati = objectMapper.readValue(inputDataFile,
				DatiDomandaRegistrazioneUtente.class);

		Long id = service.registraDomanda(dati);
		assertNotNull(id);
		domandaUtenteRep.findById(id).get();
	}

	@Test
	@WithMockUser("nuovaDomandaRegistrazioneUtenteNonCompleto")
	public void conDomandaNonCompletaErroreInRegistraDomanda() throws Exception {
		try {
			File inputDataFile = new File(
					"src/test/resources/domandaRegistrazioneUtente/registrazioneNuovaDomanda/nuovaDomandaRegistrazioneUtenteNonCompleto.json");

			DatiDomandaRegistrazioneUtente dati = objectMapper.readValue(inputDataFile,
					DatiDomandaRegistrazioneUtente.class);

			service.registraDomanda(dati);
			assertNull("Deve andare in errore");
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	@Test
	@WithMockUser("utenteCAA")
	public void conDomandaCompletaRegistraUnaNuovaDomandaPerCAA() throws Exception {
		File inputDataFile = new File(
				"src/test/resources/domandaRegistrazioneUtente/registrazioneNuovaDomanda/nuovaDomandaRegistrazioneUtenteCAA.json");

		DatiDomandaRegistrazioneUtente dati = objectMapper.readValue(inputDataFile,
				DatiDomandaRegistrazioneUtente.class);

		Long id = service.registraDomanda(dati);
		assertNotNull(id);
		DomandaRegistrazioneUtente res = domandaUtenteRep.findById(id).get();

		assertNotNull(allegatoResponsabilitDao.findAll().stream().filter(x -> x.getDomandaRegistrazione().getId() == id)
				.findAny());
	}

	@Test
	@WithMockUser("NNFGRL92L27G273P")
	@Transactional
	public void conDomandaAltriEntiDatiCompletiRegistraDomandaResponsabilitaAltriEnti() throws Exception {
		File inputDataFile = new File(
				"src/test/resources/domandaRegistrazioneUtente/registrazioneNuovaDomanda/nuovaDomandaRegistrazioneUtenteAltriEnti.json");

		DatiDomandaRegistrazioneUtente dati = objectMapper.readValue(inputDataFile,
				DatiDomandaRegistrazioneUtente.class);

		Long id = service.registraDomanda(dati);
		
		assertNotNull(id);
		DomandaRegistrazioneUtente res = domandaUtenteRep.findById(id).get();
		assertEquals(id, res.getId());
		assertNotNull(res.getResponsabilita());

		ResponsabilitaRichieste responsabilita = objectMapper.readValue(res.getResponsabilita(),
				ResponsabilitaRichieste.class);
		
		assertNotNull(responsabilita);
		List<CollaboratoreAltriEnti> coll = responsabilita.getResponsabilitaAltriEnti();
		assertNotNull(coll);
		assertEquals(coll.size(), 1);
		
		Set<A4gtAllegatoResponsabilita> allegati = res.getA4gtAllegatoResponsabilita();
		assertNotNull(allegati);
		assertEquals(allegati.size(), 1);
		allegati.forEach(a -> assertEquals(CodResponsabilita.ALTRI, a.getCodResponsabilita()));
	}
	
	@Test
	@WithMockUser("NNFGRL92L27G273P")
	@Transactional
	public void conDomandaConsulenteDatiCompletiRegistraDomandaResponsabilitaConsulente() throws Exception {
		File inputDataFile = new File(
				"src/test/resources/domandaRegistrazioneUtente/registrazioneNuovaDomanda/nuovaDomandaRegistrazioneUtenteConsulente.json");

		DatiDomandaRegistrazioneUtente dati = objectMapper.readValue(inputDataFile,
				DatiDomandaRegistrazioneUtente.class);

		Long id = service.registraDomanda(dati);
		
		assertNotNull(id);
		DomandaRegistrazioneUtente res = domandaUtenteRep.findById(id).get();
		assertEquals(id, res.getId());
		assertNotNull(res.getResponsabilita());

		ResponsabilitaRichieste responsabilita = objectMapper.readValue(res.getResponsabilita(),
				ResponsabilitaRichieste.class);
		
		assertNotNull(responsabilita);
		List<Consulente> coll = responsabilita.getResponsabilitaConsulente();
		assertNotNull(coll);
		assertEquals(coll.size(), 1);
		
		Set<A4gtAllegatoResponsabilita> allegati = res.getA4gtAllegatoResponsabilita();
		assertNotNull(allegati);
		assertEquals(allegati.size(), 1);
		allegati.forEach(a -> assertEquals(CodResponsabilita.LPC, a.getCodResponsabilita()));
	}
	
	@Test
	@WithMockUser("TRRCST78B08C794X")
	public void conDomandaGiaProtocollataErrore() throws Exception {
		File inputDataFile = new File(
				"src/test/resources/domandaRegistrazioneUtente/registrazioneNuovaDomanda/nuovaDomandaRegistrazioneUtenteCompleto.json");

		DatiDomandaRegistrazioneUtente dati = objectMapper.readValue(inputDataFile,
				DatiDomandaRegistrazioneUtente.class);

		try {
			service.registraDomanda(dati);
			throw new RuntimeException("Non deve essere possibile eseguire due volte il salvataggio");
		} catch (UtenteException ue) {
			assertEquals("Per l'utente esiste gia' una domanda, non e' possibile registrarne una seconda",
					ue.getMessage());
		}
	}

	@Test
	@WithMockUser("TRRCST78B08C7941")
	public void conDomandaInCompilazioneRegistraNuovaDomanda() throws Exception {
		File inputDataFile = new File(
				"src/test/resources/domandaRegistrazioneUtente/registrazioneNuovaDomanda/nuovaDomandaRegistrazioneUtenteCompleto.json");

		DatiDomandaRegistrazioneUtente dati = objectMapper.readValue(inputDataFile,
				DatiDomandaRegistrazioneUtente.class);

		Long id = service.registraDomanda(dati);
		assertNotNull(id);
		domandaUtenteRep.findById(id).get();
	}

	@Test
	@WithMockUser("BZZLBN21M45A178J")
	@Transactional
	public void checkModificaDomanda() throws Exception {

		DatiDomandaRegistrazioneUtente dati = preelaborazioneDomandaModifica(
				"src/test/resources/domandaRegistrazioneUtente/aggiornaDomanda/domandaAggiornaRegistrazioneUtenteCAA.json");

		Long idNew = service.aggiornaDomanda(dati);
		// aggiornamento andato a buon fine
		assertNotNull(idNew);
		assertEquals(dati.getId(), idNew);

		DomandaRegistrazioneUtente mod = domandaUtenteRep.findById(idNew).get();
		// dati anagrafici effettivamante modificati
		assertEquals(mod.getNome(), "bb");
		// dati servizi effettivamante modificati
		assertEquals(mod.getSrt(), false);

		ResponsabilitaRichieste responsabilitaNew = objectMapper.readValue(mod.getResponsabilita(), ResponsabilitaRichieste.class);

		// dati responsabilita effettivamante modificati
		assertEquals(responsabilitaNew.getResponsabilitaCaa().size(), 1);
		assertEquals(responsabilitaNew.getResponsabilitaCaa().get(0).getSedi().size(), 1);
	}

	@Test
	@WithMockUser("BZZLBN21M45A178J")
	@Transactional
	public void checkModificaDomandaVersioneErrata() throws Exception {

		DatiDomandaRegistrazioneUtente dati = preelaborazioneDomandaModifica(
				"src/test/resources/domandaRegistrazioneUtente/aggiornaDomanda/domandaAggiornaRegistrazioneUtenteCAA.json");
		
		try {
			service.aggiornaDomanda(dati, 4);
			assertTrue(false);
		} catch (ObjectOptimisticLockingFailureException ole) {
			assertTrue(true);
		} catch (Throwable t) {
			assertTrue(false);
		}
	}

	@Test
	@WithMockUser("BZZLBN21M45A178J")
	@Transactional
	public void checkmodificaDomandaNoModificaAllegati() throws Exception {
		Long idAllegato = new Long(295);
		DatiDomandaRegistrazioneUtente dati = preelaborazioneDomandaModifica(
				"src/test/resources/domandaRegistrazioneUtente/aggiornaDomanda/domandaAggiornaRegistrazioneUtenteCAA.json");

		Long idNew = service.aggiornaDomanda(dati);
		assertNotNull(idNew);
		assertEquals(dati.getId(), idNew);

		DomandaRegistrazioneUtente mod = domandaUtenteRep.findById(idNew).get();

		ResponsabilitaRichieste responsabilitaNew = objectMapper.readValue(mod.getResponsabilita(), ResponsabilitaRichieste.class);

		assertEquals(responsabilitaNew.getResponsabilitaCaa().size(), 1);

		Optional<A4gtAllegatoResponsabilita> allegatoOri = allegatoResponsabilitDao.findById(idAllegato);
		// allegato non modificato

		List<A4gtAllegatoResponsabilita> allegatiOri = (allegatoResponsabilitDao.findAll().stream()
				.filter(x -> x.getDomandaRegistrazione().getId().equals(idNew))).collect(Collectors.toList());

		assertEquals(allegatoOri.isPresent(), true);
	}

	@Test
	@WithMockUser("BZZLBN21M45A178J")
	@Transactional
	public void checkmodificaDomandaModificaAllegati() throws Exception {
		// id allegato old
		Long idAllegato = new Long(295);
		DatiDomandaRegistrazioneUtente dati = preelaborazioneDomandaModifica(
				"src/test/resources/domandaRegistrazioneUtente/aggiornaDomanda/domandaAggiornaRegistrazioneUtenteCAA2.json");

		Long idNew = service.aggiornaDomanda(dati);
		assertNotNull(idNew);
		assertEquals(dati.getId(), idNew);

		DomandaRegistrazioneUtente mod = domandaUtenteRep.findById(idNew).get();

		ResponsabilitaRichieste responsabilitaNew = objectMapper.readValue(mod.getResponsabilita(), ResponsabilitaRichieste.class);

		assertEquals(responsabilitaNew.getResponsabilitaCaa().size(), 1);

		// esiste 1 allegato con id diverso da 295
		List<A4gtAllegatoResponsabilita> allegati = (allegatoResponsabilitDao.findAll().stream()
				.filter(x -> x.getDomandaRegistrazione().getId().equals(idNew))).collect(Collectors.toList());

		assertEquals(allegati.size(), 1);
		assertNotEquals(allegati.get(0).getId(), idAllegato);
		assertEquals(allegati.get(0).getIdResponsabilita(), new Long(1));
	}

	@Test
	@WithMockUser("BZZLBN21M45A178J")
	@Transactional
	public void checkmodificaDomandaNuovaResponsabilita() throws Exception {
		// id allegato old
		Long idAllegato = new Long(295);
		DatiDomandaRegistrazioneUtente dati = preelaborazioneDomandaModifica(
				"src/test/resources/domandaRegistrazioneUtente/aggiornaDomanda/domandaAggiornaRegistrazioneUtenteCAA3.json");

		Long idNew = service.aggiornaDomanda(dati);
		assertNotNull(idNew);
		assertEquals(dati.getId(), idNew);

		DomandaRegistrazioneUtente mod = domandaUtenteRep.findById(idNew).get();

		ResponsabilitaRichieste responsabilitaNew = objectMapper.readValue(mod.getResponsabilita(), ResponsabilitaRichieste.class);

		// esiste un'unica responsabilita con id 2 (precesente è stata cancellata e
		// sostituita)
		assertEquals(responsabilitaNew.getResponsabilitaCaa().size(), 1);

		// nuovo id responsabilita
		Long idResponsabilitaNew = new Long(2);
		assertEquals(responsabilitaNew.getResponsabilitaCaa().get(0).getIdResponsabilita(), idResponsabilitaNew);

		// esiste 1 allegato con id diverso da 295
		List<A4gtAllegatoResponsabilita> allegati2 = allegatoResponsabilitDao.findAll();
		
		List<A4gtAllegatoResponsabilita> allegati = (allegatoResponsabilitDao.findAll().stream()
				.filter(x -> x.getDomandaRegistrazione().getId().equals(idNew))).collect(Collectors.toList());

		assertEquals(allegati.size(), 1);
		assertNotEquals(allegati.get(0).getId(), idAllegato);
		assertEquals(allegati.get(0).getIdResponsabilita(), idResponsabilitaNew);
	}

	public DatiDomandaRegistrazioneUtente preelaborazioneDomandaModifica(String file) throws Exception {
		Long id = new Long(294);
		Long idAllegato = new Long(295);
		String cf = "BZZLBN21M45A178J";

		considerazioniDomandDb(id, idAllegato, cf);

		return getDomandaModificata(file, id, cf);
	}

	public DatiDomandaRegistrazioneUtente getDomandaModificata(String file, Long id, String cf) throws Exception {
		File inputDataFile = new File(file);

		DatiDomandaRegistrazioneUtente dati = objectMapper.readValue(inputDataFile,
				DatiDomandaRegistrazioneUtente.class);

		assertEquals(dati.getId(), id);
		assertEquals(dati.getCodiceFiscale(), cf);
		return dati;
	}

	private void considerazioniDomandDb(Long id, Long idAllegato, String cf)
			throws IOException, JsonParseException, JsonMappingException {
		// considerazioni su domanda in db
		DomandaRegistrazioneUtente ori = domandaUtenteRep.findById(id).get();
		assertEquals(ori.getCodiceFiscale(), cf);
		assertEquals(ori.getNome(), "Albina");
		assertEquals(ori.getSrt(), true);

		// esiste 1 allegato con id 295
		List<A4gtAllegatoResponsabilita> allegatiOri = (allegatoResponsabilitDao.findAll().stream()
				.filter(x -> x.getDomandaRegistrazione().getId().equals(id))).collect(Collectors.toList());

		assertEquals(allegatiOri.size(), 1);
		assertEquals(allegatiOri.get(0).getId(), idAllegato);

		// esiste 1 responsabilita caa con 4 sedi e id 1
		ResponsabilitaRichieste responsabilita = objectMapper.readValue(ori.getResponsabilita(), ResponsabilitaRichieste.class);

		assertEquals(responsabilita.getResponsabilitaCaa().size(), 1);
		assertEquals(responsabilita.getResponsabilitaCaa().get(0).getSedi().size(), 4);
		assertEquals(responsabilita.getResponsabilitaCaa().get(0).getIdResponsabilita(), new Long(1));
	}

	@Test
	@WithMockUser("FRSLBT76H42E625Z")
	@Transactional
	public void ricercaDomandaProtocollataTRRRNZ56R23F837ZTrovaRisultato() throws Exception {
		DomandaRegistrazioneUtenteFilter criteri = new DomandaRegistrazioneUtenteFilter();
		String cf = "TRRRNZ56R23F837Z";
		criteri.setCodiceFiscale(cf);
		criteri.setStato(StatoDomandaRegistrazioneUtente.PROTOCOLLATA);
		List<DatiDomandaRegistrazioneUtente> result = service.ricerca(criteri);
		assertNotNull(result);
		assertEquals(1, result.size());
		DatiDomandaRegistrazioneUtente domandaProtocollata = result.get(0);
		assertEquals(cf, domandaProtocollata.getCodiceFiscale());
		assertEquals(StatoDomandaRegistrazioneUtente.PROTOCOLLATA, domandaProtocollata.getStato());
		
	}

}
