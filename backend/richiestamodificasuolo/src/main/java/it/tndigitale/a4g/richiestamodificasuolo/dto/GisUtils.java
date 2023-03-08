package it.tndigitale.a4g.richiestamodificasuolo.dto;

import java.util.Map;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

public class GisUtils {
	private static final Logger log = LoggerFactory.getLogger(GisUtils.class);

	private final static int SRID_ETRS89 = 25832;

	/**
	 * Ricava il WKT dall'oggetto Geometry
	 * 
	 * @param geometry
	 *            la geometria
	 * @return la geometria in formato WKT
	 */
	public static String getWKTGeometry(Geometry geometry) {
		String result = null;
		if (geometry != null) {
			WKTWriter wktWriter = new WKTWriter();
			result = wktWriter.write(geometry);
		}
		return result;
	}

	/**
	 * Ricava il Geometry dalla stringa WKT
	 * 
	 * @param wkt
	 *            la stringa che contiene la geometria in formato WKT
	 * @return la geometria in formato Geometry
	 */
	public static Geometry getGeometry(String wkt) {
		WKTReader wKTReader = new WKTReader();
		Geometry geo = null;
		try {

			geo = wKTReader.read(wkt);
			geo = TopologyPreservingSimplifier.simplify(geo, 0.001);
			geo.setSRID(SRID_ETRS89);
		} catch (ParseException ex) {
			log.error("Errore creando la geometria dal wkt {}", wkt, ex);
			throw SuoloException.ExceptionType.GENERIC_EXCEPTION.newSuoloExceptionInstance("Errore creando la geometria", ex);
		}
		return geo;
	}

	/**
	 * Calcola l'extent di una Geometry
	 * 
	 * @param org.locationtech.jts.geom.Geometry
	 * 
	 * @return Double[] {minX,minY,maxX,maxY}
	 */
	public static Double[] calculateExtent(Geometry shape) {
		Double[] result = null;
		if (shape != null) {
			Envelope env = shape.getEnvelopeInternal();
			result = new Double[] { env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY() };
		}
		return result;
	}

	public static String parse(Map<String, Object> mapMessage, String key) {
		try {
			String val = String.valueOf(mapMessage.get(key));
			if (val.equals("null"))
				return null;
			else
				return val;
		} catch (Exception e) {
			return null;
		}
	}

}
