package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;

public class ApriFascicoloDto {

	private String codiceFiscale;

	private String codiceFiscaleRappresentante;

	private Long identificativoSportello;

	private ByteArrayResource contratto;

	private List<ByteArrayResource> allegati;

	private LocalDate dataAperturaFascicolo;
	
	private LocalDate dataInizioMandato;
	
	private LocalDate dataFineMandato;
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public ApriFascicoloDto setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}
	public String getCodiceFiscaleRappresentante() {
		return codiceFiscaleRappresentante;
	}
	public ApriFascicoloDto setCodiceFiscaleRappresentante(String codiceFiscaleRappresentante) {
		this.codiceFiscaleRappresentante = codiceFiscaleRappresentante;
		return this;
	}
	public Long getIdentificativoSportello() {
		return identificativoSportello;
	}
	public ApriFascicoloDto setIdentificativoSportello(Long identificativoSportello) {
		this.identificativoSportello = identificativoSportello;
		return this;
	}
	public ByteArrayResource getContratto() {
		return contratto;
	}
	public ApriFascicoloDto setContratto(ByteArrayResource contratto) {
		this.contratto = contratto;
		return this;
	}
	public List<ByteArrayResource> getAllegati() {
		return allegati;
	}
	public ApriFascicoloDto setAllegati(List<ByteArrayResource> allegati) {
		this.allegati = allegati;
		return this;
	}

	public LocalDate getDataAperturaFascicolo() {
		return dataAperturaFascicolo;
	}
	public void setDataAperturaFascicolo(LocalDate dataAperturaFascicolo) {
		this.dataAperturaFascicolo = dataAperturaFascicolo;
	}
	public LocalDate getDataInizioMandato() {
		return dataInizioMandato;
	}
	public void setDataInizioMandato(LocalDate dataInizioMandato) {
		this.dataInizioMandato = dataInizioMandato;
	}
	public LocalDate getDataFineMandato() {
		return dataFineMandato;
	}
	public void setDataFineMandato(LocalDate dataFineMandato) {
		this.dataFineMandato = dataFineMandato;
	}

}
