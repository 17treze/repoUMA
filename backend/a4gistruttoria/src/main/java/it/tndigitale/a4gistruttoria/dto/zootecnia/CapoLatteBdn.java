package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.util.ArrayList;

import java.util.List;

@Deprecated
public class CapoLatteBdn {
	
	private List<DetenzioneBdn>  detenzioniBdn;
	private List<VitelloBdn> vitelli;
	
	public List<DetenzioneBdn> getDetenzioniBdn() {
		return detenzioniBdn;
	}
	public void setDetenzioniBdn(List<DetenzioneBdn> detenzioniBdn) {
		this.detenzioniBdn = detenzioniBdn;
	}
	public List<VitelloBdn> getVitelli() {
		return vitelli;
	}
	public void setVitelli(List<VitelloBdn> vitelli) {
		this.vitelli = vitelli;
	}
	
	public void addVitello(VitelloBdn vitello) {
		if (getVitelli() == null) {
			setVitelli(new ArrayList<VitelloBdn>());
		}
		getVitelli().add(vitello);
	}

}
