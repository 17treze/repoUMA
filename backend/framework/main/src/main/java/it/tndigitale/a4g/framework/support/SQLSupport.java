package it.tndigitale.a4g.framework.support;

public class SQLSupport {

		public static String addOr(String query) {
		    return SQLSupport.addElement(query, "or");
		}

		public static String addAnd(String query) {
		    return SQLSupport.addElement(query, "and");
		}

		public static String addWhere(String query) {
		    if (!query.contains("where")) {
		        query = query + " where ";
		    }
		    return query;
		}

		public static String upperLike(String str) {
		    return "%" + str.toUpperCase() + "%";
		}

		private static String addElement(String query, String element) {
		    String queryTrim = query.trim();
		    if (queryTrim.contains("where") &&
		        queryTrim.indexOf("where") < queryTrim.length() - 5) {
		        query = query + " " + element + " ";
		    }
		    return query;
		}

}
