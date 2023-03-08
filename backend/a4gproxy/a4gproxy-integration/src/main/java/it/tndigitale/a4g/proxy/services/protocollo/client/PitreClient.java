package it.tndigitale.a4g.proxy.services.protocollo.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.protocollo.client.model.AddCorrespondentRequest;
import it.tndigitale.a4g.protocollo.client.model.AddDocInProjectRequest;
import it.tndigitale.a4g.protocollo.client.model.AuthenticateRequest;
import it.tndigitale.a4g.protocollo.client.model.AuthenticateResponse;
import it.tndigitale.a4g.protocollo.client.model.CreateDocAndAddInPrjRequest;
import it.tndigitale.a4g.protocollo.client.model.CreateDocumentRequest;
import it.tndigitale.a4g.protocollo.client.model.CreateDocumentResponse;
import it.tndigitale.a4g.protocollo.client.model.ExecuteTransmissionDocumentRequest;
import it.tndigitale.a4g.protocollo.client.model.GetCorrespondentResponse;
import it.tndigitale.a4g.protocollo.client.model.GetDocumentRequest;
import it.tndigitale.a4g.protocollo.client.model.GetDocumentResponse;
import it.tndigitale.a4g.protocollo.client.model.GetRoleResponse;
import it.tndigitale.a4g.protocollo.client.model.GetTemplateResponse;
import it.tndigitale.a4g.protocollo.client.model.MessageResponse;
import it.tndigitale.a4g.protocollo.client.model.ProtocolPredisposedRequest;
import it.tndigitale.a4g.protocollo.client.model.SearchCorrespondentsResponse;
import it.tndigitale.a4g.protocollo.client.model.SearchFiltersOnlyRequest;
import it.tndigitale.a4g.protocollo.client.model.SearchProjectsResponse;
import it.tndigitale.a4g.protocollo.client.model.SearchRequest;
import it.tndigitale.a4g.protocollo.client.model.TransmissionResponse;
import it.tndigitale.a4g.protocollo.client.model.UploadFileToDocumentRequest;
import it.tndigitale.a4g.protocollo.client.model.UploadFileToDocumentResponse;

/**
 * 
 * Per accedere ed interagire con i dati presenti in P.I.Tre., i client dovranno 
 * utilizzare il servizio RouteRestRequest, disponibile nei metodi GET, PUT e POST.
 * 
 * @author s.caccia
 *
 */
@Component
public class PitreClient {

	@Autowired
	private PiTreCreateHttpRequest piTreCreateRequest;

	/**
	 * [Authenticate] - Servizio per il prelievo del token di autenticazione.
	 * @param AuthenticateRequest
	 * @return AuthenticateResponse
	 */
	public AuthenticateResponse postAuthenticationTokenRoute(AuthenticateRequest request) {
		return piTreCreateRequest.createRestCall(request, AuthenticateResponse.class, PiTreRestRoutedAction.GET_TOKEN.getAction(), HttpMethod.POST);
	}

	/**
	 * [Roles] - Servizio per il reperimento dei dettagli di un ruolo dato il codice del ruolo o l'id.
	 * Inserire soltanto uno di queesti due valori. Metodo GET.
	 * @param codeRole
	 * @param idRole
	 * @return GetRoleResponse
	 */
	public GetRoleResponse getRole(String codeRole,String idRole) {
		Map<String,String> request = new HashMap<>();
		request.computeIfAbsent("codeRole", value -> codeRole);
		request.computeIfAbsent("idRole", value -> idRole);
		return piTreCreateRequest.createRestCall(request, GetRoleResponse.class, PiTreRestRoutedAction.GET_ROLE.getAction(),HttpMethod.GET);
	}

	/**
	 * [Documents] - Protocolla i documenti precedentemente predisposti.
	 * @param ProtocolPredisposedRequest
	 * @return GetDocumentResponse
	 */
	public GetDocumentResponse postProtocolPredisposed(ProtocolPredisposedRequest request) {
		return piTreCreateRequest.createRestCall(request, GetDocumentResponse.class, PiTreRestRoutedAction.PROTOCOL_PREDISPOSED.getAction(),HttpMethod.POST);
	}

	/**
	 * [Documents] - Servizio che crea un nuovo documento e lo inserisce nel relativo fascicolo indicato.
	 * @param CreateDocAndAddInPrjRequest
	 * @return CreateDocumentResponse
	 */
	public CreateDocumentResponse putCreateDocumentAndAddInProject(CreateDocAndAddInPrjRequest request) {
		return piTreCreateRequest.createRestCall(request, CreateDocumentResponse.class, PiTreRestRoutedAction.CREATE_DOCUMENT_AND_ADD_IN_PROJECT.getAction(),HttpMethod.PUT);
	}
	
	/**
	 * [Documents] - Metodo per l'inserimento di un documento in un fascicolo.
	 * @param request
	 * @return
	 */
	public MessageResponse postAddInProject(AddDocInProjectRequest request) {
		return piTreCreateRequest.createRestCall(request, MessageResponse.class, PiTreRestRoutedAction.ADD_IN_PROJECT.getAction(),HttpMethod.POST);
	}
	
	/**
	 * [Documents] - Metodo per la creazione di un documento non protocollato, protocollato in arrivo, in uscita, interno oppure un predisposto in arrivo, in uscita oppure interno.
	 * @param request
	 * @return
	 */
	public CreateDocumentResponse putCreateDocument(CreateDocumentRequest request) {
		return piTreCreateRequest.createRestCall(request, CreateDocumentResponse.class, PiTreRestRoutedAction.CREATE_DOCUMENT.getAction(),HttpMethod.PUT);
	}

	/**
	 * [Documents] - Servizio per aggiungere un file ad un documento, per creare un allegato o per aggiungere una nuova versione del file.
	 * @param UploadFileToDocumentRequest
	 * @return UploadFileToDocumentResponse
	 */
	public UploadFileToDocumentResponse putUploadFileToDocument(UploadFileToDocumentRequest request) {
		return piTreCreateRequest.createRestCall(request, UploadFileToDocumentResponse.class, PiTreRestRoutedAction.UPLOAD_FILE_TO_DOCUMENT.getAction(),HttpMethod.PUT);
	}
	
	/**
	 * [Documents] - Servizio per il reperimento di un documento
	 * @param GetDocumentRequest
	 * @return GetDocumentResponse
	 */
	public GetDocumentResponse postGetDocument(GetDocumentRequest request) {
		return piTreCreateRequest.createRestCall(request, GetDocumentResponse.class, PiTreRestRoutedAction.GET_DOCUMENT.getAction(),HttpMethod.POST);
	}

	/**
	 * [AddressBook] - Servizio per la ricerca di corrispondenti.
	 * @param SearchFiltersOnlyRequest
	 * @return SearchCorrespondentsResponse
	 */
	public SearchCorrespondentsResponse postSearchCorrespondents(SearchFiltersOnlyRequest request) {
		return piTreCreateRequest.createRestCall(request, SearchCorrespondentsResponse.class, PiTreRestRoutedAction.SEARCH_CORRESPONDENTS.getAction(),HttpMethod.POST);
	}

	/**
	 * [AddressBook] - Servizio per la creazione di un nuovo corrispondente.
	 * @param AddCorrespondentRequest
	 * @return GetCorrespondentResponse
	 */
	public GetCorrespondentResponse putAddCorrespondent(AddCorrespondentRequest request) {
		return piTreCreateRequest.createRestCall(request, GetCorrespondentResponse.class, PiTreRestRoutedAction.ADD_CORRESPONDENT.getAction(),HttpMethod.PUT);
	}

	/**
	 * [Transmissions] - Servizio per la trasmissione singola di un documento senza l’utilizzo di un modello di trasmissione.
	 * @param ExecuteTransmissionDocumentRequest
	 * @return TransmissionResponse
	 */
	public TransmissionResponse postExecuteTransmissionDocument(ExecuteTransmissionDocumentRequest request) {
		return piTreCreateRequest.createRestCall(request, TransmissionResponse.class, PiTreRestRoutedAction.EXECUTE_TRANSMISSION_DOCUMENT.getAction(),HttpMethod.POST);
	}

	/**
	 * [Projects] - Servizio per la ricerca di fascicoli.
	 * @param SearchRequest
	 * @return SearchProjectsResponse
	 */
	public SearchProjectsResponse postSearchProjects(SearchRequest searchRequest) {
		return piTreCreateRequest.createRestCall(searchRequest, SearchProjectsResponse.class,PiTreRestRoutedAction.SEARCH_PROJECTS.getAction(),HttpMethod.POST);
	}
	
	/**
	 * [Documents] - Servizio per il reperimento di un documento
	 * @param GetDocumentTemplate
	 * @return GetDocumentResponse
	 */
	public GetTemplateResponse getDocumentTemplate(String descriptionTemplate) {
		Map<String,String> request = new HashMap<>();
		request.computeIfAbsent("descriptionTemplate", value -> descriptionTemplate);
		return piTreCreateRequest.createRestCall(request, GetTemplateResponse.class, PiTreRestRoutedAction.GET_DOCUMENT_TEMPLATE.getAction(),HttpMethod.GET);
	}
	
}