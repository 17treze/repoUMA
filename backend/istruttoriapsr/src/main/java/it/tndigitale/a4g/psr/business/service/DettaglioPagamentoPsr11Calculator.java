package it.tndigitale.a4g.psr.business.service;

import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPsr;
import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPsr11;
import it.tndigitale.a4g.psr.dto.DettaglioPagametoPsrRow;
import it.tndigitale.a4g.psr.dto.DettaglioSostegnoMantenimentoPsr;
import it.tndigitale.a4g.psr.dto.EsitoFinaleDettaglioPagamentoPsr11;

import java.math.BigDecimal;
import java.util.List;

import static it.tndigitale.a4g.psr.business.service.DettaglioPagamentoUtils.*;

public class DettaglioPagamentoPsr11Calculator implements DettaglioPagamentoPsrCalculator {

    private static final String[] tableTitlesMap = {
            "Sostegno all’Introduzione - Intervento Colture Arboree Specializzate",
            "Sostegno all’Introduzione - Prati permanenti ",
            "Sostegno all’Introduzione -Intervento Colture Arboree Non Specializzate e Piccoli Frutti",
            "Sostegno all’Introduzione -Intervento Colture Orticole e altre Colture Annuali",
            "Mantenimento - Intervento Colture Arboree Specializzate",
            "Mantenimento – Intervento Prati permanenti ",
            "Mantenimento – Intervento Colture Arboree Non Specializzate e Piccoli Frutti",
            "Mantenimento – Intervento Colture Orticole e altre Colture Annuali"
    };
    
    @Override
    public DettaglioPagamentoPsr calculate(List<DettaglioPagametoPsrRow> dettaglioPagamento, boolean isCampione) {
        DettaglioPagamentoPsr11 dettaglioPagamentoPsr11 = new DettaglioPagamentoPsr11();

        for (int i = 1; i <= 8; i++) {
            BigDecimal supric = findValoreByRowValue(dettaglioPagamento, "SUPRIC".concat(String.valueOf(i))).getValoreAsBigDecimal();
            BigDecimal supAmm = findValoreByRowValue(dettaglioPagamento, "SUPAMM".concat(String.valueOf(i))).getValoreAsBigDecimal();
            BigDecimal percsCostSup = findValoreByRowValue(dettaglioPagamento, "PERCSCOSTSUP".concat(String.valueOf(i))).getValoreAsBigDecimal();
            BigDecimal premio = findValoreByRowValue(dettaglioPagamento, "PREMIO".concat(String.valueOf(i))).getValoreAsBigDecimal();
            BigDecimal impliq = findValoreByRowValue(dettaglioPagamento, "IMPLIQ".concat(String.valueOf(i))).getValoreAsBigDecimal();

            if (supric.compareTo(BigDecimal.ZERO) > 0) {
                DettaglioSostegnoMantenimentoPsr sostegnoMantenimentoPsr = new DettaglioSostegnoMantenimentoPsr();
                sostegnoMantenimentoPsr.setSuperficieRichiesta(supric);
                sostegnoMantenimentoPsr.setSuperficieAmmissibile(supAmm);
                sostegnoMantenimentoPsr.setPercentualeScostamento(percsCostSup);
                sostegnoMantenimentoPsr.setAliquotaSostegno(premio);
                sostegnoMantenimentoPsr.setImportoAmmissibile(supAmm.multiply(premio));
                sostegnoMantenimentoPsr.setImportoSanzioneSovradichiarazione(supAmm.multiply(premio).subtract(impliq));
                sostegnoMantenimentoPsr.setCalcolatoIntervento(impliq);
                sostegnoMantenimentoPsr.setNomeSostegno(tableTitlesMap[i-1]);
                dettaglioPagamentoPsr11.addSostegnoMantenimentoPsrs(sostegnoMantenimentoPsr);
            }
        }

        BigDecimal premioTot = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOT").getValoreAsBigDecimal();
        BigDecimal percdecur = findValoreByRowValue(dettaglioPagamento, "PERCDECUR").getValoreAsBigDecimal();
        BigDecimal coeffBudget = findValoreByRowValue(dettaglioPagamento, "COEFFBUDGET").getValoreAsBigDecimal();
        BigDecimal premioTotDecur1 = findValoreByRowValue(dettaglioPagamento, "PREMIOTOTDECUR1").getValoreAsBigDecimal();
        BigDecimal premioTotDecur2 = findValoreByRowValue(dettaglioPagamento, "PREMIOTOTDECUR2").getValoreAsBigDecimal();
        BigDecimal premioTotDecur = findValoreByRowValue(dettaglioPagamento, "PREMIOTOTDECUR").getValoreAsBigDecimal();

        BigDecimal persanzfito = findValoreByRowValueNotRequired(dettaglioPagamento, "PERSANZFITO").getValoreAsBigDecimal();
        BigDecimal impsanztot1 = findValoreByRowValueNotRequired(dettaglioPagamento, "IMPSANZTOT1").getValoreAsBigDecimal();
        BigDecimal impsanztot2 = findValoreByRowValueNotRequired(dettaglioPagamento, "IMPSANZTOT2").getValoreAsBigDecimal();
        BigDecimal premiopostsanz1 = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOPOSTSANZ1").getValoreAsBigDecimal();
        BigDecimal premiopostsanz2 = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOPOSTSANZ2").getValoreAsBigDecimal();

        BigDecimal premioTotLiquidato1 = getValueOrDefault(findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTLIQUIDATO1").getValoreAsBigDecimal(), BigDecimal.ZERO);
        BigDecimal premioTotLiquidato2 = getValueOrDefault(findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTLIQUIDATO2").getValoreAsBigDecimal(), BigDecimal.ZERO);
        BigDecimal premioTotIntegr1 = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTINTEGR1").getValoreAsBigDecimal();
        BigDecimal premioTotIntegr2 = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTINTEGR2").getValoreAsBigDecimal();
        BigDecimal premioTotIntegr = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTINTEGR").getValoreAsBigDecimal();


        EsitoFinaleDettaglioPagamentoPsr11 esitoFinaleDettaglioPagamentoPsr11 = new EsitoFinaleDettaglioPagamentoPsr11();
        if (isCampione) {
            esitoFinaleDettaglioPagamentoPsr11.setImportoRiduzioneRitardataPresentazione(premiopostsanz1.add(premiopostsanz2).multiply((percdecur.divide(BigDecimal.valueOf(100)))));
            esitoFinaleDettaglioPagamentoPsr11.setPercentualeRiduzioneMancatoRispettoImpegniBaseline(persanzfito);
            esitoFinaleDettaglioPagamentoPsr11.setImportoRiduzioneMancatoRispettoImpegniBaselineIntroduzione(impsanztot1);
            esitoFinaleDettaglioPagamentoPsr11.setImportoRiduzioneMancatoRispettoImpegniBaselineMantenimento(impsanztot2);
        } else {
            esitoFinaleDettaglioPagamentoPsr11.setImportoRiduzioneRitardataPresentazione(premioTot.multiply((percdecur.divide(BigDecimal.valueOf(100)))));
        }
        esitoFinaleDettaglioPagamentoPsr11.setCoefficienteRiduzioneLineareSuperamentoBudget((BigDecimal.ONE.subtract(coeffBudget)).multiply(BigDecimal.valueOf(100)));
        if (BigDecimal.ZERO.compareTo(premioTotLiquidato1) < 0 || BigDecimal.ZERO.compareTo(premioTotLiquidato2) < 0) {
            esitoFinaleDettaglioPagamentoPsr11.setImportoCalcolatoIntroduzione(premioTotDecur1);
            esitoFinaleDettaglioPagamentoPsr11.setImportoCalcolatoMantenimento(premioTotDecur2);
            esitoFinaleDettaglioPagamentoPsr11.setImportoCalcolatoTotale(premioTotDecur);
            esitoFinaleDettaglioPagamentoPsr11.setImportoLiquidatoPagamentiPrecedentiIntroduzione(premioTotLiquidato1);
            esitoFinaleDettaglioPagamentoPsr11.setImportoLiquidatoPagamentiPrecedentiMantenimento(premioTotLiquidato2);
        }
        esitoFinaleDettaglioPagamentoPsr11.setImportoCalcolatoLiquidatoIntroduzione(getValueOrDefault(premioTotIntegr1, premioTotDecur1));
        esitoFinaleDettaglioPagamentoPsr11.setImportoCalcolatoLiquidatoMantenimento(getValueOrDefault(premioTotIntegr2, premioTotDecur2));
        esitoFinaleDettaglioPagamentoPsr11.setImportoCalcolatoLiquidatoTotale(getValueOrDefault(premioTotIntegr, premioTotDecur));

        dettaglioPagamentoPsr11.setEsitoFinaleDettaglioPagamentoPsr(esitoFinaleDettaglioPagamentoPsr11);

        return dettaglioPagamentoPsr11;
    }

}
