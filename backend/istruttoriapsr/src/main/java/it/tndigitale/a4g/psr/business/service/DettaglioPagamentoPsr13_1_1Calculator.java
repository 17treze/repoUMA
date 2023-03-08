package it.tndigitale.a4g.psr.business.service;

import it.tndigitale.a4g.psr.dto.CalcoloDegressivitaPremio;
import it.tndigitale.a4g.psr.dto.DatiAziendali;
import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPSR13_1_1;
import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPsr;
import it.tndigitale.a4g.psr.dto.DettaglioPagametoPsrRow;
import it.tndigitale.a4g.psr.dto.EsitoFinaleDettaglioPagamentoEsitoPsr13_1_1;
import it.tndigitale.a4g.psr.dto.GestionePluriennaleSanzioni;
import it.tndigitale.a4g.psr.dto.RiduzioniControlloInLoco;
import it.tndigitale.a4g.psr.dto.SistemaAgricolo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static it.tndigitale.a4g.psr.business.service.DettaglioPagamentoUtils.*;
import static java.lang.String.valueOf;
import static java.math.BigDecimal.ZERO;

public class DettaglioPagamentoPsr13_1_1Calculator implements DettaglioPagamentoPsrCalculator {
    public static final int NUMBER_OF_DATI_AZIENDALI = 6;
    List<Integer> minAliquota = List.of(600, 450, 450, 600, 450);
    List<String> alphabet = List.of("A", "B", "C", "D", "E");
    List<String> tableNames = List.of("Sistema agricolo - agricoltura zootecnica intermedia",
            "Sistema agricolo - agricoltura zootecnica estensiva",
            "Sistema agricolo - arboricoltura intensiva",
            "Sistema agricolo - arboricoltura estensiva",
            "Sistema agricolo - ortofloricoltura");

    @Override
    public DettaglioPagamentoPsr calculate(List<DettaglioPagametoPsrRow> dettaglioPagamento, boolean isCampione) {
        List<SistemaAgricolo> sistemiAgrigoli = new ArrayList<>();
        BigDecimal rhue = findValoreByRowValue(dettaglioPagamento, "RUHE").getValoreAsBigDecimal();
        BigDecimal supFor = findValoreByRowValue(dettaglioPagamento, "SUPFOR").getValoreAsBigDecimal();
        BigDecimal ubaAlp = findValoreByRowValue(dettaglioPagamento, "UBAALP").getValoreAsBigDecimal();
        String aziTrans = findValoreByRowValue(dettaglioPagamento, "AZITRANS").getValoreAString();
        BigDecimal coeffPa = findValoreByRowValue(dettaglioPagamento, "COEFFPA").getValoreAsBigDecimal();
        String sanzAnniPrec = findValoreByRowValue(dettaglioPagamento, "SANZANNIPREC").getValoreAString();


        final DatiAziendali datiAziendali = new DatiAziendali(rhue, supFor, ubaAlp, aziTrans, coeffPa, sanzAnniPrec);

        if (isCampione) {
            datiAziendali.setCaricoBestiameAccertatoControllo(findValoreByRowValueNotRequired(dettaglioPagamento, "RUHEACC").getValoreAsBigDecimal());
            datiAziendali.setSuperficieForaggeraAccertataControllo(findValoreByRowValueNotRequired(dettaglioPagamento, "SUPFORACC").getValoreAsBigDecimal());
        }

        for (int inidiceDatoAziendale = 1; inidiceDatoAziendale < NUMBER_OF_DATI_AZIENDALI; inidiceDatoAziendale++) {
            BigDecimal supriccolt = findValoreByRowValue(dettaglioPagamento, "SUPRICCOLT".concat(valueOf(inidiceDatoAziendale))).getValoreAsBigDecimal();
            BigDecimal supammcolt = findValoreByRowValue(dettaglioPagamento, "SUPAMMCOLT".concat(valueOf(inidiceDatoAziendale))).getValoreAsBigDecimal();
            BigDecimal superficieRichiesta = calculateSuperficieRichiesta(dettaglioPagamento,supriccolt, inidiceDatoAziendale);

            if(superficieRichiesta.compareTo(BigDecimal.ZERO) > 0)
                sistemiAgrigoli.add(getSistemaAgricolo(dettaglioPagamento, inidiceDatoAziendale, supammcolt, superficieRichiesta));
        }

        final GestionePluriennaleSanzioni gestionePluriennaleSanzioni = getGestionePluriennaleSanzioni(dettaglioPagamento, sanzAnniPrec);

        final BigDecimal impliqazi = findValoreByRowValue(dettaglioPagamento, "IMPLIQAZI").getValoreAsBigDecimal();
        final BigDecimal supammazi = findValoreByRowValue(dettaglioPagamento, "SUPAMMAZI").getValoreAsBigDecimal();
        final BigDecimal premiocalc = findValoreByRowValue(dettaglioPagamento, "PREMIOCALC").getValoreAsBigDecimal();

        // TODO: These fields are not yet implemented, see TDAC-123
        final BigDecimal supricazi = null;

        final CalcoloDegressivitaPremio calcoloDegressivitaPremio = new CalcoloDegressivitaPremio(impliqazi, getValueOrDefault(supricazi, supammazi), premiocalc);

        final EsitoFinaleDettaglioPagamentoEsitoPsr13_1_1 esitoFinale = getEsitoFinaleDettaglioPagamentoEsitoPsr13_1_1(dettaglioPagamento, premiocalc);

        DettaglioPagamentoPSR13_1_1 dettaglioPagamentoPSR1311 = new DettaglioPagamentoPSR13_1_1(datiAziendali, sistemiAgrigoli, gestionePluriennaleSanzioni, calcoloDegressivitaPremio, esitoFinale);

        final BigDecimal impsanzubaalp = findValoreByRowValueNotRequired(dettaglioPagamento, "IMPSANZUBAALP").getValoreAsBigDecimal();
        final BigDecimal impsanzzoo = findValoreByRowValueNotRequired(dettaglioPagamento, "IMPSANZZOO").getValoreAsBigDecimal();
        if (isCampione && impsanzubaalp != null && impsanzzoo!= null && (ZERO.compareTo(impsanzubaalp) < 0 || ZERO.compareTo(impsanzzoo) < 0)) {
            RiduzioniControlloInLoco riduzioniControlloInLoco =  new RiduzioniControlloInLoco();
            riduzioniControlloInLoco.setImportoRiduzioneMancatoRispettoImpegniUba(impsanzubaalp);
            riduzioniControlloInLoco.setPercentualeRiduzioneMancatoRispettoImpegniZootecnico(findValoreByRowValueNotRequired(dettaglioPagamento, "PERSANZLOCO").getValoreAsBigDecimal());
            riduzioniControlloInLoco.setImportoRiduzioneMancatoRispettoImpegniZootecnico(impsanzzoo);
            dettaglioPagamentoPSR1311.setRiduzioniControlloInLoco(riduzioniControlloInLoco);
        }
        return dettaglioPagamentoPSR1311;

    }

    private EsitoFinaleDettaglioPagamentoEsitoPsr13_1_1 getEsitoFinaleDettaglioPagamentoEsitoPsr13_1_1(List<DettaglioPagametoPsrRow> dettaglioPagamento, BigDecimal premiocalc) {
        final BigDecimal percedur = findValoreByRowValue(dettaglioPagamento, "PERCDECUR").getValoreAsBigDecimal();
        final BigDecimal coeffbudget = findValoreByRowValue(dettaglioPagamento, "COEFFBUDGET").getValoreAsBigDecimal();
        final BigDecimal coeffriduz = findValoreByRowValue(dettaglioPagamento, "COEFFRIDUZ").getValoreAsBigDecimal();
        final BigDecimal premiototdecur = findValoreByRowValue(dettaglioPagamento, "PREMIOTOTDECUR").getValoreAsBigDecimal();
        final BigDecimal premiototdecuragg = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTDECURAGG").getValoreAsBigDecimal();
        final BigDecimal premiototliquidato = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTLIQUIDATO").getValoreAsBigDecimal();
        final BigDecimal premiototintegr = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOTOTINTEGR").getValoreAsBigDecimal();
        final BigDecimal premioantliq = findValoreByRowValueNotRequired(dettaglioPagamento, "PREMIOANTLIQ").getValoreAsBigDecimal();

        return new EsitoFinaleDettaglioPagamentoEsitoPsr13_1_1(
                calculateImportoRiduzioneRitardataPresentazione(premiocalc, percedur),
                calculateCoefficienteRiduzione(coeffbudget),
                calculateCoefficienteRiduzione(coeffriduz),
                calculateImportoTotale(premiototliquidato, premiototdecur, premioantliq, premiototdecuragg, premioantliq, premiototliquidato),
                calculatePremioLiquidatoPrecedentemente(premioantliq, premiototliquidato),
                getValueOrDefault(premiototintegr, premiototdecur),
                premioantliq,
                premiototliquidato
        );
    }

    private GestionePluriennaleSanzioni getGestionePluriennaleSanzioni(List<DettaglioPagametoPsrRow> dettaglioPagamento, String sanzAnniPrec) {
        final String impsanzscostazi = findValoreByRowValue(dettaglioPagamento, "IMPSANZSCOSTAZI").getValoreAString();
        final BigDecimal impsanzsconto = findValoreByRowValue(dettaglioPagamento, "IMPSANZSCONTO").getValoreAsBigDecimal();
        final BigDecimal impsanzrecidiva = findValoreByRowValue(dettaglioPagamento, "IMPSANZRECIDIVA").getValoreAsBigDecimal();
        final BigDecimal impsanznorisc = findValoreByRowValue(dettaglioPagamento, "IMPSANZNORISC").getValoreAsBigDecimal();

        if (new BigDecimal(impsanzscostazi).compareTo(BigDecimal.ZERO) > 0) {
            return new GestionePluriennaleSanzioni(sanzAnniPrec, impsanzscostazi, calculatePresenzaRecidiva(sanzAnniPrec, impsanzscostazi), impsanzsconto, impsanzrecidiva, impsanznorisc);
        } else {
            return null;
        }
    }

    private SistemaAgricolo getSistemaAgricolo(List<DettaglioPagametoPsrRow> dettaglioPagamento, int indiceDatoAziendale, BigDecimal supammcolt, BigDecimal superficieRichiesta) {
            BigDecimal supammliq = findValoreByRowValue(dettaglioPagamento, (indiceDatoAziendale <3 ? "SUPAMMLIQ" : "SUPAMMCOLT").concat(valueOf(indiceDatoAziendale))).getValoreAsBigDecimal();
            BigDecimal percscostsup = findValoreByRowValue(dettaglioPagamento, "PERCSCOSTSUP".concat(valueOf(indiceDatoAziendale))).getValoreAsBigDecimal();
            String azitrans = findValoreByRowValue(dettaglioPagamento, "AZITRANS").getVariabile();
            BigDecimal impgrpa = findValoreByRowValue(dettaglioPagamento, getImpGrp(indiceDatoAziendale)).getValoreAsBigDecimal();
            BigDecimal coeffpa = findValoreByRowValue(dettaglioPagamento, "COEFFPA").getValoreAsBigDecimal();
            BigDecimal impammliq = findValoreByRowValue(dettaglioPagamento, "IMPAMMLIQ".concat(valueOf(indiceDatoAziendale))).getValoreAsBigDecimal();
            BigDecimal impsanzscost = findValoreByRowValue(dettaglioPagamento, "IMPSANZSCOST".concat(valueOf(indiceDatoAziendale))).getValoreAsBigDecimal();


            BigDecimal superficieAmmissibile = getSuperficieAmmissibile(supammliq, supammcolt, indiceDatoAziendale);
            BigDecimal aliquotaSostegno = calulateAliquotaDiSostegno(azitrans, impgrpa, coeffpa, indiceDatoAziendale);
            BigDecimal importoCalcolatoIntervento = impammliq.subtract(impsanzscost);

            return new SistemaAgricolo(getTableNames(indiceDatoAziendale),
                    superficieRichiesta,
                    superficieAmmissibile,
                    percscostsup,
                    aliquotaSostegno,
                    impammliq,
                    impsanzscost,
                    importoCalcolatoIntervento);
    }

    private BigDecimal calculateImportoTotale(BigDecimal premiototliquidato, BigDecimal premiototdecur, BigDecimal premioantliq, BigDecimal premiototdecuragg, BigDecimal premioAntiLiq, BigDecimal premioTotLiquidato) {
        if (getValueOrDefault(premioAntiLiq, ZERO).equals(ZERO) && getValueOrDefault(premioTotLiquidato, ZERO).equals(ZERO)) {
            return ZERO;
        }
        if (premiototliquidato == null) {
            return premiototdecur.add(getValueOrDefault(premioantliq, BigDecimal.ZERO));
        }
        return getValueOrDefault(premiototdecur, premiototdecuragg.add(premiototliquidato));
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

    private BigDecimal calculateCoefficienteRiduzione(BigDecimal coefficiente) {
        return BigDecimal.ONE.subtract(coefficiente).multiply(new BigDecimal(100));
    }

    private BigDecimal calculateImportoRiduzioneRitardataPresentazione(BigDecimal premiocalc, BigDecimal percedur) {
        return premiocalc.multiply(percedur.multiply(BigDecimal.valueOf(100)));
    }

    private String calculatePresenzaRecidiva(String sanzAnniPrec, String impsanzscostazi) {
        if (new BigDecimal(sanzAnniPrec).compareTo(BigDecimal.ONE) == 0 && new BigDecimal(impsanzscostazi).compareTo(BigDecimal.ZERO) > 0)
            return "SI";
        return "NO";
    }

    private String getTableNames(int i) {
        return tableNames.get(i-1);
    }

    private BigDecimal getSuperficieAmmissibile(BigDecimal supammliq1, BigDecimal supammcolt, int indiceDatoAziendale) {
        if (indiceDatoAziendale > 2)
            return supammcolt;
        return supammliq1;
    }

    private BigDecimal calculateSuperficieRichiesta(List<DettaglioPagametoPsrRow> dettaglioPagamento, BigDecimal supriccolt, int indiceDatoAziendale) {
        if (indiceDatoAziendale > 2)
            return supriccolt;
        BigDecimal supubaalp = findValoreByRowValue(dettaglioPagamento, "SUPUBAALP".concat(valueOf(indiceDatoAziendale))).getValoreAsBigDecimal();
        return supriccolt.add(supubaalp);
    }

    private String getImpGrp(int indiceDatoAziendale) {
        return "IMPGRP".concat(alphabet.get(indiceDatoAziendale-1));
    }

    private BigDecimal calulateAliquotaDiSostegno(String azitrans, BigDecimal impgrpa, BigDecimal coeffpa, int indiceDatoAziendale) {
        if ("S".equals(azitrans)) {
            return impgrpa;
        }
        return BigDecimal.valueOf(getAliquota(indiceDatoAziendale)).min(impgrpa.multiply(coeffpa));
    }

    private Integer getAliquota(int indiceDatoAziendale) {
        return minAliquota.get(indiceDatoAziendale-1);
    }

}
