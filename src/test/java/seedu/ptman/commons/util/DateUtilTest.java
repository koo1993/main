package seedu.ptman.commons.util;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

//@@author shanwpf
public class DateUtilTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getWeekFromDate_validDate_returnsWeekNumber() {
        // 14th Jan 2018 is in the 2nd week of 2018
        assertEquals(DateUtil.getWeekFromDate(LocalDate.of(2018, 1, 14)), 2);

        // 15th Jan 2018 is in the 3rd week of 2018
        assertEquals(DateUtil.getWeekFromDate(LocalDate.of(2018, 1, 15)), 3);
    }

    @Test
    public void getWeekFromDate_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        DateUtil.getWeekFromDate(null);
    }

    @Test
    public void getMondayOfDate_validDate_returnsMondayDate() {
        // Sunday 8th April 2018 returns Monday 2nd April 2018
        assertEquals(DateUtil.getMondayOfDate(LocalDate.of(2018, 4, 8)), LocalDate.of(2018, 4, 2));

        // Monday 9th April 2018 returns Monday 9th April 2018
        assertEquals(DateUtil.getMondayOfDate(LocalDate.of(2018, 4, 9)), LocalDate.of(2018, 4, 9));
    }

    @Test
    public void getMondayOfDate_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        DateUtil.getMondayOfDate(null);
    }

    //@@author hzxcaryn
    @Test
    public void getThursdayOfDate_validDate_returnsThursdayDate() {
        // Sunday 8th April 2018 returns Thursday 5th April 2018
        assertEquals(DateUtil.getThursdayOfDate(LocalDate.of(2018, 4, 8)), LocalDate.of(2018, 4, 5));

        // Monday 9th April 2018 returns Thursday 12th April 2018
        assertEquals(DateUtil.getThursdayOfDate(LocalDate.of(2018, 4, 9)), LocalDate.of(2018, 4, 12));
    }

    @Test
    public void getThursdayOfDate_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        DateUtil.getThursdayOfDate(null);
    }

    @Test
    public void getNextWeekDate_validDate_returnsNextWeekDate() {
        // Sunday 8th April 2018 returns 15th April 2018
        assertEquals(DateUtil.getNextWeekDate(LocalDate.of(2018, 4, 8)), LocalDate.of(2018, 4, 15));
    }

    @Test
    public void getNextWeekDate_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        DateUtil.getNextWeekDate(null);
    }

    @Test
    public void getPrevWeekDate_validDate_returnsNextWeekDate() {
        // Sunday 8th April 2018 returns 1st April 2018
        assertEquals(DateUtil.getPrevWeekDate(LocalDate.of(2018, 4, 8)), LocalDate.of(2018, 4, 1));
    }

    @Test
    public void getPrevWeekDate_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        DateUtil.getPrevWeekDate(null);
    }

    @Test
    public void getMonthYearFromDate_validDate_returnsMonthYear() {
        // Sunday 8th April 2018 returns APRIL 2018
        assertEquals(DateUtil.getMonthYearFromDate(LocalDate.of(2018, 4, 8)), "APRIL 2018");
    }

    @Test
    public void getMonthYearFromDate_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        DateUtil.getMonthYearFromDate(null);
    }

}
