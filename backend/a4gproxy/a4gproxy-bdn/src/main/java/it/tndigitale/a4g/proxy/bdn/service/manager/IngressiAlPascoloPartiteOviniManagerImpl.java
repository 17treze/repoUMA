package it.tndigitale.a4g.proxy.bdn.service.manager;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import it.tndigitale.a4g.proxy.bdn.dto.ErroreDO;
import it.tndigitale.a4g.proxy.bdn.dto.MovimentazionePascoloDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.infotn.bdn.wsbdnagea.wsclient.ListaIngressiAlPascoloPartiteOviniService;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiOVIINGRESSIPASCOLO;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiOVIINGRESSIPASCOLO.OVIINGRESSIPASCOLO;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.MovimentazionePascoloDAO;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.StatoSincronizzazioneBdnDAO;

@Service
public class IngressiAlPascoloPartiteOviniManagerImpl implements IngressiAlPascoloPartiteOviniManager {

	@Autowired
	protected MovimentazionePascoloDAO movimentiPascoloDao;
	@Autowired
	protected StatoSincronizzazioneBdnDAO statoSyncDao;

	ListaIngressiAlPascoloPartiteOviniService service = new ListaIngressiAlPascoloPartiteOviniService();

	// Logger
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	@Transactional
	public ErroreDO inserimentoIngressiAlPascoloPartiteOvini(String cuaa, Integer annoCampagna) {

		String dataInizioPeriodo = "01/01/".concat(annoCampagna.toString());
		String dataFinePeriodo = "31/12/".concat(annoCampagna.toString());

		ErroreDO errore = null;

		try {
			ArrayOfRootDatiOVIINGRESSIPASCOLO ingressiPascolo = service.getListaIngressiAlPascoloPartiteOvini(cuaa, dataInizioPeriodo, dataFinePeriodo);

			if (ingressiPascolo == null) {
				log.debug("nessun record attivo sulla BDN, chiudo eventuali record esistenti sul DB");
				movimentiPascoloDao.closeRecordByCuaa(cuaa, MovimentazionePascoloDO.ingressoPascolo, annoCampagna);
				return null;
			}

			List<OVIINGRESSIPASCOLO> listaIngressi = ingressiPascolo.getOVIINGRESSIPASCOLO();

			Iterator<OVIINGRESSIPASCOLO> i = listaIngressi.iterator();

			while (i.hasNext()) {
				OVIINGRESSIPASCOLO ingressoPascolo = i.next();
				MovimentazionePascoloDO movimentazionePascoloDO = new MovimentazionePascoloDO(ingressoPascolo, cuaa, annoCampagna);

				// select per trovare eventuale record presente
				MovimentazionePascoloDO onDb = movimentiPascoloDao.getMovimentazionePascoloIngresso(cuaa, annoCampagna, movimentazionePascoloDO);

				if (onDb != null) {

					if (onDb.compareTo(movimentazionePascoloDO) == 0) {
						log.debug("record su DBN e DB uguali, nessuna operazione sul DB");
						continue;
					}

					log.debug("record sul DB con ID " + onDb.getIdMovi() + " da chiudere prima di inserire il nuovo");
					movimentiPascoloDao.closeRecordById(onDb);

				}

				log.debug("inserimento sul DB");
				movimentiPascoloDao.insert(movimentazionePascoloDO, cuaa, annoCampagna);

			}

		} catch (Exception e) {
			log.error("Errore nel caricamento degli Ingressi al Pascolo Partite Ovini per cuaa [" + cuaa + "]: ", e);
			errore = new ErroreDO(cuaa, ErroreDO.ingressiAlPascoloPartiteOvini, new GregorianCalendar(), e);

		}

		return errore;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ErroreDO> inserimentoMassivo(Integer annoCampagna) {

		List<ErroreDO> listaErrori = new ArrayList<ErroreDO>();

		List<String> listOfCuaa;
		try {
			listOfCuaa = statoSyncDao.getListaCuaaTabella(annoCampagna);
		} catch (Exception e1) {
			log.error("Errore nell'estrazione dell'elenco CUAA. Operazione non eseguita");
			ErroreDO errore = new ErroreDO("", ErroreDO.ingressiAlPascoloPartiteOvini, new GregorianCalendar(), e1);
			listaErrori.add(errore);
			return listaErrori;
		}

		int cont = 0;
		for (String element : listOfCuaa) {

			log.debug("Iterazione #: " + ++cont + " cuaa " + element);

			ErroreDO errore = inserimentoIngressiAlPascoloPartiteOvini(element, annoCampagna);
			if (errore != null) {
				listaErrori.add(errore);
			}
		}
		return listaErrori;

	}

}
