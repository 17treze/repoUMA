package it.tndigitale.a4g.proxy.dto.persona;

import java.util.List;

public class PersonaFisicaConCaricaDto {
    private List<CaricaDto> listaCarica;

    private String codiceFiscale;

    private AnagraficaDto anagrafica;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public AnagraficaDto getAnagrafica() {
		return anagrafica;
	}

	public void setAnagrafica(AnagraficaDto anagrafica) {
		this.anagrafica = anagrafica;
	}

	public List<CaricaDto> getListaCarica() {
		return listaCarica;
	}

	public void setListaCarica(List<CaricaDto> listaCarica) {
		this.listaCarica = listaCarica;
	}
}
