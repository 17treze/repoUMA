package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder.SuoloSpecificationBuilder;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.SuoloFilter;

@SpringBootTest
public class SuoloDaoTest {

	@Autowired
	private SuoloDao dao;

	@Test
	void creaLavorazioneSuolo() throws Exception {
		Geometry poligonoIntersezione = GisUtils.getGeometry(
				"POLYGON ((1622244.461 5080260.00175, 1622244.215 5080259.464, 1622246.53 5080259.395, 1622274.727 5080238.607, 1622287.678 5080240.694, 1622273.44 5080262.574, 1622262.571 5080273.997, 1622249.62 5080271.28, 1622247.48829 5080266.61922, 1622244.461 5080260.00175))");
		Integer anno = 2022;
		List<SuoloModel> suoli = dao.findByintersects(poligonoIntersezione, anno);
		assertNotNull(suoli);
		assertThat(suoli.size()).isGreaterThan(0);
		for (SuoloModel suolo : suoli) {
			assertThat(GisUtils.getWKTGeometry(suolo.getShape())).startsWith("POLYGON ((");
		}
	}

	@Test
	void testSuoloFilter() throws Exception {
		SuoloFilter filtri = new SuoloFilter();
		filtri.setCampagna(2022);
		filtri.setDataFineValidita(LocalDateTime.of(LocalDate.of(2021, 03, 19), LocalTime.of(18, 00)));
		filtri.setDataInizioValidita(LocalDateTime.of(LocalDate.of(2021, 03, 19), LocalTime.of(15, 00)));
		filtri.setId(142971L);
		filtri.setIdGrid(96L);
		filtri.setIdLavorazioneFine(142080L);
		filtri.setIdLavorazioneInCorso(142080L);
		filtri.setIdLavorazioneInizio(142080L);
		filtri.setNote("NOTES");
		filtri.setSorgente("CARICAMENTO_INIZIALE");
		filtri.setStatoColt(1L);

		List<SuoloModel> suoli = dao.findAll(SuoloSpecificationBuilder.getFilter(filtri));
		assertNotNull(suoli);
	}

}
