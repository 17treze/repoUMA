package it.tndigitale.a4g.psr.business.service;
import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPSR10_1_1;
import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPsr;
import it.tndigitale.a4g.psr.dto.DettaglioPagametoPsrRow;
import it.tndigitale.a4g.psr.dto.EsitoFinaleDettaglioPagamentoPsr10_1_1;
import it.tndigitale.a4g.psr.dto.InterventoPsr10_1_1;

import java.math.BigDecimal;
import java.util.List;

import static it.tndigitale.a4g.psr.business.service.DettaglioPagamentoUtils.*;

public class DettaglioPagamentoPsr10_1_1Calculator implements DettaglioPagamentoPsrCalculator {

  @Override
  public DettaglioPagamentoPsr calculate(List<DettaglioPagametoPsrRow> dettaglioPagamento, boolean isCampione) {
      final BigDecimal rhue = findValoreByRowValue(dettaglioPagamento, "RUHE").getValoreAsBigDecimal();
      final BigDecimal sricbasetot = findValoreByRowValue(dettaglioPagamento, "SRICBASETOT").getValoreAsBigDecimal();
      final BigDecimal supammbase = findValoreByRowValue(dettaglioPagamento, "SUPAMMBASE").getValoreAsBigDecimal();
      final BigDecimal percscosbase = findValoreByRowValue(dettaglioPagamento, "PERCSCOSBASE").getValoreAsBigDecimal();
      final BigDecimal impliqbase = findValoreByRowValue(dettaglioPagamento, "IMPLIQBASE").getValoreAsBigDecimal();
      final BigDecimal sricmagg = findValoreByRowValue(dettaglioPagamento, "SRICMAGG").getValoreAsBigDecimal();
      final BigDecimal supammmagg = findValoreByRowValue(dettaglioPagamento, "SUPAMMMAGG").getValoreAsBigDecimal();
      final BigDecimal percscosmagg = findValoreByRowValue(dettaglioPagamento, "PERCSCOSMAGG").getValoreAsBigDecimal();
      final BigDecimal impliqmagg = findValoreByRowValue(dettaglioPagamento, "IMPLIQMAGG").getValoreAsBigDecimal();
      final BigDecimal premiotot = findValoreByRowValue(dettaglioPagamento, "PREMIOTOT").getValoreAsBigDecimal();
      final BigDecimal persanzistr = findValoreByRowValueNotRequired(dettaglioPagamento, "PERSANZISTR").getValoreAsBigDecimal();
      final BigDecimal impsanzistr = findValoreByRowValueNotRequired(dettaglioPagamento, "IMPSANZISTR").getValoreAsBigDecimal();
      final BigDecimal percdecur = findValoreByRowValue(dettaglioPagamento, "PERCDECUR").getValoreAsBigDecimal();
      final BigDecimal coeffbudget = findValoreByRowValue(dettaglioPagamento, "COEFFBUDGET").getValoreAsBigDecimal();
      final BigDecimal premiototdecur = findValoreByRowValue(dettaglioPagamento, "PREMIOTOTDECUR").getValoreAsBigDecimal();
      final BigDecimal premiototliquidato = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTLIQUIDATO").getValoreAsBigDecimal();
      final BigDecimal premiototintegr = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTINTEGR").getValoreAsBigDecimal();


      final InterventoPsr10_1_1 premioBase = buildIntervento(
          "Intervento Base",
          rhue,
          sricbasetot,
          supammbase,
          percscosbase,
          impliqbase,
          calculateAliquotaSostegnoBase(rhue, 330, 280, 180));

      final InterventoPsr10_1_1 premioMagg = buildIntervento(
          "Premio base più premio aggiuntivo per lo sfalcio tardivo delle zone Zone Natura 2000 e dei prati ricchi di specie",
          rhue,
          sricmagg,
          supammmagg,
          percscosmagg,
          impliqmagg,
          calculateAliquotaSostegnoBase(rhue, 430, 380, 280));

      if (isCampione) {
          final BigDecimal ruheacc = findValoreByRowValueNotRequired(dettaglioPagamento, "RUHEACC").getValoreAsBigDecimal();
          premioBase.setCaricoBestiameAccertatoControllo(ruheacc);
          premioMagg.setCaricoBestiameAccertatoControllo(ruheacc);
      }

      final BigDecimal persanzloco = findValoreByRowValueNotRequired(dettaglioPagamento, "PERSANZLOCO").getValoreAsBigDecimal();
      final BigDecimal persanzfito = findValoreByRowValueNotRequired(dettaglioPagamento, "PERSANZFITO").getValoreAsBigDecimal();
      final BigDecimal impsanztot = findValoreByRowValueNotRequired(dettaglioPagamento, "IMPSANZTOT").getValoreAsBigDecimal();
      final BigDecimal premiopostsanz = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOPOSTSANZ").getValoreAsBigDecimal();
      final EsitoFinaleDettaglioPagamentoPsr10_1_1 esitoFinale = calcualteEsitoFinale(isCampione, premiotot, persanzistr, impsanzistr, percdecur, coeffbudget, premiototdecur, persanzloco, persanzfito, premiototliquidato, premiototintegr, impsanztot, premiopostsanz);

      return new DettaglioPagamentoPSR10_1_1(premioBase, premioMagg, esitoFinale);
  }

  private EsitoFinaleDettaglioPagamentoPsr10_1_1 calcualteEsitoFinale(boolean isCampione, BigDecimal premiotot, BigDecimal persanzistr, BigDecimal impsanzistr, BigDecimal percdecur, BigDecimal coeffbudget, BigDecimal premiototdecur, BigDecimal persanzloco, BigDecimal persanzfito, BigDecimal premiototliquidato, BigDecimal premiototintegr, BigDecimal impsanztot, BigDecimal premiopostsanz) {
    BigDecimal importoRiduzioneMancatoRispettoImpegni = isCampione ? impsanztot : premiotot.multiply(persanzistr);
    BigDecimal importoRiduzioneRitardataPresentazione = isCampione ? premiopostsanz.multiply((percdecur.divide(new BigDecimal(100)))) : impsanzistr.multiply((percdecur.divide(new BigDecimal(100))));
    BigDecimal coefficienteRiduzioneSuperamentoBudget =  (BigDecimal.ONE.subtract(coeffbudget)).multiply(new BigDecimal(100));
    BigDecimal importoGiaLiquidatoPagamentiPrecedenti = getValueOrDefault(premiototliquidato, BigDecimal.ZERO);
    BigDecimal importoCalcolato = BigDecimal.ZERO.equals(importoGiaLiquidatoPagamentiPrecedenti) ? BigDecimal.ZERO : getValueOrDefault(premiototdecur, BigDecimal.ZERO);
    BigDecimal importoCalcolatoLiquidato = getValueOrDefault(premiototintegr, premiototdecur);
    BigDecimal percentualeRiduzioneMancatoRispettoImpegniSpecifici = isCampione ? persanzloco : null;
    BigDecimal percentualeRiduzioneMancatoRispettoImpegniBaseline = isCampione ? persanzfito : null;

    final EsitoFinaleDettaglioPagamentoPsr10_1_1 esitoFinale = new EsitoFinaleDettaglioPagamentoPsr10_1_1(importoRiduzioneMancatoRispettoImpegni, importoRiduzioneRitardataPresentazione, coefficienteRiduzioneSuperamentoBudget, importoCalcolato, importoGiaLiquidatoPagamentiPrecedenti, importoCalcolatoLiquidato, percentualeRiduzioneMancatoRispettoImpegniSpecifici, percentualeRiduzioneMancatoRispettoImpegniBaseline);
    return esitoFinale;
  }


  private InterventoPsr10_1_1 buildIntervento(String title, BigDecimal caricoBestiame, BigDecimal superficieRichiesta, BigDecimal superficieAmmissibile, BigDecimal percentualeScostamento, BigDecimal importoCalcolatoIntervento, BigDecimal aliquotaSostegno) {
    if(superficieRichiesta.compareTo(BigDecimal.ZERO)>0){
      BigDecimal importoAmissibile = superficieAmmissibile.multiply(aliquotaSostegno);
      BigDecimal importoSanzioneSovradichiarazione =  importoAmissibile.subtract(importoCalcolatoIntervento);

     return new InterventoPsr10_1_1(title, caricoBestiame, superficieRichiesta, superficieAmmissibile, percentualeScostamento, aliquotaSostegno, importoAmissibile, importoSanzioneSovradichiarazione, importoCalcolatoIntervento);

    }else {
     return new InterventoPsr10_1_1();
    }
  }

  private BigDecimal calculateAliquotaSostegnoBase(BigDecimal rhue, int firstValue, int secondValue, int thirdValue) {
    if (rhue.compareTo(new BigDecimal("0.3")) >= 0 && rhue.compareTo(new BigDecimal("1.5")) <= 0){
      return new BigDecimal(firstValue);
    }
    if (rhue.compareTo(new BigDecimal("1.5")) > 0 && rhue.compareTo(new BigDecimal("2")) <= 0){
      return new BigDecimal(secondValue);
    }
    if (rhue.compareTo(new BigDecimal("2")) > 0 && rhue.compareTo(new BigDecimal("2.6")) <= 0){
      return new BigDecimal(thirdValue);
    }
    return BigDecimal.ZERO;
  }

}
