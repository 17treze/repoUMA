/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

/**
 * @author B.Conetta
 *
 */
public class IstruttoriaAntimafiaEsito {

	private Long idDomandaAntimafia;
	private String stato;
	private String parixMessage;
	private String siapMessage;
	private List<String> soggettiMessages;

	public IstruttoriaAntimafiaEsito() {
	 super();
	}
	
	public IstruttoriaAntimafiaEsito(Long idDomandaAntimafia) {
		super();
		this.idDomandaAntimafia = idDomandaAntimafia;
	}

	public Long getIdDomandaAntimafia() {
		return idDomandaAntimafia;
	}

	public void setIdDomandaAntimafia(Long idDomandaAntimafia) {
		this.idDomandaAntimafia = idDomandaAntimafia;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getParixMessage() {
		return parixMessage;
	}

	public void setParixMessage(String parixMessage) {
		this.parixMessage = parixMessage;
	}

	public String getSiapMessage() {
		return siapMessage;
	}

	public void setSiapMessage(String siapMessage) {
		this.siapMessage = siapMessage;
	}

	public List<String> getSoggettiMessages() {
		return soggettiMessages;
	}

	public void setSoggettiMessages(List<String> soggettiMessages) {
		this.soggettiMessages = soggettiMessages;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IstruttoriaAntimafiaEsito [idDomandaAntimafia=");
		builder.append(idDomandaAntimafia);
		builder.append(", stato=");
		builder.append(stato);
		builder.append(", parixMessage=");
		builder.append(parixMessage);
		builder.append(", siapMessage=");
		builder.append(siapMessage);
		builder.append(", soggettiMessages=");
		builder.append(soggettiMessages);
		builder.append("]");
		return builder.toString();
	}

}
