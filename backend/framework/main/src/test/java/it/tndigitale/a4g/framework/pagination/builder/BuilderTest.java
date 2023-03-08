package it.tndigitale.a4g.framework.pagination.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;

public class BuilderTest {

		@Test
    public void sortWithDefault() {
				Sort sort = SortBuilder.build().from(new ArrayList<Ordinamento>());
				assertTrue(sort.isSorted());
				assertEquals(1, sort.stream().count());
				assertTrue(sort.stream().allMatch(s -> s.getProperty().equals(Ordinamento.DEFAULT_ORDER_BY.getProprieta())));
				assertTrue(sort.stream().allMatch(s -> s.getDirection().toString().equals(Ordinamento.DEFAULT_ORDER_BY.getOrdine().name())));
		}
		
		@Test
    public void sortWithOneOrderBy() {
				final Ordinamento ORDINAMENTO = new Ordinamento("descrizione", Ordine.ASC);
				
				Sort sort = SortBuilder.build().from(ORDINAMENTO);
				assertTrue(sort.isSorted());
				assertEquals(2L, sort.stream().count());
				assertTrue(sort.stream().anyMatch(s -> s.getProperty().equals(Ordinamento.DEFAULT_ORDER_BY.getProprieta())));
				assertTrue(sort.stream().anyMatch(s -> s.getDirection().toString().equals(Ordinamento.DEFAULT_ORDER_BY.getOrdine().name())));
				
				assertTrue(sort.stream().anyMatch(s -> s.getProperty().equals(ORDINAMENTO.getProprieta())));
				assertTrue(sort.stream().anyMatch(s -> s.getDirection().toString().equals(ORDINAMENTO.getOrdine().name())));
		}
		
		@Test
    public void sortWithManyOrderBy() {
				List<Ordinamento> ordinamenti = new ArrayList<Ordinamento>();
				final Ordinamento ORDINAMENTO_1 = new Ordinamento("descrizione_1", Ordine.ASC);
				final Ordinamento ORDINAMENTO_2 = new Ordinamento("descrizione_2", Ordine.DESC);
				ordinamenti.add(ORDINAMENTO_1);
				ordinamenti.add(ORDINAMENTO_2);
				
				final long sizeOrdinamenti = ordinamenti.size();
				
				Sort sort = SortBuilder.build().from(ordinamenti);
				assertTrue(sort.isSorted());
				assertEquals(sizeOrdinamenti + 1, sort.stream().count());
				assertTrue(sort.stream().anyMatch(s -> s.getProperty().equals(Ordinamento.DEFAULT_ORDER_BY.getProprieta())));
				assertTrue(sort.stream().anyMatch(s -> s.getDirection().toString().equals(Ordinamento.DEFAULT_ORDER_BY.getOrdine().name())));
				
				assertTrue(sort.stream().anyMatch(s -> s.getProperty().equals(ORDINAMENTO_1.getProprieta())));
				assertTrue(sort.stream().anyMatch(s -> s.getDirection().toString().equals(ORDINAMENTO_1.getOrdine().name())));
				
				assertTrue(sort.stream().anyMatch(s -> s.getProperty().equals(ORDINAMENTO_2.getProprieta())));
				assertTrue(sort.stream().anyMatch(s -> s.getDirection().toString().equals(ORDINAMENTO_2.getOrdine().name())));
		}
		
		@Test
    public void sortWithNullOrderBy() {
				List<Ordinamento> ordinamenti = null;
				final Ordinamento ORDINAMENTO = null;
				final long sizeOrdinamenti = 0L;
				
				Sort sort = SortBuilder.build().from(ordinamenti);
				assertTrue(sort.isSorted());
				assertEquals(sizeOrdinamenti + 1, sort.stream().count());
				assertTrue(sort.stream().anyMatch(s -> s.getProperty().equals(Ordinamento.DEFAULT_ORDER_BY.getProprieta())));
				assertTrue(sort.stream().anyMatch(s -> s.getDirection().toString().equals(Ordinamento.DEFAULT_ORDER_BY.getOrdine().name())));
				
				sort = SortBuilder.build().from(ORDINAMENTO);
				assertTrue(sort.isSorted());
				assertEquals(1L, sort.stream().count());
				assertTrue(sort.stream().allMatch(s -> s.getProperty().equals(Ordinamento.DEFAULT_ORDER_BY.getProprieta())));
				assertTrue(sort.stream().allMatch(s -> s.getDirection().toString().equals(Ordinamento.DEFAULT_ORDER_BY.getOrdine().name())));
		}
		
		@Test
    public void pageableWithNullPaginazione() {
				Paginazione paginazione = null;
				List<Ordinamento> ordinamento = null;
				
				Pageable pageable = PageableBuilder.build().from(paginazione);
				assertNotNull(pageable);
				assertTrue(pageable.isPaged());
				assertEquals(Paginazione.PAGINAZIONE_DEFAULT.getNumeroElementiPagina().intValue(), pageable.getPageSize());
				assertEquals(Paginazione.PAGINAZIONE_DEFAULT.getPagina().intValue(), pageable.getPageNumber());
				assertTrue(pageable.getSort().isSorted());
				
				
				pageable = PageableBuilder.build().from(paginazione, ordinamento);
				assertNotNull(pageable);
				assertTrue(pageable.isPaged());
				assertEquals(Paginazione.PAGINAZIONE_DEFAULT.getNumeroElementiPagina().intValue(), pageable.getPageSize());
				assertEquals(Paginazione.PAGINAZIONE_DEFAULT.getPagina().intValue(), pageable.getPageNumber());
				assertTrue(pageable.getSort().isSorted());
		}
		
		@Test
    public void pageableWithPaginazioneAndSort() {
				final int NUMERO_ELEMENTI = 99;
				final int PAGINA = 6;
				
				Paginazione paginazione = new Paginazione(NUMERO_ELEMENTI, PAGINA);
				
				List<Ordinamento> ordinamenti = new ArrayList<Ordinamento>();
				ordinamenti.add(new Ordinamento("my_prop",Ordinamento.Ordine.DESC));
				ordinamenti.add(new Ordinamento("my_prop_2",Ordinamento.Ordine.ASC));
				
				Pageable pageable = 
						PageableBuilder.build().from(paginazione, ordinamenti);
				assertNotNull(pageable);
				assertTrue(pageable.isPaged());
				assertEquals(NUMERO_ELEMENTI, pageable.getPageSize());
				assertEquals(PAGINA, pageable.getPageNumber());
				assertTrue(pageable.getSort().isSorted());
		}
		
		@Test
    public void pageableWithPaginazioneAndSortNull() {
				final int NUMERO_ELEMENTI = 55;
				final int PAGINA = 3;
				
				Paginazione paginazione = new Paginazione(NUMERO_ELEMENTI, PAGINA);
				List<Ordinamento> ordinamenti = null;
				
				Pageable pageable = PageableBuilder.build().from(paginazione);
				assertNotNull(pageable);
				assertTrue(pageable.isPaged());
				assertEquals(NUMERO_ELEMENTI, pageable.getPageSize());
				assertEquals(PAGINA, pageable.getPageNumber());
				assertTrue(pageable.getSort().isSorted());
				
				pageable = PageableBuilder.build().from(paginazione, ordinamenti);
				assertNotNull(pageable);
				assertTrue(pageable.isPaged());
				assertEquals(NUMERO_ELEMENTI, pageable.getPageSize());
				assertEquals(PAGINA, pageable.getPageNumber());
				assertTrue(pageable.getSort().isSorted());
		}
		
		@Test
    public void risultatiFromNull() {
				RisultatiPaginati results = RisultatiPaginatiBuilder.build().from(null);
				assertNotNull(results);
				assertNull(results.getRisultati());
				assertEquals(0L, results.getCount().longValue());
		}

}
