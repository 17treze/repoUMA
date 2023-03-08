package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.time.LocalDate;

public class DetenzioneDto {
	private String cuaa;
	private String allevId;
	private String aziendaCodice;
	private LocalDate dtInizioDetenzione;
	private LocalDate dtFineDetenzione;


	private Boolean allevamentoMontagna;			// presente solo per vacche latte
	private LocalDate vaccaDtIngresso;				// campo comune a interventi latte (vaccaDtIngresso), macello e ovicaprini (dtIngresso) 
	private LocalDate vaccaDtComAutoritaIngresso;	// campo comume a interventi latte (vaccaDtComAutoritaIngresso) e macello (dtComAutoritaIngresso)
	private LocalDate vaccaDtInserimentoBdnIngresso;// campo comune a interventi latte (vaccaDtInserimentoBdnIngresso), macello e ovicaprini (dtInserimentoBdnIngresso) 

	// valutare una estensione di classe
	private LocalDate vaccaDtInserimentoBdnUscita; 	// presente solo per i capi macellati
	private LocalDate dtflagDelegatoIngresso;		// presente solo per i capi macellati
	private LocalDate dtUscita;						// presente solo per i capi macellati			

	public String getCuaa() {
		return cuaa;
	}
	public DetenzioneDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getAllevId() {
		return allevId;
	}
	public DetenzioneDto setAllevId(String allevId) {
		this.allevId = allevId;
		return this;
	}
	public String getAziendaCodice() {
		return aziendaCodice;
	}
	public DetenzioneDto setAziendaCodice(String aziendaCodice) {
		this.aziendaCodice = aziendaCodice;
		return this;
	}
	public LocalDate getDtInizioDetenzione() {
		return dtInizioDetenzione;
	}
	public DetenzioneDto setDtInizioDetenzione(LocalDate dtInizioDetenzione) {
		this.dtInizioDetenzione = dtInizioDetenzione;
		return this;
	}
	public LocalDate getDtFineDetenzione() {
		return dtFineDetenzione;
	}
	public DetenzioneDto setDtFineDetenzione(LocalDate dtFineDetenzione) {
		this.dtFineDetenzione = dtFineDetenzione;
		return this;
	}


	public Boolean getAllevamentoMontagna() {
		return allevamentoMontagna;
	}
	public DetenzioneDto setAllevamentoMontagna(Boolean allevamentoMontagna) {
		this.allevamentoMontagna = allevamentoMontagna;
		return this;
	}
	public LocalDate getVaccaDtIngresso() {
		return vaccaDtIngresso;
	}
	public DetenzioneDto setVaccaDtIngresso(LocalDate vaccaDtIngresso) {
		this.vaccaDtIngresso = vaccaDtIngresso;
		return this;
	}
	public LocalDate getVaccaDtComAutoritaIngresso() {
		return vaccaDtComAutoritaIngresso;
	}
	public DetenzioneDto setVaccaDtComAutoritaIngresso(LocalDate vaccaDtComAutoritaIngresso) {
		this.vaccaDtComAutoritaIngresso = vaccaDtComAutoritaIngresso;
		return this;
	}
	public LocalDate getVaccaDtInserimentoBdnIngresso() {
		return vaccaDtInserimentoBdnIngresso;
	}
	public DetenzioneDto setVaccaDtInserimentoBdnIngresso(LocalDate vaccaDtInserimentoBdnIngresso) {
		this.vaccaDtInserimentoBdnIngresso = vaccaDtInserimentoBdnIngresso;
		return this;
	}
	public LocalDate getVaccaDtInserimentoBdnUscita() {
		return vaccaDtInserimentoBdnUscita;
	}
	public DetenzioneDto setVaccaDtInserimentoBdnUscita(LocalDate vaccaDtInserimentoBdnUscita) {
		this.vaccaDtInserimentoBdnUscita = vaccaDtInserimentoBdnUscita;
		return this;
	}
	public LocalDate getDtflagDelegatoIngresso() {
		return dtflagDelegatoIngresso;
	}
	public DetenzioneDto setDtflagDelegatoIngresso(LocalDate dtflagDelegatoIngresso) {
		this.dtflagDelegatoIngresso = dtflagDelegatoIngresso;
		return this;
	}
	public LocalDate getDtUscita() {
		return dtUscita;
	}
	public DetenzioneDto setDtUscita(LocalDate dtUscita) {
		this.dtUscita = dtUscita;
		return this;
	}

}
