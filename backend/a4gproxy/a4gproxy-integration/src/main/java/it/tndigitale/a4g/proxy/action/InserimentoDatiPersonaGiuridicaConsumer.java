package it.tndigitale.a4g.proxy.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiConsumer;

import it.tndigitale.a4g.proxy.dto.Allegato2AntimafiaBuilder;
import it.tndigitale.a4g.proxy.dto.AziendeCollegateAntimafiaBuilder;
import it.tndigitale.a4g.proxy.dto.FamiliariAntimafiaBuilder;
import it.tndigitale.a4g.proxy.dto.SoggettiAntimafiaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.Allegato2AntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.Allegato3AntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.AziendeCollegateAntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.FamiliariAntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.SoggettiAntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.Aabaall2Tab;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.Aabaall3Tab;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaantiTab;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaazcoTab;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabafacoTab;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabasgcaTab;

@Component
public class InserimentoDatiPersonaGiuridicaConsumer implements BiConsumer<JsonNode,AabaantiTab>{
	
	private static final Logger log = LoggerFactory.getLogger(InserimentoDatiPersonaGiuridicaConsumer.class);
	
	//builder
	@Autowired
	private Allegato2AntimafiaBuilder allegato2AntimafiaBuilder;
	@Autowired
	private SoggettiAntimafiaBuilder soggettiAntimafiaBuilder;
	@Autowired
	private AziendeCollegateAntimafiaBuilder aziendeCollegateAntimafiaBuilder;
	@Autowired
	private FamiliariAntimafiaBuilder familiariAntimafiaBuilder;
	//dao
	@Autowired
	private Allegato2AntimafiaDao allegato2AntimafiaDao;
	@Autowired
	private Allegato3AntimafiaDao allegato3AntimafiaDao;
	@Autowired
	private SoggettiAntimafiaDao soggettiAntimafiaDao;
	@Autowired
	private AziendeCollegateAntimafiaDao aziendeCollegateAntimafiaDao;
	@Autowired
	private FamiliariAntimafiaDao familiariAntimafiaDao;
	
	@Autowired
	private AziendeCollegateConsumer aziendeCollegateConsumer;

	@Override
	public void accept(JsonNode jsonDatiDichiarazione, AabaantiTab dichiarazioneAntimafia) throws RuntimeException {
		log.debug("Invio dati per PERSONA GIURIDICA");
		Aabaall2Tab allegato2 = allegato2AntimafiaBuilder.build(jsonDatiDichiarazione.path("datiDichiarazione"));
		//allegato2.setCuaa(dichiarazioneAntimafia.getCuaa());//Mail nicola 08/02/2019
		allegato2=allegato2AntimafiaDao.saveAndFlush(allegato2);
		dichiarazioneAntimafia.setIdAll2(BigDecimal.valueOf(allegato2.getIdAll2()));
		List<AabasgcaTab> soggetti = soggettiAntimafiaBuilder.build(jsonDatiDichiarazione.path("datiDichiarazione"),false);
		log.debug("Inserimento {} soggetti",soggetti.size());
		for (AabasgcaTab soggetto : soggetti) {
			soggetto.setAabaall2Tab(allegato2);
			soggetto=soggettiAntimafiaDao.saveAndFlush(soggetto);
			List<AabafacoTab> familiari = familiariAntimafiaBuilder
						.setDichiarazione(jsonDatiDichiarazione)
						.setCodiceFiscale(soggetto.getCuaa())
						.build();
			Aabaall3Tab allegato3=new Aabaall3Tab();
			allegato3.setIdSgca(BigDecimal.valueOf(soggetto.getIdSgca()));
			allegato3=allegato3AntimafiaDao.saveAndFlush(allegato3);
			for (AabafacoTab familiare : familiari) {
				familiare.setAabaall3Tab(allegato3);
				familiariAntimafiaDao.saveAndFlush(familiare);
			}
		}
		List<AabaazcoTab> aziendeCollegate = aziendeCollegateAntimafiaBuilder.build(jsonDatiDichiarazione.path("datiDichiarazione"));
		for (AabaazcoTab azienda : aziendeCollegate) {
			azienda.setAabaall2Tab(allegato2);
			aziendeCollegateAntimafiaDao.save(azienda);
		}
		//AM-01-13-01 INTEGRAZIONE SINCRONIZZAZIONE AGEA DATI DETTAGLIO AZIENDE COLLEGATE 
		inserimentoDatiAziendeCollegate(jsonDatiDichiarazione, dichiarazioneAntimafia);
	}
	
	private void inserimentoDatiAziendeCollegate(JsonNode jsonDatiDichiarazione, AabaantiTab dichiarazioneAntimafia) {
		JsonNode aziendeCollegateArray=jsonDatiDichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("aziendeCollegate");
		aziendeCollegateArray.forEach(aziendeCollegata -> aziendeCollegateConsumer.accept(dichiarazioneAntimafia.getCuaa(), aziendeCollegata));
	}

}
