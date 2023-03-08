package it.tndigitale.a4g.richiestamodificasuolo.business.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Map;

import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.spatial.SpatialRelation;
import org.junit.jupiter.api.Test;

public class A4GOracleSpatialSDODialectTest {

	@Test
	public void testRegisterIntersects() {
		A4GOracleSpatialSDODialect dialect = new A4GOracleSpatialSDODialect();
		Map<String, SQLFunction> mappaFunzioni = dialect.getFunctions();
		assertThat(mappaFunzioni.containsKey("intersects")).isTrue();
		SQLFunction sqlFunction = mappaFunzioni.get("intersects");
		String function = sqlFunction.render(null, Arrays.asList("primo", "secondo"), null);
		assertThat(function).isEqualTo("SDO_RELATE(primo,secondo,'mask=CONTAINS+COVERS+INSIDE+COVEREDBY+EQUAL+OVERLAPBDYDISJOINT+OVERLAPBDYINTERSECT') ");
	}

	@Test
	public void testGetSpatialRelateSQL() {
		A4GOracleSpatialSDODialect dialect = new A4GOracleSpatialSDODialect();
		String function = dialect.getSpatialRelateSQL("miacolonna", SpatialRelation.TOUCHES);
		assertThat(function).isEqualTo("SDO_RELATE(miacolonna,?,'mask=TOUCH')  = 1 and miacolonna is not null");
	}

	@Test
	public void testRegisterOtherOracleFunction() {
		A4GOracleSpatialSDODialect dialect = new A4GOracleSpatialSDODialect();
		Map<String, SQLFunction> mappaFunzioni = dialect.getFunctions();
		assertThat(mappaFunzioni.containsKey("concat_lines")).isTrue();
	}

	@Test
	public void testRegisterContains() {
		A4GOracleSpatialSDODialect dialect = new A4GOracleSpatialSDODialect();
		Map<String, SQLFunction> mappaFunzioni = dialect.getFunctions();
		assertThat(mappaFunzioni.containsKey("contains")).isTrue();
		SQLFunction sqlFunction = mappaFunzioni.get("contains");
		String function = sqlFunction.render(null, Arrays.asList("primo", "secondo"), null);
		assertThat(function).isEqualTo("SDO_RELATE(primo,secondo,'mask=CONTAINS+COVERS') ");
	}
}
