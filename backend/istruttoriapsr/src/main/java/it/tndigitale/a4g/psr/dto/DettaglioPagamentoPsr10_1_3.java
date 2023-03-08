package it.tndigitale.a4g.psr.dto;

import java.util.List;

public class DettaglioPagamentoPsr10_1_3 implements DettaglioPagamentoPsr {
  private List<DettaglioRazzaDettaglioPagamentoPsr> dettaglioRazze;
  private EsitoFinaleDettaglioPagamentoPsr10_1_3 esitoFinaleDettaglioPagamentoPsr;

  public DettaglioPagamentoPsr10_1_3(List<DettaglioRazzaDettaglioPagamentoPsr> dettaglioRazze, EsitoFinaleDettaglioPagamentoPsr10_1_3 esitoFinaleDettaglioPagamentoPsr) {
    this.dettaglioRazze = dettaglioRazze;
    this.esitoFinaleDettaglioPagamentoPsr = esitoFinaleDettaglioPagamentoPsr;
  }

  public List<DettaglioRazzaDettaglioPagamentoPsr> getDettaglioRazze() {
    return dettaglioRazze;
  }

  public void setDettaglioRazze(List<DettaglioRazzaDettaglioPagamentoPsr> dettaglioRazze) {
    this.dettaglioRazze = dettaglioRazze;
  }

  public EsitoFinaleDettaglioPagamentoPsr10_1_3 getEsitoFinaleDettaglioPagamentoPsr() {
    return esitoFinaleDettaglioPagamentoPsr;
  }

  public void setEsitoFinaleDettaglioPagamentoPsr(EsitoFinaleDettaglioPagamentoPsr10_1_3 esitoFinaleDettaglioPagamentoPsr) {
    this.esitoFinaleDettaglioPagamentoPsr = esitoFinaleDettaglioPagamentoPsr;
  }
}
