package it.tndigitale.a4g.uma.dto.richiesta;

import java.time.LocalDateTime;

import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;

public class RichiestaCarburanteDto extends CarburanteRichiestoDto {

	private Long id;
	private String cuaa;
	private Long campagna;
	private LocalDateTime dataPresentazione;
	private LocalDateTime dataProtocollazione;
	private String cfRichiedente;
	private StatoRichiestaCarburante stato;
	// C'è almeno una macchina a benzina con flag true in utilizzo macchinari
	private Boolean haMacchineBenzina;
	// C'è almeno una macchina a gasolio con flag true in utilizzo macchinari
	private Boolean haMacchineGasolio;
	// C'è almeno un fabbricato importato in fase di creazione richiesta
	private Boolean haFabbricati;
	// C'è almeno un fabbricato di tipo serra importato in fase di creazione richiesta
	private Boolean haSerre;
	// C'è almeno una coltura in ags che ha una corrispondenza in gruppi di lavorazione per questa azienda
	private Boolean haSuperfici;

	// C'è almeno una lavorazione dichiarata (qualsiasi sia l'ambito)
	private Boolean haDichiarazioni;

	// Dato di anagrafica fascicolo
	private String denominazione;

	// Protocollo
	private String protocollo;

	// Indica, se esiste, l'id della richiesta che si sta rettificando
	private Long idRettificata;

	public Long getId() {
		return id;
	}
	public RichiestaCarburanteDto setId(Long id) {
		this.id = id;
		return this;
	}
	public String getCuaa() {
		return cuaa;
	}
	public RichiestaCarburanteDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public Long getCampagna() {
		return campagna;
	}
	public RichiestaCarburanteDto setCampagna(Long campagna) {
		this.campagna = campagna;
		return this;
	}
	public LocalDateTime getDataPresentazione() {
		return dataPresentazione;
	}
	public RichiestaCarburanteDto setDataPresentazione(LocalDateTime dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
		return this;
	}
	public LocalDateTime getDataProtocollazione() {
		return dataProtocollazione;
	}
	public RichiestaCarburanteDto setDataProtocollazione(LocalDateTime dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
		return this;
	}
	public String getCfRichiedente() {
		return cfRichiedente;
	}
	public RichiestaCarburanteDto setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
		return this;
	}
	public StatoRichiestaCarburante getStato() {
		return stato;
	}
	public RichiestaCarburanteDto setStato(StatoRichiestaCarburante stato) {
		this.stato = stato;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public RichiestaCarburanteDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public Boolean getHaMacchineBenzina() {
		return haMacchineBenzina;
	}
	public RichiestaCarburanteDto setHaMacchineBenzina(Boolean haMacchineBenzina) {
		this.haMacchineBenzina = haMacchineBenzina;
		return this;
	}
	public Boolean getHaMacchineGasolio() {
		return haMacchineGasolio;
	}
	public RichiestaCarburanteDto setHaMacchineGasolio(Boolean haMacchineGasolio) {
		this.haMacchineGasolio = haMacchineGasolio;
		return this;
	}
	public Boolean getHaFabbricati() {
		return haFabbricati;
	}
	public RichiestaCarburanteDto setHaFabbricati(Boolean haFabbricati) {
		this.haFabbricati = haFabbricati;
		return this;
	}
	public Boolean getHaSuperfici() {
		return haSuperfici;
	}
	public RichiestaCarburanteDto setHaSuperfici(Boolean haSuperfici) {
		this.haSuperfici = haSuperfici;
		return this;
	}
	public Boolean getHaSerre() {
		return haSerre;
	}
	public RichiestaCarburanteDto setHaSerre(Boolean haSerre) {
		this.haSerre = haSerre;
		return this;
	}
	public Boolean getHaDichiarazioni() {
		return haDichiarazioni;
	}
	public RichiestaCarburanteDto setHaDichiarazioni(Boolean haDichiarazioni) {
		this.haDichiarazioni = haDichiarazioni;
		return this;
	}
	public String getProtocollo() {
		return protocollo;
	}
	public RichiestaCarburanteDto setProtocollo(String protocollo) {
		this.protocollo = protocollo;
		return this;
	}
	public Long getIdRettificata() {
		return idRettificata;
	}
	public RichiestaCarburanteDto setIdRettificata(Long idRettificata) {
		this.idRettificata = idRettificata;
		return this;
	}
}
