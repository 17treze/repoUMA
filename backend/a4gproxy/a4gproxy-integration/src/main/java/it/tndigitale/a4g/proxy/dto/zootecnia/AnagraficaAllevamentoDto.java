package it.tndigitale.a4g.proxy.dto.zootecnia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AnagraficaAllevamentoDto implements Serializable {

	protected BigDecimal identificativo;

	protected String specie;

	protected String denominazione;

	protected String tipoProduzione;

	protected String orientamentoProduttivo;

	protected String autorizzazioneLatte;

	protected LocalDate dtInizioAttivita;

	protected LocalDate dtFineAttivita;

	protected String cfProprietario;

	protected String denominazioneProprietario;

	protected String cfDetentore;

	protected String denominazioneDetentore;

	protected LocalDate dtInizioDetentore;

	protected LocalDate dtFineDetentore;

	protected String soccida;

	protected BigDecimal capiTotali;

	protected LocalDate dataCalcoloCapi;

	protected String tipologiaAllevamentoCodice;

	protected String tipoAllevamentoDescrizione;

	protected StrutturaAllevamentoDto struttura;

	public BigDecimal getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(BigDecimal identificativo) {
		this.identificativo = identificativo;
	}

	public String getSpecie() {
		return specie;
	}

	public void setSpecie(String specie) {
		this.specie = specie;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
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

	public String getAutorizzazioneLatte() {
		return autorizzazioneLatte;
	}

	public void setAutorizzazioneLatte(String autorizzazioneLatte) {
		this.autorizzazioneLatte = autorizzazioneLatte;
	}

	public LocalDate getDtInizioAttivita() {
		return dtInizioAttivita;
	}

	public void setDtInizioAttivita(LocalDate dtInizioAttivita) {
		this.dtInizioAttivita = dtInizioAttivita;
	}

	public LocalDate getDtFineAttivita() {
		return dtFineAttivita;
	}

	public void setDtFineAttivita(LocalDate dtFineAttivita) {
		this.dtFineAttivita = dtFineAttivita;
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

	public LocalDate getDtInizioDetentore() {
		return dtInizioDetentore;
	}

	public void setDtInizioDetentore(LocalDate dtInizioDetentore) {
		this.dtInizioDetentore = dtInizioDetentore;
	}

	public LocalDate getDtFineDetentore() {
		return dtFineDetentore;
	}

	public void setDtFineDetentore(LocalDate dtFineDetentore) {
		this.dtFineDetentore = dtFineDetentore;
	}

	public String getSoccida() {
		return soccida;
	}

	public void setSoccida(String soccida) {
		this.soccida = soccida;
	}

	public BigDecimal getCapiTotali() {
		return capiTotali;
	}

	public void setCapiTotali(BigDecimal capiTotali) {
		this.capiTotali = capiTotali;
	}

	public LocalDate getDataCalcoloCapi() {
		return dataCalcoloCapi;
	}

	public void setDataCalcoloCapi(LocalDate dataCalcoloCapi) {
		this.dataCalcoloCapi = dataCalcoloCapi;
	}

	public String getTipologiaAllevamentoCodice() {
		return tipologiaAllevamentoCodice;
	}

	public void setTipologiaAllevamentoCodice(String tipologiaAllevamentoCodice) {
		this.tipologiaAllevamentoCodice = tipologiaAllevamentoCodice;
	}

	public String getTipoAllevamentoDescrizione() {
		return tipoAllevamentoDescrizione;
	}

	public void setTipoAllevamentoDescrizione(String tipoAllevamentoDescrizione) {
		this.tipoAllevamentoDescrizione = tipoAllevamentoDescrizione;
	}

	public StrutturaAllevamentoDto getStruttura() {
		return struttura;
	}

	public void setStruttura(StrutturaAllevamentoDto struttura) {
		this.struttura = struttura;
	}

}
