package seedu.ptman.storage;

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

/**
 * JAXB-friendly version of the OutletInformation.
 */
@XmlRootElement(name = "outletinformation")
public class XmlAdaptedOutletInformation {

    public static final String FAIL_MESSAGE = "Outlet's %s field is missing!";

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
        this.outletName = null;
        this.operatingHours = null;
        this.outletContact = null;
        this.outletEmail = null;
        this.passwordHash = null;
        this.announcement = null;
    }

    public XmlAdaptedOutletInformation(String outletName, String operatingHours, String outletContact,
                                       String outletEmail, String passwordHash, String announcement) {
        this.outletName = outletName;
        this.operatingHours = operatingHours;
        this.outletContact = outletContact;
        this.outletEmail = outletEmail;
        this.passwordHash = passwordHash;
        this.announcement = announcement;
    }

    /**
     * Converts a given OutletInformation into this class for JAXB use.
     */
    public XmlAdaptedOutletInformation(OutletInformation source) {
        this();
        outletName = source.getName().fullName;
        operatingHours = source.getOperatingHours().value;
        outletContact = source.getOutletContact().value;
        outletEmail = source.getOutletEmail().value;
        passwordHash = source.getMasterPassword().getPasswordHash();
        announcement = source.getAnnouncement().value;
    }

    private OutletName setOutletName() throws IllegalValueException {
        if (this.outletName == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, OutletName.class.getSimpleName()));
        }
        if (!OutletName.isValidName(this.outletName)) {
            throw new IllegalValueException(OutletName.MESSAGE_NAME_CONSTRAINTS);
        }
        OutletName outletName = new OutletName(this.outletName);
        return outletName;
    }

    private OperatingHours setOperatingHours() throws IllegalValueException {
        if (this.operatingHours == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, OperatingHours.class.getSimpleName()));
        }
        if (!OperatingHours.isValidOperatingHours(this.operatingHours)) {
            throw new IllegalValueException(OperatingHours.MESSAGE_OPERATING_HOUR_CONSTRAINTS);
        }
        OperatingHours operatingHours = new OperatingHours(this.operatingHours);
        return operatingHours;
    }

    private OutletContact setOutletContact() throws IllegalValueException {
        if (this.outletContact == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, OutletContact.class.getSimpleName()));
        }
        if (!OutletContact.isValidOutletContact(this.outletContact)) {
            throw new IllegalValueException(OutletContact.MESSAGE_OUTLET_CONTACT_CONSTRAINTS);
        }
        OutletContact outletContact = new OutletContact(this.outletContact);
        return outletContact;
    }

    private OutletEmail setOutletEmail() throws IllegalValueException {
        if (this.outletEmail == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, OutletEmail.class.getSimpleName()));
        }
        if (!OutletEmail.isValidOutletEmail(this.outletEmail)) {
            throw new IllegalValueException(OutletEmail.MESSAGE_OUTLET_EMAIL_CONSTRAINTS);
        }
        OutletEmail outletEmail = new OutletEmail(this.outletEmail);
        return outletEmail;
    }

    private Password setPassword() throws IllegalValueException {
        if (this.passwordHash == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, Password.class.getSimpleName()));
        }
        Password masterPassword = new Password(this.passwordHash);
        return masterPassword;
    }

    private Announcement setAnnouncement() throws IllegalValueException {
        if (this.announcement == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, Announcement.class.getSimpleName()));
        }
        Announcement announcement = new Announcement(this.announcement);
        return announcement;
    }

    /**
     * Converts this jaxb-friendly adapted outlet object into the model's OutletInformation object
     */
    public OutletInformation toModelType() throws IllegalValueException {
        final OutletName outletName = setOutletName();
        final OperatingHours operatingHours = setOperatingHours();
        final OutletContact outletContact = setOutletContact();
        final OutletEmail outletEmail = setOutletEmail();
        final Password masterPassword = setPassword();
        final Announcement announcement = setAnnouncement();
        return new OutletInformation(outletName, operatingHours, outletContact, outletEmail,
                masterPassword, announcement);
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
                && Objects.equals(announcement, otherOutlet.announcement);
    }
}
