package it.tndigitale.a4g.ags.dto;

import java.util.Date;

public class AnagraficaAziendaDocumentData {

    private final int tipoDocumento;
    private final String numeroDocumento;
    private final Date dataDocumento;
    private final Date dataScadDocumento;

    public AnagraficaAziendaDocumentData(int tipoDocumento, String numeroDocumento,
                                         Date dataDocumento, Date dataScadDocumento) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.dataDocumento = dataDocumento;
        this.dataScadDocumento = dataScadDocumento;
    }

    public int getTipoDocumento() {
        return tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public Date getDataDocumento() {
        return dataDocumento;
    }

    public Date getDataScadDocumento() {
        return dataScadDocumento;
    }
}
