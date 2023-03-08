package it.tndigitale.a4g.ags.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.ags.dto.domandaunica.Particella;

@JsonInclude(Include.NON_NULL)
public class SostegniSuperficie {

	private String sostegno;
	private Long idIntervento;
	private String codIntervento;
	private String descIntervento;
	private Particella particella;
	private Long idParcella;
	private Long idIsola;
	private String codIsola;
	private Long supDichiarata;
	private Long idPianoColture;
	private Long idColtura;
	private String codColtura3;
	private String descColtura;
	private Float coeffTara;
	private String codColtura5;
	private String codLivello;
	private Long supImpegnata;
	private String descMantenimento;

	public String getSostegno() {
		return sostegno;
	}

	public void setSostegno(String sostegno) {
		this.sostegno = sostegno;
	}

	public Long getIdIntervento() {
		return idIntervento;
	}

	public void setIdIntervento(Long idIntervento) {
		this.idIntervento = idIntervento;
	}

	public String getCodIntervento() {
		return codIntervento;
	}

	public void setCodIntervento(String codIntervento) {
		this.codIntervento = codIntervento;
	}

	public String getDescIntervento() {
		return descIntervento;
	}

	public void setDescIntervento(String descIntervento) {
		this.descIntervento = descIntervento;
	}

	public Particella getParticella() {
		return particella;
	}

	public void setParticella(Particella particella) {
		this.particella = particella;
	}

	public Long getIdParcella() {
		return idParcella;
	}

	public void setIdParcella(Long idParcella) {
		this.idParcella = idParcella;
	}

	public Long getIdIsola() {
		return idIsola;
	}

	public void setIdIsola(Long idIsola) {
		this.idIsola = idIsola;
	}

	public String getCodIsola() {
		return codIsola;
	}

	public void setCodIsola(String codIsola) {
		this.codIsola = codIsola;
	}

	public Long getSupDichiarata() {
		return supDichiarata;
	}

	public void setSupDichiarata(Long supDichiarata) {
		this.supDichiarata = supDichiarata;
	}

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

	public String getDescColtura() {
		return descColtura;
	}

	public void setDescColtura(String descColtura) {
		this.descColtura = descColtura;
	}

	public Float getCoeffTara() {
		return coeffTara;
	}

	public void setCoeffTara(Float coeffTara) {
		this.coeffTara = coeffTara;
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

	public Long getSupImpegnata() {
		return supImpegnata;
	}

	public void setSupImpegnata(Long supImpegnata) {
		this.supImpegnata = supImpegnata;
	}

	public String getDescMantenimento() {
		return descMantenimento;
	}

	public void setDescMantenimento(String descMantenimento) {
		this.descMantenimento = descMantenimento;
	}

}
