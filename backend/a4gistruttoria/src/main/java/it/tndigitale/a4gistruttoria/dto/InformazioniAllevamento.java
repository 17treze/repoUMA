package it.tndigitale.a4gistruttoria.dto;

public class InformazioniAllevamento {

	private Long idAllevamento;
	private String codiceAllevamento;
	private String codiceAllevamentoBdn;
	private String descrizioneAllevamento;
	private String comune;
	private String indirizzo;

	public Long getIdAllevamento() {
		return idAllevamento;
	}

	public void setIdAllevamento(Long idAllevamento) {
		this.idAllevamento = idAllevamento;
	}

	public String getCodiceAllevamento() {
		return codiceAllevamento;
	}

	public void setCodiceAllevamento(String codiceAllevamento) {
		this.codiceAllevamento = codiceAllevamento;
	}

	public String getCodiceAllevamentoBdn() {
		return codiceAllevamentoBdn;
	}

	public void setCodiceAllevamentoBdn(String codiceAllevamentoBdn) {
		this.codiceAllevamentoBdn = codiceAllevamentoBdn;
	}

	public String getDescrizioneAllevamento() {
		return descrizioneAllevamento;
	}

	public void setDescrizioneAllevamento(String descrizioneAllevamento) {
		this.descrizioneAllevamento = descrizioneAllevamento;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
}
