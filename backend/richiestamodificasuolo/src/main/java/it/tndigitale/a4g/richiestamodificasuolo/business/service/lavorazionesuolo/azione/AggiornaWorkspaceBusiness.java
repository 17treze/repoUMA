package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoColtModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.UsoSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceLavSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceTmpModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.StatoColtDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.UsoSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.InputDataGeoJson;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.LavorazioneSuoloService;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("AGGIORNA_WORKSPACE")
public class AggiornaWorkspaceBusiness extends AzioneLavorazioneBase<String, InputDataGeoJson> {

	private static final Logger log = LoggerFactory.getLogger(AggiornaWorkspaceBusiness.class);

	@Autowired
	private UtenteComponent utenteComponent;

	@Autowired
	private UsoSuoloDao usoSuoloDao;

	@Autowired
	private StatoColtDao statoColtDao;

	@Value("${it.tndigit.srid.etrs89}")
	private int sridEtrs89;

	@Autowired
	private UtilsFme utilsFme;

	@Value("${it.tndigit.serverFme.creaAreaDiLavoro}")
	private String creaAreaDiLavoro;

	@Override
	protected String eseguiAzione(InputDataGeoJson input) {
		return aggiornaWorkspaceLavSuolo(input);
	}

	public String aggiornaWorkspaceLavSuolo(InputDataGeoJson input) {
		log.debug("START - aggiornaWorkspaceLavSuolo {}", input.getIdLavorazione());
		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrNotFound(input.getIdLavorazione(), utenteComponent.username());

		// Ripulisco il workspace
		List<WorkspaceLavSuoloModel> workspaceToRemove = checkValidState(lavorazione);
		getWorkspaceLavorazioneDao().deleteInBatch(workspaceToRemove);

		// Aggiorno il nuovo workspace
		FeatureCollection featureCollection = checkValidFeatureInput(input.getGeoJson());
		GeoJSONReader reader = new GeoJSONReader();

		for (Feature feature : featureCollection.getFeatures()) {
			WorkspaceLavSuoloModel workspaceModel = new WorkspaceLavSuoloModel();

			Geometry geometry = reader.read(feature.getGeometry());
			geometry.setSRID(sridEtrs89);

			Map<String, Object> mapProperties = feature.getProperties();

			String usoSuolo = GisUtils.parse(mapProperties, "COD_USO_SUOLO");
			UsoSuoloModel usoSuoloModel = null;
			if (usoSuolo == null || usoSuolo.equals("null")) {
				usoSuoloModel = usoSuoloDao.findByCodUsoSuolo(LavorazioneSuoloService.USO_SUOLO_NON_DEFINITO);
			} else {

				usoSuoloModel = usoSuoloDao.findByCodUsoSuolo(usoSuolo);
			}
			workspaceModel.setCodUsoSuoloWorkspaceLavSuolo(usoSuoloModel);

			String statoColt = GisUtils.parse(mapProperties, "STATO_COLT");
			StatoColtModel statoColtModel = null;
			if (statoColt == null || statoColt.equals("null")) {
				statoColtModel = statoColtDao.findByStatoColt("0");
			} else {
				statoColtModel = statoColtDao.findByStatoColt(statoColt);
			}

			workspaceModel.setStatoColtWorkspaceLavSuolo(statoColtModel);
			workspaceModel.setShape(geometry);
			workspaceModel.setArea(geometry.getArea());
			workspaceModel.setIdLavorazioneWorkspaceLavSuolo(lavorazione);
			workspaceModel.setDataUltimaModifica(getClock().now());
			workspaceModel.setNote(GisUtils.parse(mapProperties, "NOTE"));

			getWorkspaceLavorazioneDao().save(workspaceModel);
		}

		log.debug("END - aggiornaWorkspaceLavSuolo ");
		return null;
	}

	private LavorazioneSuoloModel svuotaWorkspaceTemporaneo(LavorazioneSuoloModel lavorazione) {
		List<WorkspaceTmpModel> listWorkspaceTmp = lavorazione.getListaLavorazioneWorkspaceTmpModel();
		for (WorkspaceTmpModel workspaceTmpModel : listWorkspaceTmp) {
			lavorazione.removeWorkspaceTmpModel(workspaceTmpModel);
		}
		return getLavorazioneSuoloDao().cleanSave(lavorazione);
	}

	protected List<WorkspaceLavSuoloModel> checkValidState(LavorazioneSuoloModel lavorazione) {
		List<WorkspaceLavSuoloModel> workspaceToRemove = null;
		if (!StatoLavorazioneSuolo.IN_CORSO.equals(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per aggiornare il workspace {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(
					"Lo stato ".concat(lavorazione.getStato().name()).concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente l'aggiornamento del workspace "))));

		} else {
			workspaceToRemove = lavorazione.getListaLavorazioneWorkspaceModel();
		}
		return workspaceToRemove;
	}

	protected FeatureCollection checkValidFeatureInput(String jsoSon) {
		try {
			FeatureCollection featureCollection = (FeatureCollection) GeoJSONFactory.create(jsoSon);
			return featureCollection;
		} catch (Exception e) {
			log.error("FeatureCollection in input {} non valida", jsoSon);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("FeatureCollection in input ".concat(jsoSon).concat(" non valida"));

		}
	}

	/**
	 * calcolare l'area di lavoro
	 * 
	 * @param LavorazioneSuoloModel
	 * @throws URISyntaxException
	 * @throws SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
	 */
	private void calcolareAreaDiLavoro(LavorazioneSuoloModel lavorazione) {

		ResponseEntity<String> responseFme;
		try {
			responseFme = utilsFme.callProcedureFme(lavorazione.getId(), creaAreaDiLavoro);
		} catch (URISyntaxException e) {
			log.error("Errore trasformata FME nel calcolo dell'area di lavoro", e);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nell'avvio della lavorazione");
		}

		if (responseFme.getStatusCodeValue() != 200) {
			log.error("Errore trasformata FME".concat("responseCode= ").concat(String.valueOf(responseFme.getStatusCodeValue())).concat(" nel calcolo dell'area di lavoro"));
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nell'avvio della lavorazione");
		}
	}

}