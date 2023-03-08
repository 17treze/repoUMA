package it.tndigitale.a4g.srt.dto;

import java.time.LocalDate;
import java.util.List;

public class PsrDomandeDto {
	private Integer idProgetto;
	private Integer idBando;
	private PsrAccontoSaldoDto saldo;
	private PsrAnticipoDto anticipo;
	private List<PsrAccontoSaldoDto> acconti;
	private PsrFinanziabilitaDto finanziabilita;
	private Character codiceStatoProgetto;
	private String statoProgetto;
	private String protocolloProgetto;
	private LocalDate dataProtocollazione;
	private Boolean isFinanziatoPat;
    private String codice;
	
	public Integer getIdProgetto() {
		return idProgetto;
	}
	public void setIdProgetto(Integer idProgetto) {
		this.idProgetto = idProgetto;
	}
	public Integer getIdBando() {
		return idBando;
	}
	public void setIdBando(Integer idBando) {
		this.idBando = idBando;
	}
	public PsrAccontoSaldoDto getSaldo() {
		return saldo;
	}
	public void setSaldo(PsrAccontoSaldoDto saldo) {
		this.saldo = saldo;
	}
	public List<PsrAccontoSaldoDto> getAcconti() {
		return acconti;
	}
	public void setAcconti(List<PsrAccontoSaldoDto> acconti) {
		this.acconti = acconti;
	}
	public PsrAnticipoDto getAnticipo() {
		return anticipo;
	}
	public void setAnticipo(PsrAnticipoDto anticipo) {
		this.anticipo = anticipo;
	}
	public PsrFinanziabilitaDto getFinanziabilita() {
		return finanziabilita;
	}
	public void setFinanziabilita(PsrFinanziabilitaDto finanziabilita) {
		this.finanziabilita = finanziabilita;
	}
	public Character getCodiceStatoProgetto() {
		return codiceStatoProgetto;
	}
	public void setCodiceStatoProgetto(Character codiceStatoProgetto) {
		this.codiceStatoProgetto = codiceStatoProgetto;
	}
	public String getStatoProgetto() {
		return statoProgetto;
	}
	public void setStatoProgetto(String statoProgetto) {
		this.statoProgetto = statoProgetto;
	}
	public String getProtocolloProgetto() {
		return protocolloProgetto;
	}
	public void setProtocolloProgetto(String protocolloProgetto) {
		this.protocolloProgetto = protocolloProgetto;
	}
	public LocalDate getDataProtocollazione() {
		return dataProtocollazione;
	}
	public void setDataProtocollazione(LocalDate dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
	}
	public Boolean getIsFinanziatoPat() {
		return isFinanziatoPat;
	}
	public void setIsFinanziatoPat(Boolean isFinanziatoPat) {
		this.isFinanziatoPat = isFinanziatoPat;
	}
    public String getCodice() {
        return codice;
    }
    public void setCodice(String codice) {
        this.codice = codice;
    }
}
