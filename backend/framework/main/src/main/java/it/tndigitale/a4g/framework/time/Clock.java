package it.tndigitale.a4g.framework.time;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class Clock {
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

	public Date nowDate() {
        return new Date();
    }
	
    public LocalDate today() {
        return LocalDate.now();
    }

    public static LocalDateTime ofStartOfDay(LocalDate day) {
        return day.atStartOfDay();
    }

    public static LocalDateTime ofEndOfDay(LocalDate day) {
        return day.atTime(23, 59, 59);
    }

    public static boolean isInInterval(
    		final LocalDate startDate, final LocalDate endDate, final LocalDate targetDate) {
    	return ((startDate.isEqual(targetDate) || startDate.isBefore(targetDate))
				&& (endDate == null || endDate.isAfter(targetDate)));
    }
}
