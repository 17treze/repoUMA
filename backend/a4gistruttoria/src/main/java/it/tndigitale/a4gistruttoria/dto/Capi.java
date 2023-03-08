package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;
import java.util.List;

import it.tndigitale.a4gistruttoria.util.StatoDomandaAllevamentoEsito;

public class Capi implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String codiceSpecie;
	private String datiAllevamento;
	private String datiDetentore;
	private String datiProprietario;
	private String codiceIntervento;
	private String cuaaIntestatario;
	private StatoDomandaAllevamentoEsito stato;
	private List<Capo> richiesteAllevamentoDuEsito;
	private Long count;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodiceSpecie() {
		return codiceSpecie;
	}

	public void setCodiceSpecie(String codiceSpecie) {
		this.codiceSpecie = codiceSpecie;
	}

	public String getDatiAllevamento() {
		return datiAllevamento;
	}

	public void setDatiAllevamento(String datiAllevamento) {
		this.datiAllevamento = datiAllevamento;
	}

	public String getDatiDetentore() {
		return datiDetentore;
	}

	public void setDatiDetentore(String datiDetentore) {
		this.datiDetentore = datiDetentore;
	}

	public String getDatiProprietario() {
		return datiProprietario;
	}

	public void setDatiProprietario(String datiProprietario) {
		this.datiProprietario = datiProprietario;
	}

	public String getCodiceIntervento() {
		return codiceIntervento;
	}

	public void setCodiceIntervento(String codiceIntervento) {
		this.codiceIntervento = codiceIntervento;
	}

	public String getCuaaIntestatario() {
		return cuaaIntestatario;
	}

	public void setCuaaIntestatario(String cuaaIntestatario) {
		this.cuaaIntestatario = cuaaIntestatario;
	}

	public StatoDomandaAllevamentoEsito getStato() {
		return stato;
	}

	public void setStato(StatoDomandaAllevamentoEsito stato) {
		this.stato = stato;
	}

	public void setStato(String stato) {
		this.stato = StatoDomandaAllevamentoEsito.valueOf(stato);
	}

	public List<Capo> getRichiesteAllevamentoDuEsito() {
		return richiesteAllevamentoDuEsito;
	}

	public void setRichiesteAllevamentoDuEsito(List<Capo> richiesteAllevamentoDuEsito) {
		this.richiesteAllevamentoDuEsito = richiesteAllevamentoDuEsito;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
