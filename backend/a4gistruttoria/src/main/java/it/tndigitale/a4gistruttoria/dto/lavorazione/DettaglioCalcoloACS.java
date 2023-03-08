package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "m8", "m9", "m10", "m11", "m14", "m15", "m16", "m17" })
public class DettaglioCalcoloACS implements IDettaglioCalcolo {

	private Map<String, String> m8;
	private Map<String, String> m9;
	private Map<String, String> m10;
	private Map<String, String> m11;
	private Map<String, String> m14;
	private Map<String, String> m15;
	private Map<String, String> m16;
	private Map<String, String> m17;

	public Map<String, String> getM8() {
		return m8;
	}

	public void setM8(Map<String, String> m8) {
		this.m8 = m8;
	}

	public Map<String, String> getM9() {
		return m9;
	}

	public void setM9(Map<String, String> m9) {
		this.m9 = m9;
	}

	public Map<String, String> getM10() {
		return m10;
	}

	public void setM10(Map<String, String> m10) {
		this.m10 = m10;
	}

	public Map<String, String> getM11() {
		return m11;
	}

	public void setM11(Map<String, String> m11) {
		this.m11 = m11;
	}

	public Map<String, String> getM14() {
		return m14;
	}

	public void setM14(Map<String, String> m14) {
		this.m14 = m14;
	}

	public Map<String, String> getM15() {
		return m15;
	}

	public void setM15(Map<String, String> m15) {
		this.m15 = m15;
	}

	public Map<String, String> getM16() {
		return m16;
	}

	public void setM16(Map<String, String> m16) {
		this.m16 = m16;
	}

	public Map<String, String> getM17() {
		return m17;
	}

	public void setM17(Map<String, String> m17) {
		this.m17 = m17;
	}
}