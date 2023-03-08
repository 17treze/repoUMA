package it.tndigitale.a4g.fascicolo.anagrafica.dto.caa;

import java.util.List;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.IndirizzoDto;

public class CentroAssistenzaAgricolaDto {

	private Long id;
	private String denominazione;
	private IndirizzoDto indirizzo;
	private String codiceFiscale;
	private String partitaIVA;
	private String estremiAtto;
	private String societaServizi;
	private List<SportelloCAADto> sportelli;

	public Long getId() {
		return id;
	}

	public CentroAssistenzaAgricolaDto setId(Long id) {
		this.id = id;
		return this;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public CentroAssistenzaAgricolaDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public IndirizzoDto getIndirizzo() {
		return indirizzo;
	}

	public CentroAssistenzaAgricolaDto setIndirizzo(IndirizzoDto indirizzo) {
		this.indirizzo = indirizzo;
		return this;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public CentroAssistenzaAgricolaDto setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getPartitaIVA() {
		return partitaIVA;
	}

	public CentroAssistenzaAgricolaDto setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
		return this;
	}

	public String getEstremiAtto() {
		return estremiAtto;
	}

	public CentroAssistenzaAgricolaDto setEstremiAtto(String estremiAtto) {
		this.estremiAtto = estremiAtto;
		return this;
	}

	public String getSocietaServizi() {
		return societaServizi;
	}

	public CentroAssistenzaAgricolaDto setSocietaServizi(String societaServizi) {
		this.societaServizi = societaServizi;
		return this;
	}

	public List<SportelloCAADto> getSportelli() {
		return sportelli;
	}

	public CentroAssistenzaAgricolaDto setSportelli(List<SportelloCAADto> sportelli) {
		this.sportelli = sportelli;
		return this;
	}
}
