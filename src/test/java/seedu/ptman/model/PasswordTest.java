package seedu.ptman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

public class PasswordTest {
    @Test
    public void constructor_invalidPassword_throwsIllegalArgumentException() {
        String invalidName = "have space";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Password(invalidName));
    }

    @Test
    public void isValidPassword() {
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
        Password password = new Password("ThisIsThePassword");

        assertFalse(password.isCorrectPassword("thisiswrongpassword")); //wrong password
        assertFalse(password.isCorrectPassword("ThisIsThepassword")); // wrong password

        assertTrue(password.isCorrectPassword("ThisIsThePassword")); //correct password
    }


    @Test
    public void changePassword() {

        Password password = new Password("ThisIsThePassword");
        Password expectedPassword = new Password("newPassword");

        assertFalse(password.checkAndChangePassword("this is the password", "newPassword")); //wrong password
        assertFalse(password.checkAndChangePassword("notapassword", "newPassword")); //wrong password

        assertTrue(password.checkAndChangePassword("ThisIsThePassword", "newPassword")); //wrong password
        assertEquals(password, expectedPassword);

    }

    @Test
    public void changeHash() {
        String encodedHash = "XCmpWavOTtpfDnpOfqU9zk+g8Ku+jqpjcX4v7V8ZPTE="; // hash code for newPassword
        Password password = new Password("ThisIsThePassword");

        assertFalse(password.isCorrectPassword("newPassword"));

        password.changeHash(encodedHash);
        assertTrue(password.isCorrectPassword("newPassword"));
    }

}
