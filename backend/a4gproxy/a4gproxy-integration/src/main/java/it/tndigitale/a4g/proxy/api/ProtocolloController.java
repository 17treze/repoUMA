package it.tndigitale.a4g.proxy.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.protocollo.client.model.GetDocumentRequest;
import it.tndigitale.a4g.protocollo.client.model.GetDocumentResponse;
import it.tndigitale.a4g.proxy.dto.DocumentCreationResultDto;
import it.tndigitale.a4g.proxy.dto.protocollo.DocumentDto;
import it.tndigitale.a4g.proxy.dto.protocollo.DocumentRegistrationInfoDto;
import it.tndigitale.a4g.proxy.dto.protocollo.FileDocumentDto;
import it.tndigitale.a4g.proxy.dto.protocollo.TipologiaDocumento;
import it.tndigitale.a4g.proxy.services.protocollo.PiTreException;
import it.tndigitale.a4g.proxy.services.protocollo.rest.DocumentsServiceImpl;
import it.tndigitale.a4g.proxy.services.protocollo.rest.PiTreDocumentsServiceImpl;

@RestController
//@RequestMapping("/api/v1/protocollo")
@RequestMapping(ApiUrls.PROTOCOLLO)
@Api(value = "ProtocolloController")
public class ProtocolloController {

	private static final Logger logger = LoggerFactory.getLogger(ProtocolloController.class);

	private static final String DESCRIZIONE_ALLEGATO = "Allegato";
	
	@Autowired
	private DocumentsServiceImpl documentsService;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PiTreDocumentsServiceImpl docServiceImpl;

	@ApiOperation("Crea un nuovo documento protocollato in ingresso, lo aggiunge al fascicolo specificato ed effettua la trasmissione al ruolo. "
			+ "N.B.: se il mittente specificato non è presente in rubrica, viene aggiunto.")
	@PostMapping(path = "/documenti")
	public String createIncomingDocumentREST(
			@ApiParam(value = "Informazioni di creazione del nuovo documento: metadati, file principale, allegati, informazioni sul mittente e fascicolo") @RequestPart(value = "info") String info,
			@ApiParam(value = "Documento da protocollare") @RequestPart(value = "documento") MultipartFile documento,
			@ApiParam(value = "Allegati al documento da protocollare") @RequestPart(value = "allegati") List<MultipartFile> allegati) throws Exception {
		logger.debug("ProtocolloController createIncomingDocument");
		DocumentRegistrationInfoDto infoDto = objectMapper.readValue(info, DocumentRegistrationInfoDto.class);

		if (infoDto.getTipologiaDocumentoPrincipale() == null || infoDto.getTipologiaDocumentoPrincipale().isEmpty()) {
			throw new PiTreException("Tipo di documento non specificato. Verificare la configurazione.");
		}

		String contentType = documento.getContentType();
		if (documento.getOriginalFilename().contains("p7m")) {
			contentType = "application/pkcs7-mime";
		}
		FileDocumentDto fileDocumentDto = new FileDocumentDto(documento.getBytes(), documento.getOriginalFilename(), contentType);

		List<FileDocumentDto> allegatiIn = new LinkedList<>();

		int i = 1;
		for (MultipartFile allegato : allegati) {
			try {
				var descrizione = "";
				var nome = "";
				if (allegato.getOriginalFilename().contains("$$")) {
					List<String> collect = Arrays.stream(allegato.getOriginalFilename().split("\\$\\$")).collect(Collectors.toList());
					nome = collect.get(0);
					descrizione = collect.get(1);
				} else {
					nome = allegato.getOriginalFilename();
					descrizione =  DESCRIZIONE_ALLEGATO + "_" + i;
				}
				allegatiIn.add(new FileDocumentDto(allegato.getBytes(), nome, descrizione, allegato.getContentType()));
				i++;
			} catch (IOException e) {
				logger.error("Fallito il caricamento dell'allegato ".concat(allegato.getName()), e);
			}
		}

		DocumentDto document = new DocumentDto();

		document.setMainDocument(fileDocumentDto)  
		.setAttachments(allegatiIn)
		.setObject(infoDto.getOggetto())
		.setSender(infoDto.getMittente())
		.setMetadatiTemplate(infoDto.getMetadatiTemplate())
		.setMainDocumentType(TipologiaDocumento.valueOf(infoDto.getTipologiaDocumentoPrincipale()));

		DocumentCreationResultDto result = documentsService.createIncomingDocument(document);

		return result.getSignature();
	}

	// Send And Download da PostMan per scaricare il/i file allegati
	@PostMapping(path = "/getDocumentDownload")
	public void testGetDocument(@RequestBody GetDocumentRequest req, HttpServletResponse res) throws Exception {
		GetDocumentResponse docRes = docServiceImpl.postGetDocument(req);
		res.setContentType(docRes.getDocument().getMainDocument().getMimeType());
		res.setHeader("Content-Disposition", "attachment;filename=" + docRes.getDocument().getMainDocument().getName());
		res.getOutputStream().write(docRes.getDocument().getMainDocument().getContent());
	}
}
