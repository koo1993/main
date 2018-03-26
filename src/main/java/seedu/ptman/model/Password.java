package seedu.ptman.model;

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Represents a Password in PartTimeManger
 * Store password as hashCode
 */
public class Password {


    public static final String MESSAGE_PASSWORD_CONSTRAINTS =
            "Password should be at least 8 character and no spaces.";

    public static final String DEFAULT_PASSWORD =
            "DEFAULT1";

    /**
     * accept all password that do not have whitespaces and at least 8 characters.
     */
    public static final String PASSWORD_VALIDATION_REGEX = "^(?=\\S+$).{8,}$";

    private String passwordHash;
    private final String initialValue = "IV";

    /**
     * constructor for default password
     */
    public Password() {
        createPassword(DEFAULT_PASSWORD);
    }

    /**
     * use this if hashcode is known
     * @param hashCode
     */
    public Password(String hashCode) {
        requireNonNull(hashCode);
        passwordHash = hashCode;
    }

    public Password(Password masterPassword) {
        requireNonNull(masterPassword);
        this.passwordHash = masterPassword.getPasswordHash();
    }


    /**
     * @param test
     * @return true if password is of correct format
     */
    public static boolean isValidPassword(String test) {
        return test.matches(PASSWORD_VALIDATION_REGEX);
    }

    /**
     * check if passwordHash generated from the string is same as current passwordHash
     * @param password
     * @return true if same
     */
    public boolean isCorrectPassword(String password) {
        return passwordHash.equals(generatePasswordHash(password));
    }

    /**
     * Change password given a password
     * @return true if password is changed
     */
    public boolean checkAndChangePassword(String oldPassword, String newPassword) {
        if (isCorrectPassword(oldPassword)) {
            createPassword(newPassword);
            return true;
        }
        return false;
    }

    /**
     * Create passwordHash when password is entered in plain text
     * @param password
     */
    public void createPassword(String password) {
        requireNonNull(password);
        passwordHash = generatePasswordHash(password);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Generate passwordHash given a string password
     * @param password
     * @return passwordHash in String
     */
    private String generatePasswordHash(String password) {
        String encodedHash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(initialValue.getBytes());
            byte[] byteHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            encodedHash = Base64.getEncoder().encodeToString(byteHash);
        } catch (NoSuchAlgorithmException noSuchAlgoException) {
            System.out.println("cannot generate hash: MessageDigest.getInstance");
        }
        return encodedHash;
    }
    

    @Override
    public int hashCode() {
        return passwordHash.hashCode();
    }
}
