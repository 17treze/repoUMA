package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

public class Richieste {

	private SintesiRichieste sintesiRichieste;

	private List<SostegniSuperficieDto> sostegniSuperficie;

	private List<SostegniAllevamentoDto> sostegniAllevamento;

	private List<DatiPascolo> datiPascolo;

	private List<DichiarazioniDomandaUnica> dichiarazioniDomandaUnica;

	public SintesiRichieste getSintesiRichieste() {
		return sintesiRichieste;
	}

	public void setSintesiRichieste(SintesiRichieste sintesiRichieste) {
		this.sintesiRichieste = sintesiRichieste;
	}

	public List<SostegniSuperficieDto> getSostegniSuperficie() {
		return sostegniSuperficie;
	}

	public void setSostegniSuperficie(List<SostegniSuperficieDto> sostegniSuperficie) {
		this.sostegniSuperficie = sostegniSuperficie;
	}

	public List<SostegniAllevamentoDto> getSostegniAllevamento() {
		return sostegniAllevamento;
	}

	public void setSostegniAllevamento(List<SostegniAllevamentoDto> sostegniAllevamento) {
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
