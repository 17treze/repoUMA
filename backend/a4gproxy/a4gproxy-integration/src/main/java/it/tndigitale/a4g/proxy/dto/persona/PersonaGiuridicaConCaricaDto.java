package it.tndigitale.a4g.proxy.dto.persona;

import java.util.List;

public class PersonaGiuridicaConCaricaDto {

    private String codiceFiscale;
    
    private String denominazione;
    
    private List<CaricaDto> listaCarica;


	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public List<CaricaDto> getListaCarica() {
		return listaCarica;
	}

	public void setListaCarica(List<CaricaDto> listaCarica) {
		this.listaCarica = listaCarica;
	}
}
