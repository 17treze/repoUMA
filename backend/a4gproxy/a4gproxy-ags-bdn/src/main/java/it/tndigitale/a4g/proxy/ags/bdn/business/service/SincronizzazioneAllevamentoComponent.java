package it.tndigitale.a4g.proxy.ags.bdn.business.service;

import it.izs.bdr.dscapiallev_g.DsCAPIALLEVG;
import it.izs.bdr.webservices.GetCapiAllevamentoPeriodoResponse;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiANAGRAFICAALLEVAMENTO;
import it.tndigitale.a4g.proxy.ags.bdn.business.persistence.repository.ZootecniaDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.time.LocalDate;

@Component
class SincronizzazioneAllevamentoComponent {
	private static final Logger logger = LoggerFactory.getLogger(SincronizzazioneAllevamentoComponent.class);

	@Autowired
	private ZootecniaDao zootecniaDao;

	@Autowired
	private AnagraficaAllevamentoClient anagraficaAllevamentoClient;

	@Autowired
	private CapiAllevamentoClient capiAllevamentoClient;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Boolean sincronizzaSingoloAllevamento(String cuaa, LocalDate dataRiferimento) {
		logger.debug("attivo sincronizzazione per {} {}", cuaa, dataRiferimento);
		try {
			// Prima chiamata al web service
			ArrayOfRootDatiANAGRAFICAALLEVAMENTO anagrafiche = anagraficaAllevamentoClient.getAnagraficaAllevamenti(cuaa, dataRiferimento);

			if (anagrafiche == null || CollectionUtils.isEmpty(anagrafiche.getANAGRAFICAALLEVAMENTO())) {
				logger.warn("CUAA {} non presente su servizio BDN Anagrafica Allevamenti", cuaa);
				return Boolean.FALSE;
			}

			// Salvo la lista di allevamenti dell'azienda identificata dal suo CUAA
			zootecniaDao.saveAllevamento(anagrafiche);

			GetCapiAllevamentoPeriodoResponse.GetCapiAllevamentoPeriodoResult capiAllevamento = null;
			for (ArrayOfRootDatiANAGRAFICAALLEVAMENTO.ANAGRAFICAALLEVAMENTO allevamento : anagrafiche.getANAGRAFICAALLEVAMENTO()) {
				try {
					// Seconda chiamata al web service
					capiAllevamento = capiAllevamentoClient.getCapiAllevamentoPeriodo(allevamento.getALLEVID().toPlainString(),
							dataRiferimento, dataRiferimento);
				} catch (Throwable e) {
					logger.warn("Interrogazione Get Capo Animale CUAA {} allevamento {} con errori: ",
							cuaa, allevamento.getALLEVID().toPlainString());
					throw e;
				}

				logger.debug("nome classe {}", capiAllevamento.getContent().get(0).getClass().getName());

				DsCAPIALLEVG capiBovini = CapiBoviniFactory.mapCapiBovini(capiAllevamento.getContent().get(0));

				zootecniaDao.persistCapiAllevamenti(capiBovini);
			}
		} catch (Exception e) {
			logger.warn("Interrogazione Anagrafica Allevamenti CUAA {} per data {} e' andata in errore", cuaa, dataRiferimento, e);
			throw new RuntimeException("Non sono riuscito ad importare i dati per il CUAA " + cuaa + " in data " + dataRiferimento, e);
		}

		return Boolean.TRUE;
	}

	private static class CapiBoviniFactory {

		private static DsCAPIALLEVG mapCapiBovini(Object obj) throws JAXBException {
			Node content = (Node) obj;
			NodeList childList = content.getChildNodes();
			Node dati = childList.item(1);

			JAXBContext context = JAXBContext.newInstance(DsCAPIALLEVG.class);
			Unmarshaller um = context.createUnmarshaller();
			DsCAPIALLEVG response = null;
			if (dati.getFirstChild() != null)
				response = (DsCAPIALLEVG)um.unmarshal((Node)dati.getFirstChild());

			return response;
		}
	}
}
