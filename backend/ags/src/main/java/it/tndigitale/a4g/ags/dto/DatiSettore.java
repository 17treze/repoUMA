package it.tndigitale.a4g.ags.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;

@ApiModel(description = "Rappresenta il modello di dati per la ricerca dei dati di settore")
public class DatiSettore {
	
	@ApiParam(value="Anno di riferimento", required = true)
	private BigDecimal annoRiferimento;
	
	@ApiParam(value="Codice PAC", required = false)
	private String codicePac;
	
	@ApiParam(value="Tipo di domanda", required = false)
	private String tipoDomanda;

	public DatiSettore() {
	}

	public BigDecimal getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(BigDecimal annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public String getCodicePac() {
		return codicePac;
	}

	public void setCodicePac(String codicePac) {
		this.codicePac = codicePac;
	}

	public String getTipoDomanda() {
		return tipoDomanda;
	}

	public void setTipoDomanda(String tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
	}

}
