package it.tndigitale.a4g.uma.business.service.protocollo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

//import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;
//import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto;
import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto.TipologiaDocumentoPrincipale;
import it.tndigitale.a4g.framework.client.custom.MittenteDto;
import it.tndigitale.a4g.framework.support.PersonaSelector;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.AllegatoConsuntivoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.ClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.ConsuntivoConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FatturaClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburanteConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.dto.aual.FascicoloAualDto;
import it.tndigitale.a4g.uma.dto.aual.SoggettoAualDto;
import it.tndigitale.a4g.uma.dto.protocollo.ProtocollaDocumentoUmaDto;
import it.tndigitale.a4g.uma.dto.protocollo.TipoDocumentoUma;

@Component("PROTOCOLLA_DICHIARAZIONE_CONSUMI")
public class ProtocollaDichiarazioneConsumi extends ProtocollazioneStrategy {

	private static final Logger logger = LoggerFactory.getLogger(ProtocollaDichiarazioneConsumi.class);

	private static final String SUFFISSO_NOME_FILE_DICHIARAZIONE_CONSUMI = "_dichiarazioneconsumi";
	private static final String PREFISSO_OGGETTO_DICHIARAZIONE_CONSUMI = "A4G - DICHIARAZIONE CONSUMI UMA - ";

	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private Clock clock;
	@Value("${it.tndigit.a4g.uma.protocollazione.firma.obbligatoria}")
	private boolean firmaObbligatoria;

	@Override
	@Transactional
	public void avviaProtocollo(Long id, ByteArrayResource documento, boolean haFirma) {

		DichiarazioneConsumiModel dichiarazioneConsumi = dichiarazioneConsumiDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Nessuna Dichiarazione Consumi trovata per id ".concat(String.valueOf(id))));
		RichiestaCarburanteModel richiesta = dichiarazioneConsumi.getRichiestaCarburante();

		// chiamata ad anagrafica - get fascicolo per i campi: PEC, descrizione impresa e denominazione sportello
		FascicoloAualDto fascicolo = getFascicolo(richiesta.getCuaa());

		// trova dati richiedente
		SoggettoAualDto richiedente = reperisciDatiRichiedente(richiesta.getCuaa(), dichiarazioneConsumi.getCfRichiedente(), TipoDocumentoUma.DICHIARAZIONE_CONSUMI);

		// aggiornamento dichiarazione
		dichiarazioneConsumiDao.save(dichiarazioneConsumi.setStato(StatoDichiarazioneConsumi.PROTOCOLLATA)
				.setDocumento(documento.getByteArray())
				.setFirma(firmaObbligatoria ? Boolean.TRUE : haFirma)
				.setEntePresentatore(getEntePresentatore(fascicolo)));

		// Allegati consumi
		List<ByteArrayResource> allegati = estraiAllegatiConsuntivi(dichiarazioneConsumi);

		// Allegati Clienti
		allegati.addAll(estraiAllegatiClienti(dichiarazioneConsumi));

		ProtocollaDocumentoUmaDto protocollaDocumentoUmaDto = new ProtocollaDocumentoUmaDto()
				.setDocumento(documento)
				.setId(id)
				.setCuaa(richiesta.getCuaa())
				.setAnno(richiesta.getCampagna().intValue())
				.setNome(richiedente.getDescNome())
				.setCognome(richiedente.getDescCogn())
				.setDescrizioneImpresa(fascicolo.getDescDeno())
				.setPec(fascicolo.getDescPec())
				.setAllegati(allegati) // Aggiungo gli allegati solo se presenti (Dichiarazione Consumi)
				.setTipoDocumentoUma(TipoDocumentoUma.DICHIARAZIONE_CONSUMI);

		// assicurati che il fascicolo sia valido
		controlloFascicoloValido(fascicolo);

		// controlla la firma del documento
		if (firmaObbligatoria) {
			verificaFirmaDocumento(documento, dichiarazioneConsumi.getCfRichiedente());
		} else {
			verificaFirmaDocumentoAndSave(documento, dichiarazioneConsumi.getCfRichiedente(), haFirma);
		}

		// pubblica evento
		publish(protocollaDocumentoUmaDto);
	}

	@Override
	@Transactional
	public DocumentDto buildDocumentDto(ProtocollaDocumentoUmaDto dati) {
		// Documento Principale
		ByteArrayResource documentoByteAsResource = getAllegatoAsByteArrayResource(dati.getDocumento().getByteArray(), String.valueOf(dati.getId()).concat(SUFFISSO_NOME_FILE_DICHIARAZIONE_CONSUMI).concat(".pdf"));
		// Metadati
		var mittenteDto = new MittenteDto()
				.setName(dati.getNome())
				.setSurname(dati.getCognome())
				.setEmail(dati.getPec())
				.setNationalIdentificationNumber(dati.getCuaa())
				.setDescription(dati.getCuaa() + " - " + dati.getDescrizioneImpresa());

		// build oggetto in base a persone fisica o giuridica
		String oggetto = PersonaSelector.isPersonaFisica(dati.getCuaa()) ? 
				String.format(PREFISSO_OGGETTO_DICHIARAZIONE_CONSUMI + "%s - %s - %s %s", dati.getAnno(), dati.getCuaa(), dati.getNome(), dati.getCognome()) :
					String.format(PREFISSO_OGGETTO_DICHIARAZIONE_CONSUMI + "%s - %s - %s", dati.getAnno(), dati.getCuaa(), dati.getDescrizioneImpresa());

		var metadatiDto = new MetadatiDto()
				.setMittente(mittenteDto)
				.setOggetto(oggetto)
				.setTipologiaDocumentoPrincipale(TipologiaDocumentoPrincipale.RICHIESTA_CARBURANTE);

		List<ByteArrayResource> allegati = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dati.getAllegati())) {
			dati.getAllegati().forEach(allegato -> allegati.add(new ByteArrayResource(allegato.getByteArray()) {
				@Override
				public String getFilename() {
					return allegato.getFilename();
				}
			}));
		}

		return new DocumentDto()
				.setDocumentoPrincipale(documentoByteAsResource)
				.setAllegati(!CollectionUtils.isEmpty(allegati) ? allegati : null)
				.setMetadati(metadatiDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void aggiornaDomanda(ProtocollaDocumentoUmaDto dati, String numeroProtocollo) {
		logger.info("Aggiornamento Numero di protocollo {} per dichiarazione consumi {}", numeroProtocollo, dati.getId());
		DichiarazioneConsumiModel dichiarazione = dichiarazioneConsumiDao.findById(dati.getId()).orElseThrow(() -> new EntityNotFoundException(String.format(" Nessuna dichiarazione da aggiornare %s", dati.getId())));
		dichiarazione.setDataProtocollazione(clock.now());
		dichiarazione.setProtocollo(numeroProtocollo);
		dichiarazioneConsumiDao.save(dichiarazione);
	}

	// La descrizione degli allegati è inserita all'interno del filename separata da $$
	private List<ByteArrayResource> estraiAllegatiConsuntivi(DichiarazioneConsumiModel dichiarazioneConsumi) {
		List<ByteArrayResource> allegati = new ArrayList<>();
		// Verifico la presenza di consuntivi
		if (CollectionUtils.isEmpty(dichiarazioneConsumi.getConsuntivi())) {
			return new ArrayList<>();
		}
		// Mappo i consuntivi aventi allegati  per tipo
		dichiarazioneConsumi.getConsuntivi()
		.stream()
		.filter(consuntivo -> !consuntivo.getAllegati().isEmpty()) 
		.collect(Collectors.groupingBy(ConsuntivoConsumiModel::getTipoConsuntivo))
		.entrySet()
		.stream()
		.forEach(entry -> {
			// Per ogni consuntivo prendo gli allegati e li aggiungo alla lista generale con il progressivo per tipo consuntivo
			if (entry.getKey().equals(TipoConsuntivo.AMMISSIBILE) && entry.getValue().stream().anyMatch(value -> value.getTipoCarburante().equals(TipoCarburanteConsuntivo.GASOLIO_TERZI))) {
				Integer i = 1;
				for (AllegatoConsuntivoModel allegato : entry.getValue().stream().filter(value -> value.getTipoCarburante().equals(TipoCarburanteConsuntivo.GASOLIO_TERZI)).findFirst().get().getAllegati()) {
					var joiner = new StringJoiner("_").add(i.toString())
							.add("gasolioct")
							.add(dichiarazioneConsumi.getRichiestaCarburante().getCuaa())
							.add(dichiarazioneConsumi.getRichiestaCarburante().getCampagna().toString());
					// Descrizione inserita dall'operatore
					allegati.add(getAllegatoAsByteArrayResource(allegato.getDocumento(), new StringJoiner("$$").add((joiner + ".pdf").toString()).add(allegato.getDescrizione()).toString()));
					i++;
				}
			}
			if (entry.getKey().equals(TipoConsuntivo.RECUPERO)) {
				Integer i = 1;
				for (ConsuntivoConsumiModel consuntivo: entry.getValue()) {
					for (AllegatoConsuntivoModel allegato : consuntivo.getAllegati()) {
						var joiner = new StringJoiner("_").add(i.toString())
								.add("recupero")
								.add(allegato.getConsuntivoModel().getTipoCarburante().name().toLowerCase())
								.add(dichiarazioneConsumi.getRichiestaCarburante().getCuaa())
								.add(dichiarazioneConsumi.getRichiestaCarburante().getCampagna().toString());
						// Descrizione uguale al nome del file
						allegati.add(getAllegatoAsByteArrayResource(allegato.getDocumento(), new StringJoiner("$$").add((joiner + ".pdf").toString()).add(joiner.toString()).toString()));
						i++;
					}
				}
			}
		});
		return allegati;
	}

	private List<ByteArrayResource> estraiAllegatiClienti(DichiarazioneConsumiModel dichiarazioneConsumi) {
		List<ByteArrayResource> allegati = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dichiarazioneConsumi.getClienti())) {
			// Ottengo i clienti con allegati
			List<ClienteModel> clienti = dichiarazioneConsumi.getClienti().stream()
					.filter(cliente -> !CollectionUtils.isEmpty(cliente.getAllegati())).collect(Collectors.toList());

			// Aggiungo gli allegati alla lista generale con il progressivo
			Integer i = 1;
			for (ClienteModel cliente : clienti) {
				for (FatturaClienteModel allegato : cliente.getAllegati()) {
					var joiner = new StringJoiner("_")
							.add(i.toString())
							.add("contoterzi")
							.add(dichiarazioneConsumi.getRichiestaCarburante().getCuaa())
							.add(dichiarazioneConsumi.getRichiestaCarburante().getCampagna().toString())
							.add(allegato.getCliente().getCuaa());
					// Descrizione uguale al nome del file
					allegati.add(getAllegatoAsByteArrayResource(allegato.getDocumento(), new StringJoiner("$$").add((joiner + ".pdf").toString()).add(joiner.toString()).toString()));
					i++;
				}
			}
		}
		return allegati;
	}

	private ByteArrayResource getAllegatoAsByteArrayResource(byte[] allegato, String nome) {
		return new ByteArrayResource(allegato) {
			@Override
			public String getFilename() {
				return nome;
			}
		};
	}
}
