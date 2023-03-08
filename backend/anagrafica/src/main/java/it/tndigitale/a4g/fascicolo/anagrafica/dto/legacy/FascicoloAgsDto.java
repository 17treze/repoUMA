package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

/*
 * Informazioni del fascicolo Ags necessarie al funzionamento del sistema
 *  
 */
public class FascicoloAgsDto {

	private Long idAgs;
	private String cuaa;
	private String denominazione;
	private LocalDateTime dataMorteTitolare; // presente solo per le persone fisiche
	private StatoFascicoloLegacy stato;
	private String organismoPagatore;
	private LocalDateTime dataCostituzione;
	private LocalDateTime dataAggiornamento;
	private LocalDateTime dataValidazione;
	private boolean iscrittoSezioneSpecialeAgricola;
	private boolean nonIscrittoSezioneSpecialeAgricola;
	private String pec;
	private List<DetenzioneAgsDto> detenzioni;

	public String getCuaa() {
		return cuaa;
	}
	public FascicoloAgsDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public boolean isIscrittoSezioneSpecialeAgricola() {
		return iscrittoSezioneSpecialeAgricola;
	}
	public FascicoloAgsDto setIscrittoSezioneSpecialeAgricola(boolean iscrittoSezioneSpecialeAgricola) {
		this.iscrittoSezioneSpecialeAgricola = iscrittoSezioneSpecialeAgricola;
		return this;
	}
	public boolean isNonIscrittoSezioneSpecialeAgricola() {
		return nonIscrittoSezioneSpecialeAgricola;
	}
	public FascicoloAgsDto setNonIscrittoSezioneSpecialeAgricola(boolean nonIscrittoSezioneSpecialeAgricola) {
		this.nonIscrittoSezioneSpecialeAgricola = nonIscrittoSezioneSpecialeAgricola;
		return this;
	}
	public StatoFascicoloLegacy getStato() {
		return stato;
	}
	public FascicoloAgsDto setStato(StatoFascicoloLegacy stato) {
		this.stato = stato;
		return this;
	}
	public LocalDateTime getDataAggiornamento() {
		return dataAggiornamento;
	}
	public FascicoloAgsDto setDataAggiornamento(LocalDateTime dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public FascicoloAgsDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public String getPec() {
		return pec;
	}
	public FascicoloAgsDto setPec(String pec) {
		this.pec = pec;
		return this;
	}
	public Long getIdAgs() {
		return idAgs;
	}
	public FascicoloAgsDto setIdAgs(Long idAgs) {
		this.idAgs = idAgs;
		return this;
	}
	public String getOrganismoPagatore() {
		return organismoPagatore;
	}
	public FascicoloAgsDto setOrganismoPagatore(String organismoPagatore) {
		this.organismoPagatore = organismoPagatore;
		return this;
	}
	public LocalDateTime getDataCostituzione() {
		return dataCostituzione;
	}
	public FascicoloAgsDto setDataCostituzione(LocalDateTime dataCostituzione) {
		this.dataCostituzione = dataCostituzione;
		return this;
	}
	public LocalDateTime getDataValidazione() {
		return dataValidazione;
	}
	public FascicoloAgsDto setDataValidazione(LocalDateTime dataValidazione) {
		this.dataValidazione = dataValidazione;
		return this;
	}
	public List<DetenzioneAgsDto> getDetenzioni() {
		return detenzioni;
	}
	public FascicoloAgsDto setDetenzioni(List<DetenzioneAgsDto> detenzioni) {
		this.detenzioni = detenzioni;
		return this;
	}
	public LocalDateTime getDataMorteTitolare() {
		return dataMorteTitolare;
	}
	public FascicoloAgsDto setDataMorteTitolare(LocalDateTime dataMorteTitolare) {
		this.dataMorteTitolare = dataMorteTitolare;
		return this;
	}

	public FascicoloAgsDto addDetenzione(DetenzioneAgsDto detenzione) {
		if (CollectionUtils.isEmpty(detenzioni)) {
			this.detenzioni = new ArrayList<>();
		}
		this.detenzioni.add(detenzione);
		return this;
	}
}
