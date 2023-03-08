package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wololo.geojson.Feature;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.InputDataGeoJson;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("CERCA_POLIGONI_VIGENTE_DA_CLICK_IN_MAPPA")
public class CercaPoligonoVigenteDaPunto extends AzioneLavorazioneBase<String, InputDataGeoJson> {

	@Value("${it.tndigit.srid.etrs89}")
	private int sridEtrs89;

	private static final Logger log = LoggerFactory.getLogger(CercaPoligonoVigenteDaPunto.class);

	@Override
	protected String eseguiAzione(InputDataGeoJson input) {
		return cercaPoligonoVigenteDaPunto(input);
	}

	public String cercaPoligonoVigenteDaPunto(InputDataGeoJson input) {
		log.debug("START - cercaPoligonoVigenteDaPunto {}", input.getIdLavorazione());

		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrNotFound(input.getIdLavorazione(), input.getUtente());

		// 1 Valida stato Lavorazione
		checkValidState(lavorazione);

		// 2 Valida geometria
		checkEmptyGeoJson(input.getGeoJson());

		// 3 Trasforma geojson in geometry
		Feature feature = (Feature) GeoJSONFactory.create(input.getGeoJson());
		GeoJSONReader reader = new GeoJSONReader();
		Geometry geometry = reader.read(feature.getGeometry());
		geometry.setSRID(sridEtrs89);

		List<SuoloModel> suoli = getSuoloDao().findByContains(lavorazione.getCampagna(), geometry);

		if (suoli.isEmpty()) {
			String error = "Impossibile aggiungere/rimuovere il poligono dalla lavorazione. Nessun poligono è stato trovato ";
			log.error(error);
			throw SuoloException.ExceptionType.NOT_FOUND_EXCEPTION.newSuoloExceptionInstance(error);
		}

		for (SuoloModel suolo : suoli) {
			if (isSuoloAssociabile(suolo, lavorazione)) { // se libero associo
				// se associato alla lavorazione in input lo rimuovo

				associaDissociaSuoloALavorazione(lavorazione, suolo);
			} else {
				String error = "Impossibile aggiungere/rimuovere il poligono alla lavorazione. Il poligono di suolo vigente ".concat(String.valueOf(suolo.getId()))
						.concat(" risulta associato alla lavorazione ").concat(String.valueOf(suolo.getIdLavorazioneInCorso()));
				log.error(error);
				throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(error);
			}
		}

		return null;
	}

	private void checkEmptyGeoJson(String geoJson) {
		if (geoJson.isEmpty()) {
			log.error("La geometria passata è vuota");
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("La geometria passata è vuota");

		}

	}

	private boolean isSuoloAssociabile(SuoloModel suoloVigente, LavorazioneSuoloModel lavorazione) {
		return (suoloVigente.getIdLavorazioneInCorso() == null || lavorazione.getId().equals(suoloVigente.getIdLavorazioneInCorso().getId()));
	}

	private void associaDissociaSuoloALavorazione(LavorazioneSuoloModel lavorazione, SuoloModel suoloVigente) {
		if (suoloVigente.getIdLavorazioneInCorso() == null) {
			log.debug("Associo suolo vigente {} a lavorazione {}", suoloVigente.getId(), lavorazione.getId());
			lavorazione.addSuoloInCorsoModel(suoloVigente);
			lavorazione.setDataUltimaModifica(getClock().now());
			lavorazione = getLavorazioneSuoloDao().saveAndFlush(lavorazione);
		} else if (suoloVigente.getIdLavorazioneInCorso().getId().equals(lavorazione.getId())) {
			log.debug("Dissocio suolo vigente {} a lavorazione {}", suoloVigente.getId(), lavorazione.getId());
			lavorazione.removeSuoloInCorsoModel(suoloVigente);
			lavorazione.setDataUltimaModifica(getClock().now());
			lavorazione = getLavorazioneSuoloDao().saveAndFlush(lavorazione);
		}

	}

	protected void checkValidState(LavorazioneSuoloModel lavorazione) {
		if (!StatoLavorazioneSuolo.IN_CREAZIONE.equals(lavorazione.getStato()) && !StatoLavorazioneSuolo.IN_MODIFICA.equals(lavorazione.getStato())) {
			log.error("Lo stato ".concat(lavorazione.getStato().name())
					.concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente l'associazione di suolo vigente "))));
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Lo stato ".concat(lavorazione.getStato().name())
					.concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente l'associazione di suolo vigente "))));

		}

	}

}
