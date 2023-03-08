package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy;

import java.time.LocalDateTime;

public class FascicoloAgsDtoPaged {

	private Long idAgs;
	private String cuaa;
	private String denominazione;
	private StatoFascicoloLegacy stato;
	private String organismoPagatore;
	private String caa;
	private String sportello;
	private Long identificativoSportello;
	private LocalDateTime dataCostituzione;
	private LocalDateTime dataAggiornamento;
	private LocalDateTime dataValidazione;
	private boolean iscrittoSezioneSpecialeAgricola;
	private boolean nonIscrittoSezioneSpecialeAgricola;
	private String pec;
	private Integer totale;

	public Integer getTotale() {
		return totale;
	}
	public FascicoloAgsDtoPaged setTotale(Integer totale) {
		this.totale = totale;
		return this;
	}
	public Long getIdAgs() {
		return idAgs;
	}
	public FascicoloAgsDtoPaged setIdAgs(Long idAgs) {
		this.idAgs = idAgs;
		return this;
	}
	public String getCuaa() {
		return cuaa;
	}
	public FascicoloAgsDtoPaged setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public FascicoloAgsDtoPaged setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public StatoFascicoloLegacy getStato() {
		return stato;
	}
	public FascicoloAgsDtoPaged setStato(StatoFascicoloLegacy stato) {
		this.stato = stato;
		return this;
	}
	public String getOrganismoPagatore() {
		return organismoPagatore;
	}
	public FascicoloAgsDtoPaged setOrganismoPagatore(String organismoPagatore) {
		this.organismoPagatore = organismoPagatore;
		return this;
	}
	public String getCaa() {
		return caa;
	}
	public FascicoloAgsDtoPaged setCaa(String caa) {
		this.caa = caa;
		return this;
	}
	public String getSportello() {
		return sportello;
	}
	public FascicoloAgsDtoPaged setSportello(String sportello) {
		this.sportello = sportello;
		return this;
	}
	public Long getIdentificativoSportello() {
		return identificativoSportello;
	}
	public FascicoloAgsDtoPaged setIdentificativoSportello(Long identificativoSportello) {
		this.identificativoSportello = identificativoSportello;
		return this;
	}
	public LocalDateTime getDataCostituzione() {
		return dataCostituzione;
	}
	public FascicoloAgsDtoPaged setDataCostituzione(LocalDateTime dataCostituzione) {
		this.dataCostituzione = dataCostituzione;
		return this;
	}
	public LocalDateTime getDataAggiornamento() {
		return dataAggiornamento;
	}
	public FascicoloAgsDtoPaged setDataAggiornamento(LocalDateTime dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
		return this;
	}
	public LocalDateTime getDataValidazione() {
		return dataValidazione;
	}
	public FascicoloAgsDtoPaged setDataValidazione(LocalDateTime dataValidazione) {
		this.dataValidazione = dataValidazione;
		return this;
	}
	public boolean isIscrittoSezioneSpecialeAgricola() {
		return iscrittoSezioneSpecialeAgricola;
	}
	public FascicoloAgsDtoPaged setIscrittoSezioneSpecialeAgricola(boolean iscrittoSezioneSpecialeAgricola) {
		this.iscrittoSezioneSpecialeAgricola = iscrittoSezioneSpecialeAgricola;
		return this;
	}
	public boolean isNonIscrittoSezioneSpecialeAgricola() {
		return nonIscrittoSezioneSpecialeAgricola;
	}
	public FascicoloAgsDtoPaged setNonIscrittoSezioneSpecialeAgricola(boolean nonIscrittoSezioneSpecialeAgricola) {
		this.nonIscrittoSezioneSpecialeAgricola = nonIscrittoSezioneSpecialeAgricola;
		return this;
	}
	public String getPec() {
		return pec;
	}
	public FascicoloAgsDtoPaged setPec(String pec) {
		this.pec = pec;
		return this;
	}

}
