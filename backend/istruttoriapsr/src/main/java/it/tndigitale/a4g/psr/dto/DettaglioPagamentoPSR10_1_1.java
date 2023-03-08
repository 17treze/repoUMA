package it.tndigitale.a4g.psr.dto;

public class DettaglioPagamentoPSR10_1_1 implements DettaglioPagamentoPsr{
  private final InterventoPsr10_1_1 interventoBase;
  private final InterventoPsr10_1_1 interventoMagg;
  private final EsitoFinaleDettaglioPagamentoPsr10_1_1 esitoFinale;

  public DettaglioPagamentoPSR10_1_1(InterventoPsr10_1_1 interventoBase, InterventoPsr10_1_1 interventoMagg, EsitoFinaleDettaglioPagamentoPsr10_1_1 esitoFinale) {
    this.interventoBase = interventoBase;
    this.interventoMagg = interventoMagg;
    this.esitoFinale = esitoFinale;
  }

  public InterventoPsr10_1_1 getInterventoBase() {
    return interventoBase;
  }

  public InterventoPsr10_1_1 getInterventoMagg() {
    return interventoMagg;
  }

  public EsitoFinaleDettaglioPagamentoPsr10_1_1 getEsitoFinale() {
    return esitoFinale;
  }
}
