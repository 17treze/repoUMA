package it.tndigitale.a4g.psr.dto;

import java.util.List;

public class DettaglioPagamentoPSR13_1_1 implements DettaglioPagamentoPsr {

    private final DatiAziendali datiAziendali;
    private final List<SistemaAgricolo> sistemiAgricoli;
    private final GestionePluriennaleSanzioni gestionePluriennaleSanzioni;
    private final CalcoloDegressivitaPremio calcoloDegressivitaPremio;
    private final EsitoFinaleDettaglioPagamentoEsitoPsr13_1_1 esitoFinale;
    private RiduzioniControlloInLoco riduzioniControlloInLoco;

    public DettaglioPagamentoPSR13_1_1(DatiAziendali datiAziendali, List<SistemaAgricolo> sistemiAgricoli, GestionePluriennaleSanzioni gestionePluriennaleSanzioni, CalcoloDegressivitaPremio calcoloDegressivitaPremio, EsitoFinaleDettaglioPagamentoEsitoPsr13_1_1 esitoFinale) {
        this.datiAziendali = datiAziendali;
        this.sistemiAgricoli = sistemiAgricoli;
        this.gestionePluriennaleSanzioni = gestionePluriennaleSanzioni;
        this.calcoloDegressivitaPremio = calcoloDegressivitaPremio;
        this.esitoFinale = esitoFinale;
    }

    public DatiAziendali getDatiAziendali() {
        return datiAziendali;
    }

    public List<SistemaAgricolo> getSistemiAgricoli() {
        return sistemiAgricoli;
    }

    public GestionePluriennaleSanzioni getGestionePluriennaleSanzioni() {
        return gestionePluriennaleSanzioni;
    }

    public CalcoloDegressivitaPremio getCalcoloDegressivitaPremio() {
        return calcoloDegressivitaPremio;
    }

    public EsitoFinaleDettaglioPagamentoEsitoPsr13_1_1 getEsitoFinale() {
        return esitoFinale;
    }

    public void setRiduzioniControlloInLoco(RiduzioniControlloInLoco riduzioniControlloInLoco) {
        this.riduzioniControlloInLoco = riduzioniControlloInLoco;
    }

    public RiduzioniControlloInLoco getRiduzioniControlloInLoco() {
        return riduzioniControlloInLoco;
    }
}
