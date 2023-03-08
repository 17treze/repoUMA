package it.tndigitale.a4g.proxy.services.protocollo.client;

public enum PiTreRestRoutedAction {
	GET_TOKEN("GetToken"),
	GET_ROLE("GetRole"), 
	PROTOCOL_PREDISPOSED("ProtocolPredisposed"),
	CREATE_DOCUMENT_AND_ADD_IN_PROJECT("CreateDocumentAndAddInProject"),
	CREATE_DOCUMENT("CreateDocument"),
	ADD_IN_PROJECT("AddDocInProject"),
	UPLOAD_FILE_TO_DOCUMENT("UploadFileToDocument"),
	GET_DOCUMENT("GetDocument"),
	SEARCH_CORRESPONDENTS("SearchCorrespondents"),
	ADD_CORRESPONDENT("AddCorrespondent"),
	EXECUTE_TRANSMISSION_DOCUMENT("ExecuteTransmissionDocument"),
	SEARCH_PROJECTS("SearchProjects"),
	GET_DOCUMENT_TEMPLATE("GetDocumentTemplate");

	private String action;

	private PiTreRestRoutedAction(final String action) {
		this.action = action;
	}
	public String getAction() {
		return action;
	}

}
