package it.tndigitale.a4gistruttoria.dto.domandaunica;

import java.math.BigDecimal;

public class Debito {

	private BigDecimal importo;
	private String descrizione;
	
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
