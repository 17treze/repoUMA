package it.tndigitale.a4gistruttoria.service.businesslogic.verbale;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.action.acz.InitVariabiliNumeroUbaAmmessiConsumer;
import it.tndigitale.a4gistruttoria.component.StampaComponent;
import it.tndigitale.a4gistruttoria.dto.DatiIstruttoriaACZStampa;
import it.tndigitale.a4gistruttoria.dto.InfoLiquidazioneDomandaStampa;
import it.tndigitale.a4gistruttoria.dto.PassoLavorazioneStampa;
import it.tndigitale.a4gistruttoria.dto.VariabileStampa;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.ElencoLiquidazioneModel;
import it.tndigitale.a4gistruttoria.repository.model.InterventoZootecnico;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.TransizioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.strategy.DatiControlliSostegnoAbstract;
import it.tndigitale.a4gistruttoria.util.CustomCollectors;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Service
@Transactional(readOnly = true)
public class VerbaleIstruttoriaZootecniaService extends VerbaleIstruttoriaService {

    private static final Logger logger = LoggerFactory.getLogger(VerbaleIstruttoriaZootecniaService.class);
    private static final String NON_APPLICABILE = "NON APPLICABILE";
    private static final String VARIABILE_CAPI_RICHIESTI = "ACZCAPIRIC";
    private static final String TEMPLATE_NAME = "template/verbaleIstruttoriaZootecnia.docx";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private StampaComponent stampaComponent;

    @Autowired
    private InitVariabiliNumeroUbaAmmessiConsumer ubaAmmessiConsumer;

    @Autowired
    private PassoTransizioneDao daoPassiLavSostegno;

    @Autowired
    TransizioneIstruttoriaService transizioneService;

    @Autowired
    DatiControlliSostegnoAbstract datiControlliSostegnoAbstract;

    @Override
    public byte[] stampa(final Long idIstruttoria) throws Exception {
        Optional<IstruttoriaModel> istruttoriaOpt = istruttoriaDao.findById(idIstruttoria);
        DomandaUnicaModel domandaUnicaModel = getDomandaUnica(idIstruttoria);
        InfoLiquidazioneDomandaStampa infoLiquidazione = new InfoLiquidazioneDomandaStampa();
        infoLiquidazione.setDomandaNonRicevibile(false);
        infoLiquidazione.setDomandaNonAmmissibile(false);
        infoLiquidazione.setDomandaNonLiquidabile(false);
        List<String> interventiEsclusi = new ArrayList<>();

        DatiIstruttoriaACZStampa datiDomandaStampa = new DatiIstruttoriaACZStampa(getDatiDomandaStampa(istruttoriaOpt.get()));

        getEsitoRicevibilita(datiDomandaStampa, domandaUnicaModel);

        if (domandaUnicaModel.getStato().equals(StatoDomanda.NON_RICEVIBILE)) {
            infoLiquidazione.setDomandaNonRicevibile(true);
        } else if (istruttoriaOpt.isPresent()) {
            IstruttoriaModel istruttoria = istruttoriaOpt.get();
            popolaInfoLiquidazione(infoLiquidazione, istruttoria);
            if (!infoLiquidazione.getDomandaNonAmmissibile()) {
                popolaTransizioneCalcolo(datiDomandaStampa, istruttoria, interventiEsclusi);
                popolaTransizioneLiquidabilita(datiDomandaStampa, istruttoria);
                popolaTransizioneControlloIntersostegno(datiDomandaStampa, istruttoria, interventiEsclusi);
            }
        }

        datiDomandaStampa.setInfoLiquidazione(infoLiquidazione);
        String jsonToken = mapper.writeValueAsString(datiDomandaStampa);

        return stampaComponent.stampaPDF_A(jsonToken, TEMPLATE_NAME);
    }

    private void popolaInfoLiquidazione(final InfoLiquidazioneDomandaStampa infoLiquidazione, final IstruttoriaModel istruttoria) {
        ElencoLiquidazioneModel elencoLiquidazione = istruttoria.getElencoLiquidazione();
        if (elencoLiquidazione != null) {
            infoLiquidazione.setCodiceElencoLiquidazione(elencoLiquidazione.getCodElenco());
        } else {
            if (istruttoria.getStato().equals(StatoIstruttoria.NON_AMMISSIBILE)) {
                infoLiquidazione.setDomandaNonAmmissibile(true);
            }
            if (istruttoria.getStato().equals(StatoIstruttoria.NON_LIQUIDABILE)) {
                infoLiquidazione.setDomandaNonLiquidabile(true);
            }
        }
    }

    private void popolaTransizioneCalcolo(DatiIstruttoriaACZStampa datiDomandaStampa, IstruttoriaModel istruttoria, List<String> interventiEsclusi) {
        Optional<TransizioneIstruttoriaModel> transizioneIstruttoriaModelOptional = getUltimaTransizione(istruttoria, StatoIstruttoria.CONTROLLI_CALCOLO_OK);
        if (transizioneIstruttoriaModelOptional.isPresent()) {
            TransizioneIstruttoriaModel transizione = transizioneIstruttoriaModelOptional.get();
            List<PassoTransizioneModel> a4gtPassiLavSostegno = daoPassiLavSostegno.findByTransizioneIstruttoria(transizione);
            if (!a4gtPassiLavSostegno.isEmpty()) {
                a4gtPassiLavSostegno.forEach(p -> {
                    if (p.getCodicePasso().equals(TipologiaPassoTransizione.CALCOLO_ACZ)) {
                        popolaControlliAmmissibilita(datiDomandaStampa, p, interventiEsclusi);
                    }
                });
            }
        }
    }

    private void popolaTransizioneLiquidabilita(DatiIstruttoriaACZStampa datiDomandaStampa, IstruttoriaModel istruttoria) {
        Optional<TransizioneIstruttoriaModel> transizioneIstruttoriaModelOptional = getUltimaTransizione(istruttoria, StatoIstruttoria.LIQUIDABILE);
        if (transizioneIstruttoriaModelOptional.isPresent()) {
            TransizioneIstruttoriaModel transizione = transizioneIstruttoriaModelOptional.get();
            List<PassoTransizioneModel> a4gtPassiLavSostegno = daoPassiLavSostegno.findByTransizioneIstruttoria(transizione);
            if (!a4gtPassiLavSostegno.isEmpty()) {
                a4gtPassiLavSostegno.forEach(p -> {
                    if (p.getCodicePasso().equals(TipologiaPassoTransizione.LIQUIDABILITA)) {
                        popolaEsitoControlloLiquidabilita(datiDomandaStampa, p);
                    }
                });
            }
        }
    }

    private void popolaTransizioneControlloIntersostegno(DatiIstruttoriaACZStampa datiDomandaStampa, IstruttoriaModel istruttoria, List<String> interventiEsclusi) {
        Optional<TransizioneIstruttoriaModel> transizioneIstruttoriaModelOptional = getUltimaTransizione(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
        if (!transizioneIstruttoriaModelOptional.isPresent()) {
            transizioneIstruttoriaModelOptional = getUltimaTransizione(istruttoria, StatoIstruttoria.NON_LIQUIDABILE);
        }
        if (transizioneIstruttoriaModelOptional.isPresent()) {
            TransizioneIstruttoriaModel transizione = transizioneIstruttoriaModelOptional.get();
            List<PassoTransizioneModel> a4gtPassiLavSostegno = daoPassiLavSostegno.findByTransizioneIstruttoria(transizione);
            if (!a4gtPassiLavSostegno.isEmpty()) {
                a4gtPassiLavSostegno
                      .forEach(p -> {
                    if (p.getCodicePasso().equals(TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA)) {
                        popolaCalcoloDisciplinaFinanziaria(datiDomandaStampa, p, interventiEsclusi);
                    } else {
                        popolaEsitoControlloLiquidazione(datiDomandaStampa, p);
                    }
                });
            }
        }
    }

    private void popolaControlliAmmissibilita(DatiIstruttoriaACZStampa datiDomandaStampa, PassoTransizioneModel p, List<String> interventiEsclusi) {
        List<PassoLavorazioneStampa> passiLavorazioneStampa = new ArrayList<>();

        try {
            DatiInput input = new DatiInput();
            DatiOutput output = new DatiOutput();
            if (p.getDatiInput() != null) {
                input = mapper.readValue(p.getDatiInput(), DatiInput.class);
            }
            if (p.getDatiOutput() != null) {
                output = mapper.readValue(p.getDatiOutput(), DatiOutput.class);
            }
            popolaVariabiliGenerali(datiDomandaStampa, input);
            popolaCalcoloAccoppiatoZootecnia(passiLavorazioneStampa, input, output, interventiEsclusi,p.getCodicePasso());
        } catch (IOException e) {
            logger.error("Stampa domande : errore non previsto", e);
        }
        datiDomandaStampa.setPassoLavStampa(passiLavorazioneStampa);
    }

    private void popolaVariabiliGenerali(DatiIstruttoriaACZStampa datiDomandaStampa, final DatiInput datiInput) {
        datiInput.getVariabiliCalcoloDaStampare().forEach(v -> {
            VariabileStampa vStampa = new VariabileStampa();
            if(v.getTipoVariabile().equals(TipoVariabile.PERCRIT)){
                v.setValNumber(v.getValNumber().multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
            }
            setValoreVariabileDaStampare(v, vStampa);
            TipoVariabile tipoVariabile = v.getTipoVariabile();
            switch (tipoVariabile) {
                case ACZUBA_LAT:
                    datiDomandaStampa.setAczUbaLat(vStampa.getValore());
                    break;
                case ACZUBA_MAC:
                    datiDomandaStampa.setAczUbaMac(vStampa.getValore());
                    break;
                case ACZUBA_OVI:
                    datiDomandaStampa.setAczUbaOvi(vStampa.getValore());
                    break;
                case ACZUBATOT:
                    datiDomandaStampa.setAczUbaTot(vStampa.getValore());
                    break;
                case AGRATT:
                    datiDomandaStampa.setAgratt(vStampa.getValore());
                    break;
                case AZCMPBOV:
                    datiDomandaStampa.setAzcmpbov(vStampa.getValore());
                    break;
                case AZCMPOVI:
                    datiDomandaStampa.setAzcmpovi(vStampa.getValore());
                    break;
                case PERCRIT:
                    datiDomandaStampa.setPercrit(vStampa.getValore());
                    break;
                default:
                    break;
            }
        });
    }

    private void popolaCalcoloAccoppiatoZootecnia(List<PassoLavorazioneStampa> passiLavorazioneStampa, DatiInput input, DatiOutput output, List<String> interventiEsclusi,TipologiaPassoTransizione tipologiaPasso) {
        List<VariabileStampa> variabiliInput = null, variabiliOutput = null;
        PassoLavorazioneStampa passoLavSostegno = null;
        List<InterventoZootecnico> listaInterventi = getListaInterventiStampa();
        for (InterventoZootecnico intervento : listaInterventi) {
            passoLavSostegno = new PassoLavorazioneStampa();
            variabiliInput = popolaVariabiliCalcoloInput(input, intervento.getCodiceAgea(),tipologiaPasso);
            if (variabiliInput != null) {
                variabiliOutput = popolaVariabiliCalcoloOutput(output, intervento.getCodiceAgea(),tipologiaPasso);
                passoLavSostegno.setTipoPassoLavorazione(getDescrizioneIntervento(intervento));
                passoLavSostegno.setVariabiliInput(variabiliInput);
                passoLavSostegno.setVariabiliOutput(variabiliOutput);
                passiLavorazioneStampa.add(passoLavSostegno);
            }else{
                interventiEsclusi.add(intervento.getCodiceAgea());
            }
        }
    }

    private void popolaEsitoControlloLiquidabilita(DatiIstruttoriaACZStampa datiDomandaStampa, PassoTransizioneModel p) {
        DatiSintesi datiSintesi = new DatiSintesi();
        try {
            if (p.getDatiSintesiLavorazione() != null) {
                datiSintesi = mapper.readValue(p.getDatiSintesiLavorazione(), DatiSintesi.class);
            }
            datiSintesi.getEsitiControlli().forEach(esitiControlli -> {
                TipoControllo tipoControllo = esitiControlli.getTipoControllo();
                switch (tipoControllo) {
                    case BRIDUSDL037_iban:
                        datiDomandaStampa.setValiditaIBAN(esitiControlli.recuperaValoreString());
                        break;
                    case BRIDUSDL038_titolare:
                        datiDomandaStampa.setTitolareDeceduto(esitiControlli.recuperaValoreString());
                        break;
                    case BRIDUSDL039_agea:
                        datiDomandaStampa.setListaNeraAGEA(esitiControlli.recuperaValoreString());
                        break;
                    default:
                        break;
                }
            });
        } catch (IOException e) {
            logger.error("Stampa domande : errore non previsto", e);
        }
    }

    private void popolaEsitoControlloLiquidazione(DatiIstruttoriaACZStampa datiDomandaStampa, PassoTransizioneModel p) {
        DatiSintesi datiSintesi = new DatiSintesi();
        datiDomandaStampa.setCertificazioneAntimafia(NON_APPLICABILE);
        try {
            if (p.getDatiSintesiLavorazione() != null) {
                datiSintesi = mapper.readValue(p.getDatiSintesiLavorazione(), DatiSintesi.class);
            }
            datiSintesi.getEsitiControlli().forEach(esitiControlli -> {
                TipoControllo tipoControllo = esitiControlli.getTipoControllo();
                switch (tipoControllo) {
                    case BRIDUSDS040_importoMinimo:
                        datiDomandaStampa.setImportoMinimoLiquidabile300(esitiControlli.recuperaValoreString());
                        break;
                    case BRIDUSDS050_esitoAntimafia:
                        datiDomandaStampa.setCertificazioneAntimafia(esitiControlli.recuperaValoreString());
                        break;
                    case importoMinimoAntimafia:
                        datiDomandaStampa.setImportoMinimoAntimafia(esitiControlli.recuperaValoreString());
                        break;
                    default:
                        break;
                }
            });
        } catch (IOException e) {
            logger.error("Stampa domande : errore non previsto", e);
        }
    }

    private void popolaCalcoloDisciplinaFinanziaria(DatiIstruttoriaACZStampa datiDomandaStampa, PassoTransizioneModel p, final List<String> interventiEsclusi) {
        List<PassoLavorazioneStampa> passiDisciplinaFinanziaria = new ArrayList<>();
        PassoLavorazioneStampa passoLavSostegno = new PassoLavorazioneStampa();
        DatiInput input = new DatiInput();
        DatiOutput output = new DatiOutput();

        try {
            if (p.getDatiInput() != null) {
                input = mapper.readValue(p.getDatiInput(), DatiInput.class);
            }
            if (p.getDatiOutput() != null) {
                output = mapper.readValue(p.getDatiOutput(), DatiOutput.class);
            }
            List<VariabileStampa> variabiliInput = new ArrayList<>();
            List<VariabileStampa> variabiliOutput = new ArrayList<>();
            popolaVariabiliCalcoloDisciplinaFinanziariaInput(input, output, variabiliInput,p.getCodicePasso());
            popolaVariabiliCalcoloDisciplinaFinanziariaOutput(input, output, variabiliOutput,p.getCodicePasso());

            List<VariabileStampa> variabiliInputCalcolate = removeInterventiEsclusi(interventiEsclusi, variabiliInput);
            List<VariabileStampa> variabiliOutputCalcolate = removeInterventiEsclusi(interventiEsclusi, variabiliOutput);
            
            passoLavSostegno.setVariabiliInput(variabiliInputCalcolate);
            passoLavSostegno.setVariabiliOutput(variabiliOutputCalcolate);
            passoLavSostegno.setTipoPassoLavorazione(p.getCodicePasso().getDescrizione());
            passiDisciplinaFinanziaria.add(passoLavSostegno);
        } catch (IOException e) {
            logger.error("Stampa domande : errore non previsto", e);
        }
        datiDomandaStampa.setDisciplinaFinanziaria(passiDisciplinaFinanziaria);
    }

    private List<VariabileStampa> removeInterventiEsclusi(List<String> interventiEsclusi, List<VariabileStampa> variabiliStampa) {
        List<VariabileStampa> variabiliOutput = new ArrayList<>();
        variabiliStampa.forEach(variabileStampa -> {
            String codice = variabileStampa.getCodiceVariabile();
            if(codice.indexOf("_")>0){
                String code = codice.substring(codice.indexOf("_")+1);
                if(!interventiEsclusi.contains(code)){
                    variabiliOutput.add(variabileStampa);
                }
            }else{
                variabiliOutput.add(variabileStampa);
            }
        });
        return variabiliOutput;
    }

    private List<VariabileStampa> popolaVariabiliCalcoloInput(final DatiInput datiInput, final String codiceAgea,TipologiaPassoTransizione tipologiaPasso) {
        List<VariabileStampa> listVariabiliStampa = new ArrayList<>();
        String valoreCapiRichiesti = null;
        for (VariabileCalcolo v : datiInput.getVariabiliCalcoloDaStampare()) {
            VariabileStampa vStampa = new VariabileStampa();
            TipoVariabile tipoVariabile = v.getTipoVariabile();
            if (tipoVariabile.toString().endsWith(codiceAgea)) {
                vStampa.setCodiceVariabile(tipoVariabile.toString());
                vStampa.setDescrizioneVariabile(tipoVariabile.getDescrizione());
                setOrdineVariabileDaStampare(v, vStampa, tipologiaPasso);
                setValoreVariabileDaStampare(v, vStampa);
                if (tipoVariabile.toString().startsWith(VARIABILE_CAPI_RICHIESTI)) {
                    valoreCapiRichiesti = vStampa.getValore();
                }
                if (vStampa.getValore() != null) {
                    listVariabiliStampa.add(vStampa);
                }
            }
        }

        if (valoreCapiRichiesti != null) {
            return getListaOrdinataVariabiliStampa(listVariabiliStampa);
        }

        return null;
    }

    private List<VariabileStampa> popolaVariabiliCalcoloOutput(final DatiOutput datiOutput, final String codiceAgea,TipologiaPassoTransizione tipologiaPasso) {
        List<VariabileStampa> listVariabiliStampa = new ArrayList<>();
        for (VariabileCalcolo v : datiOutput.getVariabiliCalcoloDaStampare()) {
            VariabileStampa vStampa = new VariabileStampa();
            TipoVariabile tipoVariabile = v.getTipoVariabile();
            if (tipoVariabile.toString().endsWith(codiceAgea)) {
                vStampa.setCodiceVariabile(tipoVariabile.toString());
                vStampa.setDescrizioneVariabile(tipoVariabile.getDescrizione());
                setOrdineVariabileDaStampare(v, vStampa, tipologiaPasso);
                setValoreVariabileDaStampare(v, vStampa);
                if (vStampa.getValore() != null) {
                    listVariabiliStampa.add(vStampa);
                }
            }
        }

        return getListaOrdinataVariabiliStampa(listVariabiliStampa);
    }

    private List<VariabileStampa> getListaOrdinataVariabiliStampa(List<VariabileStampa> listVariabiliStampa) {
        List<VariabileStampa> sortedList = null;

        if (!listVariabiliStampa.isEmpty()) {
            sortedList = listVariabiliStampa.stream()
                    .sorted(Comparator.comparingLong(VariabileStampa::getOrdine))
                    .collect(Collectors.toList());
        }
        return sortedList;
    }

    private void popolaVariabiliCalcoloDisciplinaFinanziariaInput(final DatiInput input, final DatiOutput output, final List<VariabileStampa> listVariabili,TipologiaPassoTransizione tipologiaPasso) {
        popolaVariabiliCalcoloDisciplinaFinanziaria(getListaTipoVariabileInputDisciplinaFinanziaria(), input, output, listVariabili,tipologiaPasso);
    }

    private void popolaVariabiliCalcoloDisciplinaFinanziariaOutput(final DatiInput input, final DatiOutput output, final List<VariabileStampa> listVariabili,TipologiaPassoTransizione tipologiaPasso) {
        popolaVariabiliCalcoloDisciplinaFinanziaria(getListaTipoVariabileOutputDisciplinaFinanziaria(), input, output, listVariabili,tipologiaPasso);
    }

    private void popolaVariabiliCalcoloDisciplinaFinanziaria(final List<TipoVariabile> variabiliDisciplinaFinanziaria, final DatiInput input, final DatiOutput output, final List<VariabileStampa> listVariabili,TipologiaPassoTransizione tipologiaPasso) {
        variabiliDisciplinaFinanziaria.forEach(varInput -> {
            VariabileStampa vStampa = new VariabileStampa();
            VariabileCalcolo v = null;
            try {
                v = input.getVariabiliCalcolo().stream().filter(p -> p.getTipoVariabile().equals(varInput)).collect(CustomCollectors.toSingleton());
            } catch (IllegalStateException e) {
                logger.error("ATTENZIONE! Variabile {} non presente nella lista delle variabili!", varInput);
            }
            try {
                if (v == null) {
                    v = output.getVariabiliCalcolo().stream().filter(p -> p.getTipoVariabile().equals(varInput)).collect(CustomCollectors.toSingleton());
                }
                
                vStampa.setCodiceVariabile(v.getTipoVariabile().toString());
                vStampa.setDescrizioneVariabile(v.getTipoVariabile().getDescrizione());
                setValoreVariabileDaStampare(v, vStampa);
                if (vStampa.getValore() != null) {
                    listVariabili.add(vStampa);
                }
            } catch (IllegalStateException e) {
                logger.error("ATTENZIONE! Variabile {} non presente nella lista delle variabili!", varInput);
            }
        });
    }


    private void setValoreVariabileDaStampare(VariabileCalcolo variabileCalcolo, VariabileStampa variabileStampa) {
    	variabileStampa.setValore(variabileCalcolo.recuperaValoreString());
    }
    
    private String getDescrizioneIntervento(InterventoZootecnico interventoZootecnico) {
        switch (interventoZootecnico.getCodiceAgea()) {
            case "310":
                return "M1 - Vacche da latte (310)";
            case "311":
                return "M2 - Vacche da latte in zone di montagna (311)";
            case "313":
                return "M4 - Vacche nutrici (313)";
            case "315":
                return "M5 - Bovini macellati (315)";
            case "316":
                return "M19 - Bovini macellati - Allevati 12 mesi (316)";
            case "318":
                return "M19 - Bovini macellati - Etichettatura (318)";
            case "320":
                return "M6 - Agnelle (320)";
            case "321":
                return "M7 - Ovicaprini macellati (320)";
            case "322":
                return "M20 - Vacche nutrici non iscritte (320)";
            default:
                return null;
        }
    }

   
    private List<TipoVariabile> getListaTipoVariabileInputDisciplinaFinanziaria() {
        List<TipoVariabile> variabiliDisciplinaFinanziariaInput = new ArrayList<TipoVariabile>();
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFPERC);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFR);
        
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.BPSIMPCALCFIN);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.GREIMPCALCFIN);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.GIOIMPCALCFIN);
		variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DISIMPCALC); 
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALC_310);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALC_311);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALC_313);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALC_322);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALC_315);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALC_316);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALC_318);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALC_320);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALC_321);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALC);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALCLORDO_310);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALCLORDO_311);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALCLORDO_313);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALCLORDO_322);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALCLORDO_315);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALCLORDO_316);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALCLORDO_318);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALCLORDO_320);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALCLORDO_321);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALCLORDO);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFIMPLIPAGACZ_310);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFIMPLIPAGACZ_311);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFIMPLIPAGACZ_313);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFIMPLIPAGACZ_322);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFIMPLIPAGACZ_315);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFIMPLIPAGACZ_316);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFIMPLIPAGACZ_318);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFIMPLIPAGACZ_320);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFIMPLIPAGACZ_321);
       
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFRAPPDIS);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFRAPPACZ);
        //visualizzare solo le variabili con interventi richiesti
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFRAPPACZ_310);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFRAPPACZ_311);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFRAPPACZ_313);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFRAPPACZ_322);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFRAPPACZ_315);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFRAPPACZ_316);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFRAPPACZ_318);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFRAPPACZ_320);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFRAPPACZ_321);
         
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.DFFRAPPACS);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACZIMPCALCTOT);
        variabiliDisciplinaFinanziariaInput.add(TipoVariabile.ACSIMPCALCTOT);
      
        return variabiliDisciplinaFinanziariaInput;
        
        
    }

    private List<TipoVariabile> getListaTipoVariabileOutputDisciplinaFinanziaria() {
        List<TipoVariabile> variabiliDisciplinaFinanziariaOutput = new ArrayList<TipoVariabile>();
        
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFIMPLIQACZLORDO);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGLORACZ);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGLORACZ_310);
    	variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGLORACZ_311);
    	variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGLORACZ_313);
    	variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGLORACZ_322);
    	variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGLORACZ_315);
    	variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGLORACZ_316);
    	variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGLORACZ_318);
    	variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGLORACZ_320);
    	variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGLORACZ_321);
    	variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGACZ);
    	variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGACZ_310);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGACZ_311);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGACZ_313);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGACZ_322);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGACZ_315);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGACZ_316);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGACZ_318);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGACZ_320);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFFRPAGACZ_321);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFIMPDFDISACZ_310);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFIMPDFDISACZ_311);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFIMPDFDISACZ_313);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFIMPDFDISACZ_322);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFIMPDFDISACZ_315);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFIMPDFDISACZ_316);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFIMPDFDISACZ_318);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFIMPDFDISACZ_320);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFIMPDFDISACZ_321);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFIMPRIDACZ);
        variabiliDisciplinaFinanziariaOutput.add(TipoVariabile.DFIMPLIQACZ);
        
        return variabiliDisciplinaFinanziariaOutput;        
    }

    private List<InterventoZootecnico> getListaInterventiStampa() {
        List<InterventoZootecnico> listaInterventi = new ArrayList<>();
        listaInterventi.add(InterventoZootecnico.VACCA_LATTE);
        listaInterventi.add(InterventoZootecnico.VACCA_LATTE_MONTANA);
        listaInterventi.add(InterventoZootecnico.VACCA_NUTRICE);
        listaInterventi.add(InterventoZootecnico.BOVINO_MACELLATO);
        listaInterventi.add(InterventoZootecnico.AGNELLA);
        listaInterventi.add(InterventoZootecnico.OVICAPRINO_MACELLATO);
        listaInterventi.add(InterventoZootecnico.BOVINO_MACELLATO_12MESI);
        listaInterventi.add(InterventoZootecnico.BOVINO_MACELLATO_ETICHETTATO);
        listaInterventi.add(InterventoZootecnico.VACCA_NUTRICE_NON_ISCRITTA);
        return listaInterventi;
    }

}
