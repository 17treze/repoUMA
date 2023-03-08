package it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Ente {
	private Long id;
	private Long codice;
	private String descrizione;

	public Long getCodice() {
		return codice;
	}
	public void setCodice(Long codice) {
		this.codice = codice;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
