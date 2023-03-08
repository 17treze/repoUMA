package it.tndigitale.a4g.ags.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DomandeCollegatePsrFilter {

	private List<String> cuaa;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date dataPresentazione;
	private BigDecimal importo;
	private List<Integer> anniCampagna;

	public List<String> getCuaa() {
		return cuaa;
	}

	public Date getDataPresentazione() {
		return dataPresentazione;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setCuaa(List<String> cuaa) {
		this.cuaa = cuaa;
	}

	public void setDataPresentazione(Date dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	
	//To Check
	@Override
	public String toString() {
		return "DomandeCollegatePsrFilter [cuaa=" + cuaa + ", dataPresentazione=" + dataPresentazione + ", importo=" + importo + ", campagne=" + anniCampagna.toString() + "]";
	}

	public List<Integer> getAnniCampagna() {
		return anniCampagna;
	}

	public void setAnniCampagna(List<Integer> anniCampagna) {
		this.anniCampagna = anniCampagna;
	}

}
