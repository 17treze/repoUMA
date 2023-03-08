package it.tndigitale.a4g.fascicolo.anagrafica.dto;

public class IndirizzoSedeAmministrativaDto extends IndirizzoDto {
	private static final long serialVersionUID = 6938128192663682709L;
	
	private String toponimo;
	
	public String getToponimo() {
		return toponimo;
	}
	public void setToponimo(String toponimo) {
		this.toponimo = toponimo;
	}
}
