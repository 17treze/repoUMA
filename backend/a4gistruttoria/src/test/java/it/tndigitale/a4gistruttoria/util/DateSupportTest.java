package it.tndigitale.a4gistruttoria.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

public class DateSupportTest {

    @Test
    public void forCalcolaGiorniLavorativiIfSameDateThenReturnZero() {
        Date date = new Date();

        int giorniLavorativi = DateSupport.calcolaGiorniLavorativi(date, date);

        assertThat(giorniLavorativi).isEqualTo(0);
    }

    @Test
    public void forCalcolaGiorniLavorativiIfStartDateGreaterEndDateThenReturnZero() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(1);

        int giorniLavorativi = DateSupport.calcolaGiorniLavorativi(DateSupport.convertToDateViaInstant(startDate),
                DateSupport.convertToDateViaInstant(endDate));

        assertThat(giorniLavorativi).isEqualTo(0);
    }

    @Test
    public void forCalcolaGiorniLavorativiIfStartDateLessEndDateWithoutHolidaysThenReturnNotZero() {
        LocalDate startDate = LocalDate.of(2020, 1, 21);
        LocalDate endDate = LocalDate.of(2020, 1, 23);

        int giorniLavorativi = DateSupport.calcolaGiorniLavorativi(DateSupport.convertToDateViaInstant(startDate),
                DateSupport.convertToDateViaInstant(endDate));

        assertThat(giorniLavorativi).isEqualTo(2);
    }

    @Test
    public void forCalcolaGiorniLavorativiIfStartDateLessEndDateWithHolidaysThenReturnNotZero() {
        LocalDate startDate = LocalDate.of(2020, 1, 21);
        LocalDate endDate = LocalDate.of(2020, 1, 27);

        int giorniLavorativi = DateSupport.calcolaGiorniLavorativi(DateSupport.convertToDateViaInstant(startDate),
                DateSupport.convertToDateViaInstant(endDate));

        assertThat(giorniLavorativi).isEqualTo(4);
    }
    
    
    @Test
    public void forCalcolaGiorniLavorativiIfStartDateLessEndDateWithoutHolidaysThenReturnNotZero_date() {
    	Date startDate = new GregorianCalendar(2020, Calendar.JANUARY, 21, 10, 10).getTime();
    	Date endDate =   new GregorianCalendar(2020, Calendar.JANUARY, 23, 11, 11).getTime();

        int giorniLavorativi = DateSupport.calcolaGiorniLavorativi(startDate,
        		endDate);

        assertThat(giorniLavorativi).isEqualTo(2);
    }


}
