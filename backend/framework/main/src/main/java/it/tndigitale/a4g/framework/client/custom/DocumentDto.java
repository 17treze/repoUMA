package it.tndigitale.a4g.framework.client.custom;

import org.springframework.core.io.ByteArrayResource;

import java.util.List;
import java.util.Objects;

/**
 * metadati ha la seguente struttura:
 *
 * [
 *   {
 *     "oggetto": "<Oggeto del documento da protocollare o repertoriare>",
 *     "mittente": {
 *       "address": "<valore attributo>",
 *       "admCode": "<valore attributo>",
 *       "aooCode": "<valore attributo>",
 *       "cap": "<valore attributo>",
 *       "city": "<valore attributo>",
 *       "code": "<valore attributo>",
 *       "codeRegisterOrRF": "<valore attributo>",
 *       "correspondentType": "<valore attributo>",
 *       "description": "<valore attributo>",
 *       "email": "<valore attributo>",
 *       "fax": "<valore attributo>",
 *       "id": "<valore attributo>",
 *       " isCommonAddress": "<valore attributo>",
 *       "location": "<valore attributo>",
 *       "name": "<valore attributo>",
 *       "nation": "<valore attributo>",
 *       "nationalIdentificationNumber": "<valore attributo>",
 *       "note": "<valore attributo>",
 *       "phoneNumber": "<valore attributo>",
 *       "preferredChannel": "<valore attributo>",
 *       "province": "<valore attributo>",
 *       "surname": "<valore attributo>",
 *       "type": "<valore attributo>",
 *       "vatNumber": "<valore attributo>"
 *     },
 *     "tipologiaDocumentoPrincipale": "<vale uno dei seguenti PRIVACY ANTIMAFIA ACCESSO_SISTEMA MANDATO>",
 *     "metadatiTemplate": <chiave valori da capire per ora mai usato>
 *   }
 * ]
 */
public class DocumentDto {
	private MetadatiDto metadati;

	private ByteArrayResource documentoPrincipale;

	private List<ByteArrayResource> allegati;

	public MetadatiDto getMetadati() {
		return metadati;
	}

	public DocumentDto setMetadati(MetadatiDto metadati) {
		this.metadati = metadati;
		return this;
	}

	public ByteArrayResource getDocumentoPrincipale() {
		return documentoPrincipale;
	}

	public DocumentDto setDocumentoPrincipale(ByteArrayResource documentoPrincipale) {
		this.documentoPrincipale = documentoPrincipale;
		return this;
	}

	public List<ByteArrayResource> getAllegati() {
		return allegati;
	}

	public DocumentDto setAllegati(List<ByteArrayResource> allegati) {
		this.allegati = allegati;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentDto that = (DocumentDto) o;
		return Objects.equals(metadati, that.metadati) &&
				Objects.equals(documentoPrincipale, that.documentoPrincipale) &&
				Objects.equals(allegati, that.allegati);
	}

	@Override
	public int hashCode() {
		return Objects.hash(metadati, documentoPrincipale, allegati);
	}

	public enum TipologiaDocumento {
		PRIVACY, ANTIMAFIA, ACCESSO_SISTEMA, MANDATO
	}
}
