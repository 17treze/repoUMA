package it.tndigitale.a4g.framework.support;

public class StringSupport {

    public static Boolean isEmptyOrNull(String target){
        return target == null || target.equals("");
    }

    public static Boolean isEmptyOrNullTrim(String target){
        return target == null || target.trim().equals("");
    }

    public static String getOrDefault(String str) {
        return (str != null && str.trim().length() > 0) ? str.trim() : null;
    }

    public static Boolean isNotEmpty(String target) {
        return (target != null && !target.isEmpty());
    }

    public static boolean isNumber(String number){
        return number.trim().matches("\\d+");
    }

    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);
     
        return sb.toString();
    }

}
