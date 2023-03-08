package it.tndigitale.a4g.proxy.dto.protocollo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DocumentDto {
	private TipologiaDocumento mainDocumentType;

	private FileDocumentDto mainDocument;

	private List<FileDocumentDto> attachments;

	private CorrespondentDto sender;

	private String object;

	private Map<String, String> metadatiTemplate = new HashMap<String, String>();

	public TipologiaDocumento getMainDocumentType() {
		return mainDocumentType;
	}

	public DocumentDto setMainDocumentType(TipologiaDocumento mainDocumentType) {
		this.mainDocumentType = mainDocumentType;
		return this;
	}

	public FileDocumentDto getMainDocument() {
		return mainDocument;
	}

	public DocumentDto setMainDocument(FileDocumentDto mainDocument) {
		this.mainDocument = mainDocument;
		return this;
	}

	public List<FileDocumentDto> getAttachments() {
		return attachments;
	}

	public DocumentDto setAttachments(List<FileDocumentDto> attachments) {
		this.attachments = attachments;
		return this;
	}

	public CorrespondentDto getSender() {
		return sender;
	}

	public DocumentDto setSender(CorrespondentDto sender) {
		this.sender = sender;
		return this;
	}

	public String getObject() {
		return object;
	}

	public DocumentDto setObject(String object) {
		this.object = object;
		return this;
	}

	public Map<String, String> getMetadatiTemplate() {
		return metadatiTemplate;
	}

	public DocumentDto setMetadatiTemplate(Map<String, String> metadatiTemplate) {
		this.metadatiTemplate = metadatiTemplate;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentDto that = (DocumentDto) o;
		return mainDocumentType == that.mainDocumentType &&
				Objects.equals(mainDocument, that.mainDocument) &&
				Objects.equals(attachments, that.attachments) &&
				Objects.equals(sender, that.sender) &&
				Objects.equals(object, that.object) &&
				Objects.equals(metadatiTemplate, that.metadatiTemplate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mainDocumentType, mainDocument, attachments, sender, object, metadatiTemplate);
	}
}
