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
        Name name = new Name("outlet");
        Password masterPassword = new Password();
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(null,
                masterPassword, operatingHours));
    }

    @Test
    public void constructor_nullMasterPassword_throwsNullPointerException() {
        Name name = new Name("outlet");
        Password masterPassword = new Password();
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name,
                null, operatingHours));
    }

    @Test
    public void constructor_nullOperatingHours_throwsNullPointerException() {
        Name name = new Name("outlet");
        Password masterPassword = new Password();
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        Assert.assertThrows(NullPointerException.class, () -> new OutletInformation(name,
                masterPassword, null));
    }

    @Test
    public void getName_validInput_returnsTrue() {
        Name name = new Name("outlet");
        Password masterPassword = new Password();
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletInformation outlet = new OutletInformation(name, masterPassword, operatingHours);
        assertEquals(outlet.getName(), name);
    }

    @Test
    public void getMasterPassword_validInput_returnsTrue() {
        Name name = new Name("outlet");
        Password masterPassword = new Password();
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletInformation outlet = new OutletInformation(name, masterPassword, operatingHours);
        assertEquals(outlet.getMasterPassword(), masterPassword);
    }

    @Test
    public void getOperatingHours_validInput_returnsTrue() {
        Name name = new Name("outlet");
        Password masterPassword = new Password();
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletInformation outlet = new OutletInformation(name, masterPassword, operatingHours);
        assertEquals(outlet.getOperatingHours(), operatingHours);
    }

    @Test
    public void equals_sameOutletInformation_returnsTrue() {
        Name name = new Name("outlet");
        Password masterPassword = new Password();
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletInformation outlet = new OutletInformation(name, masterPassword, operatingHours);
        OutletInformation other = new OutletInformation(name, masterPassword, operatingHours);
        assertTrue(outlet.equals(other));
    }

    @Test
    public void hashCode_sameObject_returnsTrue() {
        Name name = new Name("outlet");
        Password masterPassword = new Password();
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletInformation outlet = new OutletInformation(name, masterPassword, operatingHours);
        assertEquals(outlet.hashCode(), Objects.hash(name, masterPassword, operatingHours));
    }

    @Test
    public void toString_validInput_returnsTrue() {
        Name name = new Name("outlet");
        Password masterPassword = new Password();
        OperatingHours operatingHours = new OperatingHours("09:00-22:00");
        OutletInformation outlet = new OutletInformation(name, masterPassword, operatingHours);
        String expected = "outlet Operating Hour: 09:00-22:00";
        assertEquals(outlet.toString(), expected);
    }
}
