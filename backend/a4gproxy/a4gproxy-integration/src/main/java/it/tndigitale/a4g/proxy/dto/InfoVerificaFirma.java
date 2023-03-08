/**
 * 
 */
package it.tndigitale.a4g.proxy.dto;

import java.time.LocalDate;

/**
 * @author B.Conetta
 *
 */
public class InfoVerificaFirma {

	private LocalDate dataFirma;
	private String cfFirmatario;
	
	public LocalDate getDataFirma() {
		return dataFirma;
	}
	public void setDataFirma(LocalDate dataFirma) {
		this.dataFirma = dataFirma;
	}
	public String getCfFirmatario() {
		return cfFirmatario;
	}
	public void setCfFirmatario(String cfFirmatario) {
		this.cfFirmatario = cfFirmatario;
	}
}
