package it.tndigitale.a4g.proxy.services.protocollo.rest;

import static it.tndigitale.a4g.proxy.services.protocollo.rest.ErrorMessageEvaluation.checkPiTreResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.protocollo.client.model.AddDocInProjectRequest;
import it.tndigitale.a4g.protocollo.client.model.Correspondent;
import it.tndigitale.a4g.protocollo.client.model.CreateDocAndAddInPrjRequest;
import it.tndigitale.a4g.protocollo.client.model.CreateDocumentRequest;
import it.tndigitale.a4g.protocollo.client.model.CreateDocumentResponse;
import it.tndigitale.a4g.protocollo.client.model.Document;
import it.tndigitale.a4g.protocollo.client.model.GetDocumentRequest;
import it.tndigitale.a4g.protocollo.client.model.GetDocumentResponse;
import it.tndigitale.a4g.protocollo.client.model.MainDocument;
import it.tndigitale.a4g.protocollo.client.model.MessageResponse;
import it.tndigitale.a4g.protocollo.client.model.ProtocolPredisposedRequest;
import it.tndigitale.a4g.protocollo.client.model.UploadFileToDocumentRequest;
import it.tndigitale.a4g.protocollo.client.model.UploadFileToDocumentResponse;
import it.tndigitale.a4g.proxy.dto.protocollo.CorrespondentDto;
import it.tndigitale.a4g.proxy.dto.protocollo.DocumentDto;
import it.tndigitale.a4g.proxy.services.protocollo.PiTreException;
import it.tndigitale.a4g.proxy.services.protocollo.rest.template.CreazioneTemplateComponent;

@Service
public class PiTreDocumentsServiceImpl extends PiTreBaseService {

	private static final Logger log = LoggerFactory.getLogger(PiTreDocumentsServiceImpl.class);

	@Autowired
	private ProjectsServiceImpl projectsService;
	@Autowired
	private CreazioneTemplateComponent templateComponent;

	private CreateDocumentResponse createDocumentAndAddInProject(Document document, String projectCode) throws Exception {
		log.debug("PiTre createDocumentAndAddInProject");

		CreateDocAndAddInPrjRequest request = new CreateDocAndAddInPrjRequest();
		// Nuova implementazione non prevista dai metodi SOAP originari - TASK-PITRE-02
		document.setPredisposed(true);
		document.setMeansOfSending("SERVIZI ONLINE");

		request.setDocument(document);
		request.setCodeRF(codeRF);
		request.setCodeRegister(wsPiTreCodeDefaultRegister);

		String projectId = projectsService.getProjectByCode(projectCode).getId();
		if (projectId == null)
			throw new PiTreException("Il fascicolo specificato per il documento non esiste.");

		request.setIdProject(projectId);

		// Chiamata Rest
		return restApi4Ptre.putCreateDocumentAndAddInProject(request);
	}
	
	public CreateDocumentResponse createDocument(
			DocumentDto incomingDocument,
			CorrespondentDto sender,
			String projectCode
			) throws Exception {
		return createDocument(buildDocument(incomingDocument, sender), projectCode);
	}

	private CreateDocumentResponse createDocument(Document document, String projectCode) throws Exception {
		log.debug("PiTre createDocument");

		CreateDocumentRequest request = new CreateDocumentRequest();

		// Nuova implementazione non prevista dai metodi SOAP originari - TASK-PITRE-02
		document.setPredisposed(true);
		document.setMeansOfSending("SERVIZI ONLINE");
		request.setDocument(document);
		request.setCodeRF(codeRF);
		request.setCodeRegister(wsPiTreCodeDefaultRegister);

		String projectId = projectsService.getProjectByCode(projectCode).getId();
		if (projectId == null)
			throw new PiTreException("Il fascicolo specificato per il documento non esiste.");

		// Chiamata Rest
		return restApi4Ptre.putCreateDocument(request);
	}

	public MessageResponse addInProject(String idDocument, String projectCode) throws Exception {
		log.debug("PiTre createDocumentAndAddInProject");

		AddDocInProjectRequest request = new AddDocInProjectRequest();

		String projectId = projectsService.getProjectByCode(projectCode).getId();
		if (projectId == null)
			throw new PiTreException("Il fascicolo specificato per il documento non esiste.");
		request.setIdDocument(idDocument);
		request.setIdProject(projectId);
		// Chiamata Rest
		MessageResponse postAddInProject = restApi4Ptre.postAddInProject(request);
		checkPiTreResponse(postAddInProject.getCode().getValue(), postAddInProject.getErrorMessage());
		return postAddInProject;
	}

	// MainDocument nuovo
	public UploadFileToDocumentResponse uploadFileToDocument(MainDocument attachment, String documentId) throws Exception {
		return uploadFileToDocument(attachment, documentId, true);
	}

	public UploadFileToDocumentResponse uploadFileToDocument(MainDocument attachment, String documentId, boolean createAttachment) throws Exception {
		log.debug("PiTre uploadFileToDocument");

		UploadFileToDocumentRequest request = new UploadFileToDocumentRequest();

		request.setFile(attachment);

		String description = attachment.getDescription() == null ? FilenameUtils.removeExtension(attachment.getName()) : attachment.getDescription();
		request.setDescription(description);

		request.setCreateAttachment(createAttachment);// false se documento principale
		request.setIdDocument(documentId);

		return restApi4Ptre.putUploadFileToDocument(request);
	}

	public GetDocumentResponse postProtocolPredisposed(String documentId) throws PiTreException {
		ProtocolPredisposedRequest requestProt = new ProtocolPredisposedRequest();
		requestProt.setCodeRF(codeRF);
		requestProt.setCodeRegister(wsPiTreCodeDefaultRegister);
		requestProt.setIdDocument(documentId);
		GetDocumentResponse postProtocolPredisposed = restApi4Ptre.postProtocolPredisposed(requestProt);
		checkPiTreResponse(postProtocolPredisposed.getCode().getValue(), postProtocolPredisposed.getErrorMessage());
		return postProtocolPredisposed;
	}

	public GetDocumentResponse postGetDocument(GetDocumentRequest req) {
		return restApi4Ptre.postGetDocument(req);
	}
	
	private Document buildDocument(DocumentDto incomingDocument, CorrespondentDto sender) {
		Document document = new Document();
		Correspondent correspondent = new Correspondent();
		BeanUtils.copyProperties(sender, correspondent);
		document.setSender(correspondent);

		document.setObject(incomingDocument.getObject());
		document.setDocumentType("A");
		templateComponent.createTemplate(incomingDocument, document);
		return document;
	}
}
