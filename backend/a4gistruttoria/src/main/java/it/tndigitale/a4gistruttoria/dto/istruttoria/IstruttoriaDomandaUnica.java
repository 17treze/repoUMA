package it.tndigitale.a4gistruttoria.dto.istruttoria;

import java.io.Serializable;
import java.time.LocalDateTime;

import it.tndigitale.a4gistruttoria.dto.domandaunica.SintesiDomandaUnica;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

public class IstruttoriaDomandaUnica implements Serializable {

	private static final long serialVersionUID = -4837333954212869821L;

	private Long id;
	private TipoIstruttoria tipo;
	private StatoIstruttoria stato;
	private Sostegno sostegno;
	private SintesiDomandaUnica domanda;
	private SintesiElencoLiquidazione elencoLiquidazione;
	private Boolean isBloccata;
	private Boolean isErroreCalcolo;
	private LocalDateTime dataUltimoCalcolo;
	
	public Long getId() {
		return id;
	}
	public IstruttoriaDomandaUnica setId(Long id) {
		this.id = id;
		return this;
	}
	public TipoIstruttoria getTipo() {
		return tipo;
	}
	public IstruttoriaDomandaUnica setTipo(TipoIstruttoria tipo) {
		this.tipo = tipo;
		return this;
	}
	public StatoIstruttoria getStato() {
		return stato;
	}
	public IstruttoriaDomandaUnica setStato(StatoIstruttoria stato) {
		this.stato = stato;
		return this;
	}
	public Sostegno getSostegno() {
		return sostegno;
	}
	public IstruttoriaDomandaUnica setSostegno(Sostegno sostegno) {
		this.sostegno = sostegno;
		return this;
	}
	public SintesiDomandaUnica getDomanda() {
		return domanda;
	}
	public IstruttoriaDomandaUnica setDomanda(SintesiDomandaUnica domanda) {
		this.domanda = domanda;
		return this;
	}
	public SintesiElencoLiquidazione getElencoLiquidazione() {
		return elencoLiquidazione;
	}
	public IstruttoriaDomandaUnica setElencoLiquidazione(SintesiElencoLiquidazione elencoLiquidazione) {
		this.elencoLiquidazione = elencoLiquidazione;
		return this;
	}
	public Boolean getIsBloccata() {
		return isBloccata;
	}
	public IstruttoriaDomandaUnica setIsBloccata(Boolean bloccataBool) {
		this.isBloccata = bloccataBool;
		return this;
	}
	public Boolean getIsErroreCalcolo() {
		return isErroreCalcolo;
	}
	public IstruttoriaDomandaUnica setIsErroreCalcolo(Boolean erroreCalcolo) {
		this.isErroreCalcolo = erroreCalcolo;
		return this;
	}
	public LocalDateTime getDataUltimoCalcolo() {
		return dataUltimoCalcolo;
	}
	public IstruttoriaDomandaUnica setDataUltimoCalcolo(LocalDateTime dataUltimoCalcolo) {
		this.dataUltimoCalcolo = dataUltimoCalcolo;
		return this;
	}
}
