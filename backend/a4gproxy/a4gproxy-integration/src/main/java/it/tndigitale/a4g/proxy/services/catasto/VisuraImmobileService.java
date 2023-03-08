package it.tndigitale.a4g.proxy.services.catasto;

import it.tndigitale.a4g.proxy.ws.catastoVisura.*;
import it.tndigitale.a4g.proxy.config.WSKSTSSupport;
import it.tndigitale.a4g.proxy.dto.catasto.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class VisuraImmobileService extends WSKSTSSupport {

	private static final Logger log = LoggerFactory.getLogger(VisuraImmobileService.class);

	@Value("${it.tndigit.catasto.visuraImmobile.uri}")
	private String wsUri;

	@Value("${it.tndigit.catasto.password}")
	private String wsAuthPsw;

	@Value("${it.tndigit.catasto.user}")
	private String wsAuthUsn;

	ObjectFactory factory = new ObjectFactory();

	@PostConstruct
	private void buildWebTemplate() throws Exception {
		super.buildWebTemplate("it.tndigitale.a4g.proxy.ws.catastoVisura", wsUri, wsAuthUsn, wsAuthPsw);
	}

	private ElencoSubalterniParticellaType getElencoSubalterniParticellaFromWs(String numeroParticella, Integer codiceComuneCatastale) {

		ChiaveComuneCatastaleType chiaveComuneCatastaleType = new ChiaveComuneCatastaleType();
		chiaveComuneCatastaleType.setCodice(BigInteger.valueOf(codiceComuneCatastale));

		ChiaveParticellaType chiaveParticellaType = new ChiaveParticellaType();
		chiaveParticellaType.setNumero(numeroParticella);
		chiaveParticellaType.setComuneCatastale(chiaveComuneCatastaleType);
		chiaveParticellaType.setTipologia(TipoParticellaType.EDIFICIALE);

		XMLGregorianCalendar today = null;
		try {
			DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			today = datatypeFactory.newXMLGregorianCalendar(new GregorianCalendar());
		} catch (DatatypeConfigurationException e) {
			log.error("Errore data riferimento", e);
			e.printStackTrace();
		}

		GetSubalterniParticella createGetSubalterniParticella = factory.createGetSubalterniParticella();
		createGetSubalterniParticella.setParticella(chiaveParticellaType);
		createGetSubalterniParticella.setDataRiferimento(today);
		GetSubalterniParticellaResponse marshalSendAndReceive = (GetSubalterniParticellaResponse) getWebServiceTemplate().marshalSendAndReceive(wsUri, createGetSubalterniParticella);
		//        ritorna NULL se non esiste
		if (marshalSendAndReceive.getSubalterniParticella() != null) {
			log.debug("chiamata a getSubalterniParticella {}" , marshalSendAndReceive.getSubalterniParticella().toString());
		}

		return marshalSendAndReceive.getSubalterniParticella();
	}

	public List<String> getElencoSubalterniParticella(String numeroParticella, Integer codiceComuneCatastale) {
		try {
			ElencoSubalterniParticellaType elencoSubalterniParticellaType = getElencoSubalterniParticellaFromWs(numeroParticella, codiceComuneCatastale);
			if (elencoSubalterniParticellaType != null) {
				InformazioniVisuraImmobileDto informazioniVisuraImmobileDto = new InformazioniVisuraImmobileDto();
				List<String> subalterni = new ArrayList<String>();
				if (elencoSubalterniParticellaType.getElencoSP() != null) {
					ElencoSPType elencoSP = elencoSubalterniParticellaType.getElencoSP();
					if (elencoSP.getDatiUI() != null && !elencoSP.getDatiUI().isEmpty()) {
						List<DatiUnitaImmobiliareSPType> datiUI = elencoSP.getDatiUI();
						datiUI.forEach(ui -> {
							if (ui.getDatiIdentificativi() != null) {
								IdentificativoUIType datiIdentificativi = ui.getDatiIdentificativi();
								if (datiIdentificativi.getIdentificativo()!= null && !datiIdentificativi.getIdentificativo().isEmpty()) {
									datiIdentificativi.getIdentificativo().forEach(id -> subalterni.add(id.getSubalterno()));
								}
							}
						});
					}
				}
				informazioniVisuraImmobileDto.setSubalterno(subalterni);

				return  informazioniVisuraImmobileDto.getSubalterno();
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error("Errore nel reperimento dei dati da catasto", e);
			return null;
		}
	}

	@Override
	protected String getAlias() {
		return null;
	}
}
