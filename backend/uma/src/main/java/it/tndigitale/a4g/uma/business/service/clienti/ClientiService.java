package it.tndigitale.a4g.uma.business.service.clienti;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

// import it.tndigitale.a4g.fascicolo.anagrafica.client.model.MovimentoValidazioneFascicoloAgsDto;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.ClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FatturaClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.UtilizzoMacchinariModel;
import it.tndigitale.a4g.uma.business.persistence.repository.ClienteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.ConsumiClientiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.FattureClientiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.UtilizzoMacchinariDao;
import it.tndigitale.a4g.uma.business.service.lavorazioni.LavorazioniService;
import it.tndigitale.a4g.uma.business.service.lavorazioni.RecuperaLavorazioniSuperficie;
import it.tndigitale.a4g.uma.dto.clienti.ClienteConsumiDto;
import it.tndigitale.a4g.uma.dto.clienti.builder.ClienteBuilder;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;

@Service
public class ClientiService {
	
	private static final Logger logger = LoggerFactory.getLogger(ClientiService.class);
	
	private static final String CLIENTE_FIND_BY_ERROR = "Non esiste un cliente con id ";
	private static final String SUB_DIRECTORY_RICHIESTE = "/richieste-carburante";
	
	@Value("${pathDownload}")
	private String pathDownload;
	
	@Autowired
	private ClienteDao clienteDao;
	@Autowired
	private ClientiValidator clientiValidator;
	@Autowired
	private UtilizzoMacchinariDao utilizzoMacchinariDao;
	@Autowired
	private RecuperaLavorazioniSuperficie recuperaLavorazioniSuperficie;
	@Autowired
	private LavorazioniService lavorazioniService;
	@Autowired
	private FattureClientiDao fattureClientiDao;
	@Autowired
	private ConsumiClientiDao consumiClientiDao;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private Clock clock;
	
	@Transactional
	public Long importaDatiCliente(Long id, String cuaa, List<MultipartFile> allegati) {
		
		// MovimentoValidazioneFascicoloAgsDto movFas = clientiValidator.validaFascicoloCliente(cuaa, clock.now().getYear() - 1);
		DichiarazioneConsumiModel dichiarazioneConsumi = clientiValidator.validaDichiarazioneConsumiCliente(id);
		
		// da sistemare
		var clienteToSave = new ClienteModel().setCuaa(cuaa).setDenominazione("IMPERI MADDALENA").setIdFascicolo(1L)
				.setDichiarazioneConsumi(dichiarazioneConsumi);
		
		// Salvataggio del cliente
		ClienteModel clienteSaved = clienteDao.save(clienteToSave);
		
		// Salvo i nuovi allegati per il cliente
		allegati.forEach(allegato -> fattureClientiDao.save(buildFatturaClienteModel(clienteSaved, allegato)));
		logger.info("[UMA] - Salvataggio nuovo cliente con fascicolo {} dichiarazione consumi {}", cuaa, id);
		return clienteSaved.getId();
	}
	
	public ClienteConsumiDto getCliente(Long idCliente) {
		var clienteModel = clienteDao.findById(idCliente)
				.orElseThrow(() -> new EntityNotFoundException(CLIENTE_FIND_BY_ERROR + idCliente.toString()));
		DichiarazioneConsumiModel dichiarazioneConsumi = clienteModel.getDichiarazioneConsumi();
		List<UtilizzoMacchinariModel> utilizzoMacchinariModelList = utilizzoMacchinariDao
				.findByRichiestaCarburante(dichiarazioneConsumi.getRichiestaCarburante());
		boolean haGasolio = utilizzoMacchinariModelList.stream()
				.anyMatch(macchina -> TipoCarburante.GASOLIO.equals(macchina.getAlimentazione()));
		boolean haBenzina = utilizzoMacchinariModelList.stream()
				.anyMatch(macchina -> TipoCarburante.BENZINA.equals(macchina.getAlimentazione()));
		
		return new ClienteConsumiDto().setBenzina(haBenzina).setGasolio(haGasolio)
				.buildFrom(new ClienteBuilder().from(clienteModel).build());
		
	}
	
	public List<RaggruppamentoLavorazioniDto> getLavorazioniSuperficie(Long idCliente) {
		var clienteModel = clienteDao.findById(idCliente)
				.orElseThrow(() -> new EntityNotFoundException(CLIENTE_FIND_BY_ERROR + idCliente.toString()));
		Long campagna = clienteModel.getDichiarazioneConsumi().getRichiestaCarburante().getCampagna();
		Optional<RichiestaCarburanteModel> richiestaOpt = getRichiestaCliente(idCliente);
		var dataConduzione = Clock.ofEndOfDay(LocalDate.of(campagna.intValue(), Month.NOVEMBER, 1));
		LocalDateTime dataPresentazioneDichiarazioneConsumi = clienteModel.getDichiarazioneConsumi()
				.getDataPresentazione();
		if (richiestaOpt.isPresent()) {
			dataConduzione = richiestaOpt.get().getDataPresentazione();
		}
		else if (dataConduzione.isAfter(dataPresentazioneDichiarazioneConsumi)) {
			dataConduzione = dataPresentazioneDichiarazioneConsumi;
		}
		return recuperaLavorazioniSuperficie.getRaggruppamenti(clienteModel.getCuaa(), dataConduzione);
	}
	
	public List<DichiarazioneDto> getFabbisogniSuperficie(Long idCliente) {
		return lavorazioniService.getFabbisogniCliente(idCliente);
	}
	
	public List<DichiarazioneDto> getFabbisogniRichiestaCliente(Long idCliente) {
		return lavorazioniService.getFabbisogniRichiestaCliente(idCliente);
	}
	
	public void dichiaraFabbisogniSuperficie(Long idCliente, List<DichiarazioneDto> dichiarazioni) {
		lavorazioniService.dichiaraFabbisogniCliente(idCliente, dichiarazioni);
	}
	
	public void validaCliente(Long id, String cuaa) {
		//		MovimentoValidazioneFascicoloAgsDto movFas = clientiValidator.validaFascicoloCliente(idFascicolo, clock.now().getYear() - 1);
		//		clientiValidator.validaDichiarazioneConsumiCliente(id, movFas);
	}
	
	@Transactional
	public void salvaAllegati(Long idCliente, List<MultipartFile> allegati) {
		// Reperisco il cliente
		ClienteModel cliente = clienteDao.findById(idCliente)
				.orElseThrow(() -> new EntityNotFoundException(CLIENTE_FIND_BY_ERROR + idCliente.toString()));
		
		// Elimino gli allegati salvati precedentemente per il cliente
		fattureClientiDao.deleteByCliente_id(idCliente);
		
		// Salvo i nuovi allegati per il cliente
		allegati.forEach(allegato -> fattureClientiDao.save(buildFatturaClienteModel(cliente, allegato)));
		logger.info("[UMA] - Salvataggio allegati cliente id {}", idCliente);
	}
	
	@Transactional
	public void eliminaCliente(Long idCliente) {
		// cancella consumi clienti
		consumiClientiDao.deleteByCliente_id(idCliente);
		// cancella fatture clienti 
		fattureClientiDao.deleteByCliente_id(idCliente);
		// cancella cliente
		clienteDao.deleteById(idCliente);
		logger.info("[UMA] - Clienti conto terzi - Cancellazione cliente {}", idCliente);
	}
	
	private FatturaClienteModel buildFatturaClienteModel(ClienteModel cliente, MultipartFile allegato) {
		FatturaClienteModel fatturaCliente = new FatturaClienteModel().setCliente(cliente)
				.setNomeFile(allegato.getOriginalFilename());
		
		try {
			// fatturaCliente.setDocumento(allegato.getBytes());
			this.salvaDocProtocollato(cliente.getId(), Calendar.YEAR, allegato.getBytes(),
					allegato.getOriginalFilename());
			
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
					"[UMA] - Caricamento allegati clienti - Errore nella conversione da Multipart a Byte Array");
		}
		return fatturaCliente;
	}
	
	private Optional<RichiestaCarburanteModel> getRichiestaCliente(Long idCliente) {
		var clienteModel = clienteDao.findById(idCliente)
				.orElseThrow(() -> new EntityNotFoundException(CLIENTE_FIND_BY_ERROR + idCliente.toString()));
		Long campagna = clienteModel.getDichiarazioneConsumi().getRichiestaCarburante().getCampagna();
		return richiestaCarburanteDao.findByCuaaAndCampagnaAndStato(clienteModel.getCuaa(), campagna,
				StatoRichiestaCarburante.AUTORIZZATA);
	}
	
	protected String salvaDocProtocollato(Long id, Integer anno, byte[] docBytes, String filename) throws IOException {
		
		if (id != null && anno != null && docBytes != null && filename != null) {
			Path filePath = Paths.get(this.pathDownload + SUB_DIRECTORY_RICHIESTE + "/" + anno + "/" + filename);
			
			Path parentDir = filePath.getParent();
			if (!Files.exists(parentDir)) {
				Files.createDirectories(parentDir);
			}
			Files.write(filePath, docBytes, StandardOpenOption.CREATE_NEW);
			return filename;
		}
		throw new IOException("Dati incompleti. Id: " + id + ", anno:" + anno + ", documento: " + docBytes
				+ ", filename: " + filename);
	}
}
