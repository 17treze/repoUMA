package it.tndigitale.a4gistruttoria.dto.lavorazione;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "codComune", "foglio", "particella", "sub", "codColtura", "descColtura", "controlloRegioni",
		"controlloColture", "controlloAnomCoord", "supImpegnata", "supRichiesta", "supControlliLoco",
		"supEleggibileGis", "supDeterminata" })
public class DettaglioDatiParticellaACS {

	private String codComune;
	private Long foglio;
	private String particella;
	private String sub;
	private String codColtura;
	private String descColtura;
	private Boolean controlloRegioni;
	private Boolean controlloColture;
	private Boolean controlloAnomCoord;
	private Float supImpegnata;
	private Float supRichiesta;
	private Float supControlliLoco;
	private Float supEleggibileGis;
	private Float supDeterminata;
	private Float superficieAnomaliaCoor;

	public String getCodComune() {
		return codComune;
	}

	public void setCodComune(String codComune) {
		this.codComune = codComune;
	}

	public Long getFoglio() {
		return foglio;
	}

	public void setFoglio(Long foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getCodColtura() {
		return codColtura;
	}

	public void setCodColtura(String codColtura) {
		this.codColtura = codColtura;
	}

	public String getDescColtura() {
		return descColtura;
	}

	public void setDescColtura(String descColtura) {
		this.descColtura = descColtura;
	}

	public Boolean getControlloRegioni() {
		return controlloRegioni;
	}

	public void setControlloRegioni(Boolean controlloRegioni) {
		this.controlloRegioni = controlloRegioni;
	}

	public Boolean getControlloColture() {
		return controlloColture;
	}

	public void setControlloColture(Boolean controlloColture) {
		this.controlloColture = controlloColture;
	}

	public Boolean getControlloAnomCoord() {
		return controlloAnomCoord;
	}

	public void setControlloAnomCoord(Boolean controlloAnomCoord) {
		this.controlloAnomCoord = controlloAnomCoord;
	}

	public Float getSupImpegnata() {
		return supImpegnata;
	}

	public void setSupImpegnata(Float supImpegnata) {
		this.supImpegnata = supImpegnata;
	}

	public Float getSupRichiesta() {
		return supRichiesta;
	}

	public void setSupRichiesta(Float supRichiesta) {
		this.supRichiesta = supRichiesta;
	}

	public Float getSupControlliLoco() {
		return supControlliLoco;
	}

	public void setSupControlliLoco(Float supControlliLoco) {
		this.supControlliLoco = supControlliLoco;
	}

	public Float getSupEleggibileGis() {
		return supEleggibileGis;
	}

	public void setSupEleggibileGis(Float supEleggibileGis) {
		this.supEleggibileGis = supEleggibileGis;
	}

	public Float getSupDeterminata() {
		return supDeterminata;
	}

	public void setSupDeterminata(Float supDeterminata) {
		this.supDeterminata = supDeterminata;
	}
	
	public Float getSuperficieAnomaliaCoor() {
		return superficieAnomaliaCoor;
	}

	public void setSuperficieAnomaliaCoor(Float superficieAnomaliaCoor) {
		this.superficieAnomaliaCoor = superficieAnomaliaCoor;
	}

}