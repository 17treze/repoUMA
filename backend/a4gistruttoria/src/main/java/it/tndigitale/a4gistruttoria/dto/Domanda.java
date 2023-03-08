package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;

public class Domanda {
	
	private Long id;
	private BigDecimal codEnteCompilatore;
	private String codModuloDomanda;
	private String cuaaIntestatario;
	private String descEnteCompilatore;
	private String descModuloDomanda;
	private Date dtPresentazOriginaria;
	private Date dtPresentazione;
	private Date dtProtocollazOriginaria;
	private Date dtProtocollazione;
	private BigDecimal numDomandaRettificata;
	private BigDecimal numeroDomanda;
	private String ragioneSociale;
	
	private StatoDomanda statoDomanda;
	private List<IstruttoriaDto> istruttoriaDto;
	private Boolean annulloRiduzione;
	private DatiErede datiErede;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getCodEnteCompilatore() {
		return codEnteCompilatore;
	}
	public void setCodEnteCompilatore(BigDecimal codEnteCompilatore) {
		this.codEnteCompilatore = codEnteCompilatore;
	}
	public String getCodModuloDomanda() {
		return codModuloDomanda;
	}
	public void setCodModuloDomanda(String codModuloDomanda) {
		this.codModuloDomanda = codModuloDomanda;
	}
	public String getCuaaIntestatario() {
		return cuaaIntestatario;
	}
	public void setCuaaIntestatario(String cuaaIntestatario) {
		this.cuaaIntestatario = cuaaIntestatario;
	}
	public String getDescEnteCompilatore() {
		return descEnteCompilatore;
	}
	public void setDescEnteCompilatore(String descEnteCompilatore) {
		this.descEnteCompilatore = descEnteCompilatore;
	}
	public String getDescModuloDomanda() {
		return descModuloDomanda;
	}
	public void setDescModuloDomanda(String descModuloDomanda) {
		this.descModuloDomanda = descModuloDomanda;
	}
	public Date getDtPresentazOriginaria() {
		return dtPresentazOriginaria;
	}
	public void setDtPresentazOriginaria(Date dtPresentazOriginaria) {
		this.dtPresentazOriginaria = dtPresentazOriginaria;
	}
	public Date getDtPresentazione() {
		return dtPresentazione;
	}
	public void setDtPresentazione(Date dtPresentazione) {
		this.dtPresentazione = dtPresentazione;
	}
	public Date getDtProtocollazOriginaria() {
		return dtProtocollazOriginaria;
	}
	public void setDtProtocollazOriginaria(Date dtProtocollazOriginaria) {
		this.dtProtocollazOriginaria = dtProtocollazOriginaria;
	}
	public Date getDtProtocollazione() {
		return dtProtocollazione;
	}
	public void setDtProtocollazione(Date dtProtocollazione) {
		this.dtProtocollazione = dtProtocollazione;
	}
	public BigDecimal getNumDomandaRettificata() {
		return numDomandaRettificata;
	}
	public void setNumDomandaRettificata(BigDecimal numDomandaRettificata) {
		this.numDomandaRettificata = numDomandaRettificata;
	}
	public BigDecimal getNumeroDomanda() {
		return numeroDomanda;
	}
	public void setNumeroDomanda(BigDecimal numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public StatoDomanda getStatoDomanda() {
		return statoDomanda;
	}
	public void setStatoDomanda(StatoDomanda statoDomanda) {
		this.statoDomanda = statoDomanda;
	}
	public List<IstruttoriaDto> getIstruttoriaDto() {
		return istruttoriaDto;
	}
	public void setIstruttoriaDto(List<IstruttoriaDto> istruttoriaDto) {
		this.istruttoriaDto = istruttoriaDto;
	}
	public Boolean getAnnulloRiduzione() {
		return annulloRiduzione;
	}
	public void setAnnulloRiduzione(Boolean annulloRiduzione) {
		this.annulloRiduzione = annulloRiduzione;
	}
	public DatiErede getErede() {
		return datiErede;
	}
	public void setErede(DatiErede erede) {
		this.datiErede = erede;
	}

}
