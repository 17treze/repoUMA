package it.tndigitale.a4g.ags.dto;

public class DatiAggiuntiviParticella {
	private long orientamentoMedio;
	private long pendenzaMedia;
	private long altitudineMedia;
	private boolean irrigabilita;

	public DatiAggiuntiviParticella() {
		// Default empty constructor
	}
	
	public long getOrientamentoMedio() {
		return orientamentoMedio;
	}

	public void setOrientamentoMedio(long orientamentoMedio) {
		this.orientamentoMedio = orientamentoMedio;
	}

	public long getPendenzaMedia() {
		return pendenzaMedia;
	}

	public void setPendenzaMedia(long pendenzaMedia) {
		this.pendenzaMedia = pendenzaMedia;
	}

	public long getAltitudineMedia() {
		return altitudineMedia;
	}

	public void setAltitudineMedia(long altitudineMedia) {
		this.altitudineMedia = altitudineMedia;
	}

	public boolean isIrrigabilita() {
		return irrigabilita;
	}

	public void setIrrigabilita(boolean irrigabilita) {
		this.irrigabilita = irrigabilita;
	}

}
