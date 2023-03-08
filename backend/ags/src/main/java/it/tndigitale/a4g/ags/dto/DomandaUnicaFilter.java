package it.tndigitale.a4g.ags.dto;

import java.util.List;

import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.ags.model.StatoDomanda;

public class DomandaUnicaFilter {

	@ApiParam(value = "Filtro Lista di stati delle domande uniche")
	private List<StatoDomanda> stati;
	@ApiParam(value = "Filtro anno di campagna delle domande uniche")
	private Integer campagna;
	@ApiParam(value = "Filtro cuaa delle domande uniche")
	private String cuaa;
	@ApiParam(value = "Filtro expand delle domande uniche (RICHIESTE = visualizza booleani sostegni richiesti)")
	private List<Expand> expand;
	
	public List<StatoDomanda> getStati() {
		return stati;
	}
	public void setStati(List<StatoDomanda> stati) {
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
	public List<Expand> getExpand() {
		return expand;
	}
	public void setExpand(List<Expand> expand) {
		this.expand = expand;
	}
	public enum Expand {
		RICHIESTE
	}
}


