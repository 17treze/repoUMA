package it.tndigitale.a4g.uma.business.service.protocollo;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

// import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;
// import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto;
import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto.TipologiaDocumentoPrincipale;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.client.custom.MittenteDto;
import it.tndigitale.a4g.framework.support.PersonaSelector;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.service.richiesta.RicercaRichiestaCarburanteService;
import it.tndigitale.a4g.uma.dto.aual.FascicoloAualDto;
import it.tndigitale.a4g.uma.dto.aual.SoggettoAualDto;
import it.tndigitale.a4g.uma.dto.protocollo.ProtocollaDocumentoUmaDto;
import it.tndigitale.a4g.uma.dto.protocollo.TipoDocumentoUma;

@Component("PROTOCOLLA_RETTIFICA")
public class ProtocollaRettificaCarburante extends ProtocollazioneStrategy {
	
	private static final Logger logger = LoggerFactory.getLogger(ProtocollaRettificaCarburante.class);
	
	private static final String SUFFISSO_NOME_FILE_RETTIFICA_CARBURANTE = "_rettificarichiestacarburante";
	private static final String PREFISSO_OGGETTO_RETTIFICA_CARBURANTE = "A4G - RETTIFICA RICHIESTA CARBURANTE UMA - ";
	
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private RicercaRichiestaCarburanteService ricercaRichiestaCarburanteService;
	@Autowired
	private Clock clock;
	
	@Value("${it.tndigit.a4g.uma.protocollazione.firma.obbligatoria}")
	private boolean firmaObbligatoria;
	
	@Override
	@Transactional
	public void avviaProtocollo(Long id, ByteArrayResource documento, boolean haFirma) {
		
		RichiestaCarburanteModel richiesta = richiestaCarburanteDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException(String.format("Richiesta Carburante con id : %s non trovata", id)));
		
		// chiamata ad anagrafica - get fascicolo per i campi: PEC, descrizione impresa e denominazione sportello
		FascicoloAualDto fascicolo = getFascicolo(richiesta.getCuaa());
		
		// trova dati richiedente
		// SoggettoAualDto richiedente = reperisciDatiRichiedente(richiesta.getCuaa(), richiesta.getCfRichiedente(), TipoDocumentoUma.RETTIFICA);
		
		// assicurati che il fascicolo sia valido
		controlloFascicoloValido(fascicolo);
		
		// controlla la firma del documento
		if (firmaObbligatoria) {
			verificaFirmaDocumento(documento, richiesta.getCfRichiedente());
		}
		else {
			verificaFirmaDocumentoAndSave(documento, richiesta.getCfRichiedente(), haFirma);
		}
		
		// replica i controlli che ha fatto in fase di creazione della domanda (se non già fatto nel service)
		
		// salva la superficie massima
		salvaSuperficiMassime(richiesta);
		
		// Salva la rettifica
		richiestaCarburanteDao.save(richiesta.setStato(StatoRichiestaCarburante.AUTORIZZATA)
				// .setDocumento(documento.getByteArray())
				.setFirma(firmaObbligatoria ? Boolean.TRUE : haFirma)
				.setEntePresentatore(getEntePresentatore(fascicolo)));
		
		// aggiorna la richiesta precedente
		Optional<Long> idRettificata = ricercaRichiestaCarburanteService.getIdRettificata(richiesta.getCuaa(),
				richiesta.getCampagna(), richiesta.getDataPresentazione());
		
		Assert.isTrue(idRettificata.isPresent(),
				"Errore in fase di protocollazione Rettifica di carburante: Nessuna domanda da rettificare");
		
		RichiestaCarburanteModel richiestaDaRettificare = richiestaCarburanteDao.findById(idRettificata.get())
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Richiesta Carburante con id : %s non trovata", id)));
		
		richiestaDaRettificare.setStato(StatoRichiestaCarburante.RETTIFICATA);
		
		richiestaCarburanteDao.save(richiestaDaRettificare);
		
		ProtocollaDocumentoUmaDto protocollaRichiestaCarburanteDto = new ProtocollaDocumentoUmaDto()
				.setDocumento(documento).setId(id).setCuaa(richiesta.getCuaa())
				.setAnno(richiesta.getCampagna().intValue()).setNome("Nome1") // richiedente.getDescNome())
				.setCognome("Cognome1") // richiedente.getDescCogn())
				.setDescrizioneImpresa(fascicolo.getDescDeno()).setPec(fascicolo.getDescPec())
				.setTipoDocumentoUma(TipoDocumentoUma.RETTIFICA);
		
		// pubblica evento
		publish(protocollaRichiestaCarburanteDto);
	}
	
	@Override
	@Transactional
	public DocumentDto buildDocumentDto(ProtocollaDocumentoUmaDto dati) {
		// Documento Principale
		ByteArrayResource documentoByteAsResource = new ByteArrayResource(dati.getDocumento().getByteArray()) {
			@Override
			public String getFilename() {
				return String.valueOf(dati.getId()).concat(SUFFISSO_NOME_FILE_RETTIFICA_CARBURANTE).concat(".pdf");
			}
		};
		
		// Metadati
		var mittenteDto = new MittenteDto().setName(dati.getNome()).setSurname(dati.getCognome())
				.setEmail(dati.getPec()).setNationalIdentificationNumber(dati.getCuaa())
				.setDescription(dati.getCuaa() + " - " + dati.getDescrizioneImpresa());
		
		// build oggetto in base a persone fisica o giuridica
		String oggetto = PersonaSelector.isPersonaFisica(dati.getCuaa())
				? String.format(PREFISSO_OGGETTO_RETTIFICA_CARBURANTE + "%s - %s - %s %s", dati.getAnno(),
						dati.getCuaa(), dati.getNome(), dati.getCognome())
				: String.format(PREFISSO_OGGETTO_RETTIFICA_CARBURANTE + " %s - %s - %s", dati.getAnno(), dati.getCuaa(),
						dati.getDescrizioneImpresa());
		
		var metadatiDto = new MetadatiDto().setMittente(mittenteDto).setOggetto(oggetto)
				.setTipologiaDocumentoPrincipale(TipologiaDocumentoPrincipale.RICHIESTA_CARBURANTE);
		
		return new DocumentDto().setDocumentoPrincipale(documentoByteAsResource).setMetadati(metadatiDto);
	}
	
	protected String getFilename(Long idRichiesta) {
		return String.valueOf(idRichiesta).concat(SUFFISSO_NOME_FILE_RETTIFICA_CARBURANTE).concat(".pdf");
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void aggiornaDomanda(ProtocollaDocumentoUmaDto dati, String numeroProtocollo) {
		try {
			logger.info("Aggiornamento Numero di protocollo {} per domanda {}", numeroProtocollo, dati.getId());
			RichiestaCarburanteModel richiesta = richiestaCarburanteDao.findById(dati.getId())
					.orElseThrow(() -> new EntityNotFoundException(
							String.format("Nessuna richiesta da aggiornare %s", dati.getId())));
			richiesta.setDataProtocollazione(clock.now());
			richiesta.setNomeFile(this.salvaDocProtocollato(dati.getId(), dati.getAnno(), dati.getDocumento()));
			richiesta.setProtocollo(numeroProtocollo);
			richiestaCarburanteDao.save(richiesta);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
