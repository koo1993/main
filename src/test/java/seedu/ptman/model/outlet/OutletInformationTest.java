package seedu.ptman.model.outlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Objects;

import org.junit.Test;

import seedu.ptman.model.Password;
import seedu.ptman.testutil.Assert;

public class OutletInformationTest {

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(null, operatingHours,
                outletContact, outletEmail, password, announcement));
    }

    @Test
    public void constructor_nullOperatingHours_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name,
                null, outletContact, outletEmail, password, announcement));
    }

    @Test
    public void constructor_nullOutletContact_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name,
                operatingHours, null, outletEmail, password, announcement));
    }

    @Test
    public void constructor_nullOutletEmail_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletContact outletContact = new OutletContact("91234567");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name, operatingHours,
                outletContact, null, password, announcement));
    }

    @Test
    public void constructor_nullPassword_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Announcement announcement = new Announcement("New Announcement.");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name, operatingHours,
                outletContact, outletEmail, null, announcement));
    }

    @Test
    public void constructor_nullAnnouncement_throwsNullPointerException() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name, operatingHours,
                outletContact, outletEmail, password, null));
    }

    @Test
    public void equals_sameOutletInformation_returnsTrue() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        OutletInformation outlet = new OutletInformation(name, operatingHours, outletContact, outletEmail,
                password, announcement);
        OutletInformation other = new OutletInformation(name, operatingHours, outletContact, outletEmail,
                password, announcement);
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
    public void hashCode_sameObject_returnsTrue() {
        Password masterPassword = new Password();
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Announcement announcement = new Announcement("New Announcement.");
        OutletInformation outlet = new OutletInformation(name, operatingHours, outletContact, outletEmail,
                masterPassword, announcement);
        assertEquals(outlet.hashCode(), Objects.hash(name, masterPassword, operatingHours, outletContact,
                outletEmail, announcement));
    }

    @Test
    public void toString_validInput_returnsTrue() {
        OutletName name = new OutletName("outlet");
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletContact outletContact = new OutletContact("91234567");
        OutletEmail outletEmail = new OutletEmail("outlet@gmail.com");
        Password password = new Password();
        Announcement announcement = new Announcement("New Announcement.");
        OutletInformation outlet = new OutletInformation(name, operatingHours, outletContact, outletEmail,
                password, announcement);
        String expected = "Operating Hours: 09:00-22:00 Contact: 91234567 "
                + "Email: outlet@gmail.com";
        assertEquals(outlet.toString(), expected);
    }
}
