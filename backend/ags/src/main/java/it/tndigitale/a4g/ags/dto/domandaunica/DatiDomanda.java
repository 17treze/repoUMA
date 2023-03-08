package it.tndigitale.a4g.ags.dto.domandaunica;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import it.tndigitale.a4g.ags.model.StatoDomanda;

public class DatiDomanda {

	private Integer campagna;
	private Long numeroDomanda;
	private String cuaaIntestatario;
	private String ragioneSociale;
	private TipoModulo modulo;
	private Long numeroDomandaRettificata; // valutare se mantenerlo, ai fini dell'istruttoria non occorre
	private String codEnteCompilatore; // valutare se mantenerlo, ai fini dell'istruttoria non occorre
	private String enteCompilatore; // valutare se mantenerlo, ai fini dell'istruttoria non occorre
	private LocalDateTime dataPresentazione;  // valutare se mantenerlo, ai fini dell'istruttoria non occorre
	private LocalDateTime dataProtocollazione;
	private LocalDateTime dataPresentazioneOriginaria;  // valutare se mantenerlo, ai fini dell'istruttoria non occorre
	private LocalDateTime dataProtocollazioneOriginaria; // -> usata per calcolare giorni ritardo
	private LocalDateTime dataPassaggioStato;  // valutare se mantenerlo, ai fini dell'istruttoria non occorre
	private StatoDomanda stato;
	private LocalDateTime dtProtocollazUltModifica;
	private BigDecimal NumeroDomandaUltModifica;
	
	public LocalDateTime getDtProtocollazUltModifica() {
		return dtProtocollazUltModifica;
	}

	public void setDtProtocollazUltModifica(LocalDateTime dtProtocollazUltModifica) {
		this.dtProtocollazUltModifica = dtProtocollazUltModifica;
	}

	public BigDecimal getNumeroDomandaUltModifica() {
		return NumeroDomandaUltModifica;
	}

	public void setNumeroDomandaUltModifica(BigDecimal numeroDomandaUltModifica) {
		NumeroDomandaUltModifica = numeroDomandaUltModifica;
	}

	private List<DichiarazioniDomandaUnica> dichiarazioni;
	private DatiDisaccoppiato disaccoppiato;
	private DatiSuperficie superficie;	
	private DatiZootecnia zootecnia;
	
	public static enum TipoModulo {
		DOMANDA, RITIRO_PARZIALE, MODIFICA, RITIRO_TOTALE;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public Long getNumeroDomanda() {
		return numeroDomanda;
	}

	public String getCuaaIntestatario() {
		return cuaaIntestatario;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public TipoModulo getModulo() {
		return modulo;
	}

	public Long getNumeroDomandaRettificata() {
		return numeroDomandaRettificata;
	}

	public String getCodEnteCompilatore() {
		return codEnteCompilatore;
	}

	public String getEnteCompilatore() {
		return enteCompilatore;
	}

	public LocalDateTime getDataPresentazione() {
		return dataPresentazione;
	}

	public LocalDateTime getDataProtocollazione() {
		return dataProtocollazione;
	}

	public LocalDateTime getDataPresentazioneOriginaria() {
		return dataPresentazioneOriginaria;
	}

	public LocalDateTime getDataProtocollazioneOriginaria() {
		return dataProtocollazioneOriginaria;
	}

	public StatoDomanda getStato() {
		return stato;
	}

	public List<DichiarazioniDomandaUnica> getDichiarazioni() {
		return dichiarazioni;
	}

	public DatiDisaccoppiato getDisaccoppiato() {
		return disaccoppiato;
	}

	public DatiSuperficie getSuperficie() {
		return superficie;
	}

	public DatiZootecnia getZootecnia() {
		return zootecnia;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public void setNumeroDomanda(Long numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public void setCuaaIntestatario(String cuaaIntestatario) {
		this.cuaaIntestatario = cuaaIntestatario;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public void setModulo(TipoModulo modulo) {
		this.modulo = modulo;
	}

	public void setNumeroDomandaRettificata(Long numeroDomandaRettificata) {
		this.numeroDomandaRettificata = numeroDomandaRettificata;
	}

	public void setCodEnteCompilatore(String codEnteCompilatore) {
		this.codEnteCompilatore = codEnteCompilatore;
	}

	public void setEnteCompilatore(String enteCompilatore) {
		this.enteCompilatore = enteCompilatore;
	}

	public void setDataPresentazione(LocalDateTime dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	public void setDataProtocollazione(LocalDateTime dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
	}

	public void setDataPresentazioneOriginaria(LocalDateTime dataPresentazioneOriginaria) {
		this.dataPresentazioneOriginaria = dataPresentazioneOriginaria;
	}

	public void setDataProtocollazioneOriginaria(LocalDateTime dataProtocollazioneOriginaria) {
		this.dataProtocollazioneOriginaria = dataProtocollazioneOriginaria;
	}

	public LocalDateTime getDataPassaggioStato() {
		return dataPassaggioStato;
	}

	public void setDataPassaggioStato(LocalDateTime dataPassaggioStato) {
		this.dataPassaggioStato = dataPassaggioStato;
	}

	public void setStato(StatoDomanda stato) {
		this.stato = stato;
	}

	public void setDichiarazioni(List<DichiarazioniDomandaUnica> dichiarazioni) {
		this.dichiarazioni = dichiarazioni;
	}

	public void setDisaccoppiato(DatiDisaccoppiato disaccoppiato) {
		this.disaccoppiato = disaccoppiato;
	}

	public void setSuperficie(DatiSuperficie superficie) {
		this.superficie = superficie;
	}

	public void setZootecnia(DatiZootecnia zootecnia) {
		this.zootecnia = zootecnia;
	}
}
