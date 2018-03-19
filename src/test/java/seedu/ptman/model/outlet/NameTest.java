package seedu.ptman.model.outlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new OutletName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new OutletName(invalidName));
    }

    @Test
    public void isValidName_nullName_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> OutletName.isValidName(null));
    }

    @Test
    public void isValidName_invalidName_returnsFalse() {
        assertFalse(OutletName.isValidName("")); // empty string
        assertFalse(OutletName.isValidName(" ")); // spaces only
        assertFalse(OutletName.isValidName("^")); // only non-alphanumeric characters
        assertFalse(OutletName.isValidName("peter*")); // contains non-alphanumeric characters
    }

    @Test
    public void isValidName_validName_returnsTrue() {
        assertTrue(OutletName.isValidName("peter jack")); // alphabets only
        assertTrue(OutletName.isValidName("12345")); // numbers only
        assertTrue(OutletName.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(OutletName.isValidName("Capital Tan")); // with capital letters
        assertTrue(OutletName.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
    }

    @Test
    public void toString_sameValue_returnsTrue() {
        OutletName test = new OutletName("valid name");
        assertEquals(test.toString(), "valid name");
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        OutletName test = new OutletName("valid name");
        OutletName other = new OutletName("valid name");
        assertTrue(test.equals(other));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        OutletName test = new OutletName("valid name");
        OutletName other = new OutletName("another valid name");
        assertFalse(test.equals(other));
    }

    @Test
    public void hashCode_sameFullName_returnsTrue() {
        OutletName test = new OutletName("valid name");
        assertEquals(test.hashCode(), "valid name".hashCode());
    }
}
