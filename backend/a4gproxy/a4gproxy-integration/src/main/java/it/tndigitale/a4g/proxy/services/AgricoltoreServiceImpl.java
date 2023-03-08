package it.tndigitale.a4g.proxy.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.tndigitale.a4g.proxy.dto.AgricoltoreSIAN;
import it.tndigitale.a4g.proxy.dto.InfoAgricoltoreSIAN;
import it.tndigitale.a4g.proxy.dto.TitoloAgricoltoreSIAN;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.AabagareDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.AabaraiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.TitoliPacDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaraiaTab;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.TitoliPac;

@Service
public class AgricoltoreServiceImpl implements AgricoltoreService {

	@Autowired
	private AabaraiaDao daoAgricoltore;
	@Autowired
	private TitoliPacDao daoTitoliPac;
	@Autowired
	private AabagareDao daoGiovane;

	/**
	 * Metodo per la verifica di attività di un agricoltore o di un'azienda agricola
	 * 
	 * @params string cuaaIntestatario, codice fiscale dell'agricoltore/azienda BigDecimal anno, riferimento all'anno di esercizio (per adesso si consodera il 2018)
	 * 
	 * @return oggetto AgricoltoreSIAN contenente i dati su titoli e info generali
	 */
	public AgricoltoreSIAN recuperaAgricoltoreSian(String codFisc, BigDecimal annoCamp) throws IOException {

		AabaraiaTab aabaraiaModel = daoAgricoltore.findByCodFiscAndAnnoCamp(codFisc, annoCamp);
		List<TitoliPac> titoliPacModel = daoTitoliPac.findByCuaaAndAnno(codFisc, annoCamp);
		BigDecimal annoCamp2015 = new BigDecimal(2015);
		BigDecimal annoCamp2016 = new BigDecimal(2016);
		BigDecimal annoCamp2017 = new BigDecimal(2017);
		BigDecimal annoCamp2018 = new BigDecimal(2018);
		BigDecimal annoCamp2019 = new BigDecimal(2019);
		BigDecimal annoCamp2020 = new BigDecimal(2020);
		BigDecimal annoCamp2021 = new BigDecimal(2021);
		BigDecimal annoCamp2022 = new BigDecimal(2022);

		List<TitoloAgricoltoreSIAN> titoliDto = new ArrayList<TitoloAgricoltoreSIAN>();
		titoliPacModel.forEach(titolo -> {
			TitoloAgricoltoreSIAN t = new TitoloAgricoltoreSIAN();
			t.setIdTitolare(titolo.getIdTit());
			t.setCuaaTitolare(titolo.getCuaaProp());
			t.setAnnoCampagnaInizio(titolo.getNumeCampIniz());
			t.setAnnoCampagnaFine(titolo.getNumeCampFine());
			t.setDataInizioPossesso(titolo.getDataIniz());
			t.setDataFinePossesso(titolo.getDataFinePoss());
			t.setNumeroTitolare(titolo.getNumeTito());
			t.setSuperficieTitolo(titolo.getSupeTito());
			if (annoCamp.equals(annoCamp2015)) {
				t.setValoreTitolo(titolo.getValoTito2015());
			} else if (annoCamp.equals(annoCamp2016)) {
				t.setValoreTitolo(titolo.getValoTito2016());
			} else if (annoCamp.equals(annoCamp2017)) {
				t.setValoreTitolo(titolo.getValoTito2017());
			} else if (annoCamp.equals(annoCamp2018)) {
				t.setValoreTitolo(titolo.getValoTito2018());
			} else if (annoCamp.equals(annoCamp2019)) {
				t.setValoreTitolo(titolo.getValoTito2019());
			} else if (annoCamp.equals(annoCamp2020)) {
				t.setValoreTitolo(titolo.getValoTito2020());
			} else if (annoCamp.equals(annoCamp2021)) {
				t.setValoreTitolo(titolo.getValoTito2021());
			} else if (annoCamp.equals(annoCamp2022)) {
				t.setValoreTitolo(titolo.getValoTito2022());
			}
			titoliDto.add(t);
		});
		AgricoltoreSIAN infoAgricoltore = new AgricoltoreSIAN();

		if (aabaraiaModel != null) {
			InfoAgricoltoreSIAN infoAgricoltoreSIAN = new InfoAgricoltoreSIAN();
			BeanUtils.copyProperties(aabaraiaModel, infoAgricoltoreSIAN);
			infoAgricoltore.setInfoAgricoltoreSIAN(infoAgricoltoreSIAN);
		}

		infoAgricoltore.setTitoliSIAN(titoliDto);
		infoAgricoltore.setFlagGiovAgri(daoGiovane.findFlagGiovaneByCodFiscAndAnnoCamp(codFisc, annoCamp));

		return infoAgricoltore;

	}

}
