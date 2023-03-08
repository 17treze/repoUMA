package it.tndigitale.a4gutente.utility;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import it.tndigitale.a4g.framework.time.Clock;


public class ClockTest {

    public Clock clock = new Clock();

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

}
