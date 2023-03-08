package it.tndigitale.a4g.territorio.dto;

import it.tndigitale.a4g.fascicolo.territorio.dto.TitoloConduzione;

import java.io.Serializable;
import java.util.List;

public class TipoConduzioneDto implements Serializable {

	private TitoloConduzione ambito;
	private List<SottotipoConduzioneDto> sottotipo;

	public TitoloConduzione getAmbito() {
		return ambito;
	}

	public void setAmbito(TitoloConduzione ambito) {
		this.ambito = ambito;
	}

	public List<SottotipoConduzioneDto> getSottotipo() {
		return sottotipo;
	}

	public void setSottotipo(List<SottotipoConduzioneDto> sottotipo) {
		this.sottotipo = sottotipo;
	}
}
