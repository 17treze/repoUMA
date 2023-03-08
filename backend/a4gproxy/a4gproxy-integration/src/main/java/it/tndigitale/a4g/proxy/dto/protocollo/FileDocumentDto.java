package it.tndigitale.a4g.proxy.dto.protocollo;

public class FileDocumentDto {

	private byte[] content;

	private String name;
	
	private String description;

	private String mimeType;

	public FileDocumentDto() {};

	public FileDocumentDto(byte[] content, String name, String mimeType) {
		this(content, name, null, mimeType);
	}
	
	public FileDocumentDto(byte[] content, String name, String description, String mimeType) {
		super();
		this.content = content;
		this.name = name;
		this.description = description;
		this.mimeType = mimeType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
