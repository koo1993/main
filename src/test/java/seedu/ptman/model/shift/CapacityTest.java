package seedu.ptman.model.shift;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

//@@author shanwpf
public class CapacityTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Capacity(null));
    }

    @Test
    public void constructor_invalidCapacity_throwsIllegalArgumentException() {
        String invalidCapacity = "w";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Capacity(invalidCapacity));
    }

    @Test
    public void isValidCapacity_nullCapacity_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> Capacity.isValidCapacity(null));
    }

    @Test
    public void isValidCapacity_invalidCapacity_returnsFalse() {
        assertFalse(Capacity.isValidCapacity("")); // empty string
        assertFalse(Capacity.isValidCapacity(" ")); // spaces only
        assertFalse(Capacity.isValidCapacity("^")); // only non-numeric characters
        assertFalse(Capacity.isValidCapacity("3*")); // contains non-numeric characters
        assertFalse(Capacity.isValidCapacity("-3")); // only negative numeric string
        assertFalse(Capacity.isValidCapacity("0")); // only zero
    }

    @Test
    public void isValidCapacity_validCapacity_returnsTrue() {
        assertTrue(Capacity.isValidCapacity("4")); // positive integer only
    }

    @Test
    public void toString_sameValue_returnsTrue() {
        Capacity test = new Capacity("5");
        assertEquals(test.toString(), "5");
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Capacity test = new Capacity("5");
        Capacity other = new Capacity("5");
        assertTrue(test.equals(other));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Capacity test = new Capacity("5");
        Capacity other = new Capacity("3");
        assertFalse(test.equals(other));
    }

    @Test
    public void hashCode_sameValue_returnsTrue() {
        Capacity test = new Capacity("4");
        assertEquals(test.hashCode(), new Integer(4).hashCode());
    }
}
