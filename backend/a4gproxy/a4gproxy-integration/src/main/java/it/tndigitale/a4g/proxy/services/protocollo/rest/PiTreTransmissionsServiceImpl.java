package it.tndigitale.a4g.proxy.services.protocollo.rest;

import static it.tndigitale.a4g.proxy.services.protocollo.rest.ErrorMessageEvaluation.checkPiTreResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.protocollo.client.model.ExecuteTransmissionDocumentRequest;
import it.tndigitale.a4g.protocollo.client.model.TransmissionResponse;
import it.tndigitale.a4g.proxy.services.protocollo.PiTreException;

@Service
public class PiTreTransmissionsServiceImpl extends PiTreBaseService {

	private static final Logger log = LoggerFactory.getLogger(PiTreTransmissionsServiceImpl.class);

	public TransmissionResponse executeTransmissionDocument(String documentId, it.tndigitale.a4g.protocollo.client.model.Role role) throws PiTreException {
		log.debug("PiTre executeTransmissionDocument");
		ExecuteTransmissionDocumentRequest request = new ExecuteTransmissionDocumentRequest();
		it.tndigitale.a4g.protocollo.client.model.Correspondent receiver = new it.tndigitale.a4g.protocollo.client.model.Correspondent();
		receiver.setCode(role.getCode());
		request.setIdDocument(documentId);
		request.setReceiver(receiver);
		request.setNotify(true);
		request.setTransmissionType("S");
		request.setTransmissionReason("Inoltro");
		TransmissionResponse transmissionResponse = restApi4Ptre.postExecuteTransmissionDocument(request);
		checkPiTreResponse(transmissionResponse.getCode().getValue(), transmissionResponse.getErrorMessage());
		return transmissionResponse;
	}
}
