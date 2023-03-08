package it.tndigitale.a4g.proxy.services.protocollo.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.protocollo.client.model.CreateDocumentResponse;
import it.tndigitale.a4g.protocollo.client.model.GetDocumentResponse;
import it.tndigitale.a4g.protocollo.client.model.GetRoleResponse;
import it.tndigitale.a4g.proxy.dto.DocumentCreationResultDto;
import it.tndigitale.a4g.proxy.dto.builder.MainDocumentBuilder;
import it.tndigitale.a4g.proxy.dto.protocollo.CorrespondentDto;
import it.tndigitale.a4g.proxy.dto.protocollo.DocumentDto;
import it.tndigitale.a4g.proxy.dto.protocollo.FileDocumentDto;
import it.tndigitale.a4g.proxy.services.protocollo.PiTreException;

@Service
public class DocumentsServiceImpl {
	private static Logger logger = LoggerFactory.getLogger(DocumentsServiceImpl.class);

	@Autowired
	private PiTreDocumentsServiceImpl piTreDocumentsService;

	@Autowired
	private PiTreTransmissionsServiceImpl piTreTransmissionsService;

	@Autowired
	private PiTreRolesServiceImpl piTreRolesService;

	@Autowired
	private CorrespondentsServiceImpl correspondentsService;

	@Autowired
	private PiTreDocumentsConfigurationServiceImpl piTreDocumentsConfigurationService;

	/**
	 * 
		La sequenza, secondo le istruzioni fornite dalla provincia, va modificata in:
		 
		1.	GetToken =  fatto centralmente per tutte le chiamate
		2.	SearchCorrespondent
			a.	AddCorrespondent
		3.	CreateDocument
		4.	UploadFileToDocument (documento principale) false
		5.	UploadFileToDocument (eventuali allegati)
		6.	ProtocolPredisposed
		7.	AddDocInProject
		8.	ExecuteTransmissionDocument

	 * @param incomingDocument
	 * @return
	 * @throws PiTreException
	 */
	public DocumentCreationResultDto createIncomingDocument(DocumentDto incomingDocument)
			throws PiTreException {

		try {
			DocumentCreationResultDto result = new DocumentCreationResultDto();
			String projectCode = piTreDocumentsConfigurationService.getProjectByDocumentType(incomingDocument.getMainDocumentType());
			String codeRole = piTreDocumentsConfigurationService.getRoleByDocumentType(incomingDocument.getMainDocumentType());
//			2.	SearchCorrespondent
//				a.	AddCorrespondent
			CorrespondentDto sender = correspondentsService.findOrAddCorrespondent(incomingDocument.getSender());
			// 3.	CreateDocument
			CreateDocumentResponse response = piTreDocumentsService.createDocument(incomingDocument, sender, projectCode);
			logger.debug("response da pitre = " + response);
			
			String documentId = response.getDocument().getId();
			// 4.	UploadFileToDocument (documento principale) false
			piTreDocumentsService.uploadFileToDocument(
					MainDocumentBuilder.from(incomingDocument.getMainDocument()), 
					documentId,
					false);
			//5.	UploadFileToDocument (eventuali allegati)
			for (FileDocumentDto item : incomingDocument.getAttachments()) {
					piTreDocumentsService.uploadFileToDocument(
							MainDocumentBuilder.from(item), 
							documentId);
			}
			//6.	ProtocolPredisposed
			GetDocumentResponse documentResponse = piTreDocumentsService.postProtocolPredisposed(documentId);
			result.setSignature(documentResponse.getDocument().getSignature());		
			//7.	AddDocInProject
			piTreDocumentsService.addInProject(documentId, projectCode);
			GetRoleResponse roleResponse = piTreRolesService.getRole(codeRole);
			//8.	ExecuteTransmissionDocument
			piTreTransmissionsService.executeTransmissionDocument(documentId, roleResponse.getRole());

			return result;
		} catch (PiTreException e) {
			throw e;
		} catch (Exception e) {
			throw new PiTreException(PiTreException.ERROR_UNSPECIFIED, e);
		}
	}

}
