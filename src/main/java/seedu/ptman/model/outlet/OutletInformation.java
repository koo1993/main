package seedu.ptman.model.outlet;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.ptman.model.Password;
import seedu.ptman.model.outlet.exceptions.NoOutletInformationFieldChangeException;

//@@author SunBangjie
/**
 * Represents an outlet in PTMan.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class OutletInformation {

    public static final String DEFAULT_OUTLET_NAME = "DefaultOutlet";
    public static final String DEFAULT_OPERATING_HOURS = "0900-2200";
    public static final String DEFAULT_OUTLET_CONTACT = "91234567";
    public static final String DEFAULT_OUTLET_EMAIL = "DefaultOutlet@gmail.com";
    public static final String DEFAULT_ANNOUNCEMENT_MESSAGE = "No announcement. "
            + "Please add new announcement with announcement command.";
    public static final String DATA_ENCRYPTED_MESSAGE = "Local storage files are encrypted.";
    public static final String DATA_NOT_ENCRYPTED_MESSAGE =
            "Local storage files are not encrypted.";

    private OutletName name;
    private Password masterPassword;
    private OperatingHours operatingHours;
    private OutletContact outletContact;
    private OutletEmail outletEmail;
    private Announcement announcement;
    private boolean isDataEncrypted;

    /**
     * Constructs an {@code OutletInformation}.
     */
    public OutletInformation(OutletName name, OperatingHours operatingHours, OutletContact outletContact,
                             OutletEmail outletEmail, Announcement announcement, Password masterPassword,
                             boolean isDataEncrypted) {
        requireAllNonNull(name, operatingHours, outletContact, outletEmail, announcement, masterPassword,
                isDataEncrypted);
        this.name = name;
        this.operatingHours = operatingHours;
        this.outletContact = outletContact;
        this.outletEmail = outletEmail;
        this.announcement = announcement;
        this.masterPassword = masterPassword;
        this.isDataEncrypted = isDataEncrypted;
    }

    /**
     * Constructs a new {@code OutletInformation} from source outlet.
     * @param outlet source outlet
     */
    public OutletInformation(OutletInformation outlet) {
        this.name = new OutletName(outlet.getName().toString());
        this.masterPassword = new Password(outlet.getMasterPassword());
        this.outletContact = new OutletContact(outlet.getOutletContact().toString());
        this.operatingHours = new OperatingHours(outlet.getOperatingHours().toString());
        this.outletEmail = new OutletEmail(outlet.getOutletEmail().toString());
        this.announcement = new Announcement(outlet.getAnnouncement().toString());
        this.isDataEncrypted = outlet.getEncryptionMode();
    }

    /**
     * Default constructor with default values
     */
    public OutletInformation() {
        this.name = new OutletName(DEFAULT_OUTLET_NAME);
        this.masterPassword = new Password();
        this.operatingHours = new OperatingHours(DEFAULT_OPERATING_HOURS);
        this.outletContact = new OutletContact(DEFAULT_OUTLET_CONTACT);
        this.outletEmail = new OutletEmail(DEFAULT_OUTLET_EMAIL);
        this.announcement = new Announcement(DEFAULT_ANNOUNCEMENT_MESSAGE);
        this.isDataEncrypted = false;
    }

    public OutletName getName() {
        return name;
    }

    public Password getMasterPassword() {
        return masterPassword;
    }

    public OperatingHours getOperatingHours() {
        return operatingHours;
    }

    public OutletContact getOutletContact() {
        return outletContact;
    }

    public OutletEmail getOutletEmail() {
        return outletEmail;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public boolean getEncryptionMode() {
        return isDataEncrypted;
    }

    /**
     * Sets the outlet password.
     * only set after checking against outlet password.
     * @param password
     */
    public void setOutletPassword (Password password) {
        requireNonNull(password);
        this.masterPassword = password;

    }

    /**
     * Sets the outlet information attributes.
     * Some fields can be unspecified.
     * If all fields are unspecified, NoOutletInformationFieldChangeException will be thrown.
     */
    public void setOutletInformation(OutletName name, OperatingHours operatingHours, OutletContact outletContact,
                                     OutletEmail outletEmail)
            throws NoOutletInformationFieldChangeException {
        if (name == null && operatingHours == null && outletContact == null && outletEmail == null) {
            throw new NoOutletInformationFieldChangeException();
        }
        if (name != null) {
            this.name = name;
        }
        if (operatingHours != null) {
            this.operatingHours = operatingHours;
        }
        if (outletContact != null) {
            this.outletContact = outletContact;
        }
        if (outletEmail != null) {
            this.outletEmail = outletEmail;
        }
    }

    /**
     * sets outlet information from source outlet
     * @param outlet source outlet
     * @throws NoOutletInformationFieldChangeException
     */
    public void setOutletInformation(OutletInformation outlet) throws NoOutletInformationFieldChangeException {
        try {
            requireAllNonNull(outlet.getName(), outlet.getOperatingHours(), outlet.getMasterPassword(),
                    outlet.getOutletEmail(), outlet.getOutletContact(), outlet.getAnnouncement(),
                    outlet.getEncryptionMode());
        } catch (NullPointerException e) {
            throw new NoOutletInformationFieldChangeException();
        }
        this.name = outlet.getName();
        this.operatingHours = outlet.getOperatingHours();
        this.outletContact = outlet.getOutletContact();
        this.outletEmail = outlet.getOutletEmail();
        this.masterPassword = outlet.getMasterPassword();
        this.announcement = outlet.getAnnouncement();
        this.isDataEncrypted = outlet.getEncryptionMode();
    }

    /**
     * Sets encryption mode
     * @param isEncrypted
     */
    public void setEncryptionMode(boolean isEncrypted) {
        this.isDataEncrypted = isEncrypted;
    }

    /**
     * Sets outlet announcement
     * @param announcement
     */
    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }

    /**
     * Gets encryption mode message
     * @return message that decribes whether current local files are encrypted
     */
    public String getEncryptionModeMessage() {
        if (isDataEncrypted) {
            return DATA_ENCRYPTED_MESSAGE;
        }
        return DATA_NOT_ENCRYPTED_MESSAGE;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof OutletInformation
                && ((OutletInformation) other).getName().equals(this.getName())
                && ((OutletInformation) other).getMasterPassword().equals(this.getMasterPassword())
                && ((OutletInformation) other).getOperatingHours().equals(this.getOperatingHours())
                && ((OutletInformation) other).getOutletContact().equals(this.getOutletContact())
                && ((OutletInformation) other).getOutletEmail().equals(this.getOutletEmail())
                && ((OutletInformation) other).getAnnouncement().equals(this.getAnnouncement())
                && ((OutletInformation) other).getEncryptionMode() == (this.getEncryptionMode()));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, masterPassword, operatingHours, outletContact, outletEmail, announcement,
                isDataEncrypted);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Outlet Name: ")
                .append(getName())
                .append(" Operating Hours: ")
                .append(getOperatingHours().getDisplayedMessage())
                .append(" Contact: ")
                .append(getOutletContact())
                .append(" Email: ")
                .append(getOutletEmail())
                .append(" Announcement: ")
                .append(getAnnouncement())
                .append(" Encryption: ")
                .append(getEncryptionModeMessage());
        return builder.toString();
    }
}
