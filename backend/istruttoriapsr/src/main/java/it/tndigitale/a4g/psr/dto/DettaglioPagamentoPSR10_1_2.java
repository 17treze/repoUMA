package it.tndigitale.a4g.psr.dto;

import java.util.List;

public class DettaglioPagamentoPSR10_1_2 implements DettaglioPagamentoPsr{

  private final List<DettaglioMalga> dettaglioMalghe;
  private EsitoFinaleDettaglioPagamentoPsr10_1_2 esitoFinale;

  public DettaglioPagamentoPSR10_1_2(List<DettaglioMalga> dettaglioMalghe, EsitoFinaleDettaglioPagamentoPsr10_1_2 esitoFinale) {
    this.dettaglioMalghe = dettaglioMalghe;
    this.esitoFinale = esitoFinale;
  }

  public List<DettaglioMalga> getDettaglioMalghe() {
    return dettaglioMalghe;
  }

  public EsitoFinaleDettaglioPagamentoPsr10_1_2 getEsitoFinale() {
    return esitoFinale;
  }
}
