package it.tndigitale.a4g.zootecnia.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneAllevamentoDto {

	private String specie;
	private String codiceFiscaleDetentore;
	private String identificativoStruttura;
	private String indirizzoStruttura;
	private String comuneStruttura;
	private String tipologiaAllevamento;
	private String dataInizioDetenzione;
	private String codiceFiscaleProprietario;

	
	public ReportValidazioneAllevamentoDto(String specie, String codiceFiscaleDetentore, String identificativoStruttura,
			String indirizzoStruttura, String comuneStruttura, String tipologiaAllevamento, String dataInizioDetenzione,
			String codiceFiscaleProprietario) {
		super();
		this.specie = specie;
		this.codiceFiscaleDetentore = codiceFiscaleDetentore;
		this.identificativoStruttura = identificativoStruttura;
		this.indirizzoStruttura = indirizzoStruttura;
		this.comuneStruttura = comuneStruttura;
		this.tipologiaAllevamento = tipologiaAllevamento;
		this.dataInizioDetenzione = dataInizioDetenzione;
		this.codiceFiscaleProprietario = codiceFiscaleProprietario;
	}
	public String getSpecie() {
		return specie;
	}
	public ReportValidazioneAllevamentoDto setSpecie(String specie) {
		this.specie = specie;
		return this;
	}
	public String getCodiceFiscaleDetentore() {
		return codiceFiscaleDetentore;
	}
	public ReportValidazioneAllevamentoDto setCodiceFiscaleDetentore(String codiceFiscaleDetentore) {
		this.codiceFiscaleDetentore = codiceFiscaleDetentore;
		return this;
	}
	public String getIdentificativoStruttura() {
		return identificativoStruttura;
	}
	public ReportValidazioneAllevamentoDto setIdentificativoStruttura(String identificativoStruttura) {
		this.identificativoStruttura = identificativoStruttura;
		return this;
	}
	public String getIndirizzoStruttura() {
		return indirizzoStruttura;
	}
	public ReportValidazioneAllevamentoDto setIndirizzoStruttura(String indirizzoStruttura) {
		this.indirizzoStruttura = indirizzoStruttura;
		return this;
	}
	public String getComuneStruttura() {
		return comuneStruttura;
	}
	public ReportValidazioneAllevamentoDto setComuneStruttura(String comuneStruttura) {
		this.comuneStruttura = comuneStruttura;
		return this;
	}
	public String getTipologiaAllevamento() {
		return tipologiaAllevamento;
	}
	public void setTipologiaAllevamento(String tipologiaAllevamento) {
		this.tipologiaAllevamento = tipologiaAllevamento;
	}
	public String getDataInizioDetenzione() {
		return dataInizioDetenzione;
	}
	public void setDataInizioDetenzione(String dataInizioDetenzione) {
		this.dataInizioDetenzione = dataInizioDetenzione;
	}
	public String getCodiceFiscaleProprietario() {
		return codiceFiscaleProprietario;
	}
	public void setCodiceFiscaleProprietario(String codiceFiscaleProprietario) {
		this.codiceFiscaleProprietario = codiceFiscaleProprietario;
	}

}
