package seedu.ptman.model;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.AppUtil.checkArgument;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Represents a Password in PartTimeManger
 *
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

    /**
     * constructor for default password
     */
    public Password() {
        passwordHash = generatePasswordHash(DEFAULT_PASSWORD);
    }

    /**
     * Constructor to assign a password
     * @param password
     */
    public Password(String password) {
        requireNonNull(password);
        checkArgument(isValidPassword(password), MESSAGE_PASSWORD_CONSTRAINTS);
        passwordHash = generatePasswordHash(password);
    }

    /**
     * Only use this method for loading password file,
     * given that you know the encoded hash.
     * @param encodedHash
     */
    public void changeHash(String encodedHash) {
        passwordHash = encodedHash;
    }

    /**
     *
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
            this.passwordHash = generatePasswordHash(newPassword);
            return true;
        }
        return false;
    }

    /**
     * Generate passwordHash given a string password
     * @param password
     * @return passwordHash in String
     */
    public String generatePasswordHash(String password) {
        String encodedHash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            encodedHash = Base64.getEncoder().encodeToString(byteHash);
        } catch (NoSuchAlgorithmException noSuchAlgoException) {
            System.out.println("cannot generate hash: MessageDigest.getInstance");
        }
        return encodedHash;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Password // instanceof handles nulls
                && this.passwordHash.equals(((Password) other).passwordHash)); // state check
    }

    @Override
    public int hashCode() {
        return passwordHash.hashCode();
    }
}
