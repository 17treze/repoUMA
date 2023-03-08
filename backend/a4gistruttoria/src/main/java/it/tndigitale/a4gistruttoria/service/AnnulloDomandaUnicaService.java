package it.tndigitale.a4gistruttoria.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.EsitoMantenimentoPascoloDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi4Ags;

@Service
public class AnnulloDomandaUnicaService {

	private static Logger logger = LoggerFactory.getLogger(AnnulloDomandaUnicaService.class);
	
	@Autowired
	private DomandaUnicaDao domandaUnicaDao;
	@Autowired
	private EsitoMantenimentoPascoloDao esitoMantenimentoPascoloDao;
	
    @Autowired
    private IstruttoriaDao istruttoriaDao;
    
    @Autowired
    private ConsumeExternalRestApi4Ags restApi4Ags;
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private EntityManager entityManager;

	/**
	 * Annullo domanda in istruttoria.
	 * Implementata gestione programmatica della transazione per gestire il caso in cui la chiamata all'API REST di AGS fallisca
	 * Mettendo il @Transactional l'eliminazione dei dati veniva fatta solo alla fine del metodo. In caso di errori nella
	 * eliminazione non avrebbe fatto anche il rollback del servizio AGS essendo esterno, avendo così incongruenze di dati
	 * @param idDomanda
	 * @throws Exception
	 */
    //https://www.baeldung.com/spring-programmatic-transaction-management
	public void annullaIstruttoriaDomanda(Long idDomanda) throws Exception {
		
    	DomandaUnicaModel domanda = 
				domandaUnicaDao.findById(idDomanda)
					.orElseThrow(() -> new Exception("ANNULLA_ISTRUTTORIA_DOMANDA_NON_TROVATA"));
		
		List<IstruttoriaModel> istruttorie = istruttoriaDao.findByDomandaUnicaModelId(idDomanda);
		boolean istruttoriaInPagamento = 
			istruttorie.stream()
				.anyMatch(p -> StatoIstruttoria.PAGAMENTO_AUTORIZZATO.equals(p.getStato()));
		if (istruttoriaInPagamento) {
			logger.warn("Impossibile annullare la domanda {}: esistono sostegni in pagamento o pagati.", idDomanda);
			throw new IllegalArgumentException("ANNULLA_ISTRUTTORIA_DOMANDA_IN_PAGAMENTO");
		}
		
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(definition);
		try {
			eliminaDatiIstruttoria(domanda);
			entityManager.flush();
		} catch (RuntimeException e) {
			transactionManager.rollback(status);
			logger.error("Impossibile annullare l'istruttoria della domanda {}: problema nella cancellazione dei dati di domanda", idDomanda, e);
			throw new IllegalArgumentException("ANNULLA_ISTRUTTORIA_ELIMINAZIONE_DATI_ISTRUTTORIA");
		}
		
		try {
			//annulla Domanda Su Ags
			restApi4Ags.spostaDomandaInProtocollata(domanda.getNumeroDomanda().longValue());
		} catch (RuntimeException e) {
			transactionManager.rollback(status);
			logger.error("Impossibile annullare l'istruttoria della domanda {}: impossibile movimentare la domanda su AGS", idDomanda,e);
			throw new IllegalArgumentException("ANNULLA_ISTRUTTORIA_MOVIMENTAZIONE_AGS",e);
		}	
		transactionManager.commit(status);
	}

	/**
	 * l'eliminazione di tutti i figli viene fatta usando il CascadeType.REMOVE
	 * @param domanda
	 * @param istruttorie
	 */
	private void eliminaDatiIstruttoria(DomandaUnicaModel domanda) {
		//implementato per ovviare al seguente problema di referenze incrociate
		//Caused by: java.sql.SQLException: ORA-01407: impossibile aggiornare ("SUA4G04"."A4GT_ESITO_MAN_PASCOLO"."ID_ISTRUTTORIA") a NULL
		Optional.ofNullable(domanda.getA4gtPascoloParticellas())
			.ifPresent(particelle -> 
				particelle.stream()
					.flatMap(particella -> particella.getEsitiMantenimentoPascolo().stream())
					.forEach(esito -> esitoMantenimentoPascoloDao.delete(esito))
			);
		logger.debug("eliminazione domande, relative istruttorie e tabelle referenziate");
		domandaUnicaDao.delete(domanda);
	}
}
