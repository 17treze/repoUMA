package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class InfoIstruttoriaDomanda {

	private List<InfoEleggibilitaParticella> datiEleggibilita;
	private Boolean controlloSigecoChiuso;

	public List<InfoEleggibilitaParticella> getDatiEleggibilita() {
		return datiEleggibilita;
	}

	public void setDatiEleggibilita(List<InfoEleggibilitaParticella> datiEleggibilita) {
		this.datiEleggibilita = datiEleggibilita;
	}

	public Boolean getControlloSigecoChiuso() {
		return controlloSigecoChiuso;
	}

	public void setControlloSigecoChiuso(Boolean controlloSigecoChiuso) {
		this.controlloSigecoChiuso = controlloSigecoChiuso;
	}

}