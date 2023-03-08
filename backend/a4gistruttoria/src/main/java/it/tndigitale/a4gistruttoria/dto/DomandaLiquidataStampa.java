package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import it.tndigitale.a4gistruttoria.config.ImportoBigDecimalJsonSerializer;
import it.tndigitale.a4gistruttoria.config.LocalDateJsonSerializer;

public class DomandaLiquidataStampa {

	private int counter;
	private String numeroDomanda;
	private String numeroProtocollo;
	private String nome;
	private String cuaa;
	private String domicilio;
	private String quote;
	@JsonSerialize(using = LocalDateJsonSerializer.class)
	private LocalDate dataPresentazione;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal totalePremio;

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public LocalDate getDataPresentazione() {
		return dataPresentazione;
	}

	public void setDataPresentazione(LocalDate dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public BigDecimal getTotalePremio() {
		return totalePremio;
	}

	public void setTotalePremio(BigDecimal totalePremio) {
		this.totalePremio = totalePremio;
	}

}
