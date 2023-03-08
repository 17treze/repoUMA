package it.tndigitale.a4g.richiestamodificasuolo.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateHelper {

	public static LocalDateTime getDateStartDate(LocalDateTime data) {

		return LocalDateTime.of(data.toLocalDate(), LocalTime.MIDNIGHT);
	}

	public static LocalDateTime getDateEndDate(LocalDateTime date) {
		return LocalDateTime.of(date.toLocalDate(), LocalTime.MAX);
	}
}
