package seedu.ptman.model.shift;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

//@@author shanwpf
public class DateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Date((String) null));
        Assert.assertThrows(NullPointerException.class, () -> new Date((LocalDate) null));
    }

    @Test
    public void constructor_invalidDate_throwsIllegalArgumentException() {
        String invalidDate = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Date(invalidDate));
    }

    @Test
    public void isValidDate_nullDate_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> Date.isValidDate(null));
    }

    @Test
    public void isValidDate_invalidDate_returnsFalse() {
        assertFalse(Date.isValidDate("")); // empty string
        assertFalse(Date.isValidDate(" ")); // spaces only
        assertFalse(Date.isValidDate("^")); // only non-alphanumeric characters
        assertFalse(Date.isValidDate("11-11")); // invalid date
        assertFalse(Date.isValidDate("12-13-18")); // invalid Date
    }

    @Test
    public void isValidDate_validDate_returnsTrue() {
        assertTrue(Date.isValidDate("12-12-18"));
        assertTrue(Date.isValidDate("01-01-19"));
    }

    @Test
    public void toString_sameValue_returnsTrue() {
        Date date = new Date("10-10-18");
        assertEquals(date.toString(), "10-10-18");
    }

    @Test
    public void equals_null_returnsFalse() {
        Date test = new Date("10-10-18");
        assertFalse(test.equals(null));
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Date test = new Date("10-10-18");
        Date other = new Date("10-10-18");
        assertTrue(test.equals(other));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Date test = new Date("12-10-10");
        Date other = new Date("11-01-11");
        assertFalse(test.equals(other));
    }

    @Test
    public void hashCode_sameDate_sameHashCode() {
        Date test = new Date("11-11-18");
        Date test1 = new Date("11-11-18");
        assertEquals(test.hashCode(), test1.hashCode());
    }

    @Test
    public void hashCode_differentDate_differentHashCode() {
        Date test = new Date("11-11-18");
        Date test1 = new Date("11-12-18");
        assertNotEquals(test.hashCode(), test1.hashCode());
    }
}
