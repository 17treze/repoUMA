package it.tndigitale.a4g.ags.dto.domandaunica;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Coltura {

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
	public Long getIdColtura() {
		return idColtura;
	}
	public String getCodColtura3() {
		return codColtura3;
	}
	public String getCodColtura5() {
		return codColtura5;
	}
	public String getCodLivello() {
		return codLivello;
	}
	public String getDescrizioneColtura() {
		return descrizioneColtura;
	}
	public Float getCoefficienteTara() {
		return coefficienteTara;
	}
	public Long getSuperficieDichiarata() {
		return superficieDichiarata;
	}
	public String getDescMantenimento() {
		return descMantenimento;
	}
	public void setIdPianoColture(Long idPianoColture) {
		this.idPianoColture = idPianoColture;
	}
	public void setIdColtura(Long idColtura) {
		this.idColtura = idColtura;
	}
	public void setCodColtura3(String codColtura3) {
		this.codColtura3 = codColtura3;
	}
	public void setCodColtura5(String codColtura5) {
		this.codColtura5 = codColtura5;
	}
	public void setCodLivello(String codLivello) {
		this.codLivello = codLivello;
	}
	public void setDescrizioneColtura(String descrizioneColtura) {
		this.descrizioneColtura = descrizioneColtura;
	}
	public void setCoefficienteTara(Float coefficienteTara) {
		this.coefficienteTara = coefficienteTara;
	}
	public void setSuperficieDichiarata(Long superficieDichiarata) {
		this.superficieDichiarata = superficieDichiarata;
	}
	public void setDescMantenimento(String descMantenimento) {
		this.descMantenimento = descMantenimento;
	}	
}
