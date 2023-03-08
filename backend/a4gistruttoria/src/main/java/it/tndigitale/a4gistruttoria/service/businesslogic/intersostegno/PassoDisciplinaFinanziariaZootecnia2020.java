package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.component.acz.CaricaPremioCalcolatoZootecnia;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Service("PassoDisciplinaFinanziaria2020_ZOOTECNIA")
public class PassoDisciplinaFinanziariaZootecnia2020 extends PassoDisciplinaFinanziaria2020 {

    @Autowired
    private CaricaPremioCalcolatoZootecnia calcolatore;

    private static List<String> interventi;
    private static List<TipoVariabile> tipoVariabileInputCalcoloPrecedente = new ArrayList<>();

    static {
        //mantenere il corretto ordinamento
        interventi = Arrays.asList("310", "311", "313", "322", "315", "316", "318", "320", "321");
        interventi.stream().forEach(postFix -> tipoVariabileInputCalcoloPrecedente.add(TipoVariabile.valueOf("DFFRAPPACZ_".concat(postFix))));
        interventi.stream().forEach(intervento -> tipoVariabileInputCalcoloPrecedente.add(TipoVariabile.valueOf("DFIMPLIPAGACZ_".concat(intervento))));
    }
    
    
    

    @Override
    protected MapVariabili applicaFranchigia(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
        MapVariabili output = new MapVariabili();
        MapVariabili variabili = new MapVariabili();
        variabili.getVariabiliCalcolo().putAll(mapVariabiliInput.getVariabiliCalcolo());
        BigDecimal franchigia = calcolaFranchigiaSostegno.apply(mapVariabiliInput, TipoVariabile.ACZIMPCALC);

        // NEW: DFFRPAGLORACZ = MIN((DFFR - (DFFRAPPDIS+DFFRAPPACS));ACZIMPCALCLORDO)
        aggiungiVariabile(variabili, output,new VariabileCalcolo(TipoVariabile.DFFRPAGLORACZ,
                variabili.min(TipoVariabile.ACZIMPCALCLORDO,variabili.subtract(TipoVariabile.DFFR, variabili.add(TipoVariabile.DFFRAPPDIS, TipoVariabile.DFFRAPPACS)))));
        
        
        //min(DFFRPAGLORACZ-SOMMA(DFFRAPPACZ-311 ... DFFRAPPACZ-321);ACZIMPCALCLORDO-310)
        //min(DFFRPAGLORACZ-DFFRPAGLORACZ-310-SOMMA(DFFRAPPACZ-313 ... DFFRAPPACZ-321);ACZIMPCALCLORDO-311)
        //min(DFFRPAGLORACZ-DFFRPAGLORACZ-310-DFFRPAGLORACZ-311-SOMMA(DFFRAPPACZ-322... DFFRAPPACZ-321);ACZIMPCALCLORDO-313)
        //min(DFFRPAGLORACZ-DFFRPAGLORACZ-310-DFFRPAGLORACZ-311-DFFRPAGLORACZ-313-SOMMA(DFFRAPPACZ-315... DFFRAPPACZ-321);ACZIMPCALCLORDO-322)
        //min(DFFRPAGLORACZ-DFFRPAGLORACZ-310-DFFRPAGLORACZ-311-DFFRPAGLORACZ-313-DFFRPAGLORACZ-322-SOMMA(DFFRAPPACZ-316... DFFRAPPACZ-321);ACZIMPCALCLORDO-315)
        //min(DFFRPAGLORACZ-DFFRPAGLORACZ-310-DFFRPAGLORACZ-311-DFFRPAGLORACZ-313-DFFRPAGLORACZ-322-DFFRPAGLORACZ-315-SOMMA(DFFRAPPACZ-318... DFFRAPPACZ-321);ACZIMPCALCLORDO-316)
        //min(DFFRPAGLORACZ-DFFRPAGLORACZ-310-DFFRPAGLORACZ-311-DFFRPAGLORACZ-313-DFFRPAGLORACZ-322-DFFRPAGLORACZ-315-DFFRPAGLORACZ-316-SOMMA(DFFRAPPACZ-320 ... DFFRAPPACZ-321);ACZIMPCALCLORDO-318)
        //min(DFFRPAGLORACZ-DFFRPAGLORACZ-310-DFFRPAGLORACZ-311-DFFRPAGALORACZ-313-DFFRPAGLORACZ-322-DFFRPAGLORACZ-315-DFFRPAGLORACZ-316-DFFRPAGLORACZ-318-DFFRAPPACZ-321;ACZIMPCALCLORDO-320)
        interventi.forEach(itv -> {
        	TipoVariabile DFFRPAGLORACZ_3X = TipoVariabile.valueOf("DFFRPAGLORACZ_".concat(itv));
        	TipoVariabile ACZIMPCALCLORDO_3X = TipoVariabile.valueOf("ACZIMPCALCLORDO_".concat(itv));
        	
        	aggiungiVariabile(variabili, output, new VariabileCalcolo(DFFRPAGLORACZ_3X, variabili.min(ACZIMPCALCLORDO_3X,
        			sub_DFFRPAGLORACZ_3X(TipoVariabile.DFFRPAGLORACZ,variabili,itv).subtract(sum_DFFRAPPACZ_3X(variabili,itv)))));
        });
        
        
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGACZ, franchigia));

        
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_310, variabili.min(TipoVariabile.DFFRPAGACZ, TipoVariabile.ACZIMPCALC_310)));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_311, variabili
                        .min(TipoVariabile.ACZIMPCALC_311, variabili.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310))));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_313, variabili
                        .min(TipoVariabile.ACZIMPCALC_313, variabili.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311))));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_322, variabili.min(TipoVariabile.ACZIMPCALC_322,
                        variabili.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311, TipoVariabile.DFFRPAGACZ_313))));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_315, variabili.min(TipoVariabile.ACZIMPCALC_315, variabili
                        .subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311, TipoVariabile.DFFRPAGACZ_313, TipoVariabile.DFFRPAGACZ_322))));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_316,
                        variabili.min(TipoVariabile.ACZIMPCALC_316, variabili.subtract(TipoVariabile.DFFRPAGACZ,
                                TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311, TipoVariabile.DFFRPAGACZ_313, TipoVariabile.DFFRPAGACZ_322, TipoVariabile.DFFRPAGACZ_315))));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_318,
                        variabili.min(TipoVariabile.ACZIMPCALC_318,
                                variabili.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311,
                                        TipoVariabile.DFFRPAGACZ_313, TipoVariabile.DFFRPAGACZ_322, TipoVariabile.DFFRPAGACZ_315, TipoVariabile.DFFRPAGACZ_316))));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_320,
                        variabili.min(TipoVariabile.ACZIMPCALC_320,
                                variabili.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311,
                                        TipoVariabile.DFFRPAGACZ_313, TipoVariabile.DFFRPAGACZ_322, TipoVariabile.DFFRPAGACZ_315, TipoVariabile.DFFRPAGACZ_316, TipoVariabile.DFFRPAGACZ_318))));
        aggiungiVariabile(variabili, output,
                new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_321,
                        variabili.min(TipoVariabile.ACZIMPCALC_321,
                                variabili.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311,
                                        TipoVariabile.DFFRPAGACZ_313, TipoVariabile.DFFRPAGACZ_322, TipoVariabile.DFFRPAGACZ_315, TipoVariabile.DFFRPAGACZ_316, TipoVariabile.DFFRPAGACZ_318,
                                        TipoVariabile.DFFRPAGACZ_320))));

        // (100-DFPERC)/100
        BigDecimal riduzioneDaFranchigia = variabili.subtract(BigDecimal.ONE, TipoVariabile.DFPERC);

        //EDIT: DFIMPDFDISACZ_XXX = MAX(DFFRPAGLORACZ_XXX+(ACZIMPCALCLORDO_XXX-DFFRPAGLORACZ_XXX)*(100-DFPERC)-DFIMPLIPAGACZ_XXX-DFFRPAGACZ_XXX);0)
        for (String str : interventi) {
            BigDecimal valToAdd = variabili.multiply(
                    variabili.subtract(TipoVariabile.valueOf("ACZIMPCALCLORDO_".concat(str)), TipoVariabile.valueOf("DFFRPAGLORACZ_".concat(str))),
                    riduzioneDaFranchigia).add(variabili.get(TipoVariabile.valueOf("DFFRPAGLORACZ_".concat(str))).getValNumber());
            BigDecimal valToSub = valToAdd.subtract(variabili.get(TipoVariabile.valueOf("DFIMPLIPAGACZ_".concat(str))).getValNumber()).subtract(variabili.get(TipoVariabile.valueOf("DFFRPAGACZ_".concat(str))).getValNumber());

        	aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.valueOf("DFIMPDFDISACZ_".concat(str)), valToSub.max(BigDecimal.ZERO)));
        }

        BigDecimal valSum = BigDecimal.ZERO;
        for (String str : interventi) {
            valSum = valSum.add(variabili.get(TipoVariabile.valueOf("DFFRPAGACZ_".concat(str))).getValNumber());
        }
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGACZ, valSum));

        BigDecimal dffrpagacsSum = interventi.stream().map(m -> variabili.get(TipoVariabile.valueOf("DFFRPAGACZ_".concat(m))).getValNumber()).reduce(BigDecimal.ZERO,
                (a, b) -> a.add(b));

        BigDecimal dfimpdfdisacsSum = interventi.stream().map(m -> variabili.get(TipoVariabile.valueOf("DFIMPDFDISACZ_".concat(m))).getValNumber()).reduce(BigDecimal.ZERO,
                (a, b) -> a.add(b));
        VariabileCalcolo var = new VariabileCalcolo(TipoVariabile.DFIMPLIQACZ, dffrpagacsSum.add(dfimpdfdisacsSum));
        // handler.getPassoLavorazioneDto().getDatiOutput().getVariabiliCalcolo().add(var);
        aggiungiVariabile(variabili, output, var);

        //NEW: DFIMPLIQACZLORDO = DFIMPLIQACZ+SOMMA(DFIMPLIPAGACZ-3XX)
        valSum = BigDecimal.ZERO;
        for (String str : interventi) {
            valSum = valSum.add(variabili.get(TipoVariabile.valueOf("DFIMPLIPAGACZ_".concat(str))).getValNumber());
        }
        BigDecimal dFImpLiqAcz = BigDecimal.ZERO;
        if(variabili.get(TipoVariabile.DFIMPLIQACZ) != null) {
            dFImpLiqAcz = variabili.get(TipoVariabile.DFIMPLIQACZ).getValNumber();
        }
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPLIQACZLORDO, valSum.add(dFImpLiqAcz)));


        var = new VariabileCalcolo(TipoVariabile.DFIMPRIDACZ,
                variabili.get(TipoVariabile.ACZIMPCALC).getValNumber().subtract(variabili.get(TipoVariabile.DFIMPLIQACZ).getValNumber()));

        aggiungiVariabile(variabili, output, var);

        return output;
    }
    
    protected void aggiungiVariabile(MapVariabili variabili, MapVariabili output, VariabileCalcolo variabile) {
        output.add(variabile);
        variabili.add(variabile);
    }

    @Override
    public CaricaPremioCalcolatoZootecnia getCalcolatore() {
        return calcolatore;
    }

    @Override
    public TipoVariabile[] getVariabiliPremi() {
        return new TipoVariabile[]{
                TipoVariabile.ACZIMPCALC_310, TipoVariabile.ACZIMPCALC_311,
                TipoVariabile.ACZIMPCALC_313, TipoVariabile.ACZIMPCALC_315,
                TipoVariabile.ACZIMPCALC_316,
                TipoVariabile.ACZIMPCALC_318, TipoVariabile.ACZIMPCALC_320,
                TipoVariabile.ACZIMPCALC_321, TipoVariabile.ACZIMPCALC_322
        };
    }

    @Override
    public TipoVariabile[] getVariabiliPremiLordi() {
        return new TipoVariabile[] {
        		TipoVariabile.ACZIMPCALCLORDO_310, TipoVariabile.ACZIMPCALCLORDO_311,
        		TipoVariabile.ACZIMPCALCLORDO_313,TipoVariabile.ACZIMPCALCLORDO_315,
        		TipoVariabile.ACZIMPCALCLORDO_316,
        		TipoVariabile.ACZIMPCALCLORDO_318,TipoVariabile.ACZIMPCALCLORDO_320,
        		TipoVariabile.ACZIMPCALCLORDO_321,TipoVariabile.ACZIMPCALCLORDO_322
        };
    }

    @Override
    public TipoVariabile getNomeVariabilePremioTotale() {
        return TipoVariabile.ACZIMPCALC;
    }

    @Override
    public TipoVariabile getNomeVariabilePremioTotaleLordo() {
        return TipoVariabile.ACZIMPCALCLORDO;
    }

    @Override
    public List<TipoVariabile> getVariabiliCalcoloPrecedente() {
        List<TipoVariabile> tipoVariabileList = new ArrayList<>();
        for (String str : interventi) {
            tipoVariabileList.add(TipoVariabile.valueOf("DFFRPAGACZ_".concat(str)));
            tipoVariabileList.add(TipoVariabile.valueOf("DFIMPDFDISACZ_".concat(str)));
        }
        return tipoVariabileList;
    }
    
    @Override
    public List<TipoVariabile> getVariabiliInputCalcoloPrecedente() {
    	return tipoVariabileInputCalcoloPrecedente;
    }
    
    @Override
    protected void calcolaVariabiliFranchigiaGiaApplicataCalcoloPrecedente(MapVariabili mapVariabiliInput, MapVariabili mapVariabiliInputPrecedente,MapVariabili mapVariabiliOutputPrecedente) {
    	
    	for (String itv : interventi) {
    		
    		TipoVariabile DFFRPAGACZ_3X = TipoVariabile.valueOf("DFFRPAGACZ_".concat(itv));
    		TipoVariabile DFFRAPPACZ_3X = TipoVariabile.valueOf("DFFRAPPACZ_".concat(itv));
    		
    		BigDecimal val_DFFRPAGACZ_3X = extractVariabile(mapVariabiliOutputPrecedente, DFFRPAGACZ_3X);//output del calcolo precedente
    		BigDecimal val_DFFRAPPACZ_3X = extractVariabile(mapVariabiliInputPrecedente, DFFRAPPACZ_3X);//input del calcolo precedente
    		
    		mapVariabiliInput.add(new VariabileCalcolo(DFFRAPPACZ_3X, val_DFFRPAGACZ_3X.add(val_DFFRAPPACZ_3X)));
        }
    }

    @Override
    public void initVariabiliImportiGiaErogati(MapVariabili mapVariabiliInput, MapVariabili mapVariabiliInputPrecedente, MapVariabili mapVariabiliOutputPrecedente) {
        for (String str : interventi) {
            BigDecimal df_FRPAGACZ = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.valueOf("DFFRPAGACZ_".concat(str)));
            BigDecimal df_IMPDFDISACZ = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.valueOf("DFIMPDFDISACZ_".concat(str)));
            BigDecimal df_IMPLIPAGACZ = extractVariabile(mapVariabiliInputPrecedente, TipoVariabile.valueOf("DFIMPLIPAGACZ_".concat(str)));
            mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.valueOf("DFIMPLIPAGACZ_".concat(str)), df_FRPAGACZ.add(df_IMPDFDISACZ).add(df_IMPLIPAGACZ)));
        }
    }
    
  //considera le variabili da 0 al index of
    private BigDecimal sub_DFFRPAGLORACZ_3X(TipoVariabile start,MapVariabili variabili,String misureToEnd) {
    	
    	List<String> subMisure = interventi.subList(0, interventi.indexOf(misureToEnd));
    	return subMisure.stream()
    	.map((String mx) -> {
    		return extractVariabile(variabili, TipoVariabile.valueOf("DFFRPAGLORACZ_".concat(mx)));//output del calcolo precedente
    	})
    	.reduce(variabili.get(start) != null ? variabili.get(start).getValNumber() : BigDecimal.ZERO, (subtotal, element) -> subtotal.subtract(element));    
    	
    	
    }
    
    //considera le variabili da index_of a end
    private BigDecimal sum_DFFRAPPACZ_3X(MapVariabili variabili,String misureToStart) {
    	
    	List<String> subMisure = interventi.subList(interventi.indexOf(misureToStart)+ 1, interventi.size());
    	return subMisure.stream()
    	.map((String mx) -> {
    		return extractVariabile(variabili, TipoVariabile.valueOf("DFFRAPPACZ_".concat(mx)));//output del calcolo precedente
    	})
    	.reduce(BigDecimal.ZERO, (subtotal, element) -> subtotal.add(element));    	
    }

}
