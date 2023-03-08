package it.tndigitale.a4gistruttoria.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtil {

    public static Double round(Double origin) {
        return round(origin, 2);
    }

    public static Double round(Double origin, Integer digits) {
        if (origin != null && digits != null) {
            BigDecimal valueBigDecimal = BigDecimal.valueOf(origin)
                                                   .setScale(digits, RoundingMode.HALF_UP);
            origin = valueBigDecimal.doubleValue();
        }
        return origin;
    }
    
	public static String format(Double value) {
		Locale locale  = new Locale("it", "IT");
		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
		DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
		symbols.setGroupingSeparator('.');
		decimalFormat.setDecimalFormatSymbols(symbols);
		return decimalFormat.format(value);
	}

}
