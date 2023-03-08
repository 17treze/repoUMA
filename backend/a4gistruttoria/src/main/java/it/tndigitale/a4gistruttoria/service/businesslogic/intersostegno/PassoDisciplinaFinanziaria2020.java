package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.component.CaricaPremioCalcolatoSostegno;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiCalcoli;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiPassoLavorazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.ConfigurazioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaboraPassoIstruttoria;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public abstract class PassoDisciplinaFinanziaria2020 extends ElaboraPassoIstruttoria {

    private static final Logger logger = LoggerFactory.getLogger(PassoDisciplinaFinanziariaDisaccoppiato.class);

    private static final BigDecimal SOGLIA_DISCIPLINA = BigDecimal.valueOf(2000d);

    @Autowired
    private TransizioneIstruttoriaDao transizioneIstruttoriaDao;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ConfigurazioneIstruttoriaDao confDao;

    private static Map<Sostegno, TipoVariabile> mappaSostegnoVariabileFranchigia =
            new HashMap<Sostegno, TipoVariabile>();
    

    static {
        mappaSostegnoVariabileFranchigia.put(Sostegno.DISACCOPPIATO, TipoVariabile.DFFRPAGDIS);
        mappaSostegnoVariabileFranchigia.put(Sostegno.SUPERFICIE, TipoVariabile.DFFRPAGACS);
        mappaSostegnoVariabileFranchigia.put(Sostegno.ZOOTECNIA, TipoVariabile.DFFRPAGACZ);
    }

    // Franchigia precedentemente applicata ad i sostegni
    private BiFunction<DomandaUnicaModel, Sostegno, BigDecimal> valorePrecedentePerFranchigia = (domanda, identificativoSostegno) -> {

        // prendo tutte le istruttorie della domanda
        Set<IstruttoriaModel> istruttorieDomande = domanda.getA4gtLavorazioneSostegnos();

        return istruttorieDomande.stream()
                // voglio le istruttorie PAGATE del mio sostegno
                .filter(ist -> identificativoSostegno.equals(ist.getSostegno()))
                .filter(ist -> StatoIstruttoria.PAGAMENTO_AUTORIZZATO.equals(ist.getStato()))
//			// MI INTERESSA LA PIU' RECENTE (PAGATA PER SOSTEGNO) -> no le prendo tutte e sommo
//			.sorted(Comparator.comparingLong(IstruttoriaModel::getId).reversed())
//			.findFirst()
                .map(istruttoriaUltimoPagamento -> {
                    List<TransizioneIstruttoriaModel> transizioniIntersostegno = transizioneIstruttoriaDao.findTransizioneControlloIntersostegno(istruttoriaUltimoPagamento);
                    if (CollectionUtils.isEmpty(transizioniIntersostegno)) {
                        return BigDecimal.ZERO;
                    }
                    return transizioniIntersostegno.stream()
                            .max(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione))
                            .map(TransizioneIstruttoriaModel::getPassiTransizione)
                            .get()
                            .stream()
                            .filter(passo -> TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA.equals(passo.getCodicePasso()))
                            .findAny()
                            .map(p -> {
                                try {
                                    return objectMapper.readValue(p.getDatiOutput(), DatiOutput.class);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .map(x -> x.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(mappaSostegnoVariabileFranchigia.get(identificativoSostegno)))
                                    .findFirst().get())
                            .map(variabileFranchigia -> variabileFranchigia.getValNumber())
                            .orElse(BigDecimal.ZERO);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    };
    

    // Disciplina - Franchigia sostegno accoppiato superficie
    // MIN((DFFR - (DFFRAPPDIS+DFFRAPPACZ+DFFRAPPACS));ACSIMPCALC)
    protected BiFunction<MapVariabili, TipoVariabile, BigDecimal> calcolaFranchigiaSostegno = (variabiliInput, variabileImportoCalcolato) -> {
        return variabiliInput.min(variabileImportoCalcolato,
                (variabiliInput.subtract(TipoVariabile.DFFR, variabiliInput.add(TipoVariabile.DFFRAPPDIS, TipoVariabile.DFFRAPPACZ, TipoVariabile.DFFRAPPACS))));

    };
    
    
    public void elaboraPasso(TransizioneIstruttoriaModel transizione) throws Exception {
        IstruttoriaModel istruttoria = transizione.getIstruttoria();
        MapVariabili mapVariabiliInput = creaInizializzaVariabiliInput(istruttoria);
        MapVariabili mapVariabiliOutput = applicaFranchigia(mapVariabiliInput, istruttoria);

        DatiPassoLavorazione passo = new DatiPassoLavorazione();
        passo.setIdTransizione(transizione.getId());
        passo.setPasso(getPasso());
        for (VariabileCalcolo v : mapVariabiliInput.getVariabiliCalcolo().values()) {
            passo.getDatiInput().getVariabiliCalcolo().add(v);
        }
        for (VariabileCalcolo v : mapVariabiliOutput.getVariabiliCalcolo().values()) {
            passo.getDatiOutput().getVariabiliCalcolo().add(v);
        }
        passo.setCodiceEsito("no esito");
        passo.setEsito(true);
        salvaPassoLavorazioneSostegno(passo);
    }

    protected MapVariabili creaInizializzaVariabiliInput(IstruttoriaModel istruttoria) {
        MapVariabili mapVariabiliInput = new MapVariabili();
        initVariabiliDisciplina(mapVariabiliInput, istruttoria);
        initVariabiliSostegno(mapVariabiliInput, istruttoria);
        initVariabiliSostegnoLordo(mapVariabiliInput, istruttoria);
        initVariabiliFranchigiaGiaApplicataCalcoloPrecedente(mapVariabiliInput, istruttoria);
        initVariabiliFranchigiaGiaApplicata(mapVariabiliInput,istruttoria);
        initVariabiliImportiGiaErogati(mapVariabiliInput, getMapVariabiliCalcoloPrecedente(istruttoria, DatiInput.class), getMapVariabiliCalcoloPrecedente(istruttoria, DatiOutput.class));

        return mapVariabiliInput;
    }

    protected abstract MapVariabili applicaFranchigia(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria);

    protected void initVariabiliDisciplina(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
        Double valorePercentualeDaConfigurazione = confDao.findByCampagna(istruttoria.getDomandaUnicaModel().getCampagna())
                .map(ConfigurazioneIstruttoriaModel::getPercentualeDisciplinaFinanziaria)
                .orElseThrow(() -> new RuntimeException("La disciplina finanziaria non è stata configurata per l'anno "
                        + istruttoria.getDomandaUnicaModel().getCampagna()));
        mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFPERC, BigDecimal.valueOf(valorePercentualeDaConfigurazione)));
        mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFFR, SOGLIA_DISCIPLINA));
    }

    protected void initVariabiliSostegno(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
        CaricaPremioCalcolatoSostegno caricatorePremi = getCalcolatore();
        TipoVariabile[] variabiliPremi = getVariabiliPremi();
        if (variabiliPremi != null && variabiliPremi.length > 0) {
            Map<TipoVariabile, BigDecimal> importiPremi =
                    caricatorePremi.getImportoPremioCalcolato(istruttoria, variabiliPremi);
            BigDecimal totalePremi = BigDecimal.ZERO;
            BigDecimal premioMisura = BigDecimal.ZERO;
            for (TipoVariabile variabilePremio : variabiliPremi) {
                /**
                 *  Betty 11.06.2020: caso di saldi e integrazioni con
                 *  premio giovane negativo.
                 *
                 *  Recupero il premio per la variabile (0 se non presente)
                 *  e prendo il MAX tra il premio calcolato e 0
                 */
                premioMisura = importiPremi.getOrDefault(variabilePremio, BigDecimal.ZERO).max(BigDecimal.ZERO);
                TipoVariabile nomeVariabile = mappaTipoVariabile(variabilePremio);
                logger.debug("Trovata variabile {} con valore {}", nomeVariabile, premioMisura);
                mapVariabiliInput.add(new VariabileCalcolo(nomeVariabile, premioMisura));
                totalePremi = totalePremi.add(premioMisura);
            }
            logger.debug("Totale valore {}", totalePremi);
            mapVariabiliInput.add(new VariabileCalcolo(getNomeVariabilePremioTotale(), totalePremi));
        }
    }

    protected void initVariabiliSostegnoLordo(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
        CaricaPremioCalcolatoSostegno caricatorePremi = getCalcolatore();
        TipoVariabile[] variabiliPremi = getVariabiliPremiLordi();
        if (variabiliPremi != null && variabiliPremi.length > 0) {
            Map<TipoVariabile, BigDecimal> importiPremiLordi =
                    caricatorePremi.getImportoPremioCalcolato(istruttoria, variabiliPremi);
            BigDecimal totalePremi = BigDecimal.ZERO;
            BigDecimal premioMisura = BigDecimal.ZERO;
            for (TipoVariabile variabilePremio : variabiliPremi) {
                premioMisura = importiPremiLordi.getOrDefault(variabilePremio, BigDecimal.ZERO).max(BigDecimal.ZERO);
                TipoVariabile nomeVariabile = mappaTipoVariabile(variabilePremio);
                logger.debug("Trovata variabile {} con valore {}", nomeVariabile, premioMisura);
                mapVariabiliInput.add(new VariabileCalcolo(nomeVariabile, premioMisura));
                totalePremi = totalePremi.add(premioMisura);
            }
            logger.debug("Totale valore {}", totalePremi);
            mapVariabiliInput.add(new VariabileCalcolo(getNomeVariabilePremioTotaleLordo(), totalePremi));
        }
    }

    protected TipoVariabile mappaTipoVariabile(TipoVariabile nome) {
        return nome;
    }
    
    private final void initVariabiliFranchigiaGiaApplicataCalcoloPrecedente(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria)
    {
    	MapVariabili input = getMapVariabiliCalcoloPrecedente(istruttoria,DatiInput.class);
    	MapVariabili output = getMapVariabiliCalcoloPrecedente(istruttoria,DatiOutput.class);
    	calcolaVariabiliFranchigiaGiaApplicataCalcoloPrecedente(mapVariabiliInput,input,output);
    }

    protected void initVariabiliFranchigiaGiaApplicata(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
        DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
        mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFFRAPPDIS, valorePrecedentePerFranchigia.apply(domanda, Sostegno.DISACCOPPIATO)));
        mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFFRAPPACS, valorePrecedentePerFranchigia.apply(domanda, Sostegno.SUPERFICIE)));
        mapVariabiliInput.add(new VariabileCalcolo(TipoVariabile.DFFRAPPACZ, valorePrecedentePerFranchigia.apply(domanda, Sostegno.ZOOTECNIA)));
    }
    
    
    //metodo che è possibile utilizzare sia per calcolare le variabili di output che di input passando il tipo di dati da clacolare input/output   
    private <T extends DatiCalcoli> MapVariabili getMapVariabiliCalcoloPrecedente(IstruttoriaModel istruttoria,Class<T> tipoDati) {
        MapVariabili mapVariabiliCalcoloPrecedente = new MapVariabili();
        DomandaUnicaModel domandaUnicaModel = istruttoria.getDomandaUnicaModel();
        Sostegno sostegno = istruttoria.getSostegno();

        // partendo dalla domanda prendo tutte le istruttorie con PAGAMENTO_AUTORIZZATO
        Set<IstruttoriaModel> istruttorieDomande = domandaUnicaModel.getA4gtLavorazioneSostegnos().stream()
                .filter(ist -> sostegno.equals(ist.getSostegno()))
                .filter(ist -> ist.getStato().equals(StatoIstruttoria.PAGAMENTO_AUTORIZZATO)).collect(Collectors.toSet());

        if (istruttorieDomande != null && !istruttorieDomande.isEmpty()) {
            List<TransizioneIstruttoriaModel> transizioniIntersostegno = new ArrayList<>();
            for (IstruttoriaModel istruttoriaModel : istruttorieDomande) {
                transizioniIntersostegno.addAll(transizioneIstruttoriaDao.findTransizioneControlloIntersostegno(istruttoriaModel));
            }
            Optional<TransizioneIstruttoriaModel> optionalTransizione = transizioniIntersostegno.stream()
                    .max(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione));
            if (optionalTransizione.isPresent()) {
                Optional<PassoTransizioneModel> passoDisciplinaFinanziaria = optionalTransizione.get().getPassiTransizione()
                        .stream()
                        .filter(passo -> TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA.equals(passo.getCodicePasso())).findAny();
                if (passoDisciplinaFinanziaria.isPresent()) {
                    try {
                    	addVariabiliCalcolo(mapVariabiliCalcoloPrecedente, passoDisciplinaFinanziaria,tipoDati);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return mapVariabiliCalcoloPrecedente;
    }
    
	private final <T extends DatiCalcoli>  void addVariabiliCalcolo(MapVariabili mappaVariabili,Optional<PassoTransizioneModel> passoDisciplinaFinanziaria,Class<T> tipoDati) throws JsonParseException, JsonMappingException, IOException  {
		if(tipoDati.equals(DatiInput.class)){
			addVariabiliCalcoloInput(mappaVariabili, passoDisciplinaFinanziaria);
	    }else if(tipoDati.equals(DatiOutput.class)){
	    	addVariabiliCalcoloOutput(mappaVariabili, passoDisciplinaFinanziaria);
	    }
	}
	
	private final <T extends DatiCalcoli> void addVariabiliCalcoloInput(MapVariabili mappaVariabili,Optional<PassoTransizioneModel> passoDisciplinaFinanziaria) throws IOException {
		DatiInput input = objectMapper.readValue(passoDisciplinaFinanziaria.get().getDatiInput(), DatiInput.class);
    	input.getVariabiliCalcolo().stream()
                .filter(v -> CollectionUtils.isEmpty(getVariabiliInputCalcoloPrecedente()) ? false : getVariabiliInputCalcoloPrecedente().contains(v.getTipoVariabile()))
                .forEach(v -> mappaVariabili.add(new VariabileCalcolo(v.getTipoVariabile(), v.getValNumber())));
	}
	
	private final <T extends DatiCalcoli> void addVariabiliCalcoloOutput(MapVariabili mappaVariabili,Optional<PassoTransizioneModel> passoDisciplinaFinanziaria) throws IOException {
		 DatiOutput output = objectMapper.readValue(passoDisciplinaFinanziaria.get().getDatiOutput(), DatiOutput.class);
         output.getVariabiliCalcolo().stream()
                 .filter(v -> getVariabiliCalcoloPrecedente().contains(v.getTipoVariabile()))
                 .forEach(v -> mappaVariabili.add(new VariabileCalcolo(v.getTipoVariabile(), v.getValNumber())));
	}
    
    
    

    @Override
    public TipologiaPassoTransizione getPasso() {
        return TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA;
    }

    public abstract CaricaPremioCalcolatoSostegno getCalcolatore();

    public abstract TipoVariabile[] getVariabiliPremi();

    public abstract TipoVariabile[] getVariabiliPremiLordi();

    public abstract TipoVariabile getNomeVariabilePremioTotale();

    public abstract TipoVariabile getNomeVariabilePremioTotaleLordo();

    //identifica le variabili di output
    public abstract List<TipoVariabile> getVariabiliCalcoloPrecedente();
    
    //identifica le variabili di input
    public abstract List<TipoVariabile> getVariabiliInputCalcoloPrecedente();
    
    //calcola eventuali operazioni tra variabile di nput e output legate alla franchigia
    protected abstract void calcolaVariabiliFranchigiaGiaApplicataCalcoloPrecedente(MapVariabili mapVariabiliInput, MapVariabili input,MapVariabili output);
   

    public abstract void initVariabiliImportiGiaErogati(MapVariabili mapVariabiliInput, MapVariabili mapVariabiliInputPrecedente, MapVariabili mapVariabiliOutputPrecedente);
    
    protected BigDecimal extractVariabile(MapVariabili mapVariabiliOutputPrecedente, TipoVariabile tipoVariabile) {
        BigDecimal valore = null;

        VariabileCalcolo variabileCalcolo = mapVariabiliOutputPrecedente.get(tipoVariabile);
        if (variabileCalcolo != null) {
            valore = variabileCalcolo.getValNumber();
        }
        return (valore != null) ? valore : BigDecimal.ZERO;
    }

}
