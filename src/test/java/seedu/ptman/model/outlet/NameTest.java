package seedu.ptman.model.outlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName_nullName_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> Name.isValidName(null));
    }

    @Test
    public void isValidName_invalidName_returnsFalse() {
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("^")); // only non-alphanumeric characters
        assertFalse(Name.isValidName("peter*")); // contains non-alphanumeric characters
    }

    @Test
    public void isValidName_validName_returnsTrue() {
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
    }

    @Test
    public void toString_sameValue_returnsTrue() {
        Name test = new Name("valid name");
        assertEquals(test.toString(), "valid name");
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Name test = new Name("valid name");
        Name other = new Name("valid name");
        assertTrue(test.equals(other));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Name test = new Name("valid name");
        Name other = new Name("another valid name");
        assertFalse(test.equals(other));
    }

    @Test
    public void hashCode_sameFullName_returnsTrue() {
        Name test = new Name("valid name");
        assertEquals(test.hashCode(), "valid name".hashCode());
    }
}
