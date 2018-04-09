package seedu.ptman.model.outlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Objects;

import org.junit.Test;

import seedu.ptman.model.Password;
import seedu.ptman.testutil.Assert;

//@@author SunBangjie
public class OutletInformationTest {

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(null, operatingHours,
                outletContact, outletEmail, announcement, password, false));
    }

    @Test
    public void constructor_nullOperatingHours_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name,
                null, outletContact, outletEmail, announcement, password, false));
    }

    @Test
    public void constructor_nullOutletContact_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name,
                operatingHours, null, outletEmail, announcement, password, false));
    }

    @Test
    public void constructor_nullOutletEmail_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name, operatingHours,
                outletContact, null, announcement, password, false));
    }

    @Test
    public void constructor_nullPassword_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name, operatingHours,
                outletContact, outletEmail, announcement, null, false));
    }

    @Test
    public void constructor_nullAnnouncement_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name, operatingHours,
                outletContact, outletEmail, null, password, false));
    }

    @Test
    public void equals_sameOutletInformation_returnsTrue() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        OutletInformation outlet = new OutletInformation(name, operatingHours, outletContact, outletEmail,
                announcement, password, false);
        OutletInformation other = new OutletInformation(name, operatingHours, outletContact, outletEmail,
                announcement, password, false);
        assertTrue(outlet.equals(other));
    }

    @Test
    public void setAnnouncement_validAnnouncement_success() {
        OutletInformation outlet = new OutletInformation();
        Announcement editedAnnouncement = new Announcement("Edited Announcement.");
        Announcement expectedAnnouncement = new Announcement("Edited Announcement.");
        outlet.setAnnouncement(editedAnnouncement);
        assertEquals(outlet.getAnnouncement(), expectedAnnouncement);
    }

    @Test
    public void setEncryptionMode_inputTrue_returnsTrue() {
        OutletInformation outlet = new OutletInformation();
        outlet.setEncryptionMode(true);
        assertTrue(outlet.getEncryptionMode());
    }

    @Test
    public void getEncryptionModeMessage_inputTrue_returnsEncryptedMessage() {
        OutletInformation outlet = new OutletInformation();
        outlet.setEncryptionMode(true);
        assertEquals(outlet.getEncryptionModeMessage(), OutletInformation.DATA_ENCRYPTED_MESSAGE);
    }

    @Test
    public void getEncryptionModeMessage_inputFalse_returnsNotEncryptedMessage() {
        OutletInformation outlet = new OutletInformation();
        assertEquals(outlet.getEncryptionModeMessage(), OutletInformation.DATA_NOT_ENCRYPTED_MESSAGE);
    }

    @Test
    public void hashCode_sameObject_returnsTrue() {
        Password masterPassword = new Password();
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Announcement announcement = new Announcement("New Announcement.");
        OutletInformation outlet = new OutletInformation(name, operatingHours, outletContact, outletEmail,
                announcement, masterPassword, false);
        assertEquals(outlet.hashCode(), Objects.hash(name, masterPassword, operatingHours, outletContact,
                outletEmail, announcement, false));
    }

    @Test
    public void toString_validInput_returnsTrue() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("0900-2200");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New announcement.");
        OutletInformation outlet = new OutletInformation(name, operatingHours, outletContact, outletEmail,
                announcement, password, false);
        String expected = "Outlet Name: outlet Operating Hours: 09:00-22:00 "
                + "Contact: 91234567 Email: outlet@gmail.com Announcement: New announcement. "
                + "Encryption: Outlet information storage files are not encrypted.";
        assertEquals(outlet.toString(), expected);
    }
}
