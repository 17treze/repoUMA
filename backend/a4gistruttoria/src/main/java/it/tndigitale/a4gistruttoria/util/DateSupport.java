package it.tndigitale.a4gistruttoria.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateSupport {

    public static Date convertToDateViaInstant(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay()
                   .atZone(ZoneId.systemDefault())
                   .toInstant());
    }

    public static int calcolaGiorniLavorativi(Date startDate, Date endDate) {
        int giorniLavorativi = 0;

        if (endDate.compareTo(startDate) > 0) {

            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);
            start.setTime(startDate);
            end = resetTime(end);
            start = resetTime(start);
            while (!start.after(end) && !start.equals(end)) {
                start.add(Calendar.DATE, 1);
                int day = start.get(Calendar.DAY_OF_WEEK);
                if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
                    giorniLavorativi++;
                }
            }
        }
        return giorniLavorativi;
    }
    
    private static Calendar resetTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    	return cal;
    }
}
