package it.tndigitale.a4g.ags.dto;

import java.util.Date;

public class AnagraficaAziendaFascicoloData {

    private final Date dataValidazFascicolo;

    public AnagraficaAziendaFascicoloData(Date dataValidazFascicolo) {
        this.dataValidazFascicolo = dataValidazFascicolo;
    }

    public Date getDataValidazFascicolo() {
        return dataValidazFascicolo;
    }
}
