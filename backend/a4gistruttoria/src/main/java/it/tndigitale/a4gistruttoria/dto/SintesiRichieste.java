package it.tndigitale.a4gistruttoria.dto;

public class SintesiRichieste {

	private boolean richiestaDisaccoppiato;
	private boolean richiestaSuperfici;
	private boolean richiestaZootecnia;

	public boolean isRichiestaDisaccoppiato() {
		return richiestaDisaccoppiato;
	}

	public void setRichiestaDisaccoppiato(boolean richiestaDisaccoppiato) {
		this.richiestaDisaccoppiato = richiestaDisaccoppiato;
	}

	public boolean isRichiestaSuperfici() {
		return richiestaSuperfici;
	}

	public void setRichiestaSuperfici(boolean richiestaSuperfici) {
		this.richiestaSuperfici = richiestaSuperfici;
	}

	public boolean isRichiestaZootecnia() {
		return richiestaZootecnia;
	}

	public void setRichiestaZootecnia(boolean richiestaZootecnia) {
		this.richiestaZootecnia = richiestaZootecnia;
	}
}
