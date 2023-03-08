package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "int310", "int311", "int313", "int315", "int320", "int316", "int318", "int322", "int321" })
public class DettaglioCalcoloACZ implements IDettaglioCalcolo {

	private Map<String, String> int310;
	private Map<String, String> int311;
	private Map<String, String> int313;
	private Map<String, String> int315;
	private Map<String, String> int320;
	private Map<String, String> int316;
	private Map<String, String> int318;
	private Map<String, String> int322;
	private Map<String, String> int321;
	
	public Map<String, String> getInt310() {
		return int310;
	}
	
	public void setInt310(Map<String, String> int310) {
		this.int310 = int310;
	}
	
	public Map<String, String> getInt311() {
		return int311;
	}
	
	public void setInt311(Map<String, String> int311) {
		this.int311 = int311;
	}
	
	public Map<String, String> getInt313() {
		return int313;
	}
	
	public void setInt313(Map<String, String> int313) {
		this.int313 = int313;
	}
	
	public Map<String, String> getInt315() {
		return int315;
	}
	
	public void setInt315(Map<String, String> int315) {
		this.int315 = int315;
	}
	
	public Map<String, String> getInt320() {
		return int320;
	}
	
	public void setInt320(Map<String, String> int320) {
		this.int320 = int320;
	}
	
	public Map<String, String> getInt316() {
		return int316;
	}
	
	public void setInt316(Map<String, String> int316) {
		this.int316 = int316;
	}
	
	public Map<String, String> getInt318() {
		return int318;
	}
	
	public void setInt318(Map<String, String> int318) {
		this.int318 = int318;
	}
	
	public Map<String, String> getInt322() {
		return int322;
	}
	
	public void setInt322(Map<String, String> int322) {
		this.int322 = int322;
	}
	
	public Map<String, String> getInt321() {
		return int321;
	}
	
	public void setInt321(Map<String, String> int321) {
		this.int321 = int321;
	}
}