package it.tndigitale.a4gutente.dto;

public class DomandaRegistrazioneCreataResponse {
	private Long id;
	private String base64Content;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBase64Content() {
		return base64Content;
	}
	public void setBase64Content(String base64Content) {
		this.base64Content = base64Content;
	}
}
