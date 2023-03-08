package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import java.io.Serializable;
import java.time.LocalDate;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CaricaModel;

public class CaricaDto implements Serializable {

    private static final long serialVersionUID = 6148423005569139744L;

	private String descrizione;
    
    private String identificativo;
    
    private LocalDate dataInizio;
    
    private boolean isFirmatario;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public LocalDate getDataInizio() {
		return dataInizio;
	}

	public CaricaDto setDataInizio(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
		return this;
	}
	
	public boolean isFirmatario() {
		return isFirmatario;
	}

	public void setFirmatario(boolean isFirmatario) {
		this.isFirmatario = isFirmatario;
	}

	public static CaricaDto toDto(final CaricaModel model) {
		CaricaDto c = new CaricaDto();
		c.setDescrizione(model.getDescrizione());
		c.setIdentificativo(model.getIdentificativo());
		c.setDataInizio(model.getDataInizio());
		c.setFirmatario(model.isFirmatario().booleanValue());
		return c;
	}
}
