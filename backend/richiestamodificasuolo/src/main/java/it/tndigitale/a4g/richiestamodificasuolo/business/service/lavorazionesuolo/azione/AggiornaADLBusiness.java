package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AreaDiLavoroModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ModalitaADL;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.InputDataGeoJson;
import it.tndigitale.a4g.richiestamodificasuolo.config.ErroriOracle;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("AGGIORNA_ADL")
public class AggiornaADLBusiness extends AzioneLavorazioneBase<String, InputDataGeoJson> {

	private static final Logger log = LoggerFactory.getLogger(AggiornaADLBusiness.class);

	@Autowired
	private UtenteComponent utenteComponent;

	@Value("${it.tndigit.srid.etrs89}")
	private int sridEtrs89;

	@Autowired
	private UtilsFme utilsFme;

	@Value("${it.tndigit.serverFme.calcolaPoligoniDaAdl}")
	private String calcolaPoligoniDaAdl;

	@Value("${it.tndigit.oracle.tolleranza}")
	public Double tolleranza;

	@Value("${it.tndigit.oracle.scostamentoAreaAccettato}")
	private int scostamentoAreaAccettato;

	@Value("${it.tndigit.oracle.percentualeScostamentoAreaAccettato}")
	private Double percentualeScostamentoAreaAccettato;

	@Override
	protected String eseguiAzione(InputDataGeoJson input) {
		return aggiornaADL(input);
	}

	public String aggiornaADL(InputDataGeoJson input) {
		log.debug("START - aggiornaADL {}", input.getIdLavorazione());
		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrNotFound(input.getIdLavorazione(), utenteComponent.username());

		List<AreaDiLavoroModel> adlNew = updateADL(input, lavorazione);

		String res = null;

		if (!adlNew.isEmpty() && checkValiditaOracle(adlNew)) {
			res = calcolaPoligoniDaAdl(lavorazione);
		} else {
			res = "[{\"RETURN\" : \"KO\", \"TIPO_ERRORE\" : \"VALIDAZIONE_ADL\"}]";
			clearAdlChild(lavorazione);
		}

		log.debug("END - aggiornaADL ");
		return res;
	}

	@Transactional
	public void clearAdlChild(LavorazioneSuoloModel lavorazione) {
		getTempClipSuADLDao().deleteByIdLavorazioneTempClipSuADL(lavorazione.getId());
		getSuoloDao().rimuoviSuoloDaLavorazioneInCorso(lavorazione.getId());
		if (lavorazione.getModalitaADL() == ModalitaADL.DISEGNO_ADL) {
			lavorazione.setModalitaAdl(ModalitaADL.POLIGONI_INTERI);
			getLavorazioneSuoloDao().save(lavorazione);
		}
	}

	@Transactional
	public List<AreaDiLavoroModel> updateADL(InputDataGeoJson input, LavorazioneSuoloModel lavorazione) {
		List<AreaDiLavoroModel> adlNew = new ArrayList<>();
		// Ripulisco l'adl
		List<AreaDiLavoroModel> adlToRemove = checkValidState(lavorazione);
		getAreaDiLavoroDao().deleteInBatch(adlToRemove);

		// Aggiorno la nuov ADL
		FeatureCollection featureCollection = checkValidFeatureInput(input.getGeoJson());
		GeoJSONReader reader = new GeoJSONReader();

		for (Feature feature : featureCollection.getFeatures()) {
			AreaDiLavoroModel adlModel = new AreaDiLavoroModel();

			Geometry geometry = reader.read(feature.getGeometry());
			geometry.setSRID(sridEtrs89);

			Map<String, Object> mapProperties = feature.getProperties();

			adlModel.setShape(geometry);
			adlModel.setLavorazioneSuolo(lavorazione);

			adlNew.add(getAreaDiLavoroDao().save(adlModel));
		}

		lavorazione.setModalitaAdl(ModalitaADL.DISEGNO_ADL);
		getLavorazioneSuoloDao().save(lavorazione);
		return adlNew;
	}

	protected List<AreaDiLavoroModel> checkValidState(LavorazioneSuoloModel lavorazione) {
		List<AreaDiLavoroModel> aDLToRemove = null;
		if (!StatoLavorazioneSuolo.IN_CREAZIONE.equals(lavorazione.getStato()) && !StatoLavorazioneSuolo.IN_MODIFICA.equals(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per aggiornare l'ADL {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(
					"Lo stato ".concat(lavorazione.getStato().name()).concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente l'aggiornamento dell'ADL "))));

		} else {
			aDLToRemove = lavorazione.getListaAreadiLavoro();
		}
		return aDLToRemove;
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

	boolean checkValiditaOracle(List<AreaDiLavoroModel> listaAreadiLavoro) {
		boolean res = true;
		if (!listaAreadiLavoro.isEmpty()) {
			for (AreaDiLavoroModel poligonoADL : listaAreadiLavoro) {
				String validazione = validateGeometry(poligonoADL.getId());
				if (!validazione.equals("TRUE")) {
					String descrizioneAnomalia = validazione + ErroriOracle.getMap().getOrDefault(validazione, "");
					String validazioneFixed = validateFixedGeometry(poligonoADL.getId());
					if (validazioneFixed.equals("TRUE")) {
						Double area = getArea(poligonoADL.getId());
						Double areaFixed = getAreaFixed(poligonoADL.getId());
						Double scostamentoArea = Math.abs(areaFixed - area);
						Long getNumElemFixed = getNumElemFixed(poligonoADL.getId());
						if (getNumElemFixed == 1 && scostamentoArea <= scostamentoAreaAccettato && scostamentoArea / area <= percentualeScostamentoAreaAccettato) {
							// aggiorna
							fixAdl(poligonoADL.getId());
						} else {
							res = false;
						}
					} else {
						res = false;
					}
				}
			}
		}

		return res;
	}

	// estratto ai fini di h2
	public boolean fixAdl(Long workspaceId) {
		getAreaDiLavoroDao().fixAdl(workspaceId, tolleranza);
		return true;
	}

	public Long getNumElemFixed(Long id) {
		return getAreaDiLavoroDao().getNumElemFixed(id, tolleranza);
	}

	public Double getArea(Long id) {
		return getAreaDiLavoroDao().getArea(id, tolleranza);
	}

	public Double getAreaFixed(Long id) {
		return getAreaDiLavoroDao().getAreaFixed(id, tolleranza);
	}

	public String validateFixedGeometry(Long id) {
		return getAreaDiLavoroDao().validateFixedGeometry(id, tolleranza);
	}

	public String validateGeometry(Long id) {
		return getAreaDiLavoroDao().validateGeometry(id, tolleranza);
	}

	/**
	 * calcolare poligoni clip
	 * 
	 * @param LavorazioneSuoloModel
	 * @throws URISyntaxException
	 * @throws SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
	 */
	private String calcolaPoligoniDaAdl(LavorazioneSuoloModel lavorazione) {

		log.info("call trasformata fme ", calcolaPoligoniDaAdl);

		ResponseEntity<String> responseFme;
		try {
			Map<String, String> params = new HashMap<>();
			params.put("idLavorazione", String.valueOf(lavorazione.getId()));
			params.put("annoCampagna", String.valueOf(lavorazione.getCampagna()));

			responseFme = utilsFme.callProcedureFmeDataStreaming(calcolaPoligoniDaAdl, params);
		} catch (URISyntaxException e) {
			log.error("Errore trasformata FME nel calcolo dell'area di lavoro", e);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nel calcolo dei poligoni suolo clippati sull'area di lavoro");
		}

		if (responseFme.getStatusCodeValue() != 200) {
			log.error(
					"Errore trasformata FME".concat("responseCode= ").concat(String.valueOf(responseFme.getStatusCodeValue())).concat(" nel calcolo dei poligoni suolo clippati sull'area di lavoro"));
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nel calocolo dei poligoni suolo clippati sull'area di lavoro");
		}
		return responseFme.getBody();
	}

}