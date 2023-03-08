package it.tndigitale.a4gutente.utility;

public class NumberSupport {

    public static Boolean isNullOrLessZero(Integer num) {
        return num==null || num<0;
    }

    public static Boolean isNullOrLessEqualZero(Integer num) {
        return num==null || num<=0;
    }

    public static Boolean isNullOrGreaterZero(Integer num) {
        return num==null || num>0;
    }

    public static Boolean isNullOrGreaterEqualZero(Integer num) {
        return num==null || num>=0;
    }

}
