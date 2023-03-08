package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.GrigliaSuoloModel;

@SpringBootTest
public class GigliaSuoloDao {

	@Autowired
	private GrigliaSuoloDao dao;

	@Test
	void testGrigliaDao() throws Exception {

		Long idGrid = 96L;
		Optional<GrigliaSuoloModel> griglia = dao.findById(idGrid);
		assertNotNull(griglia);
		GrigliaSuoloModel gr = griglia.get();
		assertEquals(25000000, gr.getArea());
		assertEquals(true, gr.isIntersecaSuolo());
		assertNull(gr.getShape());
		assertNotNull(gr.getListaSuoloModel());
	}

}
