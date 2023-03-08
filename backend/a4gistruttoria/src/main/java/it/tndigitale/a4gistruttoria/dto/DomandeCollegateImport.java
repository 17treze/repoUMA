package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DomandeCollegateImport implements Serializable {

	private static final long serialVersionUID = 1L;

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

	public String validate() {
		StringBuilder sb = new StringBuilder();
		if (this.cuaa == null || this.cuaa.isEmpty()) {
			sb.append("Il cuaa non può essere vuoto. ");
		}
		if (this.dataPresentazione == null) {
			sb.append("La data prensentazione non può essere vuota. ");
		}
		if (this.importo == null) {
			sb.append("L'importo richiesto non può essere vuoto. ");
		}
		return sb.toString();
	}

	public List<Integer> getAnniCampagna() {
		return anniCampagna;
	}

	public void setAnniCampagna(List<Integer> anniCampagna) {
		this.anniCampagna = anniCampagna;
	}

}
