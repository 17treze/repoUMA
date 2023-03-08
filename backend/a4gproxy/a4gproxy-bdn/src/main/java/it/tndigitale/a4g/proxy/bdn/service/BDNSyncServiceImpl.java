package it.tndigitale.a4g.proxy.bdn.service;

import java.util.ArrayList;
import java.util.List;

import it.tndigitale.a4g.proxy.bdn.config.BdnConstants;
import it.tndigitale.a4g.proxy.bdn.dto.ErroreDO;
import it.tndigitale.a4g.proxy.bdn.dto.StatoSincronizzazioneDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.proxy.bdn.repository.interfaces.StatoSincronizzazioneBdnDAO;
import it.tndigitale.a4g.proxy.bdn.service.manager.ConsistenzaAlPascoloPAC2015Manager;
import it.tndigitale.a4g.proxy.bdn.service.manager.ConsistenzaAllevamentoManager;
import it.tndigitale.a4g.proxy.bdn.service.manager.IngressiAlPascoloPartiteOviniManager;
import it.tndigitale.a4g.proxy.bdn.service.manager.UsciteDaPascoloPartiteOviniManager;

@Service
//@Transactional
public class BDNSyncServiceImpl implements BDNSyncService {

	private Logger log = LoggerFactory.getLogger(BDNSyncServiceImpl.class);

	@Autowired
	ConsistenzaAlPascoloPAC2015Manager consistenzaManager;
	@Autowired
	IngressiAlPascoloPartiteOviniManager ingressiAlPascolo;
	@Autowired
	UsciteDaPascoloPartiteOviniManager usciteDaPascolo;
	@Autowired
	ConsistenzaAllevamentoManager consistenzaAllevamento;
	@Autowired
	StatoSincronizzazioneBdnDAO statoSincronizzazioneDao;

	@Autowired
	BDNSyncService syncService;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean syncDatiDomandaUnica(String cuaa, Integer annoCampagna) {
		// caricamento dei soli dati del cuaa passato come parametro
		log.debug("syncDatiDomandaUnica");
		return caricaDatiBDNDomandaUnica(cuaa, annoCampagna);
	}

	@Override
	public Long syncDatiDomandaUnicaPerAnno(Integer annoCampagna) {
		// caricamento dei dati per tutti i cuaa associati ad un anno di campagna
		Long aggiornamentoCompletato = 1L;
		List<ErroreDO> listaErrori = new ArrayList<>();
		log.debug("Inizio esecuzione syncDatiDomandaUnicaPerAnno");
		listaErrori = consistenzaManager.inserimentoMassivo(annoCampagna);

		log.debug("Inizio esecuzione ingressiAlPascolo.inserimentoMassivo ");
		listaErrori.addAll(ingressiAlPascolo.inserimentoMassivo(annoCampagna));

		log.debug("Inizio esecuzione usciteDaPascolo.inserimentoMassivo ");
		listaErrori.addAll(usciteDaPascolo.inserimentoMassivo(annoCampagna));

		log.debug("Inizio esecuzione consistenzaAllevamento.inserimentoMassivo ");
		listaErrori.addAll(consistenzaAllevamento.inserimentoMassivo(annoCampagna));

		if (!listaErrori.isEmpty()) {
			log.debug("Esecuzione termiata per syncDatiDomandaUnicaPerAnno con Errori");
			aggiornamentoCompletato = 0L;
		}
		log.debug("Fine esecuzione syncDatiDomandaUnicaPerAnno ");
		return aggiornamentoCompletato;
	}

	@Transactional
	private boolean caricaDatiBDNDomandaUnica(String cuaa, Integer annoCampagna) {
		log.debug("caricaDatiBDNDomandaUnica");
		boolean aggiornamentoCompletato = true;
		List<ErroreDO> listaErrori = new ArrayList<>();
		ErroreDO errore = null;
		log.debug("Inizio esecuzione caricaDatiBDN_DomandaUnica per CUAA {}", cuaa);
		listaErrori = consistenzaManager.inserimentoConsistenzaAlPascoloPAC2015PerCUAA(cuaa, annoCampagna);

		log.debug("Inizio esecuzione inserimentoIngressiAlPascoloPartiteOvini per CUAA {}", cuaa);
		errore = ingressiAlPascolo.inserimentoIngressiAlPascoloPartiteOvini(cuaa, annoCampagna);

		log.debug("Inizio esecuzione inserimentoUsciteDaPascoloPartiteOvini per CUAA {}", cuaa);
		errore = usciteDaPascolo.inserimentoUsciteDaPascoloPartiteOvini(cuaa, annoCampagna);

		log.debug("Inizio esecuzione caricaConsistenzaAllevamento per CUAA {}", cuaa);
		errore = consistenzaAllevamento.inserimentoConsistenzaAllevamento(cuaa, annoCampagna);

		if (!listaErrori.isEmpty() || errore != null) {
			log.warn("Esecuzione terminata per caricaDatiBDN_DomandaUnica con errori {}; lista  {}", errore, listaErrori.isEmpty());
			aggiornamentoCompletato = false;
		}
		log.debug("Fine esecuzione caricaDatiBDN_DomandaUnica per CUAA {}", cuaa);
		return aggiornamentoCompletato;
	}

	@Override
	public Long syncIngressoUscitaPascolo(String cuaa, Integer annoCampagna) {
		Long aggiornamentoCompletato = 1L;

		log.debug("Inizio esecuzione syncIngressoUscitaPascolo per CUAA {}", cuaa);
		ErroreDO errore = ingressiAlPascolo.inserimentoIngressiAlPascoloPartiteOvini(cuaa, annoCampagna);
		log.debug("Esecuzione termiata per Ingresso Pascolo {}", errore);
		errore = usciteDaPascolo.inserimentoUsciteDaPascoloPartiteOvini(cuaa, annoCampagna);
		log.debug("Esecuzione termiata per Uscita Pascolo {}", errore);

		if (errore != null) {
			aggiornamentoCompletato = 0L;
		}

		log.debug("Fine esecuzione syncIngressoUscitaPascolo per CUAA {}", cuaa);
		return aggiornamentoCompletato;
	}

	public void sincronizzaCacheDatiDomandaUnicaPerAnno(Integer annoCampagna) throws Exception {
		log.debug("sincronizzaCachePerAnno: inizio sincronizzazione per anno {}", annoCampagna);

		Long listaCuaaAnnoCorrente = consistenzaManager.aggiornaListaCuaaDaSincronizzare(annoCampagna);

		log.debug("Nuovi cuaa da sincronizzare: {}", listaCuaaAnnoCorrente);
		List<StatoSincronizzazioneDO> listaDati = statoSincronizzazioneDao.getListaCuaaDaSincronizzare(annoCampagna);
		for (StatoSincronizzazioneDO dati : listaDati) {
			boolean elaborazioneDomandaOK = false;
			try {
				long start = System.currentTimeMillis();
				elaborazioneDomandaOK = syncService.syncDatiDomandaUnica(dati.getCuaa(), dati.getAnnoCampagna());
				long finish = System.currentTimeMillis();
				long durata = (finish - start) * 1000;
				log.debug("Elaborazione syncDatiDomandaUnica per {} - {}\nstato_ok:\t{}\ndurata(s)\t{}\n",
						dati.getCuaa(), dati.getAnnoCampagna(), elaborazioneDomandaOK, durata);
			} catch (Exception e) {
				log.error("Eccezione nell'import dei dati da BDN per il cuaa: {} e anno: {}", dati.getCuaa(),
						annoCampagna, e);
			} finally {
				try {
					syncService.sincronizzaStatoSincronizzazione(elaborazioneDomandaOK, dati);
				} catch (Exception e) {
					log.error("Errore aggiornando i dati della sincronizzazione per cuaa: {} e anno: {} a {}",
							dati.getCuaa(), annoCampagna, elaborazioneDomandaOK, e);
				}
			}
		}
		log.debug("sincronizzaCachePerAnno: fine sincronizzazione per anno {}", annoCampagna);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sincronizzaStatoSincronizzazione(boolean elaborazioneDomandaOK, StatoSincronizzazioneDO dati) {
		if (elaborazioneDomandaOK) {
			log.debug("import dati BDN completato correttamente per cuaa: {} - OK", dati.getCuaa());
			statoSincronizzazioneDao.aggiornaStatoSincronizzazione(dati.getAnnoCampagna(), dati.getCuaa(),
					BdnConstants.SYNC_OK.getBdnConstants());
		} else { // Facciamo un tentativo di lettura dei dati se fallisce una prima volta lo
					// mettimao in RETRY nel caso fallisse ancora va in FAIL e un nuovo tentativo
					// sarà fatto il giorno successivo o in alternativa forzatamente tramite
					// chiamata REST
			if (dati.getStatoEsecuzione() != null
					&& dati.getStatoEsecuzione().equals(BdnConstants.SYNC_RETRY.getBdnConstants())) {
				log.error("import dati BDN per cuaa: {} e anno {} - KO", dati.getCuaa(), dati.getAnnoCampagna());
				statoSincronizzazioneDao.aggiornaStatoSincronizzazione(dati.getAnnoCampagna(), dati.getCuaa(),
						BdnConstants.SYNC_KO.getBdnConstants());
			} else {
				log.error("import dati BDN per cuaa: {} e anno {} - RETRY", dati.getCuaa(), dati.getAnnoCampagna());
				statoSincronizzazioneDao.aggiornaStatoSincronizzazione(dati.getAnnoCampagna(), dati.getCuaa(),
						BdnConstants.SYNC_RETRY.getBdnConstants());
			}
		}
	}

	public void sincronizzaAzienda(String codiceAzienda) throws Exception {
		log.debug("sincronizzaAzienda: chiamata sincronizzazione per azienda {}", codiceAzienda);
		consistenzaAllevamento.sincronizzaAzienda(codiceAzienda);
		log.debug("sincronizzaAzienda: fine chiamata sincronizzazione per azienda {}", codiceAzienda);
	}

}
