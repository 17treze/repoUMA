package it.tndigitale.a4g.psr.business.service;

import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPsr;
import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPsr10_1_3;
import it.tndigitale.a4g.psr.dto.DettaglioPagametoPsrRow;
import it.tndigitale.a4g.psr.dto.DettaglioRazzaDettaglioPagamentoPsr;
import it.tndigitale.a4g.psr.dto.EsitoFinaleDettaglioPagamentoPsr10_1_3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static it.tndigitale.a4g.psr.business.service.DettaglioPagamentoUtils.*;

public class DettaglioPagamentoPsr10_1_3Calculator implements DettaglioPagamentoPsrCalculator {
  final Razza bovinaRendena = new Razza(1, "Razza bovina rendena", new BigDecimal(400));
  final Razza grigioAlpina = new Razza(2, "Razza bovina grigio alpina", new BigDecimal(400));
  final Razza bovinaBrunaAlpinaOriginale = new Razza(3, "Razza bovina bruna alpina originale", new BigDecimal(400));
  final Razza ovineLamonTingolaFiemmeseoVillnosserSchalf = new Razza(45, "Razze ovine del tipo Lamon, Tingola, fiemmese o Villnosser Schaf", new BigDecimal(200));
  final Razza caprinaPezzataMochena = new Razza(6, "Razza caprina pezzata mochena", new BigDecimal(200));
  final Razza caprinaBiondaAdamello = new Razza(7, "Razza caprina Bionda dell’Adamello", new BigDecimal(200));
  final Razza equinaCavalloNorico = new Razza(8, "Razza equina Cavallo Norico", new BigDecimal(400));
  final Razza tpr = new Razza(9, "Razza equina Cavallo da tiro pesante rapido – TPR", new BigDecimal(400));

  @Override
  public DettaglioPagamentoPsr calculate(List<DettaglioPagametoPsrRow> dettaglioPagamento, boolean isCampione) {
      BigDecimal ruhe = findValoreByRowValueNotRequired(dettaglioPagamento, "RUHE").getValoreAsBigDecimal();
      if (ruhe == null) {
          ruhe = findValoreByRowValueNotRequired(dettaglioPagamento, "RHUE").getValoreAsBigDecimal();
      }
      BigDecimal ruheacc = findValoreByRowValueNotRequired(dettaglioPagamento, "RUHEACC").getValoreAsBigDecimal();
      BigDecimal coeffbudget = findValoreByRowValue(dettaglioPagamento, "COEFFBUDGET").getValoreAsBigDecimal();
      BigDecimal percedur = findValoreByRowValue(dettaglioPagamento, "PERCDECUR").getValoreAsBigDecimal();
      BigDecimal premiotot = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOT").getValoreAsBigDecimal();
      BigDecimal premiototdecur = findValoreByRowValue(dettaglioPagamento, "PREMIOTOTDECUR").getValoreAsBigDecimal();
      BigDecimal premiototdecuragg = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTDECURAGG").getValoreAsBigDecimal();
      BigDecimal premiototliquidato = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTLIQUIDATO").getValoreAsBigDecimal();
      BigDecimal premiototintegr = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTINTEGR").getValoreAsBigDecimal();
      BigDecimal premiopostsanz = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOPOSTSANZ").getValoreAsBigDecimal();
      BigDecimal persanzloco = findValoreByRowValueNotRequired(dettaglioPagamento, "PERSANZLOCO").getValoreAsBigDecimal();
      BigDecimal persanzfito = findValoreByRowValueNotRequired(dettaglioPagamento, "PERSANZFITO").getValoreAsBigDecimal();
      BigDecimal impsanztot = findValoreByRowValueNotRequired(dettaglioPagamento, "IMPSANZTOT").getValoreAsBigDecimal();

      List<DettaglioRazzaDettaglioPagamentoPsr> dettagliRazza = new ArrayList<>();
      calculateForRazza(dettagliRazza, dettaglioPagamento, bovinaRendena, ruhe, ruheacc);
      calculateForRazza(dettagliRazza, dettaglioPagamento, grigioAlpina, ruhe, ruheacc);
      calculateForRazza(dettagliRazza, dettaglioPagamento, bovinaBrunaAlpinaOriginale, ruhe, ruheacc);
      calculateForRazza(dettagliRazza, dettaglioPagamento, ovineLamonTingolaFiemmeseoVillnosserSchalf, ruhe, ruheacc);
      calculateForRazza(dettagliRazza, dettaglioPagamento, caprinaPezzataMochena, ruhe, ruheacc);
      calculateForRazza(dettagliRazza, dettaglioPagamento, caprinaBiondaAdamello, ruhe, ruheacc);
      calculateForRazza(dettagliRazza, dettaglioPagamento, equinaCavalloNorico, ruhe, ruheacc);
      calculateForRazza(dettagliRazza, dettaglioPagamento, tpr, ruhe, ruheacc);
      BigDecimal importoGiaLiquidato = getValueOrDefault(premiototliquidato, BigDecimal.ZERO);

      final EsitoFinaleDettaglioPagamentoPsr10_1_3 esitoFinaleDettaglioPagamentoPsr =
          new EsitoFinaleDettaglioPagamentoPsr10_1_3(calculateImportoRiduzionePerRitardataPresentazione(percedur, isCampione ? premiopostsanz : premiotot),
              calculateCoefficienteRiduzionePerSuperamentoBudget(coeffbudget),
              importoGiaLiquidato.equals(BigDecimal.ZERO) ? BigDecimal.ZERO : getValueOrDefault(premiototdecur, premiototdecuragg),
              importoGiaLiquidato,
              getValueOrDefault(premiototintegr, premiototdecur),
              persanzloco, persanzfito, impsanztot);


      return new DettaglioPagamentoPsr10_1_3(dettagliRazza, esitoFinaleDettaglioPagamentoPsr);
  }


  private BigDecimal calculateImportoRiduzionePerRitardataPresentazione(BigDecimal perc, BigDecimal premio) {
    return premio.multiply((perc.divide(new BigDecimal(100))));
  }

  private BigDecimal calculateCoefficienteRiduzionePerSuperamentoBudget(BigDecimal COEFFBUDGET) {
    return (BigDecimal.ONE.subtract(COEFFBUDGET)).multiply(new BigDecimal(100));
  }

  private void calculateForRazza(List<DettaglioRazzaDettaglioPagamentoPsr> dettagliRazza, List<DettaglioPagametoPsrRow> dettaglioPagamento, Razza razza, BigDecimal ruhe, BigDecimal ruheacc) {
    BigDecimal animaliRichiesti = findValoreByRowValue(dettaglioPagamento, "UBARIC" + razza.getPosition()).getValoreAsBigDecimal();

    if (animaliRichiesti.compareTo(BigDecimal.ZERO) > 0) {
        BigDecimal animaliAmmissibili = findValoreByRowValue(dettaglioPagamento, "UBAAMMI" + razza.getPosition()).getValoreAsBigDecimal();
        BigDecimal percentualeScostamento = findValoreByRowValue(dettaglioPagamento, "PCAPISCOST" + razza.getPosition()).getValoreAsBigDecimal();
        BigDecimal aliquotaSostegno = razza.getAliquotaDiSostegno();
        BigDecimal importoAmmissibile = findValoreByRowValue(dettaglioPagamento, "IMPAMM" + razza.getPosition()).getValoreAsBigDecimal();
        BigDecimal importoCalcolatoIntervento = findValoreByRowValue(dettaglioPagamento, "IMPLIQ" + razza.getPosition()).getValoreAsBigDecimal();
        BigDecimal importoSanzioneSovradichiarazione = importoAmmissibile.subtract(importoCalcolatoIntervento);

      final DettaglioRazzaDettaglioPagamentoPsr dettaglioRazza = new DettaglioRazzaDettaglioPagamentoPsr(razza.getNome(), ruhe, ruheacc, animaliRichiesti,
          animaliAmmissibili, percentualeScostamento,
          aliquotaSostegno, importoAmmissibile,
          importoSanzioneSovradichiarazione, importoCalcolatoIntervento);

      dettagliRazza.add(dettaglioRazza);
    }
  }

  private static class Razza {
    private final Integer position;
    private final String nome;
    private final BigDecimal aliquotaDiSostegno;

    public Razza(Integer position, String nome, BigDecimal aliquotaDiSostegno) {
      this.position = position;
      this.nome = nome;
      this.aliquotaDiSostegno = aliquotaDiSostegno;
    }

    public Integer getPosition() {
      return position;
    }

    public String getNome() {
      return nome;
    }

    public BigDecimal getAliquotaDiSostegno() {
      return aliquotaDiSostegno;
    }
  }
}
