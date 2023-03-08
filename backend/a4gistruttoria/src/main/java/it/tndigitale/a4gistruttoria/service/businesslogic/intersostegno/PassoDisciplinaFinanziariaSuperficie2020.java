package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.component.acs.CaricaPremioCalcolatoSuperficie;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Service("PassoDisciplinaFinanziaria2020_SUPERFICIE")
public class PassoDisciplinaFinanziariaSuperficie2020 extends PassoDisciplinaFinanziaria2020 {

    @Autowired
    private CaricaPremioCalcolatoSuperficie calcolatore;

    private static List<String> misure;
    private static List<TipoVariabile> tipoVariabileInputCalcoloPrecedente = new ArrayList<TipoVariabile>();

    static {
        misure = Arrays.asList("M8", "M9", "M10", "M11", "M14", "M15", "M16", "M17");
        misure.stream().forEach(postFix -> tipoVariabileInputCalcoloPrecedente.add(TipoVariabile.valueOf("DFFRAPPACS_".concat(postFix))));
        misure.stream().forEach(intervento -> tipoVariabileInputCalcoloPrecedente.add(TipoVariabile.valueOf("DFIMPLIPAGACS_".concat(intervento))));
    }


    @Override
    protected MapVariabili applicaFranchigia(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
        MapVariabili output = new MapVariabili();
        MapVariabili variabili = new MapVariabili();
        variabili.getVariabiliCalcolo().putAll(mapVariabiliInput.getVariabiliCalcolo());
        
        // NEW: DFFRPAGLORACS = MIN((DFFR - (DFFRAPPDIS+DFFRAPPACZ));ACSIMPCALCLORDO)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGLORACS,
                variabili.min(TipoVariabile.ACSIMPCALCLORDOTOT, variabili.subtract(TipoVariabile.DFFR, variabili.add(TipoVariabile.DFFRAPPDIS, TipoVariabile.DFFRAPPACZ)))));
        
        //min(DFFRPAGLORACS - SOMMA(DFFRAPPACS-M9 ... DFFRAPPACS-M17);ACSIMPCALCLORDO-M8)
        //min(DFFRPAGLORACS-DFFRPAGLORACS-M8 - SOMMA(DFFRAPPACS-M10 ... DFFRAPPACS-M17);ACSIMPCALCLORDO-M9)
        //min(DFFRPAGLORACS-DFFRPAGLORACS-M8-DFFRPAGLORACS-M9 - SOMMA(DFFRAPPACS-M11 ... DFFRAPPACS-M17);ACSIMPCALCLORDO-M10)
        //min(DFFRPAGLORACS-DFFRPAGLORACS-M8-DFFRPAGLORACS-M9-DFFRPAGLORACS-M10 - SOMMA(DFFRAPPACS-M14 ... DFFRAPPACS-M17);ACSIMPCALCLORDO-M11)
        //min(DFFRPAGLORACS-DFFRPAGLORACS-M8-DFFRPAGLORACS-M9-DFFRPAGLORACS-M10-DFFRPAGLORACS-M11- SOMMA(DFFRAPPACS-M15 ... DFFRAPPACS-M17);ACSIMPCALCLORDO-M14)
        //min(DFFRPAGLORACS-DFFRPAGLORACS-M8-DFFRPAGLORACS-M9-DFFRPAGLORACS-M10-DFFRPAGLORACS-M11-DFFRPAGLORACS-M14- SOMMA(DFFRAPPACS-M16 ... DFFRAPPACS-M17);ACSIMPCALCLORDO-M15)
        //min(DFFRPAGLORACS-DFFRPAGLORACS-M8-DFFRPAGLORACS-M9-DFFRPAGLORACS-M10-DFFRPAGLORACS-M11-DFFRPAGLORACS-M14-DFFRPAGLORACS-M15-DFFRAPPACS-M17;ACSIMPCALCLORDO-M17)
        //min(DFFRPAGLORACS-DFFRPAGLORACS-M8-DFFRPAGLORACS-M9-DFFRPAGLORACS-M10-DFFRPAGLORACS-M11-DFFRPAGLORACS-M14-DFFRPAGLORACS-M15-DFFRPAGLORACS-M16;ACSIMPCALCLORDO-M17)
        misure.forEach(mx -> {
        	TipoVariabile DFFRPAGLORACS_MX = TipoVariabile.valueOf("DFFRPAGLORACS_".concat(mx));
        	TipoVariabile ACSIMPCALCLORDO_MX = TipoVariabile.valueOf("ACSIMPCALCLORDO_".concat(mx));
        	
        	aggiungiVariabile(variabili, output, new VariabileCalcolo(DFFRPAGLORACS_MX, variabili.min(ACSIMPCALCLORDO_MX,
        			sub_DFFRPAGLORACS_MX(TipoVariabile.DFFRPAGLORACS,variabili,mx).subtract(sum_DFFRAPPACS_MX(variabili,mx)))));

        });
        
        BigDecimal franchigia = calcolaFranchigiaSostegno.apply(variabili, TipoVariabile.ACSIMPCALC);
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGACS, franchigia));
        
        

        // DFFRPAGACS_M8 = min(DFFRPAGACS;ACSIMPCALC-M8)
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M8, variabili.min(TipoVariabile.DFFRPAGACS, TipoVariabile.ACSIMPCALC_M8)));
        // DFFRPAGACS_M9 = min(DFFRPAGACS-DFFRPAGACS-M8;ACSIMPCALC-M9)
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M9, variabili
                        .min(TipoVariabile.ACSIMPCALC_M9, variabili.subtract(TipoVariabile.DFFRPAGACS, TipoVariabile.DFFRPAGACS_M8))));
        // DFFRPAGACS_M9 = min(DFFRPAGACS-DFFRPAGACS_M8-DFFRPAGACS_M9;ACSIMPCALC_M10)
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M10, variabili
                        .min(TipoVariabile.ACSIMPCALC_M10, variabili.subtract(TipoVariabile.DFFRPAGACS, TipoVariabile.DFFRPAGACS_M8, TipoVariabile.DFFRPAGACS_M9))));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M11, variabili.min(TipoVariabile.ACSIMPCALC_M11,
                        variabili.subtract(TipoVariabile.DFFRPAGACS, TipoVariabile.DFFRPAGACS_M8, TipoVariabile.DFFRPAGACS_M9, TipoVariabile.DFFRPAGACS_M10))));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M14, variabili.min(TipoVariabile.ACSIMPCALC_M14, variabili
                        .subtract(TipoVariabile.DFFRPAGACS, TipoVariabile.DFFRPAGACS_M8, TipoVariabile.DFFRPAGACS_M9, TipoVariabile.DFFRPAGACS_M10, TipoVariabile.DFFRPAGACS_M11))));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M15,
                        variabili.min(TipoVariabile.ACSIMPCALC_M15, variabili.subtract(TipoVariabile.DFFRPAGACS,
                                TipoVariabile.DFFRPAGACS_M8, TipoVariabile.DFFRPAGACS_M9, TipoVariabile.DFFRPAGACS_M10, TipoVariabile.DFFRPAGACS_M11, TipoVariabile.DFFRPAGACS_M14))));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M16,
                        variabili.min(TipoVariabile.ACSIMPCALC_M16,
                                variabili.subtract(TipoVariabile.DFFRPAGACS, TipoVariabile.DFFRPAGACS_M8, TipoVariabile.DFFRPAGACS_M9,
                                        TipoVariabile.DFFRPAGACS_M10, TipoVariabile.DFFRPAGACS_M11, TipoVariabile.DFFRPAGACS_M14, TipoVariabile.DFFRPAGACS_M15))));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M17,
                        variabili.min(TipoVariabile.ACSIMPCALC_M17,
                                variabili.subtract(TipoVariabile.DFFRPAGACS, TipoVariabile.DFFRPAGACS_M8, TipoVariabile.DFFRPAGACS_M9,
                                        TipoVariabile.DFFRPAGACS_M10, TipoVariabile.DFFRPAGACS_M11, TipoVariabile.DFFRPAGACS_M14, TipoVariabile.DFFRPAGACS_M15, TipoVariabile.DFFRPAGACS_M16))));


        
        
 		

        //DFIMPDFDISACS_M8 alias //DFIMPDFDFACS_M8 (-> DISACS sarebbe ACS dato che si sta lavorando con la superficie e non con il disaccoppiato
        //applicativmanete è corretto e va a salvar ele variabili DFIMPDFDISACS_MX)
        

        // (100-DFPERC)/100
        BigDecimal riduzioneDaFranchigia = variabili.subtract(BigDecimal.ONE, TipoVariabile.DFPERC);

        // EDIT: MAX(DFFRPAGLORACS_XXX + (ACSIMPCALCLORDO_XXX - DFFRPAGLORACS_XXX) * (100-DFPERC) - DFIMPLIPAGACS_XXX - DFFRPAGACS_XXX;0)
        for (String str : misure) {
            aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.valueOf("DFIMPDFDISACS_".concat(str)), BigDecimal.ZERO.max(
                    variabili.add(
                            variabili.multiply(TipoVariabile.valueOf("DFFRPAGLORACS_".concat(str)), BigDecimal.ONE),
                            variabili.multiply(variabili.subtract(TipoVariabile.valueOf("ACSIMPCALCLORDO_".concat(str)), TipoVariabile.valueOf("DFFRPAGLORACS_".concat(str))), riduzioneDaFranchigia)
                    ).subtract(variabili.get(TipoVariabile.valueOf("DFIMPLIPAGACS_".concat(str))).getValNumber()).subtract(variabili.get(TipoVariabile.valueOf("DFFRPAGACS_".concat(str))).getValNumber())
            )));
        }

        BigDecimal valSum = BigDecimal.ZERO;
        for (String str : misure) {
            valSum = valSum.add(variabili.get(TipoVariabile.valueOf("DFFRPAGACS_".concat(str))).getValNumber());
        }
        
        //da capire perchè è stata fatta modificata la franchigia con la somma, stessa cosa per la zootecnia e poi non usata
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGACS, valSum));

        
        BigDecimal dffrpagacsSum = misure.stream().map(m -> variabili.get(TipoVariabile.valueOf("DFFRPAGACS_".concat(m))).getValNumber()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal dfimpdfdisacsSum = misure.stream().map(m -> variabili.get(TipoVariabile.valueOf("DFIMPDFDISACS_".concat(m))).getValNumber()).reduce(BigDecimal.ZERO,
                (a, b) -> a.add(b));
        VariabileCalcolo var = new VariabileCalcolo(TipoVariabile.DFIMPLIQACS, dffrpagacsSum.add(dfimpdfdisacsSum));

        aggiungiVariabile(variabili, output, var);

        // NEW: DFIMPLIQACSLORDO = DFIMPLIQACS+SOMMA(DFIMPLIPAGACS-MXX)
        BigDecimal impLiqAcsSum = BigDecimal.ZERO;
        for (String str : misure) {
            impLiqAcsSum = impLiqAcsSum.add(variabili.get(TipoVariabile.valueOf("DFIMPLIPAGACS_".concat(str))).getValNumber());
        }
        BigDecimal dFImpLiqAcs = BigDecimal.ZERO;
        if(variabili.get(TipoVariabile.DFIMPLIQACS) != null) {
            dFImpLiqAcs = variabili.get(TipoVariabile.DFIMPLIQACS).getValNumber();
        }
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPLIQACSLORDO, variabili.add(dFImpLiqAcs, impLiqAcsSum)));


        var = new VariabileCalcolo(TipoVariabile.DFIMPRIDACS,
                variabili.get(TipoVariabile.ACSIMPCALC).getValNumber().subtract(variabili.get(TipoVariabile.DFIMPLIQACS).getValNumber()));

        aggiungiVariabile(variabili, output, var);

        return output;
    }

    protected void aggiungiVariabile(MapVariabili variabili, MapVariabili output, VariabileCalcolo variabile) {
        output.add(variabile);
        variabili.add(variabile);
    }

    @Override
    public CaricaPremioCalcolatoSuperficie getCalcolatore() {
        return calcolatore;
    }

    @Override
    public TipoVariabile[] getVariabiliPremi() {
        return new TipoVariabile[]{
                TipoVariabile.ACSIMPCALC_M8, TipoVariabile.ACSIMPCALC_M9,
                TipoVariabile.ACSIMPCALC_M10, TipoVariabile.ACSIMPCALC_M11, TipoVariabile.ACSIMPCALC_M14,
                TipoVariabile.ACSIMPCALC_M15, TipoVariabile.ACSIMPCALC_M16, TipoVariabile.ACSIMPCALC_M17
        };
    }

    @Override
    public TipoVariabile[] getVariabiliPremiLordi() {
        return new TipoVariabile[]{
                TipoVariabile.ACSIMPCALCLORDO_M8, TipoVariabile.ACSIMPCALCLORDO_M9,
                TipoVariabile.ACSIMPCALCLORDO_M10, TipoVariabile.ACSIMPCALCLORDO_M11, TipoVariabile.ACSIMPCALCLORDO_M14,
                TipoVariabile.ACSIMPCALCLORDO_M15, TipoVariabile.ACSIMPCALCLORDO_M16, TipoVariabile.ACSIMPCALCLORDO_M17
        };
    }

    @Override
    public TipoVariabile getNomeVariabilePremioTotale() {
        return TipoVariabile.ACSIMPCALC;
    }

    @Override
    public TipoVariabile getNomeVariabilePremioTotaleLordo() {
        return TipoVariabile.ACSIMPCALCLORDOTOT;
    }

    @Override
    public List<TipoVariabile> getVariabiliCalcoloPrecedente() {
        List<TipoVariabile> tipoVariabileList = new ArrayList<>();

        for (String str : misure) {
            tipoVariabileList.add(TipoVariabile.valueOf("DFFRPAGACS_".concat(str)));
            tipoVariabileList.add(TipoVariabile.valueOf("DFIMPDFDISACS_".concat(str)));
        }

        return tipoVariabileList;
    }
    
    @Override
    public List<TipoVariabile> getVariabiliInputCalcoloPrecedente() {
    	return tipoVariabileInputCalcoloPrecedente;
    }
    
    
    @Override
    protected void calcolaVariabiliFranchigiaGiaApplicataCalcoloPrecedente(MapVariabili mapVariabiliInput, MapVariabili mapVariabiliInputPrecedente,MapVariabili mapVariabiliOutputPrecedente) {
    	
    	for (String mx : misure) {
    		
    		TipoVariabile DFFRPAGACS_MX = TipoVariabile.valueOf("DFFRPAGACS_".concat(mx));
    		TipoVariabile DFFRAPPACS_MX = TipoVariabile.valueOf("DFFRAPPACS_".concat(mx));
    		
    		BigDecimal val_DFFRPAGACS_MX = extractVariabile(mapVariabiliOutputPrecedente, DFFRPAGACS_MX);//output del calcolo precedente
    		BigDecimal val_DFFRAPPACS_MX = extractVariabile(mapVariabiliInputPrecedente, DFFRAPPACS_MX);//input del calcolo precedente
    		
    		mapVariabiliInput.add(new VariabileCalcolo(DFFRAPPACS_MX, val_DFFRPAGACS_MX.add(val_DFFRAPPACS_MX)));
        }
    }
    


    @Override
    public void initVariabiliImportiGiaErogati(MapVariabili mapVariabiliInput, MapVariabili mapVariabiliInputPrecedente, MapVariabili mapVariabiliOutputPrecedente) {
        for (String str : misure) {
            BigDecimal df_FRPAGACS = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.valueOf("DFFRPAGACS_".concat(str)));
            BigDecimal df_IMPDFDISACS = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.valueOf("DFIMPDFDISACS_".concat(str)));
            BigDecimal df_IMPLIPAGACS = extractVariabile(mapVariabiliInputPrecedente, TipoVariabile.valueOf("DFIMPLIPAGACS_".concat(str)));
            mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.valueOf("DFIMPLIPAGACS_".concat(str)), df_FRPAGACS.add(df_IMPDFDISACS).add(df_IMPLIPAGACS)));
        }
    }
    
    //considera le variabili da 0 al index of
    private BigDecimal sub_DFFRPAGLORACS_MX(TipoVariabile start,MapVariabili variabili,String misureToEnd) {
    	
    	List<String> subMisure = misure.subList(0, misure.indexOf(misureToEnd));
    	return subMisure.stream()
    	.map((String mx) -> {
    		return extractVariabile(variabili, TipoVariabile.valueOf("DFFRPAGLORACS_".concat(mx)));//output del calcolo precedente
    	})
    	.reduce(variabili.get(start) != null ? variabili.get(start).getValNumber() : BigDecimal.ZERO, (subtotal, element) -> subtotal.subtract(element));    
    	
    	
    }
    
    //considera le variabili da index_of a end
    private BigDecimal sum_DFFRAPPACS_MX(MapVariabili variabili,String misureToStart) {
    	
    	List<String> subMisure = misure.subList(misure.indexOf(misureToStart)+ 1, misure.size());
    	return subMisure.stream()
    	.map((String mx) -> {
    		return extractVariabile(variabili, TipoVariabile.valueOf("DFFRAPPACS_".concat(mx)));//output del calcolo precedente
    	})
    	.reduce(BigDecimal.ZERO, (subtotal, element) -> subtotal.add(element));    	
    }


}
