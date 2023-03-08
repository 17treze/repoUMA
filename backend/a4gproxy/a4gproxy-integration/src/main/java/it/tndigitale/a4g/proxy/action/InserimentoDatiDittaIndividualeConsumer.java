package it.tndigitale.a4g.proxy.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiConsumer;

import it.tndigitale.a4g.proxy.dto.Allegato1AntimafiaBuilder;
import it.tndigitale.a4g.proxy.dto.FamiliariAntimafiaBuilder;
import it.tndigitale.a4g.proxy.dto.SoggettiAntimafiaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.Allegato1AntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.Allegato3AntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.FamiliariAntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.SoggettiAntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.Aabaall1Tab;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.Aabaall3Tab;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaantiTab;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabafacoTab;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabasgcaTab;

@Component
public class InserimentoDatiDittaIndividualeConsumer  implements BiConsumer<JsonNode,AabaantiTab>{
	
	private static final Logger log = LoggerFactory.getLogger(InserimentoDatiDittaIndividualeConsumer.class);
	
	//builder
	@Autowired
	private Allegato1AntimafiaBuilder allegato1AntimafiaBuilder;
	@Autowired
	private FamiliariAntimafiaBuilder familiariAntimafiaBuilder;
	@Autowired
	private SoggettiAntimafiaBuilder soggettiAntimafiaBuilder;
	//dao
	@Autowired
	private FamiliariAntimafiaDao familiariAntimafiaDao;
	@Autowired
	private SoggettiAntimafiaDao soggettiAntimafiaDao;
	@Autowired
	private Allegato1AntimafiaDao allegato1AntimafiaDao;
	@Autowired
	private Allegato3AntimafiaDao allegato3AntimafiaDao;	

	@Override
	public void accept(JsonNode jsonDatiDichiarazione, AabaantiTab dichiarazioneAntimafia) throws RuntimeException{
		log.debug("Invio dati per DITTA INDIVIDUALE");
		Aabaall1Tab allegato1 = allegato1AntimafiaBuilder.build(jsonDatiDichiarazione);
		allegato1.setCuaa(dichiarazioneAntimafia.getCuaa());
		allegato1=allegato1AntimafiaDao.saveAndFlush(allegato1);
		dichiarazioneAntimafia.setIdAll1(BigDecimal.valueOf(allegato1.getIdAll1()));
		List<AabasgcaTab> soggetti = soggettiAntimafiaBuilder.build(jsonDatiDichiarazione.path("datiDichiarazione"),false);
		log.debug("Inserimento {} soggetti",soggetti.size());
		for (AabasgcaTab soggetto : soggetti) {
			soggetto.setAabaall1Tab(allegato1);
			soggetto=soggettiAntimafiaDao.saveAndFlush(soggetto);
			List<AabafacoTab> familiari = familiariAntimafiaBuilder
						.setDichiarazione(jsonDatiDichiarazione)
						.setCodiceFiscale(soggetto.getCuaa())
						.build();
			Aabaall3Tab allegato3=new Aabaall3Tab();
			allegato3.setAabaall1Tab(allegato1);
			allegato3.setIdSgca(BigDecimal.valueOf(soggetto.getIdSgca()));
			allegato3=allegato3AntimafiaDao.saveAndFlush(allegato3);
			for (AabafacoTab familiare : familiari) {
				familiare.setAabaall3Tab(allegato3);
				familiariAntimafiaDao.saveAndFlush(familiare);
			}
		}
	}

}
