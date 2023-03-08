package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import java.util.List;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaConCaricaModel;

public class PersonaGiuridicaConCaricaDto extends PersonaDto {
	private static final long serialVersionUID = -5463622992177114794L;

	private String denominazione;

    private String partitaIva;

    private List<CaricaDto> cariche;
    
	public String getDenominazione() {
		return denominazione;
	}

	public PersonaGiuridicaConCaricaDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public PersonaGiuridicaConCaricaDto setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
		return this;
	}
	
	public List<CaricaDto> getCariche() {
		return cariche;
	}

	public void setCariche(List<CaricaDto> cariche) {
		this.cariche = cariche;
	}

	public static PersonaGiuridicaConCaricaDto toDto(
			final PersonaGiuridicaConCaricaModel pgConCaricaModel) {
		PersonaGiuridicaConCaricaDto dto = new PersonaGiuridicaConCaricaDto();
		dto.setCodiceFiscale(pgConCaricaModel.getCodiceFiscale());
		dto.setPartitaIva(pgConCaricaModel.getCodiceFiscale());
		dto.setDenominazione(pgConCaricaModel.getDenominazione());
		return dto;
	}
}
