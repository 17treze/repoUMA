package it.tndigitale.a4g.framework.time;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


public class ClockTest {

    public Clock clock = new Clock();
    public static LocalDate DAY = LocalDate.of(2019,1,1);
    public static LocalDateTime START_OF_DAY = LocalDateTime.of(2019,1,1,0,0);
    public static LocalDateTime END_OF_DAY = LocalDateTime.of(2019,1,1,23,59, 59);

    @Test
    public void itReturnToday() {
        LocalDate today = clock.today();

        assertThat(today).isNotNull();
    }

    @Test
    public void itReturnNow() {
        LocalDateTime now = clock.now();

        assertThat(now).isNotNull();
    }

    @Test
    public void itReturnNowDate() {
        Date now = clock.nowDate();

        assertThat(now).isNotNull();
    }

    @Test
    public void itReturnStartOfDay() {
        LocalDateTime startOfDay = Clock.ofStartOfDay(DAY);

        assertThat(startOfDay).isEqualTo(START_OF_DAY);
    }

    @Test
    public void itReturnDayOfDay() {
        LocalDateTime endOfDay = Clock.ofEndOfDay(DAY);

        assertThat(endOfDay).isEqualTo(END_OF_DAY);
    }
}
