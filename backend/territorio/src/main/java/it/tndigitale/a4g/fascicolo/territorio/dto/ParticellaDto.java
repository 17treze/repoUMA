package it.tndigitale.a4g.fascicolo.territorio.dto;

import java.util.List;
import java.util.Optional;

public class ParticellaDto {

	private String numero;
	private String codiceNazionale;
	private Optional<String> subalterno;
	private String foglio;
	private ConduzioneDto conduzioneDto;
	private List<ColturaDto> colture;

	public String getNumero() {
		return numero;
	}
	public ParticellaDto setNumero(String numero) {
		this.numero = numero;
		return this;
	}
	public String getCodiceNazionale() {
		return codiceNazionale;
	}
	public ParticellaDto setCodiceNazionale(String codiceNazionale) {
		this.codiceNazionale = codiceNazionale;
		return this;
	}
	public Optional<String> getSubalterno() {
		return subalterno;
	}
	public ParticellaDto setSubalterno(Optional<String> subalterno) {
		this.subalterno = subalterno;
		return this;
	}
	public String getFoglio() {
		return foglio;
	}
	public ParticellaDto setFoglio(String foglio) {
		this.foglio = foglio;
		return this;
	}
	public List<ColturaDto> getColture() {
		return colture;
	}
	public ParticellaDto setColture(List<ColturaDto> coltureDto) {
		this.colture = coltureDto;
		return this;
	}
	public ParticellaDto addColtura(ColturaDto coltura) {
		this.colture.add(coltura);			
		return this;
	}
	public ConduzioneDto getConduzioneDto() {
		return conduzioneDto;
	}
	public ParticellaDto setConduzioneDto(ConduzioneDto conduzioneDto) {
		this.conduzioneDto = conduzioneDto;
		return this;
	}
}
