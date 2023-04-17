package it.tndigitale.a4g.uma.business.service.protocollo;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.DetenzioneAgsDto.TipoDetenzioneEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto.StatoEnum;
import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.VerificaFirmaClient;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.SuperficieMassimaModel;
import it.tndigitale.a4g.uma.business.persistence.repository.GruppiLavorazioneDao;
import it.tndigitale.a4g.uma.business.persistence.repository.SuperficieMassimaDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.lavorazioni.RecuperaLavorazioniSuperficie;
import it.tndigitale.a4g.uma.dto.protocollo.ProtocollaDocumentoUmaDto;
import it.tndigitale.a4g.uma.dto.protocollo.TipoDocumentoUma;

public abstract class ProtocollazioneStrategy {
	
	@Value("${it.tndigit.a4g.uma.protocollazione.firma.obbligatoria}")
	private boolean firmaObbligatoria;
	
	@Autowired
	private UmaAnagraficaClient anagraficaClient;
	@Autowired
	private EventBus eventBus;
	@Autowired
	private VerificaFirmaClient verificaFirmaClient;
	@Autowired
	private RecuperaLavorazioniSuperficie recuperaLavorazioniSuperficieComponent;
	@Autowired
	private SuperficieMassimaDao superficieMassimaDao;
	@Autowired
	private GruppiLavorazioneDao gruppiLavorazioneDao;
	
	private static final Logger logger = LoggerFactory.getLogger(ProtocollazioneStrategy.class);
	
	abstract DocumentDto buildDocumentDto(ProtocollaDocumentoUmaDto protocollaDocumentoUmaDto);
	
	abstract void avviaProtocollo(Long id, ByteArrayResource documento, boolean haFirma);
	
	abstract void aggiornaDomanda(ProtocollaDocumentoUmaDto data, String numeroProtocollo);
	
	// Salva le superfici massime - solo per RICHIESTA e RETTIFICA
	protected void salvaSuperficiMassime(RichiestaCarburanteModel richiesta) {
		var raggruppamentiSuperficie = recuperaLavorazioniSuperficieComponent.getRaggruppamenti(richiesta); // Reperisco gli eventuali raggruppamenti a superficie della richiesta
		if (!CollectionUtils.isEmpty(raggruppamentiSuperficie)) {
			raggruppamentiSuperficie.forEach(raggruppamento -> // Per ogni raggruppamento trovato salvo la superficie massima
			superficieMassimaDao.save(new SuperficieMassimaModel().setRichiestaCarburante(richiesta)
					.setGruppoLavorazione(gruppiLavorazioneDao.findById(raggruppamento.getId())
							.orElseThrow(() -> new EntityNotFoundException(String
									.format("Raggruppamento con indice : %s non trovato", raggruppamento.getIndice()))))
					.setSuperficieMassima(raggruppamento.getSuperficieMassima())));
		}
	}
	
	protected FascicoloAgsDto getFascicolo(String cuaa) {
		return anagraficaClient.getFascicolo(cuaa);
	}
	
	// Reperisce la detenzione del fascicolo ags, dando priorità alla detenzione di tipo DELEGA
	protected String getEntePresentatore(FascicoloAgsDto fascicolo) {
		// Reperisco la detenzione del fascicolo ags
		var det = fascicolo.getDetenzioni().stream()
				.filter(detenzione -> detenzione.getTipoDetenzione().equals(TipoDetenzioneEnum.MANDATO)).findFirst()
				.orElseGet(() -> fascicolo.getDetenzioni().stream()
						.filter(detenzione -> fascicolo.getDetenzioni().size() == 1
								&& detenzione.getTipoDetenzione().equals(TipoDetenzioneEnum.DELEGA))
						.findFirst()
						.orElseThrow(() -> new IllegalArgumentException("Errore nel reperimento della detenzione")));
		return det.getSportello();
	}
	
	/**
	 * Reperisce i dati di un richiedente in base al tipo di domanda indicato. La richiesta di carburante reperisce
	 * esclusivamente i Titolari e i Rappresentanti legali. La rettifica e la dichiarazione consumi reperiscono i
	 * Titolari/RL o in alternativa gli eventuali Eredi.
	 * 
	 * @param cuaa
	 * @param cfRichiedente
	 * @param tipo
	 * @return
	 */
	protected CaricaAgsDto reperisciDatiRichiedente(String cuaa, String cfRichiedente, TipoDocumentoUma tipo) {
		FascicoloAgsDto fascicolo = getFascicolo(cuaa);
		List<CaricaAgsDto> soggetti;
		if (TipoDocumentoUma.RICHIESTA.equals(tipo)) {
			soggetti = anagraficaClient.getTitolariRappresentantiLegali(cuaa);
		}
		else {
			soggetti = fascicolo.getDataMorteTitolare() == null ? anagraficaClient.getTitolariRappresentantiLegali(cuaa)
					: anagraficaClient.getEredi(cuaa);
		}
		Assert.isTrue(!CollectionUtils.isEmpty(soggetti),
				String.format("Nessun soggetto trovato nel fascicolo per il cuaa %s", cuaa));
		
		return soggetti.stream().filter(persona -> persona.getCodiceFiscale().equals(cfRichiedente)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("La persona ".concat(cfRichiedente)
						.concat(" non è presente nel fascicolo aziendale ").concat(cuaa)));
	}
	
	protected void verificaFirmaDocumento(ByteArrayResource document, String codiceFiscaleFirmatario) {
		try {
			// verificaFirmaClient.verificaFirma(document, codiceFiscaleFirmatario);
			logger.info("Verifica firma documento non eseguita");
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Errore verifica firma documento");
		}
	}
	
	protected void verificaFirmaDocumentoAndSave(ByteArrayResource document, String codiceFiscaleFirmatario,
			boolean haFirma) {
		try {
			// verificaFirmaClient.verificaFirma(document, codiceFiscaleFirmatario);
			logger.info("Verifica firma documento non eseguita");
		}
		catch (Exception e) {
			if (haFirma) {
				// se il doc ha la firma e il servizio di verifica firma va in errore vuol dire che c'è stato un errore generico nella chiamata al servizio
				throw new IllegalArgumentException("Errore verifica firma documento");
			}
		}
	}
	
	protected void controlloFascicoloValido(FascicoloAgsDto fascicolo) {
		if (!StatoEnum.VALIDO.equals(fascicolo.getStato())) {
			var errMsg = String.format("Il fascicolo %s non risulta valido", fascicolo.getCuaa());
			logger.error(errMsg);
			throw new IllegalArgumentException(errMsg);
		}
	}
	
	protected void publish(ProtocollaDocumentoUmaDto protocollaDocumentoUmaDto) {
		var event = new ProtocollaDocumentoUmaEvent(protocollaDocumentoUmaDto);
		eventBus.publishEvent(event);
	}
	
}
