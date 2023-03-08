package it.tndigitale.a4g.proxy.action;

import java.util.List;
import java.util.function.BiConsumer;

import it.tndigitale.a4g.proxy.dto.Allegato2AntimafiaBuilder;
import it.tndigitale.a4g.proxy.dto.AziendeCollegateAntimafiaBuilder;
import it.tndigitale.a4g.proxy.dto.SoggettiAntimafiaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.Allegato2AntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.AziendeCollegateAntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.SoggettiAntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.Aabaall2Tab;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaazcoTab;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabasgcaTab;

@Component
public class AziendeCollegateConsumer implements BiConsumer<String, JsonNode>{
	
	private static final Logger log = LoggerFactory.getLogger(AziendeCollegateConsumer.class);
	
	//builder
	@Autowired
	private Allegato2AntimafiaBuilder allegato2AntimafiaBuilder;
	@Autowired
	private SoggettiAntimafiaBuilder soggettiAntimafiaBuilder;
	@Autowired
	private AziendeCollegateAntimafiaBuilder aziendeCollegateAntimafiaBuilder;
	//dao
	@Autowired
	private Allegato2AntimafiaDao allegato2AntimafiaDao;
	@Autowired
	private SoggettiAntimafiaDao soggettiAntimafiaDao;
	@Autowired
	private AziendeCollegateAntimafiaDao aziendeCollegateAntimafiaDao;

	@Override
	public void accept(String cuaa, JsonNode jsonDichiarazione) {
		log.debug("Inserimento dati per aziende collegate");
		if (jsonDichiarazione.path("dettaglioImpresa").isNull() || jsonDichiarazione.path("dettaglioImpresa").isMissingNode()) return;
		Aabaall2Tab allegato2 = allegato2AntimafiaBuilder.build(jsonDichiarazione);
		//allegato2.setCuaa(cuaa);
		allegato2=allegato2AntimafiaDao.save(allegato2);
		List<AabasgcaTab> soggetti = soggettiAntimafiaBuilder.build(jsonDichiarazione,true);
		log.debug("Inserimento {} soggetti",soggetti.size());
		for (AabasgcaTab soggetto : soggetti) {
			soggetto.setAabaall2Tab(allegato2);
			soggettiAntimafiaDao.save(soggetto);
		}
		List<AabaazcoTab> aziendeCollegate = aziendeCollegateAntimafiaBuilder.build(jsonDichiarazione);
		for (AabaazcoTab azienda : aziendeCollegate) {
			azienda.setAabaall2Tab(allegato2);
			aziendeCollegateAntimafiaDao.save(azienda);
		}
		
	}

}
