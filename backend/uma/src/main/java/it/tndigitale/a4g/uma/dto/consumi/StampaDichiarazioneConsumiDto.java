package it.tndigitale.a4g.uma.dto.consumi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;

@JsonInclude(Include.NON_EMPTY)
public class StampaDichiarazioneConsumiDto {
	private Long idDichiarazioneConsumi;
	private Long annoCampagna;
	private Long annoCampagnaRimanenza;
	private String cuaa;
	private String denominazione;
	private List<StampaPrelievoDto> prelievi;
	private CarburanteCompletoDto residuo;
	private CarburanteCompletoDto ammissibile;
	private String motivazioneAmmissibile;
	private CarburanteCompletoDto richiesto;
	private CarburanteDto ricevuto;
	private CarburanteDto trasferito;
	private CarburanteDto prelevato;
	private CarburanteDto disponibile;
	private CarburanteDto accisa;
	private String motivazioneAccisa;
	private CarburanteCompletoDto consumato;
	private CarburanteCompletoDto rimanenza;
	private CarburanteCompletoDto recupero;
	private String motivazioneRecuperoGasolio;
	private String motivazioneRecuperoGasolioTerzi;
	private String motivazioneRecuperoBenzina;
	private String motivazioneRecuperoGasolioSerre;
	private String dataStampa;

	public Long getIdDichiarazioneConsumi() {
		return idDichiarazioneConsumi;
	}
	public StampaDichiarazioneConsumiDto setIdDichiarazioneConsumi(Long idDichiarazioneConsumi) {
		this.idDichiarazioneConsumi = idDichiarazioneConsumi;
		return this;
	}
	public Long getAnnoCampagna() {
		return annoCampagna;
	}
	public StampaDichiarazioneConsumiDto setAnnoCampagna(Long annoCampagna) {
		this.annoCampagna = annoCampagna;
		return this;
	}
	public String getCuaa() {
		return cuaa;
	}
	public StampaDichiarazioneConsumiDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public StampaDichiarazioneConsumiDto setDenominazione(String denominazioneDaAnagrafica) {
		this.denominazione = denominazioneDaAnagrafica;
		return this;
	}
	public CarburanteCompletoDto getResiduo() {
		return residuo;
	}
	public StampaDichiarazioneConsumiDto setResiduo(CarburanteCompletoDto residuo) {
		this.residuo = residuo;
		return this;
	}
	public CarburanteCompletoDto getAmmissibile() {
		return ammissibile;
	}
	public StampaDichiarazioneConsumiDto setAmmissibile(CarburanteCompletoDto ammissibile) {
		this.ammissibile = ammissibile;
		return this;
	}
	public CarburanteCompletoDto getRichiesto() {
		return richiesto;
	}
	public StampaDichiarazioneConsumiDto setRichiesto(CarburanteCompletoDto richiesto) {
		this.richiesto = richiesto;
		return this;
	}
	public CarburanteDto getRicevuto() {
		return ricevuto;
	}
	public StampaDichiarazioneConsumiDto setRicevuto(CarburanteDto ricevuto) {
		this.ricevuto = ricevuto;
		return this;
	}
	public CarburanteDto getTrasferito() {
		return trasferito;
	}
	public StampaDichiarazioneConsumiDto setTrasferito(CarburanteDto trasferito) {
		this.trasferito = trasferito;
		return this;
	}
	public CarburanteDto getPrelevato() {
		return prelevato;
	}
	public StampaDichiarazioneConsumiDto setPrelevato(CarburanteDto prelevato) {
		this.prelevato = prelevato;
		return this;
	}
	public CarburanteDto getDisponibile() {
		return disponibile;
	}
	public StampaDichiarazioneConsumiDto setDisponibile(CarburanteDto disponibile) {
		this.disponibile = disponibile;
		return this;
	}
	public CarburanteDto getAccisa() {
		return accisa;
	}
	public StampaDichiarazioneConsumiDto setAccisa(CarburanteDto accisa) {
		this.accisa = accisa;
		return this;
	}
	public CarburanteCompletoDto getConsumato() {
		return consumato;
	}
	public StampaDichiarazioneConsumiDto setConsumato(CarburanteCompletoDto consumato) {
		this.consumato = consumato;
		return this;
	}
	public CarburanteCompletoDto getRimanenza() {
		return rimanenza;
	}
	public StampaDichiarazioneConsumiDto setRimanenza(CarburanteCompletoDto rimanenza) {
		this.rimanenza = rimanenza;
		return this;
	}
	public CarburanteCompletoDto getRecupero() {
		return recupero;
	}
	public StampaDichiarazioneConsumiDto setRecupero(CarburanteCompletoDto recupero) {
		this.recupero = recupero;
		return this;
	}
	public String getMotivazioneRecuperoGasolio() {
		return motivazioneRecuperoGasolio;
	}
	public StampaDichiarazioneConsumiDto setMotivazioneRecuperoGasolio(String motivazioneRecuperoGasolio) {
		this.motivazioneRecuperoGasolio = motivazioneRecuperoGasolio;
		return this;
	}
	public String getMotivazioneRecuperoGasolioTerzi() {
		return motivazioneRecuperoGasolioTerzi;
	}
	public StampaDichiarazioneConsumiDto setMotivazioneRecuperoGasolioTerzi(String motivazioneRecuperoGasolioTerzi) {
		this.motivazioneRecuperoGasolioTerzi = motivazioneRecuperoGasolioTerzi;
		return this;
	}
	public String getMotivazioneRecuperoBenzina() {
		return motivazioneRecuperoBenzina;
	}
	public StampaDichiarazioneConsumiDto setMotivazioneRecuperoBenzina(String motivazioneRecuperoBenzina) {
		this.motivazioneRecuperoBenzina = motivazioneRecuperoBenzina;
		return this;
	}
	public String getMotivazioneRecuperoGasolioSerre() {
		return motivazioneRecuperoGasolioSerre;
	}
	public StampaDichiarazioneConsumiDto setMotivazioneRecuperoGasolioSerre(String motivazioneRecuperoGasolioSerre) {
		this.motivazioneRecuperoGasolioSerre = motivazioneRecuperoGasolioSerre;
		return this;
	}
	public Long getAnnoCampagnaRimanenza() {
		return annoCampagnaRimanenza;
	}
	public StampaDichiarazioneConsumiDto setAnnoCampagnaRimanenza(Long annoCampagnaRimanenza) {
		this.annoCampagnaRimanenza = annoCampagnaRimanenza;
		return this;
	}
	public String getDataStampa() {
		return dataStampa;
	}
	public StampaDichiarazioneConsumiDto setDataStampa(String dataStampa) {
		this.dataStampa = dataStampa;
		return this;
	}
	public List<StampaPrelievoDto> getPrelievi() {
		return prelievi;
	}
	public StampaDichiarazioneConsumiDto setPrelievi(List<StampaPrelievoDto> prelievi) {
		this.prelievi = prelievi;
		return this;
	}
	public String getMotivazioneAmmissibile() {
		return motivazioneAmmissibile;
	}
	public StampaDichiarazioneConsumiDto setMotivazioneAmmissibile(String motivazioneAmmissibile) {
		this.motivazioneAmmissibile = motivazioneAmmissibile;
		return this;
	}
	public String getMotivazioneAccisa() {
		return motivazioneAccisa;
	}
	public StampaDichiarazioneConsumiDto setMotivazioneAccisa(String motivazioneAccisa) {
		this.motivazioneAccisa = motivazioneAccisa;
		return this;
	}
}
