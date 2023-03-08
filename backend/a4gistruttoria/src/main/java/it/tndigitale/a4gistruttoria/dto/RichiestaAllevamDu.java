package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;
import java.util.List;

import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.Intervento;
import it.tndigitale.a4gistruttoria.util.StatoDomandaAllevamentoEsito;

public class RichiestaAllevamDu implements Serializable {

	private static final long serialVersionUID = 7894299551066131916L;
	
	private Long id;
	
	private Long idDomanda;
	
	private String codiceSpecie;
	
	private String datiAllevamento;
	
	private String datiDetentore;
	
	private String datiProprietario;
	
	private String codiceIntervento;
	
	private Intervento  intervento;
	
	private String cuaaIntestatario;
	
	private String cuaaSubentrante;
	
	private Integer campagna;

	private StatoDomandaAllevamentoEsito stato;
	
	private List<RichiestaAllevamDuEsito> richiesteAllevamentoDuEsito;

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

	public List<RichiestaAllevamDuEsito> getRichiesteAllevamentoDuEsito() {
		return richiesteAllevamentoDuEsito;
	}

	public void setRichiesteAllevamentoDuEsito(
			List<RichiestaAllevamDuEsito> richiesteAllevamentoDuEsito) {
		this.richiesteAllevamentoDuEsito = richiesteAllevamentoDuEsito;
	}

	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}
	
	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public Intervento getIntervento() {
		return intervento;
	}

	public void setIntervento(Intervento intervento) {
		this.intervento = intervento;
	}

	public String getCuaaSubentrante() {
		return cuaaSubentrante;
	}

	public void setCuaaSubentrante(String cuaaSubentrante) {
		this.cuaaSubentrante = cuaaSubentrante;
	}

}
