package seedu.ptman.model.shift;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

//@@author shanwpf
public class TimeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Time(null));
    }

    @Test
    public void constructor_invalidTime_throwsIllegalArgumentException() {
        String invalidTime = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Time(invalidTime));
    }

    @Test
    public void isValidTime_nullTime_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> Time.isValidTime(null));
    }

    @Test
    public void isValidTime_invalidTime_returnsFalse() {
        assertFalse(Time.isValidTime("")); // empty string
        assertFalse(Time.isValidTime(" ")); // spaces only
        assertFalse(Time.isValidTime("^")); // only non-alphanumeric characters
        assertFalse(Time.isValidTime("11:11")); // contains non-alphanumeric characters
        assertFalse(Time.isValidTime("2500")); // contains invalid time
    }

    @Test
    public void isValidTime_validTime_returnsTrue() {
        assertTrue(Time.isValidTime("0000"));
        assertTrue(Time.isValidTime("2359"));
    }

    @Test
    public void toString_sameValue_returnsTrue() {
        Time day = new Time("1000");
        assertEquals(day.toString(), "1000");
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Time test = new Time("1000");
        Time other = new Time("1000");
        assertTrue(test.equals(other));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Time test = new Time("1200");
        Time other = new Time("2300");
        assertFalse(test.equals(other));
    }

    @Test
    public void hashCode_sameTime_returnsTrue() {
        Time test = new Time("1100");
        assertEquals(test.hashCode(), LocalTime.of(11, 0).hashCode());
    }
}
