package seedu.ptman.model.outlet;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.Test;

import seedu.ptman.model.outlet.exceptions.IllegalTimeException;
import seedu.ptman.testutil.Assert;

public class ShiftTest {
    public static final LocalTime TIME_AM = LocalTime.of(9, 0);
    public static final LocalTime TIME_PM = LocalTime.of(20, 0);

    @Test
    public void constructor_illegalTime_throwsIllegalTimeException() {
        Assert.assertThrows(IllegalTimeException.class, () ->
                new Shift(TIME_PM, TIME_AM, DayOfWeek.MONDAY, 3)
        );
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
                new Shift(null, null, null, 4)
        );
        Assert.assertThrows(NullPointerException.class, () ->
                new Shift(TIME_AM, null, DayOfWeek.MONDAY, 1)
        );
        Assert.assertThrows(NullPointerException.class, () ->
                new Shift(null, TIME_PM, DayOfWeek.MONDAY, 4)
        );
    }

}
