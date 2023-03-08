package it.tndigitale.a4g.ags.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class DomandaUnica {

	//TODO capire cos'è
	private InfoGeneraliDomanda infoGeneraliDomanda;
	private ControlliPresentazione controlliPresentazione;
	private Richieste richieste;
	private List<CalcoloSostegnoAgs> calcoliSostegno;
	private InfoIstruttoriaDomanda infoIstruttoriaDomanda;
	private InfoLiquidabilita infoLiquidabilita;
	private SostegnoDisaccoppiato sostegnoDisaccoppiato;
	private SostegnoSuperfici sostegnoSuperfici;
	private List<ErroreControlloRicevibilitaDomanda> listaErroriRicevibilita;

	public DomandaUnica() {

	}

	public DomandaUnica(InfoGeneraliDomanda infoGeneraliDomanda, ControlliPresentazione controlliPresentazione, Richieste richieste) {
		this.infoGeneraliDomanda = infoGeneraliDomanda;
		this.controlliPresentazione = controlliPresentazione;
		this.richieste = richieste;
	}

	public DomandaUnica(InfoGeneraliDomanda infoGeneraliDomanda, ControlliPresentazione controlliPresentazione, Richieste richieste, List<CalcoloSostegnoAgs> calcoliSostegno,
			InfoIstruttoriaDomanda infoIstruttoriaDomanda, InfoLiquidabilita infoLiquidabilita) {
		this.infoGeneraliDomanda = infoGeneraliDomanda;
		this.controlliPresentazione = controlliPresentazione;
		this.richieste = richieste;
		this.calcoliSostegno = calcoliSostegno;
		this.infoIstruttoriaDomanda = infoIstruttoriaDomanda;
		this.infoLiquidabilita = infoLiquidabilita;
	}

	public InfoLiquidabilita getInfoLiquidabilita() {
		return infoLiquidabilita;
	}

	public void setInfoLiquidabilita(InfoLiquidabilita infoLiquidabilita) {
		this.infoLiquidabilita = infoLiquidabilita;
	}

	public InfoGeneraliDomanda getInfoGeneraliDomanda() {
		return infoGeneraliDomanda;
	}

	public void setInfoGeneraliDomanda(InfoGeneraliDomanda infoGeneraliDomanda) {
		this.infoGeneraliDomanda = infoGeneraliDomanda;
	}

	public ControlliPresentazione getControlliPresentazione() {
		return controlliPresentazione;
	}

	public void setControlliPresentazione(ControlliPresentazione controlliPresentazione) {
		this.controlliPresentazione = controlliPresentazione;
	}

	public Richieste getRichieste() {
		return richieste;
	}

	public void setRichieste(Richieste richieste) {
		this.richieste = richieste;
	}

	public List<CalcoloSostegnoAgs> getCalcoliSostegno() {
		return calcoliSostegno;
	}

	public void setCalcoliSostegno(List<CalcoloSostegnoAgs> calcoliSostegno) {
		this.calcoliSostegno = calcoliSostegno;
	}

	public InfoIstruttoriaDomanda getInfoIstruttoriaDomanda() {
		return infoIstruttoriaDomanda;
	}

	public void setInfoIstruttoriaDomanda(InfoIstruttoriaDomanda infoIstruttoriaDomanda) {
		this.infoIstruttoriaDomanda = infoIstruttoriaDomanda;
	}

	public SostegnoDisaccoppiato getSostegnoDisaccoppiato() {
		return sostegnoDisaccoppiato;
	}

	public DomandaUnica setSostegnoDisaccoppiato(SostegnoDisaccoppiato sostegnoDisaccoppiato) {
		this.sostegnoDisaccoppiato = sostegnoDisaccoppiato;
		return this;
	}

	public SostegnoSuperfici getSostegnoSuperfici() {
		return sostegnoSuperfici;
	}

	public DomandaUnica setSostegnoSuperfici(SostegnoSuperfici sostegnoSuperfici) {
		this.sostegnoSuperfici = sostegnoSuperfici;
		return this;
	}

	public List<ErroreControlloRicevibilitaDomanda> getListaErroriRicevibilita() {
		return listaErroriRicevibilita;
	}

	public DomandaUnica setListaErroriRicevibilita(List<ErroreControlloRicevibilitaDomanda> listaErroriRicevibilita) {
		this.listaErroriRicevibilita = listaErroriRicevibilita;
		return this;
	}
}
