package it.tndigitale.a4g.psr.business.service;

import it.tndigitale.a4g.psr.business.persistence.repository.DomandePsrDao;
import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPsr;
import it.tndigitale.a4g.psr.dto.DettaglioPagametoPsrRow;
import it.tndigitale.a4g.psr.dto.DomandaPsr;
import it.tndigitale.a4g.psr.dto.ImpegnoRichiestoPSRSuperficie;
import it.tndigitale.a4g.psr.dto.ImpegnoZooPascoloPsr;
import it.tndigitale.a4g.psr.dto.ImportiDomandaPsr;
import it.tndigitale.a4g.psr.dto.StatoDomandaPsr;
import it.tndigitale.a4g.psr.dto.TipoPagamento;
import it.tndigitale.a4g.psr.dto.UbaAlpeggiatePsr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static it.tndigitale.a4g.psr.dto.StatoOperazione.INIZIO_ISTRUTTORIA;
import static it.tndigitale.a4g.psr.dto.StatoOperazione.INIZIO_ISTRUTTORIA_DOMANDE_A_CAMPIONE;
import static it.tndigitale.a4g.psr.dto.StatoOperazione.ISTRUTTORIA_PARZIALE;
import static it.tndigitale.a4g.psr.dto.StatoOperazione.ISTRUTTORIA_PARZIALE_DOMANDE_A_CAMPIONE;
import static it.tndigitale.a4g.psr.dto.TipoPagamento.*;

@Service
public class DomandaPsrService {

  public static Map<String, DettaglioPagamentoPsrCalculator> dettagliPagametoMap;
  private static final Logger logger = LoggerFactory.getLogger(DomandaPsrService.class);
  static {
    dettagliPagametoMap = new HashMap<>();
    dettagliPagametoMap.put("10.1.1", new DettaglioPagamentoPsr10_1_1Calculator());
    dettagliPagametoMap.put("10.1.2", new DettaglioPagamentoPsr10_1_2Calculator());
    dettagliPagametoMap.put("10.1.3", new DettaglioPagamentoPsr10_1_3Calculator());
    dettagliPagametoMap.put("10.1.4", new DettaglioPagamentoPsr10_1_4Calculator());
    dettagliPagametoMap.put("11", new DettaglioPagamentoPsr11Calculator());
    dettagliPagametoMap.put("13.1.1", new DettaglioPagamentoPsr13_1_1Calculator());
  }


    @Autowired
    private DomandePsrDao domandePsrDao;

	/**
	 * query che recupera le domande
	 * negli anni di campagna dal 2015 fino all’anno corrente
	 * Relative ai seguenti MODULI:
	 *-Pagamenti agro-climatico, Agricoltura Biologica (Misure 10, 11) ed Indennità compensativa (Misura 13)
	 *-DOMANDA DI MODIFICA AI SENSI DELL'ART. 15 DEL REG. UE 809/2014 (Misure 10, 11 e Misura 13)
	 *-Pagamenti Misure 10, 11, 13 - DOMANDA DI RITIRO PARZIALE AI SENSI DELL ART. 3 DEL REG. (UE) 809/2014
	 *
	 */
	public List<DomandaPsr> consultazioneDomandePsr(String cuaa) {
		List<DomandaPsr> listaDomande = domandePsrDao.recuperaDomandePsr(cuaa);
		listaDomande.forEach(domanda -> {
		    domanda.setOperazioni(domandePsrDao.recuperaMisureInterventoDomandaPsr(domanda.getNumeroDomanda()));
            hideDomandaCampioneStatusFromSottostato(domanda);
        });
		return listaDomande;
	}

    public List<ImpegnoRichiestoPSRSuperficie> getImpegniRichiestiPSRSuperficie(Integer idDomanda) {
        List<ImpegnoRichiestoPSRSuperficie> impegniRichiestiPSRSuperficie = domandePsrDao.getImpegniRichiestiPSRSuperficie(idDomanda);
        for (ImpegnoRichiestoPSRSuperficie impegnoRichiestoPSRSuperficie : impegniRichiestiPSRSuperficie) {
            if ("M13_sa123".equals(impegnoRichiestoPSRSuperficie.getCodDestinazione())) {
                Optional<UbaAlpeggiatePsr> ubaAlpeggiatePsrSuperficie = domandePsrDao.getUBAAlpeggiatePsrSuperficie(idDomanda);
                ubaAlpeggiatePsrSuperficie.ifPresent(impegnoRichiestoPSRSuperficie::setUbaAlpeggiatePsr);
            }
            if ("M10.1.2_75".equals(impegnoRichiestoPSRSuperficie.getCodDestinazione()) || "M10.1.2_90".equals(impegnoRichiestoPSRSuperficie.getCodDestinazione())) {
                impegnoRichiestoPSRSuperficie.addPascoliPsr(domandePsrDao.getQuadroPascoliByIdDomanda(idDomanda));
            }
        }
        return impegniRichiestiPSRSuperficie;
    }

    public DomandaPsr getDomandaPsrSuperficieById(Integer idDomanda) {
        DomandaPsr domandaPsr = domandePsrDao.getDomandaPsrSuperficieById(idDomanda);
        domandaPsr.setOperazioni(domandePsrDao.recuperaMisureInterventoDomandaPsr(domandaPsr.getNumeroDomanda()));
        hideDomandaCampioneStatusFromSottostato(domandaPsr);
        return domandaPsr;
    }

    public List<ImpegnoZooPascoloPsr> getImpegniZooPascoliByIdDomanda(Integer idDomanda) {
        return domandePsrDao.getImpegniZooPascoliByIdDomanda(idDomanda);
    }

    public List<StatoDomandaPsr> getStatiOperazioneByIdDomanda(Integer idDomanda) {
        return domandePsrDao.getStatiOperazioneByIdDomanda(idDomanda);
    }

    public boolean isCodOperazioneOfDomandaCampione(String idDomanda, String codOperazione){
        Optional<Boolean> codOperazioneForIdDomandaCampione = domandePsrDao.isCodOperazioneForIdDomandaCampione(idDomanda, codOperazione);
        return codOperazioneForIdDomandaCampione.orElse(false);
    }

    public ImportiDomandaPsr getImportiByIdDomandaAndAnno(String cuaa, Integer anno) {
        return domandePsrDao.getImportiByIdDomandaAndAnno(cuaa, anno);
    }


    public DettaglioPagamentoPsr getDettaglioPagamento(String idDomanda, Integer annoDiCampagna, String codiceOperazione, String tipoDiPagamento) {
        boolean isCampione = isCodOperazioneOfDomandaCampione(idDomanda, codiceOperazione);
        String tipoPagamentoCodiceCalcolo = tipoDiPagamento;
        if (isCampione) {
            tipoPagamentoCodiceCalcolo = mapTipoPagamentoDomandaCampione(tipoDiPagamento);
        }
        List<DettaglioPagametoPsrRow> dettaglioPagamento = domandePsrDao.getDettaglioPagamento(idDomanda, annoDiCampagna, codiceOperazione, tipoPagamentoCodiceCalcolo);
        if (dettaglioPagamento.isEmpty() && "11".equals(codiceOperazione)) {
            dettaglioPagamento = domandePsrDao.getDettaglioPagamento(idDomanda, annoDiCampagna, codiceOperazione, mapTipoPagamentoDomandaPat(tipoDiPagamento));
        }
        if(dettaglioPagamento.isEmpty()){
          logger.error("La query di dettaglio pagmaento non ha tornato risultati");
          throw new NoSuchElementException("Query did not return any result");
        }
        DettaglioPagamentoPsrCalculator calcoloDettaglioPagamentoPsr = dettagliPagametoMap.get(codiceOperazione);
        return calcoloDettaglioPagamentoPsr.calculate(dettaglioPagamento, isCampione);
    }

    public BigDecimal getImportoCalcolato(Integer idDomanda, String codMisura, String tipoPagamento) {
        return domandePsrDao.getImportoCalcolatoByIdDomandaMisuraAndTipoPagamento(idDomanda, codMisura, tipoPagamento);
    }


    public BigDecimal getTotaleImportoCalcolato(Integer idDomanda) {
        List<StatoDomandaPsr> statiOperazioneByIdDomanda = domandePsrDao.getStatiOperazioneByIdDomanda(idDomanda);
        BigDecimal totaleImpotoCalcolato = BigDecimal.ZERO;
        for (StatoDomandaPsr statoOperazione : statiOperazioneByIdDomanda) {
            BigDecimal importoCalcolato = domandePsrDao.getImportoCalcolatoByIdDomandaMisuraAndTipoPagamento(
                    idDomanda, statoOperazione.getCodOperazione(), statoOperazione.getTipoPagamento());
            if (importoCalcolato != null) {
                totaleImpotoCalcolato = totaleImpotoCalcolato.add(importoCalcolato);
            }
        }
        return totaleImpotoCalcolato;
    }

    private String mapTipoPagamentoDomandaCampione(String tipoPagamento) {
        switch (TipoPagamento.valueOf(tipoPagamento)) {
            case SALDO:
            case CAMPIONE:
                return CAMPIONE.name();
            case INTEGRAZIONE:
            case INTCAMPIONE:
            default:
                return INTCAMPIONE.name();
        }
    }

    private String mapTipoPagamentoDomandaPat(String tipoPagamento) {
        switch (TipoPagamento.valueOf(tipoPagamento)) {
            case INTEGRAZIONE:
                return "INTEGR_PAT";
            case SALDO:
            case CAMPIONE:
            case INTCAMPIONE:
            default:
                return tipoPagamento + "_PAT";
        }
    }

    private void hideDomandaCampioneStatusFromSottostato(DomandaPsr domanda){
        if (INIZIO_ISTRUTTORIA_DOMANDE_A_CAMPIONE.equals(domanda.getSottoStato())) {
          domanda.setSottoStato(INIZIO_ISTRUTTORIA);
        }
        if (ISTRUTTORIA_PARZIALE_DOMANDE_A_CAMPIONE.equals(domanda.getSottoStato())) {
          domanda.setSottoStato(ISTRUTTORIA_PARZIALE);
        }
    }
}
