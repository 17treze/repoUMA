package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "m8", "m9", "m10", "m11", "m14", "m15", "m16", "m17" })
public class DatiParticellaACS {

	private List<DettaglioDatiParticellaACS> m8;
	private List<DettaglioDatiParticellaACS> m9;
	private List<DettaglioDatiParticellaACS> m10;
	private List<DettaglioDatiParticellaACS> m11;
	private List<DettaglioDatiParticellaACS> m14;
	private List<DettaglioDatiParticellaACS> m15;
	private List<DettaglioDatiParticellaACS> m16;
	private List<DettaglioDatiParticellaACS> m17;

	public List<DettaglioDatiParticellaACS> getM8() {
		return m8;
	}

	public void setM8(List<DettaglioDatiParticellaACS> m8) {
		this.m8 = m8;
	}

	public List<DettaglioDatiParticellaACS> getM9() {
		return m9;
	}

	public void setM9(List<DettaglioDatiParticellaACS> m9) {
		this.m9 = m9;
	}

	public List<DettaglioDatiParticellaACS> getM10() {
		return m10;
	}

	public void setM10(List<DettaglioDatiParticellaACS> m10) {
		this.m10 = m10;
	}

	public List<DettaglioDatiParticellaACS> getM11() {
		return m11;
	}

	public void setM11(List<DettaglioDatiParticellaACS> m11) {
		this.m11 = m11;
	}

	public List<DettaglioDatiParticellaACS> getM14() {
		return m14;
	}

	public void setM14(List<DettaglioDatiParticellaACS> m14) {
		this.m14 = m14;
	}

	public List<DettaglioDatiParticellaACS> getM15() {
		return m15;
	}

	public void setM15(List<DettaglioDatiParticellaACS> m15) {
		this.m15 = m15;
	}

	public List<DettaglioDatiParticellaACS> getM16() {
		return m16;
	}

	public void setM16(List<DettaglioDatiParticellaACS> m16) {
		this.m16 = m16;
	}

	public List<DettaglioDatiParticellaACS> getM17() {
		return m17;
	}

	public void setM17(List<DettaglioDatiParticellaACS> m17) {
		this.m17 = m17;
	}
}