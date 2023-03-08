package it.tndigitale.a4gistruttoria.dto;

public class InfoLiquidabilita {

	private Boolean ibanValido;
	private Boolean titolareDeceduto;
	private Boolean domandaSospesaAgea;
	private String iban;
	private String ibanFascicolo;

	public InfoLiquidabilita() {

	}

	public InfoLiquidabilita(Boolean ibanValido, Boolean titolareDeceduto, Boolean domandaSospesaAgea) {
		super();
		this.ibanValido = ibanValido;
		this.titolareDeceduto = titolareDeceduto;
		this.domandaSospesaAgea = domandaSospesaAgea;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public Boolean getIbanValido() {
		return ibanValido;
	}

	public void setIbanValido(Boolean ibanValido) {
		this.ibanValido = ibanValido;
	}

	public Boolean getTitolareDeceduto() {
		return titolareDeceduto;
	}

	public void setTitolareDeceduto(Boolean titolareDeceduto) {
		this.titolareDeceduto = titolareDeceduto;
	}

	public Boolean getDomandaSospesaAgea() {
		return domandaSospesaAgea;
	}

	public void setDomandaSospesaAgea(Boolean domandaSospesaAgea) {
		this.domandaSospesaAgea = domandaSospesaAgea;
	}

	public String getIbanFascicolo() {
		return ibanFascicolo;
	}

	public void setIbanFascicolo(String ibanFascicolo) {
		this.ibanFascicolo = ibanFascicolo;
	}

}
