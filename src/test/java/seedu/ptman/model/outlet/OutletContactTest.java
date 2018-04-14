package seedu.ptman.model.outlet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

//@@author SunBangjie
public class OutletContactTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new OutletContact(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new OutletContact(invalidPhone));
    }

    @Test
    public void isValidOutletContact_nullValue_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> OutletContact.isValidOutletContact(null));
    }

    @Test
    public void isValidOutletContact_invalidValues_returnsFalse() {
        assertFalse(OutletContact.isValidOutletContact("")); // empty string
        assertFalse(OutletContact.isValidOutletContact(" ")); // spaces only
        assertFalse(OutletContact.isValidOutletContact("91")); // less than 3 numbers
        assertFalse(OutletContact.isValidOutletContact("phone")); // non-numeric
        assertFalse(OutletContact.isValidOutletContact("9011p041")); // alphabets within digits
        assertFalse(OutletContact.isValidOutletContact("9312 1534")); // spaces within digits
    }

    @Test
    public void isValidOutletContact_validValues_returnsTrue() {
        assertTrue(OutletContact.isValidOutletContact("911")); // exactly 3 numbers
        assertTrue(OutletContact.isValidOutletContact("93121534"));
        assertTrue(OutletContact.isValidOutletContact("124293842033123")); // long phone numbers
    }
}
