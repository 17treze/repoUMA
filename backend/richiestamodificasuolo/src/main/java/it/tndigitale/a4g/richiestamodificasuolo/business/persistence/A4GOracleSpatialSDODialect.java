package it.tndigitale.a4g.richiestamodificasuolo.business.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.spatial.SpatialRelation;
import org.hibernate.spatial.dialect.oracle.OracleSpatialSDO10gDialect;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

public class A4GOracleSpatialSDODialect extends OracleSpatialSDO10gDialect {
	
	private static final long serialVersionUID = 3100578877661049218L;

	private static final Map<String, Integer> myCustomFunction = new HashMap<>();
	static {
		myCustomFunction.put("overlaps", SpatialRelation.OVERLAPS);
		myCustomFunction.put("intersects", SpatialRelation.INTERSECTS);
		myCustomFunction.put("contains", SpatialRelation.CONTAINS);
		myCustomFunction.put("crosses", SpatialRelation.CROSSES);
		myCustomFunction.put("disjoint", SpatialRelation.DISJOINT);
		myCustomFunction.put("equals", SpatialRelation.EQUALS);
		myCustomFunction.put("touches", SpatialRelation.TOUCHES);
		myCustomFunction.put("within", SpatialRelation.WITHIN);
	}

	public A4GOracleSpatialSDODialect() {
		super();
	}

	@Override
	public String getSpatialRelateSQL(String columnName, int spatialRelation) {
		String sql = getNativeSpatialRelateSQL( columnName, "?", spatialRelation ) + " = 1";
		sql += " and " + columnName + " is not null";
		return sql;
	}

	
	@Override
	protected void registerFunction(String name, SQLFunction function) {
		if (isMyCustomFunction(name)) {
			super.registerFunction(name, new A4GSpatialRelateFunction(name, getFunctionCase(name), this));
		} else {
			super.registerFunction(name, function);			
		}
	}
	
	protected boolean isMyCustomFunction(String name) {
		return myCustomFunction.keySet().contains(name.toLowerCase());
	}
	
	protected Integer getFunctionCase(String name) {
		return myCustomFunction.get(name.toLowerCase());
	}

	String getNativeSpatialRelateSQL(String arg1, String arg2, int spatialRelation) {
		if (spatialRelation == SpatialRelation.DISJOINT) {
			return getNativeSpatialRelateForDISJOINTSQL(arg1, arg2);
		}
		String mask;
		switch ( spatialRelation ) {
			case SpatialRelation.INTERSECTS:
				//mask = "ANYINTERACT";
				mask = "CONTAINS+COVERS+INSIDE+COVEREDBY+EQUAL+OVERLAPBDYDISJOINT+OVERLAPBDYINTERSECT";
				break;
			case SpatialRelation.CONTAINS:
				mask = "CONTAINS+COVERS";
				break;
			case SpatialRelation.CROSSES:
				throw new UnsupportedOperationException(
						"Oracle Spatial does't have equivalent CROSSES relationship"
				);
			case SpatialRelation.DISJOINT:
				throw new UnsupportedOperationException(
						"Oracle Spatial version does't have equivalent DISJOINT relationship"
						);
			case SpatialRelation.EQUALS:
				mask = "EQUAL";
				break;
			case SpatialRelation.OVERLAPS:
				mask = "OVERLAPBDYDISJOINT+OVERLAPBDYINTERSECT";
				break;
			case SpatialRelation.TOUCHES:
				mask = "TOUCH";
				break;
			case SpatialRelation.WITHIN:
				mask = "INSIDE+COVEREDBY";
				break;
			default:
				throw new IllegalArgumentException(
						"undefined SpatialRelation passed (" + spatialRelation
								+ ")"
				);
		}
		final StringBuilder buffer = new StringBuilder( "SDO_RELATE(" ).append( arg1 )
				.append( "," )
				.append( arg2 )
				.append( ",'mask=" )
				.append( mask )
				.append( "') " );
		return buffer.toString();
	}
	
	String getNativeSpatialRelateForDISJOINTSQL(String arg1, String arg2) {
		final StringBuilder buffer = new StringBuilder( "CASE SDO_RELATE(" ).append( arg1 )
				.append( "," )
				.append( arg2 )
				.append( ",'mask=ANYINTERACT') " )
				.append( "') WHEN 'TRUE' THEN 0 ELSE 1 END" );
		return buffer.toString();
		
	}
	
	
	public static class A4GSpatialRelateFunction extends StandardSQLFunction {
		private final int relation;
		private final A4GOracleSpatialSDODialect dialect;

		private A4GSpatialRelateFunction(
				final String name,
				final int relation,
				A4GOracleSpatialSDODialect dialect) {
			super( name, StandardBasicTypes.BOOLEAN );
			this.relation = relation;
			this.dialect = dialect;
		}
		
		public String render(Type firstArgumentType, final List args, final SessionFactoryImplementor factory) {

			if ( args.size() < 2 ) {
				throw new QueryException(
						"Spatial relate functions require at least two arguments"
				);
			}

			return dialect.getNativeSpatialRelateSQL(
							(String) args.get( 0 ),
							(String) args.get( 1 ), this.relation
					);
		}
		
	}
		

}
