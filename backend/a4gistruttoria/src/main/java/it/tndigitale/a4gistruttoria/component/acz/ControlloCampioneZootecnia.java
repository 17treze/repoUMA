package it.tndigitale.a4gistruttoria.component.acz;

import it.tndigitale.a4gistruttoria.repository.dao.CampioneDao;
import it.tndigitale.a4gistruttoria.repository.model.CampioneModel;
import it.tndigitale.a4gistruttoria.repository.model.AmbitoCampione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ControlloCampioneZootecnia {

	private static final Logger logger = LoggerFactory.getLogger(ControlloCampioneZootecnia.class);

	@Autowired
	CampioneDao campioneDao;

	protected Optional<CampioneModel> getCampioneZootecnia(Integer anno, String cuaaIntestatario) {
		
		try {
			CampioneModel campione = campioneDao.findByCuaaAndAmbitoCampioneAndAnnoCampagna(
					cuaaIntestatario, AmbitoCampione.ZOOTECNIA, anno);
			return Optional.ofNullable(campione);
		} catch (Exception e) {
			logger.error("Errore getCampioneZootecnia", e);
			return Optional.empty();
		}
	}
	
	/**
	 * Verifica che l'azienda sia campione zootecnia per bovini
	 * @param anno
	 * @param cuaaIntestatario
	 * @return
	 */
	public Boolean checkCampioneBovini(Integer anno, String cuaaIntestatario) {
		return getCampioneZootecnia(anno, cuaaIntestatario)
				.filter(p -> p.getBovini()).isPresent();
	}
	
	/**
	 * Verifica che l'azienda sia campione zootecnia per ovicaprini
	 * @param anno
	 * @param cuaaIntestatario
	 * @return
	 */
	public Boolean checkCampioneOvicaprini(Integer anno, String cuaaIntestatario) {
		return getCampioneZootecnia(anno, cuaaIntestatario)
				.filter(p -> p.getOvini()).isPresent();
	}
	
	/**
	 * Verifica che l'azienda sia campione zootecnia per bovini OPPURE per ovicaprini
	 * @param anno
	 * @param cuaaIntestatario
	 * @return
	 */
	public Boolean checkCampioneZootecnia(Integer anno, String cuaaIntestatario) {
		return checkCampioneBovini(anno, cuaaIntestatario) 
				|| checkCampioneOvicaprini(anno, cuaaIntestatario);
	}
}
