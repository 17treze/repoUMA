package it.tndigitale.a4g.fascicolo.territorio.dto.conduzione;

import java.io.Serializable;

public class TipoConduzioneDto implements Serializable {
	private static final long serialVersionUID = -5190459625254433248L;
    private String codice;
    private String descrizione;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
