package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.util.List;
import java.util.stream.Collectors;

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
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.RichiestaModificaSuoloUtils;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.evento.AvvioLavorazioneEvento;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.InputDataGeoJson;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("CERCA_POLIGONI_DICHIARATO_DA_CLICK_IN_MAPPA")
public class CercaPoligonoDichiaratoDaPunto extends AzioneLavorazioneBase<String, InputDataGeoJson> {

	@Value("${it.tndigit.srid.etrs89}")
	private int sridEtrs89;

	private static final Logger log = LoggerFactory.getLogger(CercaPoligonoDichiaratoDaPunto.class);

	@Override
	protected String eseguiAzione(InputDataGeoJson input) {
		return cercaPoligonoDichiaratoDaPunto(input);
	}

	public String cercaPoligonoDichiaratoDaPunto(InputDataGeoJson input) {
		log.debug("START - cercaPoligonoDichiaratoDaPunto {}", input.getIdLavorazione());

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

		List<SuoloDichiaratoModel> suoli = getSuoloDichiaratoDao().findByContains(geometry, lavorazione.getCampagna());

		List<SuoloDichiaratoModel> suoliOkStatoRichiesta = suoli.stream().filter(x -> RichiestaModificaSuoloUtils.listaStatiSuoloAssociabile.contains(x.getRichiestaModificaSuolo().getStato()))
				.collect(Collectors.toList());

		List<SuoloDichiaratoModel> suoliOkLavorazione = suoliOkStatoRichiesta.stream().filter(x -> isSuoloAssociabile(x, lavorazione)).collect(Collectors.toList());

		if (suoliOkStatoRichiesta.size() > 0) {
			if (suoliOkLavorazione.size() > 0) {
				suoliOkLavorazione.forEach(suolo -> associaOrDissociaSuoloALavorazione(lavorazione, suolo));
			} else {
				String error = "Impossibile aggiungere o rimuovere i poligoni alla lavorazione. ".concat("I poligoni risultano associati a un'altra lavorazione ");
				log.error(error);
				throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(error);
			}
		} else {
			String error = "Impossibile aggiungere o rimuovere il poligono alla lavorazione. Nessun poligono è stato trovato ";
			log.error(error);
			throw SuoloException.ExceptionType.NOT_FOUND_EXCEPTION.newSuoloExceptionInstance(error);
		}
		return null;

	}

	private void checkEmptyGeoJson(String geoJson) {
		if (geoJson.isEmpty()) {
			log.error("La geometria passata è vuota");
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("La geometria passata è vuota");

		}

	}

	private boolean isSuoloAssociabile(SuoloDichiaratoModel suoloDichiatrato, LavorazioneSuoloModel lavorazione) {
		return (suoloDichiatrato.getLavorazioneSuolo() == null || lavorazione.getId().equals(suoloDichiatrato.getLavorazioneSuolo().getId()));
	}

	private void associaOrDissociaSuoloALavorazione(LavorazioneSuoloModel lavorazione, SuoloDichiaratoModel suoloDichiarato) {

		if (suoloDichiarato.getLavorazioneSuolo() == null) {
			log.debug("Associo suolo dichiarato {} a lavorazione {}", suoloDichiarato.getId(), lavorazione.getId());
			lavorazione.addSuoloDichiaratoModel(suoloDichiarato);
			lavorazione.setDataUltimaModifica(getClock().now());
			lavorazione = getLavorazioneSuoloDao().saveAndFlush(lavorazione);
			// Notifica evento aggiornamento richiesta modifica suolo associata al suolo dichiarato
			getEventoPublisher().notificaEvento(new AvvioLavorazioneEvento(lavorazione));
		} else if (suoloDichiarato.getLavorazioneSuolo().getId().equals(lavorazione.getId())) {
			log.debug("Dissocio suolo dichiarato {} a lavorazione {}", suoloDichiarato.getId(), lavorazione.getId());
			lavorazione.removeSuoloDichiaratoModel(suoloDichiarato);
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
