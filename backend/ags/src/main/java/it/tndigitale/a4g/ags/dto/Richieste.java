package it.tndigitale.a4g.ags.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.ags.dto.domandaunica.DatiPascolo;
import it.tndigitale.a4g.ags.dto.domandaunica.DichiarazioniDomandaUnica;
import it.tndigitale.a4g.ags.dto.domandaunica.SostegniAllevamento;

@JsonInclude(Include.NON_EMPTY)
public class Richieste {

	private SintesiRichieste sintesiRichieste;

	private List<SostegniSuperficie> sostegniSuperficie;

	private List<SostegniAllevamento> sostegniAllevamento;

	private List<DatiPascolo> datiPascolo;

	private List<DichiarazioniDomandaUnica> dichiarazioniDomandaUnica;

	public SintesiRichieste getSintesiRichieste() {
		return sintesiRichieste;
	}

	public void setSintesiRichieste(SintesiRichieste sintesiRichieste) {
		this.sintesiRichieste = sintesiRichieste;
	}

	public List<SostegniSuperficie> getSostegniSuperficie() {
		return sostegniSuperficie;
	}

	public void setSostegniSuperficie(List<SostegniSuperficie> sostegniSuperficie) {
		this.sostegniSuperficie = sostegniSuperficie;
	}

	public List<SostegniAllevamento> getSostegniAllevamento() {
		return sostegniAllevamento;
	}

	public void setSostegniAllevamento(List<SostegniAllevamento> sostegniAllevamento) {
		this.sostegniAllevamento = sostegniAllevamento;
	}

	public List<DatiPascolo> getDatiPascolo() {
		return datiPascolo;
	}

	public void setDatiPascolo(List<DatiPascolo> datiPascolo) {
		this.datiPascolo = datiPascolo;
	}

	public List<DichiarazioniDomandaUnica> getDichiarazioniDomandaUnica() {
		return dichiarazioniDomandaUnica;
	}

	public void setDichiarazioniDomandaUnica(List<DichiarazioniDomandaUnica> dichiarazioniDomandaUnica) {
		this.dichiarazioniDomandaUnica = dichiarazioniDomandaUnica;
	}

}
