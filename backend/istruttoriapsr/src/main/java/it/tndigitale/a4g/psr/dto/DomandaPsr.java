package it.tndigitale.a4g.psr.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Dati anagrafici generali della Domanda PSR ")
public class DomandaPsr implements Serializable{
	
	private static final long serialVersionUID = 5216138017432933576L;
	
	@ApiModelProperty(value = "Anno Campagna")
	private Integer campagna;
	@ApiModelProperty(value = "Numero domanda PSR")
	private Long numeroDomanda;
	@ApiModelProperty(value = "Data Domanda PSR")
	private LocalDate dataPresentazione;
	@ApiModelProperty(value = "Stato Domanda PSR")
	private StatoDomanda stato;
	@ApiModelProperty(value = "Codice fiscale azienda agricola")
	private String cuaa;
	@ApiModelProperty(value = "Modulo richiesto dalla domanda")
	private String modulo;
	@ApiModelProperty(value = "Misure Intervento richieste")
	private List<Operazione> operazioni;
	@ApiModelProperty(value = "Sotto Stato Domanda PSR")
	private StatoOperazione sottoStato;
	
	public Integer getCampagna() {
		return campagna;
	}
	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}
	public Long getNumeroDomanda() {
		return numeroDomanda;
	}
	public void setNumeroDomanda(Long numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}
	public LocalDate getDataPresentazione() {
		return dataPresentazione;
	}
	public void setDataPresentazione(LocalDate dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}
	public StatoDomanda getStato() {
		return stato;
	}
	public void setStato(StatoDomanda stato) {
		this.stato = stato;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public String getModulo() {
		return modulo;
	}
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	public List<Operazione> getOperazioni() {
		return operazioni;
	}
	public void setOperazioni(List<Operazione> operazioni) {
		this.operazioni = operazioni;
	}
	public StatoOperazione getSottoStato() {
		return sottoStato;
	}
	public void setSottoStato(StatoOperazione sottoStato) {
		this.sottoStato = sottoStato;
	}

}
