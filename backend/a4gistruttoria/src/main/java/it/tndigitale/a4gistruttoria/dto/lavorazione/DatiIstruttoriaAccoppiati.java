package it.tndigitale.a4gistruttoria.dto.lavorazione;

public class DatiIstruttoriaAccoppiati {
	private Long id;
	private Boolean controlloSigecoLoco;
	private Boolean controlloAntimafia;
	private String cuaaSubentrante;
	private Boolean annulloRiduzione;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getControlloSigecoLoco() {
		return controlloSigecoLoco;
	}
	public void setControlloSigecoLoco(Boolean controlloSigecoLoco) {
		this.controlloSigecoLoco = controlloSigecoLoco;
	}
	public Boolean getControlloAntimafia() {
		return controlloAntimafia;
	}
	public void setControlloAntimafia(Boolean controlloAntimafia) {
		this.controlloAntimafia = controlloAntimafia;
	}
	public String getCuaaSubentrante() {
		return cuaaSubentrante;
	}
	public void setCuaaSubentrante(String cuaaSubentrante) {
		this.cuaaSubentrante = cuaaSubentrante;
	}
	public Boolean getAnnulloRiduzione() {
		return annulloRiduzione;
	}
	public void setAnnulloRiduzione(Boolean annulloRiduzione) {
		this.annulloRiduzione = annulloRiduzione;
	}
	
}