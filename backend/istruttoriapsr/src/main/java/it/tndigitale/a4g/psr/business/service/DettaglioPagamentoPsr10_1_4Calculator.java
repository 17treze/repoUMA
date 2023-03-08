package it.tndigitale.a4g.psr.business.service;

import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPsr;
import it.tndigitale.a4g.psr.dto.DettaglioPagametoPsrRow;
import it.tndigitale.a4g.psr.dto.EsitoFinaleDettaglioPagamentoPsr10_1_4;

import java.math.BigDecimal;
import java.util.List;

import static it.tndigitale.a4g.psr.business.service.DettaglioPagamentoUtils.*;
import static it.tndigitale.a4g.psr.business.service.DettaglioPagamentoUtils.getValueOrDefault;

public class DettaglioPagamentoPsr10_1_4Calculator implements DettaglioPagamentoPsrCalculator {

  @Override
  public DettaglioPagamentoPsr calculate(List<DettaglioPagametoPsrRow> dettaglioPagamento, boolean isCampione) {
      final BigDecimal supric = findValoreByRowValue(dettaglioPagamento, "SUPRIC").getValoreAsBigDecimal();
      final BigDecimal supamm = findValoreByRowValue(dettaglioPagamento, "SUPAMM").getValoreAsBigDecimal();
      final BigDecimal percscost = findValoreByRowValue(dettaglioPagamento, "PERCSCOST").getValoreAsBigDecimal();
      final BigDecimal premiotot = findValoreByRowValue(dettaglioPagamento, "PREMIOTOT").getValoreAsBigDecimal();
      final BigDecimal premiopostsanz = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOPOSTSANZ").getValoreAsBigDecimal();
      final BigDecimal percedur = findValoreByRowValue(dettaglioPagamento, "PERCDECUR").getValoreAsBigDecimal();
      final BigDecimal coeffbudget = findValoreByRowValue(dettaglioPagamento, "COEFFBUDGET").getValoreAsBigDecimal();
      final BigDecimal premiototdecur = findValoreByRowValue(dettaglioPagamento, "PREMIOTOTDECUR").getValoreAsBigDecimal();
      BigDecimal premiototliquidato = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTLIQUIDATO").getValoreAsBigDecimal();
      BigDecimal premiototintegr = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTINTEGR").getValoreAsBigDecimal();
      final BigDecimal persanzloco = findValoreByRowValueNotRequired(dettaglioPagamento, "PERSANZLOCO").getValoreAsBigDecimal();
      final BigDecimal persanzfito = findValoreByRowValueNotRequired(dettaglioPagamento, "PERSANZFITO").getValoreAsBigDecimal();
      final BigDecimal impsanztot = findValoreByRowValueNotRequired(dettaglioPagamento, "IMPSANZTOT").getValoreAsBigDecimal();
      BigDecimal importoGiaLiquidato = getValueOrDefault(premiototliquidato, BigDecimal.ZERO);

      return new EsitoFinaleDettaglioPagamentoPsr10_1_4(supric, supamm, calculatePercscost(percscost), new BigDecimal(250),
          calculateImportoAmmissibile(supamm),
          calculateImportoSanzione(supamm, premiotot, percscost),
          calculateImportoRiduzionePerRitardataPresentazione(isCampione ? premiopostsanz : premiotot, percedur),
          calculateCoefficienteRiduzioneSuperamentoBudget(coeffbudget),
          importoGiaLiquidato.equals(BigDecimal.ZERO) ? BigDecimal.ZERO : premiototdecur,
          importoGiaLiquidato, getValueOrDefault(premiototintegr, premiototdecur), persanzloco, persanzfito, impsanztot
      );
  }

  private BigDecimal calculatePercscost(BigDecimal percscost) {
    if (percscostShouldBeConsidered(percscost))
      return percscost;
    else
      return null;
  }

  private boolean percscostShouldBeConsidered(BigDecimal percscost) {
    return percscost.compareTo(new BigDecimal(3)) > 0;
  }

  private BigDecimal calculateCoefficienteRiduzioneSuperamentoBudget(BigDecimal coeffbudget) {
    return (BigDecimal.ONE.subtract(coeffbudget)).multiply(new BigDecimal(100));
  }

  private BigDecimal calculateImportoRiduzionePerRitardataPresentazione(BigDecimal premiotot, BigDecimal percedur) {
    return premiotot.multiply(percedur.divide(new BigDecimal(100)));
  }

  private BigDecimal calculateImportoSanzione(BigDecimal supamm, BigDecimal premiotot, BigDecimal PERCSCOST) {
    if (percscostShouldBeConsidered(PERCSCOST))
      return (supamm.multiply(new BigDecimal(250))).subtract(premiotot);
    else
      return null;
  }

  private BigDecimal calculateImportoAmmissibile(BigDecimal supamm) {
    return supamm.multiply(new BigDecimal(250));
  }
}
