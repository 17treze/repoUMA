package it.tndigitale.a4g.proxy.services.protocollo.rest;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.protocollo.client.model.Correspondent;
import it.tndigitale.a4g.protocollo.client.model.GetCorrespondentResponse;
import it.tndigitale.a4g.protocollo.client.model.SearchCorrespondentsResponse;
import it.tndigitale.a4g.proxy.dto.protocollo.CorrespondentDto;
import it.tndigitale.a4g.proxy.services.protocollo.PiTreException;


@Service
public class CorrespondentsServiceImpl extends PiTreBaseService {

	private static final String PERSONA = "P";

	private static final String UFFICIO = "U";

	private static final String DEFAULT_CHANNEL = "MAIL";

	@Autowired
	private PiTreAddressBookServiceImpl piTreAddressBookServiceImpl;

	/**
	 * Per una richiesta di AddCorrespondent, i parametri obbligatori sono:
	 *
	 * - Code: Codice del corrispodente
	 * - Description: Nome del corrispondente (ad esempio, Mario Rossi, oppure Azienda s.p.a.)
	 * - CorrespondentType: tipo corrispondente (P se persona, U nel caso sia ufficio, azienda etc)
	 * - Type: E (sarà sempre un corrispodente esterno)
	 *
	 * Nel caso sia un corrispondente P, quindi una persona, diventano obbligatori:
	 * - Name
	 * - Surname
	 *
	 * @param correspondentInfo
	 * @return
	 * @throws PiTreException
	 */
	public CorrespondentDto findOrAddCorrespondent(CorrespondentDto correspondentInfo) throws PiTreException {
		try {
			CorrespondentDto result = new CorrespondentDto();
			String code = String.format("%1$s%2$s%3$s", "A4G", codeRF,
					correspondentInfo.getVatNumber() != null ? correspondentInfo.getVatNumber()
							: correspondentInfo.getNationalIdentificationNumber());

			SearchCorrespondentsResponse searchCorrespondentsResponse = piTreAddressBookServiceImpl.searchCorrespondents(code, codeRF);

			List<Correspondent> correspondents = searchCorrespondentsResponse.getCorrespondents();

			if (correspondents.stream().count() > 1)
				throw new PiTreException("Non è possibile avere più di un corrispondente associato al codice. Inserire un codice univoco.");

			Correspondent foundCorrespondent = correspondents.stream().findFirst().orElse(null);
			if (foundCorrespondent != null) {
				result.setAOOCode(foundCorrespondent.getAoOCode());
				BeanUtils.copyProperties(foundCorrespondent, result);
			} else {
				correspondentInfo.setCode(code);
				correspondentInfo.setCodeRegisterOrRF(codeRF);

				if (correspondentInfo.getCorrespondentType() == null ||
						(correspondentInfo.getCorrespondentType() != PERSONA &&
								correspondentInfo.getCorrespondentType() != UFFICIO))
					correspondentInfo.setCorrespondentType(PERSONA);

				if (correspondentInfo.getEmail() != null) {
					correspondentInfo.setPreferredChannel(DEFAULT_CHANNEL);
				}

				Correspondent correspondentRequest = new Correspondent();
				BeanUtils.copyProperties(correspondentInfo, correspondentRequest);
				correspondentRequest.setAoOCode(correspondentInfo.getAOOCode());

				GetCorrespondentResponse addCorrespondentResponse = piTreAddressBookServiceImpl.addCorrespondent(correspondentRequest);
				Correspondent addedCorrespondent = addCorrespondentResponse.getCorrespondent();
				BeanUtils.copyProperties(addedCorrespondent, result);
				result.setAOOCode(addedCorrespondent.getAoOCode());
				return result;
			}
			return result;
		} catch (PiTreException e) {
			throw new PiTreException(e.getMessage());
		} catch (Exception e) {
			throw new PiTreException(PiTreException.ERROR_UNSPECIFIED, e);
		}
	}

}
