package it.tndigitale.a4gistruttoria.dto.domandaunica;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;

public class SintesiDomandaUnica implements Serializable {

	private static final long serialVersionUID = -4837333954212869821L;

	private Long id;
	private Integer campagna;
	private Long codEnteCompilatore;
	private String codModuloDomanda;
	private String cuaaIntestatario;
	private String descEnteCompilatore;
	private String descModuloDomanda;
	private LocalDateTime dtPresentazOriginaria;
	private LocalDate dtPresentazione;
	private LocalDateTime dtProtocollazOriginaria;
	private LocalDateTime dtProtocollazione;
	private Long numDomandaRettificata;
	private Long numeroDomanda;
	private String ragioneSociale;
	private StatoDomanda stato;
	
	public Long getId() {
		return id;
	}
	public SintesiDomandaUnica setId(Long id) {
		this.id = id;
		return this;
	}
	public Integer getCampagna() {
		return campagna;
	}
	public SintesiDomandaUnica setCampagna(Integer campagna) {
		this.campagna = campagna;
		return this;
	}
	public Long getCodEnteCompilatore() {
		return codEnteCompilatore;
	}
	public SintesiDomandaUnica setCodEnteCompilatore(Long codEnteCompilatore) {
		this.codEnteCompilatore = codEnteCompilatore;
		return this;
	}
	public String getCodModuloDomanda() {
		return codModuloDomanda;
	}
	public SintesiDomandaUnica setCodModuloDomanda(String codModuloDomanda) {
		this.codModuloDomanda = codModuloDomanda;
		return this;
	}
	public String getCuaaIntestatario() {
		return cuaaIntestatario;
	}
	public SintesiDomandaUnica setCuaaIntestatario(String cuaaIntestatario) {
		this.cuaaIntestatario = cuaaIntestatario;
		return this;
	}
	public String getDescEnteCompilatore() {
		return descEnteCompilatore;
	}
	public SintesiDomandaUnica setDescEnteCompilatore(String descEnteCompilatore) {
		this.descEnteCompilatore = descEnteCompilatore;
		return this;
	}
	public String getDescModuloDomanda() {
		return descModuloDomanda;
	}
	public SintesiDomandaUnica setDescModuloDomanda(String descModuloDomanda) {
		this.descModuloDomanda = descModuloDomanda;
		return this;
	}
	public LocalDateTime getDtPresentazOriginaria() {
		return dtPresentazOriginaria;
	}
	public SintesiDomandaUnica setDtPresentazOriginaria(LocalDateTime dtPresentazOriginaria) {
		this.dtPresentazOriginaria = dtPresentazOriginaria;
		return this;
	}
	public LocalDate getDtPresentazione() {
		return dtPresentazione;
	}
	public SintesiDomandaUnica setDtPresentazione(LocalDate dtPresentazione) {
		this.dtPresentazione = dtPresentazione;
		return this;
	}
	public LocalDateTime getDtProtocollazOriginaria() {
		return dtProtocollazOriginaria;
	}
	public SintesiDomandaUnica setDtProtocollazOriginaria(LocalDateTime dtProtocollazOriginaria) {
		this.dtProtocollazOriginaria = dtProtocollazOriginaria;
		return this;
	}
	public LocalDateTime getDtProtocollazione() {
		return dtProtocollazione;
	}
	public SintesiDomandaUnica setDtProtocollazione(LocalDateTime dtProtocollazione) {
		this.dtProtocollazione = dtProtocollazione;
		return this;
	}
	public Long getNumDomandaRettificata() {
		return numDomandaRettificata;
	}
	public SintesiDomandaUnica setNumDomandaRettificata(Long numDomandaRettificata) {
		this.numDomandaRettificata = numDomandaRettificata;
		return this;
	}
	public Long getNumeroDomanda() {
		return numeroDomanda;
	}
	public SintesiDomandaUnica setNumeroDomanda(Long numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
		return this;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public SintesiDomandaUnica setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
		return this;
	}
	public StatoDomanda getStato() {
		return stato;
	}
	public SintesiDomandaUnica setStato(StatoDomanda stato) {
		this.stato = stato;
		return this;
	}
}
