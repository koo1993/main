package seedu.ptman.model.outlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

public class DayTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Day(null));
    }

    @Test
    public void constructor_invalidDay_throwsIllegalArgumentException() {
        String invalidDay = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Day(invalidDay));
    }

    @Test
    public void isValidDay_nullDay_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> Day.isValidDay(null));
    }

    @Test
    public void isValidDay_invalidDay_returnsFalse() {
        assertFalse(Day.isValidDay("")); // empty string
        assertFalse(Day.isValidDay(" ")); // spaces only
        assertFalse(Day.isValidDay("^")); // only non-alphanumeric characters
        assertFalse(Day.isValidDay("monday*")); // contains non-alphanumeric characters
        assertFalse(Day.isValidDay("mon")); // contains short form
    }

    @Test
    public void isValidDay_validDay_returnsTrue() {
        assertTrue(Day.isValidDay("monday")); // full spelling of day of week
        assertTrue(Day.isValidDay("tuesday")); // full spelling of day of week
        assertTrue(Day.isValidDay("sunday")); // full spelling of day of week
        assertTrue(Day.isValidDay("ThUrSday")); // upper and lower case characters
    }

    @Test
    public void toString_sameValue_returnsTrue() {
        Day day = new Day("friday");
        assertEquals(day.toString(), "FRIDAY");
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Day test = new Day("sunday");
        Day other = new Day("sunday");
        assertTrue(test.equals(other));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Day test = new Day("sunday");
        Day other = new Day("monday");
        assertFalse(test.equals(other));
    }

    @Test
    public void hashCode_sameDay_returnsTrue() {
        Day test = new Day("monday");
        assertEquals(test.hashCode(), "MONDAY".hashCode());
    }
}
