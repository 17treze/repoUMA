package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;

public class InformazioniColtivazione implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idPianoColture;
	private Long idColtura;
	private String codColtura3;
	private String codColtura5;
	private String codLivello;
	private String descrizioneColtura;
	private Float coefficienteTara;
	private Long superficieDichiarata;
	private String descMantenimento;

	public Long getIdPianoColture() {
		return idPianoColture;
	}

	public void setIdPianoColture(Long idPianoColture) {
		this.idPianoColture = idPianoColture;
	}

	public Long getIdColtura() {
		return idColtura;
	}

	public void setIdColtura(Long idColtura) {
		this.idColtura = idColtura;
	}

	public String getCodColtura3() {
		return codColtura3;
	}

	public void setCodColtura3(String codColtura3) {
		this.codColtura3 = codColtura3;
	}

	public String getCodColtura5() {
		return codColtura5;
	}

	public void setCodColtura5(String codColtura5) {
		this.codColtura5 = codColtura5;
	}

	public String getCodLivello() {
		return codLivello;
	}

	public void setCodLivello(String codLivello) {
		this.codLivello = codLivello;
	}

	public String getDescrizioneColtura() {
		return descrizioneColtura;
	}

	public void setDescrizioneColtura(String descrizioneColtura) {
		this.descrizioneColtura = descrizioneColtura;
	}

	public Float getCoefficienteTara() {
		return coefficienteTara;
	}

	public void setCoefficienteTara(Float coefficienteTara) {
		this.coefficienteTara = coefficienteTara;
	}

	public Long getSuperficieDichiarata() {
		return superficieDichiarata;
	}

	public void setSuperficieDichiarata(Long superficieDichiarata) {
		this.superficieDichiarata = superficieDichiarata;
	}

	public String getDescMantenimento() {
		return descMantenimento;
	}

	public void setDescMantenimento(String descMantenimento) {
		this.descMantenimento = descMantenimento;
	}
}
