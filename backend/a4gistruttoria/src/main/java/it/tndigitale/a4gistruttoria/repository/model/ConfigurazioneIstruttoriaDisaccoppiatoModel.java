package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "A4GT_CONF_ISTR_DISACCOPPIATO")
public class ConfigurazioneIstruttoriaDisaccoppiatoModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = 5462393352537996656L;

	@Column(name = "ANNO_CAMPAGNA", nullable = false)
	private Integer campagna;

	@Column(name = "PERC_INCREMENTO_GIOVANE")
	private Double percentualeIncrementoGiovane;

	@Column(name = "PERC_INCREMENTO_GREENING")
	private Double percentualeIncrementoGreening;

	@Column(name = "PERC_RIDUZIONE_LIN_ART51_PAR2")
	private Double percentualeRiduzioneLineareArt51Par2;

	@Column(name = "PERC_RIDUZIONE_LIN_ART51_PAR3")
	private Double percentualeRiduzioneLineareArt51Par3;

	@Column(name = "PERC_RIDUZIONE_LIN_MASS_NETTO")
	private Double percentualeRiduzioneLineareMassimaleNetto;

	@Column(name = "PERC_RIDUZIONE_TITOLI")
	private Double percentualeRiduzioneTitoli;

	@Column(name = "LIMITE_GIOVANE")
	private Double limiteGiovane;

	public Integer getCampagna() {
		return campagna;
	}

	public Double getPercentualeIncrementoGiovane() {
		return percentualeIncrementoGiovane;
	}

	public Double getPercentualeIncrementoGreening() {
		return percentualeIncrementoGreening;
	}

	public Double getPercentualeRiduzioneLineareArt51Par2() {
		return percentualeRiduzioneLineareArt51Par2;
	}

	public Double getPercentualeRiduzioneLineareArt51Par3() {
		return percentualeRiduzioneLineareArt51Par3;
	}

	public Double getPercentualeRiduzioneLineareMassimaleNetto() {
		return percentualeRiduzioneLineareMassimaleNetto;
	}

	public Double getPercentualeRiduzioneTitoli() {
		return percentualeRiduzioneTitoli;
	}

	public Double getLimiteGiovane() {
		return limiteGiovane;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public void setPercentualeIncrementoGiovane(Double percentualeIncrementoGiovane) {
		this.percentualeIncrementoGiovane = percentualeIncrementoGiovane;
	}

	public void setPercentualeIncrementoGreening(Double percentualeIncrementoGreening) {
		this.percentualeIncrementoGreening = percentualeIncrementoGreening;
	}

	public void setPercentualeRiduzioneLineareArt51Par2(Double percentualeRiduzioneLineareArt51Par2) {
		this.percentualeRiduzioneLineareArt51Par2 = percentualeRiduzioneLineareArt51Par2;
	}

	public void setPercentualeRiduzioneLineareArt51Par3(Double percentualeRiduzioneLineareArt51Par3) {
		this.percentualeRiduzioneLineareArt51Par3 = percentualeRiduzioneLineareArt51Par3;
	}

	public void setPercentualeRiduzioneLineareMassimaleNetto(Double percentualeRiduzioneLineareMassimaleNetto) {
		this.percentualeRiduzioneLineareMassimaleNetto = percentualeRiduzioneLineareMassimaleNetto;
	}

	public void setPercentualeRiduzioneTitoli(Double percentualeRiduzioneTitoli) {
		this.percentualeRiduzioneTitoli = percentualeRiduzioneTitoli;
	}

	public void setLimiteGiovane(Double limiteGiovane) {
		this.limiteGiovane = limiteGiovane;
	}

}
