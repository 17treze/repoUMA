package it.tndigitale.a4g.ags.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.ags.dto.domandaunica.Particella;

@JsonInclude(Include.NON_EMPTY)
public class InfoEleggibilitaParticella {

	private String sostegno;
	private String codIntervento;
	private Particella particella;
	private String codColtura3;
	private String codColtura5;
	private Long superficieGis;
	private Long superficieSigeco;
	private Long livello;
	private Long superficieAnomaliaCoor;

	public Long getLivello() {
		return livello;
	}

	public void setLivello(Long livello) {
		this.livello = livello;
	}

	public String getSostegno() {
		return sostegno;
	}

	public void setSostegno(String sostegno) {
		this.sostegno = sostegno;
	}

	public String getCodIntervento() {
		return codIntervento;
	}

	public void setCodIntervento(String codIntervento) {
		this.codIntervento = codIntervento;
	}

	public Particella getParticella() {
		return particella;
	}

	public void setParticella(Particella particella) {
		this.particella = particella;
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

	public Long getSuperficieGis() {
		return superficieGis;
	}

	public void setSuperficieGis(Long superficieGis) {
		this.superficieGis = superficieGis;
	}

	public Long getSuperficieSigeco() {
		return superficieSigeco;
	}

	public void setSuperficieSigeco(Long superficieSigeco) {
		this.superficieSigeco = superficieSigeco;
	}
	
	public Long getSuperficieAnomaliaCoor() {
		return superficieAnomaliaCoor;
	}

	public void setSuperficieAnomaliaCoor(Long superficieAnomaliaCoor) {
		this.superficieAnomaliaCoor = superficieAnomaliaCoor;
	}

}
