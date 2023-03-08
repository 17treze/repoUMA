package it.tndigitale.a4g.framework.time;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class LocalDateConverter {

	private LocalDateConverter() {
	}

	public static LocalDate fromDate(Date date) {
		if (date == null) {
			return null;
		}
		if (date instanceof java.sql.Date) {
			return fromDate((java.sql.Date)date);
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDate fromDate(java.sql.Date date) {
		if (date == null) {
			return null;
		}
		return date.toLocalDate();
	}

	public static Date to(LocalDate date) {
		if (date == null) {
			return null;
		}
		return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static LocalDateTime fromDateTime(Date date) {
		if (date == null) {
			return null;
		}
		if (date instanceof java.sql.Date) {
			return fromDateTime((java.sql.Date)date);
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static LocalDateTime fromDateTime(java.sql.Date date) {
		if (date == null) {
			return null;
		}
		return date.toLocalDate().atStartOfDay();
	}

	public static Date to(LocalDateTime date) {
		if (date == null) {
			return null;
		}
		return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static LocalDate from(Timestamp date) {
		if (date == null) {
			return null;
		}
		return date.toLocalDateTime().toLocalDate();
	}
}
