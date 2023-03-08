package it.tndigitale.a4g.fascicolo.anagrafica.dto;

public class EmailDto {
	
	private String to;
	private String subject;
	private String text;
	
	public String getTo() {
		return to;
	}
	public EmailDto setTo(String to) {
		this.to = to;
		return this;
	}
	public String getSubject() {
		return subject;
	}
	public EmailDto setSubject(String subject) {
		this.subject = subject;
		return this;
	}
	public String getText() {
		return text;
	}
	public EmailDto setText(String text) {
		this.text = text;
		return this;
	}

}
