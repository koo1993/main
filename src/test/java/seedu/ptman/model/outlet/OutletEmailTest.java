package seedu.ptman.model.outlet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

//@@author SunBangjie
public class OutletEmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new OutletEmail(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new OutletEmail(invalidEmail));
    }

    @Test
    public void isValidOutletEmail_nullValue_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> OutletEmail.isValidOutletEmail(null));
    }

    @Test
    public void isValidOutletEmail_blankEmails_returnsFalse() {
        assertFalse(OutletEmail.isValidOutletEmail("")); // empty string
        assertFalse(OutletEmail.isValidOutletEmail(" ")); // spaces only
    }

    @Test
    public void isValidOutletEmail_missingParts_returnsFalse() {
        assertFalse(OutletEmail.isValidOutletEmail("@example.com")); // missing local part
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee.com")); // missing '@' symbol
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@")); // missing domain name
    }

    @Test
    public void isValidOutletEmail_invalidParts_returnsFalse() {
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@-")); // invalid domain name
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@exam_ple.com")); // underscore in domain name
        assertFalse(OutletEmail.isValidOutletEmail("cool coffee@example.com")); // spaces in local part
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@exam ple.com")); // spaces in domain name
        assertFalse(OutletEmail.isValidOutletEmail(" coolcoffee@example.com")); // leading space
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@example.com ")); // trailing space
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@@example.com")); // double '@' symbol
        assertFalse(OutletEmail.isValidOutletEmail("cool@coffee@example.com")); // '@' symbol in local part
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@example@com")); // '@' symbol in domain name
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@.example.com")); // domain starts with period
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@example.com.")); // domain ends with a period
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@-example.com")); // domain starts with a hyphen
        assertFalse(OutletEmail.isValidOutletEmail("coolcoffee@example.com-")); // domain ends with a hyphen
    }

    @Test
    public void isValidOutletEmail_validEmail_returnsTrue() {
        assertTrue(OutletEmail.isValidOutletEmail("CoolCoffee_3433@example.com"));
        assertTrue(OutletEmail.isValidOutletEmail("a@bc"));  // minimal
        assertTrue(OutletEmail.isValidOutletEmail("test@localhost"));   // alphabets only
        assertTrue(OutletEmail.isValidOutletEmail("!#$%&'*+/=?`{|}~^.-@example.org")); // special characters
        assertTrue(OutletEmail.isValidOutletEmail("123@145"));  // numeric local part and domain name
        // mixture of alphanumeric and special characters
        assertTrue(OutletEmail.isValidOutletEmail("a1+be!@example1.com"));
        // long domain name
        assertTrue(OutletEmail.isValidOutletEmail("cool_coffee@very-very-very-long-example.com"));
        // long local part
        assertTrue(OutletEmail.isValidOutletEmail("if.you.dream.it_you.can.do.it@example.com"));
    }
}
