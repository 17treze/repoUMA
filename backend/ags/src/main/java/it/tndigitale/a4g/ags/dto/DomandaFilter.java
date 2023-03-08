package it.tndigitale.a4g.ags.dto;

import java.util.List;

public class DomandaFilter {

	private List<String> stati;
	private Integer campagna;
	private String cuaa;

	public List<String> getStati() {
		return stati;
	}

	public void setStati(List<String> stati) {
		this.stati = stati;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DomandaFilter [stati=" + stati + ", campagna=" + campagna + ", cuaa=" + cuaa + "]";
	}

}