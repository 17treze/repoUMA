package it.tndigitale.a4gistruttoria.dto.istruttoria;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SintesiElencoLiquidazione implements Serializable {

	private static final long serialVersionUID = 5850387200515119913L;

	private Long id;
	private String codElenco;
	private LocalDateTime dtCreazione;
	
	public Long getId() {
		return id;
	}
	public String getCodElenco() {
		return codElenco;
	}
	public LocalDateTime getDtCreazione() {
		return dtCreazione;
	}
	public SintesiElencoLiquidazione setId(Long id) {
		this.id = id;
		return this;
	}
	public SintesiElencoLiquidazione setCodElenco(String codElenco) {
		this.codElenco = codElenco;
		return this;
	}
	public SintesiElencoLiquidazione setDtCreazione(LocalDateTime dtCreazione) {
		this.dtCreazione = dtCreazione;
		return this;
	}	
}
