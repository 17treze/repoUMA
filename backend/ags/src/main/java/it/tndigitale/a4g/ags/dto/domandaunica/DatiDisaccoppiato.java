package it.tndigitale.a4g.ags.dto.domandaunica;

import java.util.List;

public class DatiDisaccoppiato {

	private boolean bps = true; //superficiImpegnate.size > 0
	private boolean green = true; 
	private boolean giovane = false; // check su dichiarazione
	
	private List<SuperficieImpegnata> superficiImpegnate; // nel nostro caso sono solo BPS
	private List<DatiPascolo> pascoli;
	public boolean isBps() {
		return bps;
	}
	public boolean isGreen() {
		return green;
	}
	public boolean isGiovane() {
		return giovane;
	}
	public List<SuperficieImpegnata> getSuperficiImpegnate() {
		return superficiImpegnate;
	}
	public List<DatiPascolo> getPascoli() {
		return pascoli;
	}
	public void setBps(boolean bps) {
		this.bps = bps;
	}
	public void setGreen(boolean green) {
		this.green = green;
	}
	public void setGiovane(boolean giovane) {
		this.giovane = giovane;
	}
	public void setSuperficiImpegnate(List<SuperficieImpegnata> superficiImpegnate) {
		this.superficiImpegnate = superficiImpegnate;
	}
	public void setPascoli(List<DatiPascolo> pascoli) {
		this.pascoli = pascoli;
	}
	
}
