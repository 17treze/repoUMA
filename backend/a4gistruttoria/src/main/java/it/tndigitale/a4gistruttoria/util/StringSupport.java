package it.tndigitale.a4gistruttoria.util;

public class StringSupport {

    public static Boolean isEmptyOrNull(String target){
        return target == null || target.equals("");
    }

    public static Boolean isEmptyOrNullTrim(String target){
        return target == null || target.trim().equals("");
    }

    public static String getOrDefault(String str) {
        return (str != null && str.trim().length()>0) ? str.trim() : null;
    }

    public static Boolean isNotEmpty(String target) {
        return (target != null && !target.isEmpty());
    }

    public static String upperlike(String str) {
        return "%" + str.toUpperCase() + "%";
    }

    public static String addWhere(String query) {
        if (!query.contains("where")) {
            query = query + " where ";
        }
        return query;
    }

    public static String addOr(String query) {
        return addElement(query, "or");
    }

    public static String addAnd(String query) {
        return addElement(query, "and");
    }

    private static String addElement(String query, String element) {
        String queryTrim = query.trim();
        if (queryTrim.contains("where") &&
            queryTrim.indexOf("where") < queryTrim.length() - 5) {
            query = query + " " + element + " ";
        }
        return query;
    }

    public static boolean isNumber(String number){
        return number.trim().matches("\\d+");
    }

}
