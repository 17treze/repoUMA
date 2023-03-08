package it.tndigitale.a4g.zootecnia.dto;

import java.time.LocalDate;

import it.tndigitale.a4g.framework.repository.dto.EntitaDominioDto;

public class AnagraficaAllevamentoDto extends EntitaDominioDto {
    
	protected String identificativo;
	
	protected String identificativoFiscale;
    
    protected String specie;
    
    protected String tipoProduzione;
    
    protected String orientamentoProduttivo;
    
    protected String autorizzazioneSanitariaLatte;
    
    protected LocalDate dtAperturaAllevamento;
    
    protected LocalDate dtChiusuraAllevamento;
    
    protected String cfProprietario;
    
    protected String denominazioneProprietario;
    
    protected String cfDetentore;
    
    protected String denominazioneDetentore;
    
    protected LocalDate dtInizioDetenzione;
    
    protected LocalDate dtFineDetenzione;
    
    protected String soccida;
    
    protected FascicoloDto fascicolo;
    
//    protected BigDecimal capitotali;
    
//    protected LocalDate datacalcolocapi;
    
    protected String tipologiaAllevamento;
    
//    protected String tipoallevdescr;
    
    protected StrutturaAllevamentoDto strutturaAllevamentoDto;

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public String getIdentificativoFiscale() {
		return identificativoFiscale;
	}

	public void setIdentificativoFiscale(String identificativoFiscale) {
		this.identificativoFiscale = identificativoFiscale;
	}

	public String getSpecie() {
		return specie;
	}

	public void setSpecie(String specie) {
		this.specie = specie;
	}

	public String getTipoProduzione() {
		return tipoProduzione;
	}

	public void setTipoProduzione(String tipoProduzione) {
		this.tipoProduzione = tipoProduzione;
	}

	public String getOrientamentoProduttivo() {
		return orientamentoProduttivo;
	}

	public void setOrientamentoProduttivo(String orientamentoProduttivo) {
		this.orientamentoProduttivo = orientamentoProduttivo;
	}

	public String getAutorizzazioneSanitariaLatte() {
		return autorizzazioneSanitariaLatte;
	}

	public void setAutorizzazioneSanitariaLatte(String autorizzazioneSanitariaLatte) {
		this.autorizzazioneSanitariaLatte = autorizzazioneSanitariaLatte;
	}

	public LocalDate getDtAperturaAllevamento() {
		return dtAperturaAllevamento;
	}

	public void setDtAperturaAllevamento(LocalDate dtAperturaAllevamento) {
		this.dtAperturaAllevamento = dtAperturaAllevamento;
	}

	public LocalDate getDtChiusuraAllevamento() {
		return dtChiusuraAllevamento;
	}

	public void setDtChiusuraAllevamento(LocalDate dtChiusuraAllevamento) {
		this.dtChiusuraAllevamento = dtChiusuraAllevamento;
	}

	public String getCfProprietario() {
		return cfProprietario;
	}

	public void setCfProprietario(String cfProprietario) {
		this.cfProprietario = cfProprietario;
	}

	public String getDenominazioneProprietario() {
		return denominazioneProprietario;
	}

	public void setDenominazioneProprietario(String denominazioneProprietario) {
		this.denominazioneProprietario = denominazioneProprietario;
	}

	public String getCfDetentore() {
		return cfDetentore;
	}

	public void setCfDetentore(String cfDetentore) {
		this.cfDetentore = cfDetentore;
	}

	public String getDenominazioneDetentore() {
		return denominazioneDetentore;
	}

	public void setDenominazioneDetentore(String denominazioneDetentore) {
		this.denominazioneDetentore = denominazioneDetentore;
	}

	public LocalDate getDtInizioDetenzione() {
		return dtInizioDetenzione;
	}

	public void setDtInizioDetenzione(LocalDate dtInizioDetenzione) {
		this.dtInizioDetenzione = dtInizioDetenzione;
	}

	public LocalDate getDtFineDetenzione() {
		return dtFineDetenzione;
	}

	public void setDtFineDetenzione(LocalDate dtFineDetenzione) {
		this.dtFineDetenzione = dtFineDetenzione;
	}

	public String getSoccida() {
		return soccida;
	}

	public void setSoccida(String soccida) {
		this.soccida = soccida;
	}

	public String getTipologiaAllevamento() {
		return tipologiaAllevamento;
	}

	public void setTipologiaAllevamento(String tipologiaAllevamento) {
		this.tipologiaAllevamento = tipologiaAllevamento;
	}

	public StrutturaAllevamentoDto getStrutturaAllevamentoDto() {
		return strutturaAllevamentoDto;
	}

	public void setStrutturaAllevamentoDto(StrutturaAllevamentoDto strutturaAllevamentoDto) {
		this.strutturaAllevamentoDto = strutturaAllevamentoDto;
	}

	public FascicoloDto getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(FascicoloDto fascicolo) {
		this.fascicolo = fascicolo;
	}

}
