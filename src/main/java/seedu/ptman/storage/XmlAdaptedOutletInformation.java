package seedu.ptman.storage;

import static seedu.ptman.commons.encrypter.DataEncrypter.decrypt;
import static seedu.ptman.commons.encrypter.DataEncrypter.encrypt;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.model.Password;
import seedu.ptman.model.outlet.Announcement;
import seedu.ptman.model.outlet.OperatingHours;
import seedu.ptman.model.outlet.OutletContact;
import seedu.ptman.model.outlet.OutletEmail;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.OutletName;

//@@author SunBangjie
/**
 * JAXB-friendly version of the OutletInformation.
 */
@XmlRootElement(name = "outletinformation")
public class XmlAdaptedOutletInformation {

    public static final String FAIL_MESSAGE = "Outlet's %s field is missing!";
    public static final String DECRYPT_FAIL_MESSAGE = "Cannot decrypt %s";
    public static final String ENCRYPTED = OutletInformation.DATA_ENCRYPTED_MESSAGE;
    public static final String DECRYPTED = OutletInformation.DATA_NOT_ENCRYPTED_MESSAGE;

    @XmlElement(required = true)
    private String encryptionMode;
    @XmlElement(required = true)
    private String outletName;
    @XmlElement(required = true)
    private String operatingHours;
    @XmlElement(required = true)
    private String outletContact;
    @XmlElement(required = true)
    private String outletEmail;
    @XmlElement(required = true)
    private String passwordHash;
    @XmlElement(required = true)
    private String announcement;

    public XmlAdaptedOutletInformation() {
        this.encryptionMode = null;
        this.outletName = null;
        this.operatingHours = null;
        this.outletContact = null;
        this.outletEmail = null;
        this.passwordHash = null;
        this.announcement = null;
    }

    public XmlAdaptedOutletInformation(String encryptionMode, String outletName, String operatingHours,
                                       String outletContact, String outletEmail, String passwordHash,
                                       String announcement) {
        if (encryptionMode.equals(ENCRYPTED)) {
            this.encryptionMode = encryptionMode;
            try {
                this.outletName = encrypt(outletName);
                this.operatingHours = encrypt(operatingHours);
                this.outletContact = encrypt(outletContact);
                this.outletEmail = encrypt(outletEmail);
                this.passwordHash = encrypt(passwordHash);
                this.announcement = encrypt(announcement);
            } catch (Exception e) {
                //encryption should not fail
            }
        } else {
            setAttributesFromStrings(encryptionMode, outletName, operatingHours, outletContact, outletEmail,
                    passwordHash, announcement);
        }
    }

    /**
     * Converts a given OutletInformation into this class for JAXB use.
     */
    public XmlAdaptedOutletInformation(OutletInformation source) {
        this();
        if (source.getEncryptionMode()) {
            encryptionMode = source.getEncryptionModeMessage();
            try {
                outletName = encrypt(source.getName().fullName);
                operatingHours = encrypt(source.getOperatingHours().value);
                outletContact = encrypt(source.getOutletContact().value);
                outletEmail = encrypt(source.getOutletEmail().value);
                passwordHash = encrypt(source.getMasterPassword().getPasswordHash());
                announcement = encrypt(source.getAnnouncement().value);
            } catch (Exception e) {
                //encryption should not fail
            }
        } else {
            setAttributesFromSource(source);
        }
    }

    public void setAttributesFromStrings(String encryptionMode, String outletName, String operatingHours,
                                         String outletContact, String outletEmail, String passwordHash,
                                         String announcement) {
        this.encryptionMode = encryptionMode;
        this.outletName = outletName;
        this.operatingHours = operatingHours;
        this.outletContact = outletContact;
        this.outletEmail = outletEmail;
        this.passwordHash = passwordHash;
        this.announcement = announcement;
    }

    public void setAttributesFromSource(OutletInformation source) {
        encryptionMode = source.getEncryptionModeMessage();
        outletName = source.getName().fullName;
        operatingHours = source.getOperatingHours().value;
        outletContact = source.getOutletContact().value;
        outletEmail = source.getOutletEmail().value;
        passwordHash = source.getMasterPassword().getPasswordHash();
        announcement = source.getAnnouncement().value;
    }

    private OutletName setOutletName() throws IllegalValueException {
        String decryptedOutletName;
        if (this.encryptionMode.equals(ENCRYPTED)) {
            try {
                decryptedOutletName = decrypt(this.outletName);
            } catch (Exception e) {
                throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, OutletName.class.getSimpleName()));
            }
        } else {
            decryptedOutletName = this.outletName;
        }

        if (decryptedOutletName == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, OutletName.class.getSimpleName()));
        }
        if (!OutletName.isValidName(decryptedOutletName)) {
            throw new IllegalValueException(OutletName.MESSAGE_NAME_CONSTRAINTS);
        }
        OutletName outletName = new OutletName(decryptedOutletName);
        return outletName;
    }

    private OperatingHours setOperatingHours() throws IllegalValueException {
        String decryptedOperatingHours;
        if (this.encryptionMode.equals(ENCRYPTED)) {
            try {
                decryptedOperatingHours = decrypt(this.operatingHours);
            } catch (Exception e) {
                throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE,
                        OperatingHours.class.getSimpleName()));
            }
        } else {
            decryptedOperatingHours = this.operatingHours;
        }

        if (decryptedOperatingHours == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, OperatingHours.class.getSimpleName()));
        }
        if (!OperatingHours.isValidOperatingHours(decryptedOperatingHours)) {
            throw new IllegalValueException(OperatingHours.MESSAGE_OPERATING_HOUR_CONSTRAINTS);
        }
        OperatingHours operatingHours = new OperatingHours(decryptedOperatingHours);
        return operatingHours;
    }

    private OutletContact setOutletContact() throws IllegalValueException {
        String decryptedOutletContact;
        if (this.encryptionMode.equals(ENCRYPTED)) {
            try {
                decryptedOutletContact = decrypt(this.outletContact);
            } catch (Exception e) {
                throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE,
                        OutletContact.class.getSimpleName()));
            }
        } else {
            decryptedOutletContact = this.outletContact;
        }

        if (decryptedOutletContact == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, OutletContact.class.getSimpleName()));
        }
        if (!OutletContact.isValidOutletContact(decryptedOutletContact)) {
            throw new IllegalValueException(OutletContact.MESSAGE_OUTLET_CONTACT_CONSTRAINTS);
        }
        OutletContact outletContact = new OutletContact(decryptedOutletContact);
        return outletContact;
    }

    private OutletEmail setOutletEmail() throws IllegalValueException {
        String decryptedOutletEmail;
        if (this.encryptionMode.equals(ENCRYPTED)) {
            try {
                decryptedOutletEmail = decrypt(this.outletEmail);
            } catch (Exception e) {
                throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE,
                        OutletEmail.class.getSimpleName()));
            }
        } else {
            decryptedOutletEmail = this.outletEmail;
        }

        if (decryptedOutletEmail == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, OutletEmail.class.getSimpleName()));
        }
        if (!OutletEmail.isValidOutletEmail(decryptedOutletEmail)) {
            throw new IllegalValueException(OutletEmail.MESSAGE_OUTLET_EMAIL_CONSTRAINTS);
        }
        OutletEmail outletEmail = new OutletEmail(decryptedOutletEmail);
        return outletEmail;
    }

    private Password setPassword() throws IllegalValueException {
        String decryptedPasswordHash;
        if (this.encryptionMode.equals(ENCRYPTED)) {
            try {
                decryptedPasswordHash = decrypt(this.passwordHash);
            } catch (Exception e) {
                throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Password.class.getSimpleName()));
            }
        } else {
            decryptedPasswordHash = this.passwordHash;
        }

        if (decryptedPasswordHash == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, Password.class.getSimpleName()));
        }
        Password masterPassword = new Password(decryptedPasswordHash);
        return masterPassword;
    }

    private Announcement setAnnouncement() throws IllegalValueException {
        String decryptedAnnouncement;
        if (this.encryptionMode.equals(ENCRYPTED)) {
            try {
                decryptedAnnouncement = decrypt(this.announcement);
            } catch (Exception e) {
                throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE,
                        Announcement.class.getSimpleName()));
            }
        } else {
            decryptedAnnouncement = this.announcement;
        }

        if (decryptedAnnouncement == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, Announcement.class.getSimpleName()));
        }
        Announcement announcement = new Announcement(decryptedAnnouncement);
        return announcement;
    }

    private boolean getEncryptionMode() throws IllegalValueException {
        if (this.encryptionMode == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, "Encryption Mode"));
        }
        if (this.encryptionMode.equals(ENCRYPTED)) {
            return true;
        } else if (this.encryptionMode.equals(DECRYPTED)) {
            return false;
        } else {
            throw new IllegalValueException("Invalid encryption mode");
        }
    }

    /**
     * Converts this jaxb-friendly adapted outlet object into the model's OutletInformation object
     */
    public OutletInformation toModelType() throws IllegalValueException {
        final boolean isDataEncrypted = getEncryptionMode();
        final OutletName outletName = setOutletName();
        final OperatingHours operatingHours = setOperatingHours();
        final OutletContact outletContact = setOutletContact();
        final OutletEmail outletEmail = setOutletEmail();
        final Password masterPassword = setPassword();
        final Announcement announcement = setAnnouncement();
        return new OutletInformation(outletName, operatingHours, outletContact, outletEmail,
                announcement, masterPassword, isDataEncrypted);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedOutletInformation)) {
            return false;
        }

        XmlAdaptedOutletInformation otherOutlet = (XmlAdaptedOutletInformation) other;
        return Objects.equals(outletName, otherOutlet.outletName)
                && Objects.equals(operatingHours, otherOutlet.operatingHours)
                && Objects.equals(outletContact, otherOutlet.outletContact)
                && Objects.equals(outletEmail, otherOutlet.outletEmail)
                && Objects.equals(passwordHash, otherOutlet.passwordHash)
                && Objects.equals(announcement, otherOutlet.announcement)
                && Objects.equals(encryptionMode, otherOutlet.encryptionMode);
    }
}
