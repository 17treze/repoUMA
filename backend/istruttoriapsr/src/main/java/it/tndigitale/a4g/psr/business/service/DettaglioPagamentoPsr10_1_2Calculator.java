package it.tndigitale.a4g.psr.business.service;

import it.tndigitale.a4g.psr.dto.DettaglioMalga;
import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPSR10_1_2;
import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPsr;
import it.tndigitale.a4g.psr.dto.DettaglioPagametoPsrRow;
import it.tndigitale.a4g.psr.dto.EsitoFinaleDettaglioPagamentoPsr10_1_2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static it.tndigitale.a4g.psr.business.service.DettaglioPagamentoUtils.*;
import static java.math.BigDecimal.ZERO;

public class DettaglioPagamentoPsr10_1_2Calculator implements DettaglioPagamentoPsrCalculator {

  @Override
  public DettaglioPagamentoPsr calculate(List<DettaglioPagametoPsrRow> dettaglioPagamento, boolean isCampione) {
    final BigDecimal premioinf15Uba = findValoreByRowValue(dettaglioPagamento, "PREMIOINF15UBA").getValoreAsBigDecimal();
    final BigDecimal premiosup15Uba = findValoreByRowValue(dettaglioPagamento, "PREMIOSUP15UBA").getValoreAsBigDecimal();

    final long numeroMalghe = dettaglioPagamento.stream()
        .filter(dettaglioPagametPsrRow -> dettaglioPagametPsrRow.getVariabile().contains("RUHEMALGA"))
        .count();

    List<DettaglioMalga> dettaglioMalghe = new ArrayList<>();

    for (int i = 1; i <= numeroMalghe; i++) {
      final BigDecimal superficieRichiesta = findValoreByRowValue(dettaglioPagamento, "SUPRICMALGA" + i).getValoreAsBigDecimal();
      if (superficieRichiesta.compareTo(ZERO) > 0) {
        dettaglioMalghe.add(calculateDettaglioMalga(isCampione, dettaglioPagamento, premioinf15Uba, premiosup15Uba, i));
      }
    }

    final BigDecimal premioTot = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOT").getValoreAsBigDecimal();
    final BigDecimal percDecur = findValoreByRowValue(dettaglioPagamento, "PERCDECUR").getValoreAsBigDecimal();
    final BigDecimal coeffBudget = findValoreByRowValue(dettaglioPagamento, "COEFFBUDGET").getValoreAsBigDecimal();
    final BigDecimal premioTotDecur = findValoreByRowValue(dettaglioPagamento, "PREMIOTOTDECUR").getValoreAsBigDecimal();

    final BigDecimal premioTotLiquidato = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTLIQUIDATO").getValoreAsBigDecimal();
    final BigDecimal premioAntiLiq = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOANTLIQ").getValoreAsBigDecimal();
    final BigDecimal premioTotIntegr = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTINTEGR").getValoreAsBigDecimal();

    final BigDecimal premioPostSanz = isCampione ? findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOPOSTSANZ").getValoreAsBigDecimal() : null;
    final BigDecimal persanzfito = isCampione ? findValoreByRowValueNotRequired(dettaglioPagamento, "PERSANZFITO").getValoreAsBigDecimal() : null;
    final BigDecimal impsanzfito = isCampione ? findValoreByRowValueNotRequired(dettaglioPagamento, "IMPSANZFITO").getValoreAsBigDecimal() : null;

    BigDecimal importoRiduzionePerRitardataPresentazione = isCampione ? premioPostSanz.multiply(percDecur.divide(new BigDecimal(100))) : premioTot.multiply(percDecur.divide(new BigDecimal(100)));
    BigDecimal coefficienteRiduzionePerSuperamentoBudget = BigDecimal.ONE.subtract(coeffBudget).multiply(new BigDecimal(100));
    BigDecimal importoCalcolato = calculateImportoCalcolato(premioTotLiquidato, premioAntiLiq, premioTotDecur);
    BigDecimal importoLiquidatoPrecedentemente = calculatePremioLiquidatoPrecedentemente(premioAntiLiq, premioTotLiquidato);
    BigDecimal importoCalcolatoLiquidato = getValueOrDefault(premioTotIntegr, premioTotDecur);
    final EsitoFinaleDettaglioPagamentoPsr10_1_2 esitoFinale = new EsitoFinaleDettaglioPagamentoPsr10_1_2(importoRiduzionePerRitardataPresentazione,
        coefficienteRiduzionePerSuperamentoBudget,
        importoCalcolato, importoLiquidatoPrecedentemente, importoCalcolatoLiquidato, premioAntiLiq, premioTotLiquidato, persanzfito, impsanzfito);

    return new DettaglioPagamentoPSR10_1_2(dettaglioMalghe, esitoFinale);
  }

  private DettaglioMalga calculateDettaglioMalga(boolean isCampione, List<DettaglioPagametoPsrRow> dettaglioPagamento, BigDecimal premioinf15Uba, BigDecimal premiosup15Uba, int index) {
    final String codiceAlpeggio = findValoreByRowValue(dettaglioPagamento, "CODICEMALGA" + index).getValoreAString();
    final BigDecimal caricoDiBestiameDellaMalga = findValoreByRowValue(dettaglioPagamento, "RUHEMALGA" + index).getValoreAsBigDecimal();
    final BigDecimal superficieRichiesta = findValoreByRowValue(dettaglioPagamento, "SUPRICMALGA" + index).getValoreAsBigDecimal();
    final BigDecimal superficieAmmissibile = findValoreByRowValue(dettaglioPagamento, "SUPAMMMALGA" + index).getValoreAsBigDecimal();
    final BigDecimal percentualeDiScostamento = findValoreByRowValue(dettaglioPagamento, "PERCSCOSTMALGA" + index).getValoreAsBigDecimal();
    final String ubalattmalga = findValoreByRowValue(dettaglioPagamento, "UBALATTMALGA" + index).getValoreAString();
    final BigDecimal importoCalcolatoMalga = findValoreByRowValueNotRequired(dettaglioPagamento, (isCampione ? "PREMIOPOSTSANZ" : "PREMIOTOTMALGA") + index).getValoreAsBigDecimal();
    final BigDecimal persanzlocom = isCampione ? findValoreByRowValueNotRequired(dettaglioPagamento, "PERSANZLOCOM" + index).getValoreAsBigDecimal() : null;
    final BigDecimal impsanzm = isCampione ? findValoreByRowValueNotRequired(dettaglioPagamento, "IMPSANZM" + index).getValoreAsBigDecimal() : null;

    BigDecimal aliquotaDiSostegno;

    if (ubalattmalga.equals("S")) {
      aliquotaDiSostegno = premiosup15Uba;
    } else {
      aliquotaDiSostegno = premioinf15Uba;
    }

    BigDecimal importoAmmissibile = superficieAmmissibile.multiply(aliquotaDiSostegno);
    BigDecimal importoDellaSanzionePerSovradichiarazione = importoAmmissibile.subtract(importoCalcolatoMalga);

    return new DettaglioMalga(codiceAlpeggio, caricoDiBestiameDellaMalga, superficieRichiesta, superficieAmmissibile, percentualeDiScostamento, aliquotaDiSostegno, importoAmmissibile, importoDellaSanzionePerSovradichiarazione, importoCalcolatoMalga, persanzlocom, impsanzm);
  }

  private BigDecimal calculateImportoCalcolato(BigDecimal premioTotLiquidato, BigDecimal premioAntiLiq, BigDecimal premioTotDecur) {
    if (getValueOrDefault(premioAntiLiq, ZERO).equals(ZERO) && getValueOrDefault(premioTotLiquidato, ZERO).equals(ZERO)) {
      return ZERO;
    }
    if (premioTotLiquidato == null) {
      return getValueOrDefault(premioAntiLiq, ZERO).add(premioTotDecur);
    }
    return premioTotDecur;
  }

  private BigDecimal calculatePremioLiquidatoPrecedentemente(BigDecimal premioAntiLiq, BigDecimal premioTotLiquidato) {
    if (getValueOrDefault(premioAntiLiq, ZERO).equals(ZERO) && getValueOrDefault(premioTotLiquidato, ZERO).equals(ZERO)) {
      return ZERO;
    }
    if (premioTotLiquidato == null) {
      return getValueOrDefault(premioAntiLiq, ZERO);
    }
    return getValueOrDefault(premioTotLiquidato, ZERO);
  }
}
