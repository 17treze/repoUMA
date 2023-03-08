/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto.antimafia;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author a.pasca
 *
 */
public class SogliaAcquisizione {
	private String settore;
	private Date dataPresentazione;
	private Date dataInizioApplicazione;
	private Date dataFineApplicazione;
	private BigDecimal soglia;
	
	public String getSettore() {
		return settore;
	}
	public void setSettore(String settore) {
		this.settore = settore;
	}
	public Date getDataPresentazione() {
		return dataPresentazione;
	}
	public void setDataPresentazione(Date dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}
	public Date getDataInizioApplicazione() {
		return dataInizioApplicazione;
	}
	public void setDataInizioApplicazione(Date dataInizioApplicazione) {
		this.dataInizioApplicazione = dataInizioApplicazione;
	}
	public Date getDataFineApplicazione() {
		return dataFineApplicazione;
	}
	public void setDataFineApplicazione(Date dataFineApplicazione) {
		this.dataFineApplicazione = dataFineApplicazione;
	}
	public BigDecimal getSoglia() {
		return soglia;
	}
	public void setSoglia(BigDecimal soglia) {
		this.soglia = soglia;
	}
}
