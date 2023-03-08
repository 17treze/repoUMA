package it.tndigitale.a4g.proxy.dto.catasto;

public class QualitaColturaDto {
    private InformazioniQualitaColturaDto dettaglio;
    private InformazioniRedditoDto reddito;

    public InformazioniQualitaColturaDto getDettaglio() {
        return dettaglio;
    }

    public void setDettaglio(InformazioniQualitaColturaDto dettaglio) {
        this.dettaglio = dettaglio;
    }

    public InformazioniRedditoDto getReddito() {
        return reddito;
    }

    public void setReddito(InformazioniRedditoDto reddito) {
        this.reddito = reddito;
    }
}
