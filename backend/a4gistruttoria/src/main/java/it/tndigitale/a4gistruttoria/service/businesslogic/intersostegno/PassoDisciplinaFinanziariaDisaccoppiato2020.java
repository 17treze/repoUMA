package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.component.dis.CaricaPremioCalcolatoDisaccoppiato;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("PassoDisciplinaFinanziaria2020_DISACCOPPIATO")
public class PassoDisciplinaFinanziariaDisaccoppiato2020 extends PassoDisciplinaFinanziaria2020 {

    private static final Logger logger = LoggerFactory.getLogger(PassoDisciplinaFinanziariaDisaccoppiato2020.class);

    private static List<TipoVariabile> tipoVariabileInputCalcoloPrecedente = new ArrayList<>();

    static {
        tipoVariabileInputCalcoloPrecedente.add(TipoVariabile.DFFRAPPDISBPS);
        tipoVariabileInputCalcoloPrecedente.add(TipoVariabile.DFFRAPPDISGRE);
        tipoVariabileInputCalcoloPrecedente.add(TipoVariabile.DFFRAPPDISGIO);
        tipoVariabileInputCalcoloPrecedente.add(TipoVariabile.DFIMPLIPAGBPS);
        tipoVariabileInputCalcoloPrecedente.add(TipoVariabile.DFIMPLIPAGGRE);
        tipoVariabileInputCalcoloPrecedente.add(TipoVariabile.DFIMPLIPAGGIO);
    }

    @Autowired
    private CaricaPremioCalcolatoDisaccoppiato calcolatorePremio;
    @Autowired
    private TransizioneIstruttoriaDao transizioneIstruttoriaDao;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected MapVariabili applicaFranchigia(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
        MapVariabili output = new MapVariabili();
        MapVariabili variabili = new MapVariabili();
        variabili.getVariabiliCalcolo().putAll(mapVariabiliInput.getVariabiliCalcolo());
        
          
        // NEW: DFFRPAGLORDIS = MIN((DFFR - (DFFRAPPACZ+DFFRAPPACS));DISIMPCALCLORDO)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGLORDIS, variabili.min(TipoVariabile.DISIMPCALCLORDO,
                variabili.subtract(TipoVariabile.DFFR, variabili.add(TipoVariabile.DFFRAPPACZ, TipoVariabile.DFFRAPPACS)))));

        // OLD: DFFRPAGLORDISBPS = min(DFFRPAGLORDIS;BPSIMPCALCFINLORDO)
        // NEW  DFFRPAGLORDISBPS = min(DFFRPAGLORDIS-DFFRAPPDISGRE-DFFRAPPDISGIO;BPSIMPCALCFINLORDO)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGLORDISBPS, variabili.min(TipoVariabile.BPSIMPCALCFINLORDO,
        		 variabili.subtract(variabili.subtract(TipoVariabile.DFFRPAGLORDIS,TipoVariabile.DFFRAPPDISGRE),TipoVariabile.DFFRAPPDISGIO)
        		)));

        // OLD: DFFRPAGLORDISGRE = min(DFFRPAGLORDIS-DFFRPAGLORDISBPS;GREIMPCALCFINLORDO)
        // NEW: DFFRPAGLORDISGRE = min(DFFRPAGLORDIS-DFFRPAGLORDISBPS-DFFRAPPDISGIO;GREIMPCALCFINLORDO)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGLORDISGRE,
                variabili.min(TipoVariabile.GREIMPCALCFINLORDO,variabili.subtract(variabili.subtract(TipoVariabile.DFFRPAGLORDIS, TipoVariabile.DFFRPAGLORDISBPS),TipoVariabile.DFFRAPPDISGIO))));

        // NEW: DFFRPAGLORDISGIO = min(DFFRPAGLORDIS-DFFRPAGLORDISBPS-DFFRPAGLORDISGRE;GIOIMPCALCFINLORDO)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGLORDISGIO, variabili.min(TipoVariabile.GIOIMPCALCFINLORDO,
        		variabili.subtract(variabili.subtract(TipoVariabile.DFFRPAGLORDIS,TipoVariabile.DFFRPAGLORDISBPS),TipoVariabile.DFFRPAGLORDISGRE))));

        BigDecimal franchigia = calcolaFranchigiaSostegno.apply(variabili, TipoVariabile.DISIMPCALC);
        logger.debug("Franchigia residua: {}", franchigia);
        // DFFRPAGDIS = MIN((DFFR - (DFFRAPPDIS+DFFRAPPACZ+DFFRAPPACS));DISIMPCALC)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGDIS, franchigia));

        // DFFRPAGDISBPS = min(DFFRPAGDIS;BPSIMPCALCFIN)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGDISBPS, variabili.min(TipoVariabile.DFFRPAGDIS, TipoVariabile.BPSIMPCALCFIN)));

        // (100-DFPERC)/100
        BigDecimal riduzioneDaFranchigia = variabili.subtract(BigDecimal.ONE, TipoVariabile.DFPERC);

        // DFFRPAGDISGRE = min(DFFRPAGDIS-DFFRPAGDISBPS;GREIMPCALCFIN)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGDISGRE,
                variabili.min(TipoVariabile.GREIMPCALCFIN, variabili.subtract(TipoVariabile.DFFRPAGDIS, TipoVariabile.DFFRPAGDISBPS))));

        // DFFRPAGDISGIO = min(DFFRPAGDIS-DFFRPAGDISBPS-DFFRPAGDISGRE;GIOIMPCALCFIN) => min (DFFRPAGDIS-(DFFRPAGDISBPS+DFFRPAGDISGRE);GIOIMPCALCFIN)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGDISGIO, variabili.min(TipoVariabile.GIOIMPCALCFIN,
                variabili.subtract(variabili.subtract(TipoVariabile.DFFRPAGDIS,TipoVariabile.DFFRPAGDISBPS),TipoVariabile.DFFRPAGDISGRE))));

        // EDIT: DFIMPDFDISBPS = max((DFFRPAGLORDISBPS+(BPSIMPCALCFINLORDO-DFFRPAGLORDISBPS)*(100-DFPERC)-DFIMPLIPAGBPS-DFFRPAGDISBPS);0)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPDFDISBPS, BigDecimal.ZERO.max(
                variabili.add(
                        variabili.multiply(TipoVariabile.DFFRPAGLORDISBPS, BigDecimal.ONE),
                        variabili.multiply(variabili.subtract(TipoVariabile.BPSIMPCALCFINLORDO, TipoVariabile.DFFRPAGLORDISBPS), riduzioneDaFranchigia)
                ).subtract(variabili.get(TipoVariabile.DFIMPLIPAGBPS).getValNumber()).subtract(variabili.get(TipoVariabile.DFFRPAGDISBPS).getValNumber())
        )));

        // EDIT: DFIMPDFDISGRE = max((DFFRPAGLORDISGRE+(GREIMPCALCFINLORDO-DFFRPAGLORDISGRE)*(100-DFPERC)-DFIMPLIPAGGRE-DFFRPAGDISGRE);0)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPDFDISGRE, BigDecimal.ZERO.max(
                variabili.add(
                        variabili.multiply(TipoVariabile.DFFRPAGLORDISGRE, BigDecimal.ONE),
                        variabili.multiply(variabili.subtract(TipoVariabile.GREIMPCALCFINLORDO, TipoVariabile.DFFRPAGLORDISGRE), riduzioneDaFranchigia)
                ).subtract(variabili.get(TipoVariabile.DFIMPLIPAGGRE).getValNumber()).subtract(variabili.get(TipoVariabile.DFFRPAGDISGRE).getValNumber())
        )));

        // EDIT: DFIMPDFDISGIO = max((DFFRPAGLORDISGIO+(GIOIMPCALCFINLORDO-DFFRPAGLORDISGIO)*(100-DFPERC)-DFIMPLIPAGGIO-DFFRPAGDISGIO;0)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPDFDISGIO, BigDecimal.ZERO.max(
                variabili.add(
                        variabili.multiply(TipoVariabile.DFFRPAGLORDISGIO, BigDecimal.ONE),
                        variabili.multiply(variabili.subtract(TipoVariabile.GIOIMPCALCFINLORDO, TipoVariabile.DFFRPAGLORDISGIO), riduzioneDaFranchigia)
                ).subtract(variabili.get(TipoVariabile.DFIMPLIPAGGIO).getValNumber()).subtract(variabili.get(TipoVariabile.DFFRPAGDISGIO).getValNumber())
        )));
        

        // DFIMPLIQDIS = DFFRPAGDISBPS+DFFRPAGDISGRE+DFFRPAGDISGIO+DFIMPDFDISBPS+DFIMPDFDISGRE+DFIMPDFDISGIO
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPLIQDIS, variabili.add(TipoVariabile.DFFRPAGDISBPS, TipoVariabile.DFFRPAGDISGRE, TipoVariabile.DFFRPAGDISGIO, TipoVariabile.DFIMPDFDISBPS,
                TipoVariabile.DFIMPDFDISGRE, TipoVariabile.DFIMPDFDISGIO)));

        // DFIMPRIDDIS = DISIMPCALC-DFIMPLIQDIS
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPRIDDIS, variabili.subtract(TipoVariabile.DISIMPCALC, TipoVariabile.DFIMPLIQDIS)));

        // NEW: DFIMPLIQDISLORDO = DFIMPLIQDIS+(DFIMPLIPAGBPS+DFIMPLIPAGGRE+DFIMPLIPAGGIO)
        aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPLIQDISLORDO, variabili.add(TipoVariabile.DFIMPLIQDIS, TipoVariabile.DFIMPLIPAGBPS, TipoVariabile.DFIMPLIPAGGRE, TipoVariabile.DFIMPLIPAGGIO)));

        
        return output;
    }

    protected void aggiungiVariabile(MapVariabili variabili, MapVariabili output, VariabileCalcolo variabile) {
        output.add(variabile);
        variabili.add(variabile);
    }

    @Override
    public CaricaPremioCalcolatoDisaccoppiato getCalcolatore() {
        return calcolatorePremio;
    }

    @Override
    public TipoVariabile[] getVariabiliPremi() {
        return new TipoVariabile[]{
                TipoVariabile.BPSIMPCALCFIN,
                TipoVariabile.GIOIMPCALCFIN,
                TipoVariabile.GREIMPCALCFIN};
    }

    @Override
    public TipoVariabile[] getVariabiliPremiLordi() {
        return new TipoVariabile[]{
                TipoVariabile.BPSIMPCALCFINLORDO,
                TipoVariabile.GREIMPCALCFINLORDO,
                TipoVariabile.GIOIMPCALCFINLORDO};
    }

    @Override
    public TipoVariabile getNomeVariabilePremioTotale() {
        return TipoVariabile.DISIMPCALC;
    }

    @Override
    public TipoVariabile getNomeVariabilePremioTotaleLordo() {
        return TipoVariabile.DISIMPCALCLORDO;
    }

    @Override
    public List<TipoVariabile> getVariabiliCalcoloPrecedente() {
        List<TipoVariabile> tipoVariabileList = new ArrayList<>();
        //considera solo le variabili di output
        tipoVariabileList.add(TipoVariabile.DFFRPAGDISBPS);
        tipoVariabileList.add(TipoVariabile.DFIMPDFDISBPS);
        tipoVariabileList.add(TipoVariabile.DFFRPAGDISGRE);
        tipoVariabileList.add(TipoVariabile.DFIMPDFDISGRE);
        tipoVariabileList.add(TipoVariabile.DFFRPAGDISGIO);
        tipoVariabileList.add(TipoVariabile.DFIMPDFDISGIO);
        
        return tipoVariabileList;
    }
    
    
    @Override
    public List<TipoVariabile> getVariabiliInputCalcoloPrecedente() {
        return tipoVariabileInputCalcoloPrecedente;
    }
    
    @Override
    protected void calcolaVariabiliFranchigiaGiaApplicataCalcoloPrecedente(MapVariabili mapVariabiliInput, MapVariabili mapVariabiliInputPrecedente,MapVariabili mapVariabiliOutputPrecedente) {
    	
    	BigDecimal DFFRPAGDISBPS = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.DFFRPAGDISBPS);//output del calcolo precedente
    	BigDecimal DFFRPAGDISGRE = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.DFFRPAGDISGRE);
    	BigDecimal DFFRPAGDISGIO = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.DFFRPAGDISGIO);
    	
    	BigDecimal DFFRAPPDISBPS = extractVariabile(mapVariabiliInputPrecedente, TipoVariabile.DFFRAPPDISBPS);//calcolo precedente
        BigDecimal DFFRAPPDISGRE = extractVariabile(mapVariabiliInputPrecedente, TipoVariabile.DFFRAPPDISGRE);
        BigDecimal DFFRAPPDISGIO = extractVariabile(mapVariabiliInputPrecedente, TipoVariabile.DFFRAPPDISGIO);
        
        mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFFRAPPDISBPS, DFFRPAGDISBPS.add(DFFRAPPDISBPS)));
        mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFFRAPPDISGRE, DFFRPAGDISGRE.add(DFFRAPPDISGRE)));
        mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFFRAPPDISGIO, DFFRPAGDISGIO.add(DFFRAPPDISGIO)));
        
    }
    

    @Override
    public void initVariabiliImportiGiaErogati(MapVariabili mapVariabiliInput, MapVariabili mapVariabiliInputPrecedente, MapVariabili mapVariabiliOutputPrecedente) {
        BigDecimal df_FRPAGDISBPS = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.DFFRPAGDISBPS);
        BigDecimal df_IMPDFDISBPS = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.DFIMPDFDISBPS);
        BigDecimal df_IMPLIPAGBPS = extractVariabile(mapVariabiliInputPrecedente, TipoVariabile.DFIMPLIPAGBPS);

        BigDecimal df_FRPAGDISGRE = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.DFFRPAGDISGRE);
        BigDecimal df_IMPDFDISGRE = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.DFIMPDFDISGRE);
        BigDecimal df_IMPLIPAGGRE = extractVariabile(mapVariabiliInputPrecedente, TipoVariabile.DFIMPLIPAGGRE);

        BigDecimal df_FRPAGDISGIO = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.DFFRPAGDISGIO);
        BigDecimal df_IMPDFDISGIO = extractVariabile(mapVariabiliOutputPrecedente, TipoVariabile.DFIMPDFDISGIO);
        BigDecimal df_IMPLIPAGGIO = extractVariabile(mapVariabiliInputPrecedente, TipoVariabile.DFIMPLIPAGGIO);
        
        
        mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFIMPLIPAGBPS, df_FRPAGDISBPS.add(df_IMPDFDISBPS).add(df_IMPLIPAGBPS)));
        mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFIMPLIPAGGRE, df_FRPAGDISGRE.add(df_IMPDFDISGRE).add(df_IMPLIPAGGRE)));
        mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFIMPLIPAGGIO, df_FRPAGDISGIO.add(df_IMPDFDISGIO).add(df_IMPLIPAGGIO)));



    }
}
