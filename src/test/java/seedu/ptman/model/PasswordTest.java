package seedu.ptman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.ptman.testutil.Assert;


public class PasswordTest {
    public static final String DEFAULT1_HASH = "wkqTFuX6NX3hucWqn2ZxB24cRo73LssRq7IDOk6Zx00="; // hash code for DEFAULT1
    public static final String DEFAULT2_HASH = "j9R1Y0IIRVI052lxIOkweVd88O+EiSLGJvnXAZXKD40=";
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Password((Password) null));
        Assert.assertThrows(NullPointerException.class, () -> new Password((String) null));
    }

    @Test
    public void constructor_defaultConstructor_noError() {
        Password password = new Password();
        Password expectPassword = new Password(DEFAULT1_HASH);

        assertEquals(password, expectPassword);
    }


    @Test
    public void isValidPassword() {
        // null password
        Assert.assertThrows(NullPointerException.class, () -> Password.isValidPassword(null));

        // invalid Password
        assertFalse(Password.isValidPassword("")); // empty string
        assertFalse(Password.isValidPassword(" ")); // spaces only
        assertFalse(Password.isValidPassword("!#@! fdsafdsafdsa")); // characters with space
        assertFalse(Password.isValidPassword("less8")); // less than 8 character

        // valid name
        assertTrue(Password.isValidPassword("peterjack")); // alphabets only
        assertTrue(Password.isValidPassword("1234567dsa6")); // numbers only
        assertTrue(Password.isValidPassword("peterthe2nd")); // alphanumeric characters
        assertTrue(Password.isValidPassword("CapitalTan")); // with capital letters
        assertTrue(Password.isValidPassword("Da@acks$RayJr2nd")); // alphanumeric with captial and special character
    }

    @Test
    public void isCorrectPassword() {
        Password password = new Password(DEFAULT1_HASH);

        // wrong password
        assertFalse(password.isCorrectPassword("thisiswrongpassword"));
        assertFalse(password.isCorrectPassword("THISISNOTTHEPASS"));

        //correct password
        assertTrue(password.isCorrectPassword("DEFAULT1")); //correct password
    }


    @Test
    public void changePassword() {
        Password password = new Password(DEFAULT1_HASH);
        Password expectedPassword = new Password(DEFAULT2_HASH);

        //wrong password
        assertFalse(password.checkAndChangePassword("this is the password", "newPassword"));
        assertFalse(password.checkAndChangePassword("notapassword", "newPassword"));

        //correct password and changed
        assertTrue(password.checkAndChangePassword("DEFAULT1", "DEFAULT2"));
        assertEquals(password, expectedPassword);

    }

    @Test
    public void changeHash() {
        String encodedHash = "wkqTFuX6NX3hucWqn2ZxB24cRo73LssRq7IDOk6Zx00="; // hash code for DEFAULT1
        Password password = new Password(encodedHash);

        assertFalse(password.isCorrectPassword("newPassword"));
        assertTrue(password.isCorrectPassword("DEFAULT1"));
    }

}
