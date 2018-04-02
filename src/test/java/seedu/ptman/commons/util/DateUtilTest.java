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
}
