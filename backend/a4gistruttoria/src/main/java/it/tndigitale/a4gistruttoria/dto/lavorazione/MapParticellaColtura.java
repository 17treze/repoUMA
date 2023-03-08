package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.HashMap;
import java.util.Map;

public class MapParticellaColtura {
	private Map<ParticellaColtura, MapVariabili> map = new HashMap<>();

	public Map<ParticellaColtura, MapVariabili> getMap() {
		return map;
	}

	public void setMap(Map<ParticellaColtura, MapVariabili> map) {
		this.map = map;
	}

	public void addElement(ParticellaColtura particellaColtura) {
		if (map == null)
			map = new HashMap<>();

		map.put(particellaColtura, new MapVariabili());
	}

	public void addElement(ParticellaColtura particellaColtura, MapVariabili mapVariabili) {
		if (map == null)
			map = new HashMap<>();

		map.put(particellaColtura, mapVariabili);
	}
}
