package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;

import java.math.BigDecimal;
import java.util.List;

public class FiltroRicercaDomandeIstruttoria extends FiltroRicercaDomandeIstruttoriaRid {
	private String statoDomanda;
	private BigDecimal numero_domanda;
	private Sostegno sostegno;
	private String statoSostegno;
	private String cuaa;
	private String denominazione;
	private Integer campagna;
	private Integer bloccataBool;
	private String erroreCalcolo;

	public String getErroreCalcolo() {
		return erroreCalcolo;
	}

	public void setErroreCalcolo(String erroreCalcolo) {
		this.erroreCalcolo = erroreCalcolo;
	}

	public Integer getBloccataBool() {
		return bloccataBool;
	}

	public void setBloccataBool(Integer bloccataBool) {
		this.bloccataBool = bloccataBool;
	}

	private List<String> interventi;

	public BigDecimal getNumero_domanda() {
		return numero_domanda;
	}

	public void setNumero_domanda(BigDecimal numero_domanda) {
		this.numero_domanda = numero_domanda;
	}

	@Override
	public void fixDati() {
		super.fixDati();
		cuaa = fixValore(cuaa);
		denominazione = fixValore(denominazione);
		super.setRiserva(fixValore(super.getRiserva()));
	}

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	private String fixValore(String value) {
		String newValue;
		if (value != null && !value.isEmpty()) {
			newValue = "%" + value.trim().toUpperCase() + "%";
		} else
			newValue = null;

		return newValue;
	}

	public String getStatoDomanda() {
		return statoDomanda;
	}

	public void setStatoDomanda(String statoDomanda) {
		this.statoDomanda = statoDomanda;
	}

	public Sostegno getSostegno() {
		return sostegno;
	}

	public void setSostegno(Sostegno sostegno) {
		this.sostegno = sostegno;
	}

	public String getStatoSostegno() {
		return statoSostegno;
	}

	public void setStatoSostegno(String statoSostegno) {
		this.statoSostegno = statoSostegno;
	}

	public String getCUAA() {
		return cuaa;
	}

	public void setCUAA(String cUAA) {
		cuaa = cUAA;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public List<String> getInterventi() {
		return interventi;
	}

	public void setInterventi(List<String> interventi) {
		this.interventi = interventi;
	}
}
