package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import java.io.Serializable;
import java.util.List;

public abstract class PersonaDto implements Serializable {
	private static final long serialVersionUID = -4592781880419160280L;
	
	private String codiceFiscale;
	
	private List<IscrizioneSezioneDto> iscrizioniSezione;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public List<IscrizioneSezioneDto> getIscrizioniSezione() {
		return iscrizioniSezione;
	}

	public void setIscrizioniSezione(List<IscrizioneSezioneDto> iscrizioniSezione) {
		this.iscrizioniSezione = iscrizioniSezione;
	}
}
