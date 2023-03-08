package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("SERRA")
public class DettaglioSerreDto extends DettaglioFabbricatoAbstract {
	
	private boolean impiantoRiscaldamento;
	private Long annoCostruzione;
	private String tipologiaMateriale;
	private Long annoAcquisto;
	private String titoloConformitaUrbanistica;
	
	public boolean isImpiantoRiscaldamento() {
		return impiantoRiscaldamento;
	}
	public DettaglioSerreDto setImpiantoRiscaldamento(boolean impiantoRiscaldamento) {
		this.impiantoRiscaldamento = impiantoRiscaldamento;
		return this;
	}
	public Long getAnnoCostruzione() {
		return annoCostruzione;
	}
	public DettaglioSerreDto setAnnoCostruzione(Long annoCostruzione) {
		this.annoCostruzione = annoCostruzione;
		return this;
	}
	public String getTipologiaMateriale() {
		return tipologiaMateriale;
	}
	public DettaglioSerreDto setTipologiaMateriale(String tipologiaMateriale) {
		this.tipologiaMateriale = tipologiaMateriale;
		return this;
	}
	public Long getAnnoAcquisto() {
		return annoAcquisto;
	}
	public DettaglioSerreDto setAnnoAcquisto(Long annoAcquisto) {
		this.annoAcquisto = annoAcquisto;
		return this;
	}
	public String getTitoloConformitaUrbanistica() {
		return titoloConformitaUrbanistica;
	}
	public DettaglioSerreDto setTitoloConformitaUrbanistica(String titoloConformitaUrbanistica) {
		this.titoloConformitaUrbanistica = titoloConformitaUrbanistica;
		return this;
	}
}
