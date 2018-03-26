package seedu.ptman.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.storage.XmlAdaptedOutletInformation.FAIL_MESSAGE;

import org.junit.Test;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.model.Password;
import seedu.ptman.model.outlet.Announcement;
import seedu.ptman.model.outlet.OperatingHours;
import seedu.ptman.model.outlet.OutletContact;
import seedu.ptman.model.outlet.OutletEmail;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.OutletName;
import seedu.ptman.testutil.Assert;

public class XmlAdaptedOutletInformationTest {
    private static final String INVALID_OUTLET_NAME = "Awesome@outlet";
    private static final String INVALID_OPERATING_HOURS = "10:00/20:00";
    private static final String INVALID_OUTLET_CONTACT = "+6591112222";
    private static final String INVALID_OUTLET_EMAIL = "example.com";

    private static final String VALID_OUTLET_NAME = "AwesomeOutlet";
    private static final String VALID_OPERATING_HOURS = "10:00-20:00";
    private static final String VALID_OUTLET_CONTACT = "91112222";
    private static final String VALID_OUTLET_EMAIL = "AwesomeOutlet@gmail.com";
    private static final String VALID_ANNOUNCEMENT = "New Announcement";
    private static final String DEFAULT_PASSWORD_HASH = new Password().getPasswordHash();

    private OutletInformation outlet = new OutletInformation();

    @Test
    public void toModelType_validOutletInformationDetails_returnsOutletInformation() throws Exception {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(outlet);
        assertEquals(outlet, outletInformation.toModelType());
    }

    @Test
    public void toModelType_invalidOutletName_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(INVALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = OutletName.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_nullOutletName_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(null,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = String.format(FAIL_MESSAGE, OutletName.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_invalidOperatingHours_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(VALID_OUTLET_NAME,
                INVALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = OperatingHours.MESSAGE_OPERATING_HOUR_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_nullOperatingHours_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(VALID_OUTLET_NAME,
                null, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = String.format(FAIL_MESSAGE, OperatingHours.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_invalidOutletContact_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, INVALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = OutletContact.MESSAGE_OUTLET_CONTACT_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_nullOutletContact_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, null, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = String.format(FAIL_MESSAGE, OutletContact.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_invalidOutletEmail_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, INVALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = OutletEmail.MESSAGE_OUTLET_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_nullOutletEmail_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, null, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        String expectedMessage = String.format(FAIL_MESSAGE, OutletEmail.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_nullPasswordHash_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, null,
                VALID_ANNOUNCEMENT);
        String expectedMessage = String.format(FAIL_MESSAGE, Password.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void toModelType_nullAnnouncement_throwsIllegalValueException() {
        XmlAdaptedOutletInformation outletInformation = new XmlAdaptedOutletInformation(VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                null);
        String expectedMessage = String.format(FAIL_MESSAGE, Announcement.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, outletInformation::toModelType);
    }

    @Test
    public void equals() {
        XmlAdaptedOutletInformation firstOutletInformation = new XmlAdaptedOutletInformation(VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        XmlAdaptedOutletInformation secondOutletInformation = new XmlAdaptedOutletInformation(outlet);

        // Same object -> return true
        assertTrue(firstOutletInformation.equals(firstOutletInformation));

        // Same values -> return true
        XmlAdaptedOutletInformation firstOutletInformationCopy = new XmlAdaptedOutletInformation(VALID_OUTLET_NAME,
                VALID_OPERATING_HOURS, VALID_OUTLET_CONTACT, VALID_OUTLET_EMAIL, DEFAULT_PASSWORD_HASH,
                VALID_ANNOUNCEMENT);
        assertTrue(firstOutletInformation.equals(firstOutletInformationCopy));

        // Different types -> return false
        assertFalse(firstOutletInformation.equals(1));

        // Null type -> return false
        assertFalse(firstOutletInformation.equals(null));

        // Different values -> return false
        assertFalse(firstOutletInformation.equals(secondOutletInformation));
    }
}
