package seedu.ptman.model.outlet;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

public class ShiftTest {

    @Test
    public void constructor_illegalTime_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () ->
                new Shift(new Day("monday"), new Time("2200"), new Time("1000"), new Capacity("4"))
        );
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
                new Shift(null, null, null, null)
        );
        Assert.assertThrows(NullPointerException.class, () ->
                new Shift(new Day("monday"), new Time("1000"), null, null)
        );
    }

}
