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

import it.infotn.bdn.wsbdnagea.wsclient.ListaUsciteDaPascoloPartiteOviniService;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiOVIUSCITEPASCOLO;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiOVIUSCITEPASCOLO.OVIUSCITEPASCOLO;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.MovimentazionePascoloDAO;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.StatoSincronizzazioneBdnDAO;

@Service
public class UsciteDaPascoloPartiteOviniManagerImpl implements UsciteDaPascoloPartiteOviniManager {

	@Autowired
	protected MovimentazionePascoloDAO movimentiPascoloDao;
	@Autowired
	protected StatoSincronizzazioneBdnDAO statoSyncDao;

	ListaUsciteDaPascoloPartiteOviniService service = new ListaUsciteDaPascoloPartiteOviniService();

	// Logger
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	@Transactional
	public ErroreDO inserimentoUsciteDaPascoloPartiteOvini(String cuaa, Integer annoCampagna) {

		String dataInizioPeriodo = "01/01/".concat(annoCampagna.toString());
		String dataFinePeriodo = "31/12/".concat(annoCampagna.toString());

		ErroreDO errore = null;

		try {
			ArrayOfRootDatiOVIUSCITEPASCOLO uscitePascolo = service.getListaUsciteDaPascoloPartiteOvini(cuaa, dataInizioPeriodo, dataFinePeriodo);

			if (uscitePascolo == null) {
				log.debug("nessun record attivo sulla BDN, chiudo eventuali record esistenti sul DB");
				movimentiPascoloDao.closeRecordByCuaa(cuaa, MovimentazionePascoloDO.uscitaPascolo, annoCampagna.intValue());
				// return errore;
			} else {

				List<OVIUSCITEPASCOLO> listaUscite = uscitePascolo.getOVIUSCITEPASCOLO();

				Iterator<OVIUSCITEPASCOLO> i = listaUscite.iterator();

				// boolean recordOnDB = false;
				while (i.hasNext()) {
					OVIUSCITEPASCOLO uscitaPascolo = i.next();
					MovimentazionePascoloDO movimentazionePascoloDO = new MovimentazionePascoloDO(uscitaPascolo, cuaa, annoCampagna.intValue());

					// select per trovare eventuale record presente
					MovimentazionePascoloDO onDb = movimentiPascoloDao.getMovimentazionePascoloUscita(cuaa, annoCampagna.intValue(), movimentazionePascoloDO);

					if (onDb != null) {

						if (onDb.compareTo(movimentazionePascoloDO) == 0) {
							log.debug("record su DBN e DB uguali, nessuna operazione sul DB");
							// recordOnDB = true;
							continue;
						}

						log.warn("record sul DB con ID " + onDb.getIdMovi() + " da chiudere prima di inserire il nuovo");
						movimentiPascoloDao.closeRecordById(onDb);

					}
					// recordOnDB = true;
					log.debug("inserimento sul DB");
					movimentiPascoloDao.insert(movimentazionePascoloDO, cuaa, annoCampagna.intValue());

				}
			}

		} catch (Exception e) {
			log.error("Errore nel caricamento degli Uscite da Pascolo Partite Ovini per cuaa [" + cuaa + "]: " + e.getMessage());
			errore = new ErroreDO(cuaa, ErroreDO.usciteDaPascoloPartiteOvini, new GregorianCalendar(), e);
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
			ErroreDO errore = new ErroreDO("", ErroreDO.usciteDaPascoloPartiteOvini, new GregorianCalendar(), e1);
			listaErrori.add(errore);
			return listaErrori;
		}

		int cont = 0;
		for (String element : listOfCuaa) {

			log.debug("Iterazione #: " + ++cont + " cuaa " + element);

			ErroreDO errore = inserimentoUsciteDaPascoloPartiteOvini(element, annoCampagna);
			if (errore != null) {
				listaErrori.add(errore);
			}
		}
		return listaErrori;

	}

}
