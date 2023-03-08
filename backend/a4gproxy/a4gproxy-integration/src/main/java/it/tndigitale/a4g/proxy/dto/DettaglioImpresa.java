package it.tndigitale.a4g.proxy.dto;

public class DettaglioImpresa {
	
	private String provinciaSede;
	private String numeroREASede;

	public DettaglioImpresa() {
	}

	public String getProvinciaSede() {
		return provinciaSede;
	}

	public void setProvinciaSede(String provinciaSede) {
		this.provinciaSede = provinciaSede;
	}

	public String getNumeroREASede() {
		return numeroREASede;
	}

	public void setNumeroREASede(String numeroREASede) {
		this.numeroREASede = numeroREASede;
	}

	@Override
	public String toString() {
		return "DettaglioImpresa [provinciaSede=" + provinciaSede + ", numeroREASede=" + numeroREASede + "]";
	}

}
