package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.SportelloCAADto;

public class RichiestaRevocaImmediataDto  implements Serializable {

	private Long id;

	private String codiceFiscale;

	private String codiceFiscaleRappresentante;

	private Long identificativoSportello;

	private ByteArrayResource richiestaRevocaImmediataFirmata;

	private String motivazioneRifiuto;

	private List<ByteArrayResource> allegati;

	private String causaRichiesta;

	private LocalDate dataValutazione;

	private SportelloCAADto sportello;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public RichiestaRevocaImmediataDto setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getCodiceFiscaleRappresentante() {
		return codiceFiscaleRappresentante;
	}

	public RichiestaRevocaImmediataDto setCodiceFiscaleRappresentante(String codiceFiscaleRappresentante) {
		this.codiceFiscaleRappresentante = codiceFiscaleRappresentante;
		return this;
	}

	public Long getIdentificativoSportello() {
		return identificativoSportello;
	}

	public RichiestaRevocaImmediataDto setIdentificativoSportello(Long identificativoSportello) {
		this.identificativoSportello = identificativoSportello;
		return this;
	}

	public ByteArrayResource getRichiestaRevocaImmediataFirmata() {
		return richiestaRevocaImmediataFirmata;
	}

	public RichiestaRevocaImmediataDto setRichiestaRevocaImmediataFirmata(ByteArrayResource richiestaRevocaImmediataFirmata) {
		this.richiestaRevocaImmediataFirmata = richiestaRevocaImmediataFirmata;
		return this;
	}

	public String getMotivazioneRifiuto() {
		return motivazioneRifiuto;
	}

	public RichiestaRevocaImmediataDto setMotivazioneRifiuto(String motivazioneRifiuto) {
		this.motivazioneRifiuto = motivazioneRifiuto;
		return this;
	}

	public List<ByteArrayResource> getAllegati() {
		return allegati;
	}

	public RichiestaRevocaImmediataDto setAllegati(List<ByteArrayResource> allegati) {
		this.allegati = allegati;
		return this;
	}

	public String getCausaRichiesta() {
		return causaRichiesta;
	}

	public RichiestaRevocaImmediataDto setCausaRichiesta(String causaRichiesta) {
		this.causaRichiesta = causaRichiesta;
		return this;
	}

	public LocalDate getDataValutazione() {
		return dataValutazione;
	}

	public RichiestaRevocaImmediataDto setDataValutazione(LocalDate dataValutazione) {
		this.dataValutazione = dataValutazione;
		return this;
	}

	public SportelloCAADto getSportello() {
		return sportello;
	}

	public RichiestaRevocaImmediataDto setSportello(SportelloCAADto sportello) {
		this.sportello = sportello;
		return this;
	}
}
