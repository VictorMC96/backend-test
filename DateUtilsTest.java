package com.neology.app.context.shaed;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateUtilsTest {

    @Test
    void shouldCalculateDifferenceInMinutesCorrectly() {
        Calendar entry = Calendar.getInstance();
        Calendar exit = (Calendar) entry.clone();
        exit.add(Calendar.MINUTE, 35);

        long minutes = DateUtils.difEnMinutes(entry, exit);

        assertEquals(35, minutes);
    }

    @Test
    void shouldReturnZeroForSameEntryAndExitTime() {
        Calendar entry = Calendar.getInstance();
        Calendar exit = (Calendar) entry.clone();

        long minutes = DateUtils.difEnMinutes(entry, exit);

        assertEquals(0, minutes);
    }

    @Test
    void shouldTruncateSecondsAndReturnWholeMinutes() {
        Calendar entry = Calendar.getInstance();
        Calendar exit = (Calendar) entry.clone();
        exit.add(Calendar.MINUTE, 2);
        exit.add(Calendar.SECOND, 30);

        long minutes = DateUtils.difEnMinutes(entry, exit);

        assertEquals(2, minutes);
    }
}
