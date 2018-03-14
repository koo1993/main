package seedu.ptman.model.outlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;
import java.util.Objects;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

public class OperatingHoursTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new OperatingHours(null));
    }

    @Test
    public void constructor_invalidOperatingHours_throwsIllegalArgumentException() {
        String invalidOperatingHours = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new OperatingHours(invalidOperatingHours));
    }

    @Test
    public void isValidOperatingHours_nullOperatingHours_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> OperatingHours.isValidOperatingHours(null));
    }

    @Test
    public void isValidOperatingHours_blankOperatingHours_returnsFalse() {
        assertFalse(OperatingHours.isValidOperatingHours("")); // empty string
        assertFalse(OperatingHours.isValidOperatingHours(" ")); // spaces only
    }

    @Test
    public void isValidOperatingHours_missingParts_returnsFalse() {
        assertFalse(OperatingHours.isValidOperatingHours("-22:00")); // missing start time
        assertFalse(OperatingHours.isValidOperatingHours("09:00-")); // missing end time
        assertFalse(OperatingHours.isValidOperatingHours("-")); // missing both start and end time
    }

    @Test
    public void isValidOperatingHours_invalidParts_returnsFalse() {
        assertFalse(OperatingHours.isValidOperatingHours("25:00-22:00")); // invalid hour in start time
        assertFalse(OperatingHours.isValidOperatingHours("090:00-22:00")); // more than two numbers of hour
        assertFalse(OperatingHours.isValidOperatingHours("09:99-22:00")); // invalid minute in start time
        assertFalse(OperatingHours.isValidOperatingHours("09:000-22:00")); // more than two numbers of minute
        assertFalse(OperatingHours.isValidOperatingHours("09:00-25:00")); // invalid hour in end time
        assertFalse(OperatingHours.isValidOperatingHours("09:00-220:00")); // more than two numbers of hour
        assertFalse(OperatingHours.isValidOperatingHours("09:00-22:99")); // invalid minute in end time
        assertFalse(OperatingHours.isValidOperatingHours("09:00-22:000")); // more than two numbers of minute
        assertFalse(OperatingHours.isValidOperatingHours("09.00-22:00")); // invalid '.' symbol used in start
        assertFalse(OperatingHours.isValidOperatingHours("09/00-22:00")); // invalid '/' symbol used in start
        assertFalse(OperatingHours.isValidOperatingHours("09@00-22:00")); // invalid '@' symbol used in start
        assertFalse(OperatingHours.isValidOperatingHours("09:00-22.00")); // invalid '.' symbol used in end
        assertFalse(OperatingHours.isValidOperatingHours("09:00-22/00")); // invalid '/' symbol used in end
        assertFalse(OperatingHours.isValidOperatingHours("09:00-22@00")); // invalid '@' symbol used in end
        assertFalse(OperatingHours.isValidOperatingHours(" 09:00-22:00")); // leading space
        assertFalse(OperatingHours.isValidOperatingHours("09:00-22:00 ")); // trailing space
        assertFalse(OperatingHours.isValidOperatingHours("09:00--22:00")); // double '-' symbol
        assertFalse(OperatingHours.isValidOperatingHours("09::00-22:00")); // double ':' symbol in start time
        assertFalse(OperatingHours.isValidOperatingHours("09:00-22::00")); // double ':' symbol in end time
        assertFalse(OperatingHours.isValidOperatingHours("09:00/22:00")); // invalid '/' symbol used to connect
        assertFalse(OperatingHours.isValidOperatingHours("09:00.22:00")); // invalid '.' symbol used to connect
        assertFalse(OperatingHours.isValidOperatingHours("09:00@22:00")); // invalid '@' symbol used to connect
    }

    @Test
    public void isValidOperatingHours_validOperatingHours_returnsTrue() {
        assertTrue(OperatingHours.isValidOperatingHours("09:00-22:00"));
        assertTrue(OperatingHours.isValidOperatingHours("10:00-21:00"));
        assertTrue(OperatingHours.isValidOperatingHours("08:00-18:00"));
    }

    @Test
    public void convertStringToLocalTime_validInput_returnsLocalTime() {
        LocalTime localTime = LocalTime.of(9, 0);
        String test = "09:00";
        assertEquals(OperatingHours.convertStringToLocalTime(test), localTime);
    }

    @Test
    public void getStartTime_validInput_returnsTrue() {
        OperatingHours test = new OperatingHours("09:00-22:00");
        LocalTime startTime = LocalTime.of(9, 0);
        assertEquals(test.getStartTime(), startTime);
    }

    @Test
    public void getEndTime_validInput_returnsTrue() {
        OperatingHours test = new OperatingHours("09:00-22:00");
        LocalTime endTime = LocalTime.of(22, 0);
        assertEquals(test.getEndTime(), endTime);
    }

    @Test
    public void equals_sameOperatingHours_returnsTrue() {
        String operatingHours = "09:00-22:00";
        OperatingHours test = new OperatingHours(operatingHours);
        OperatingHours other = new OperatingHours(operatingHours);
        assertTrue(test.equals(other));
    }

    @Test
    public void hashCode_sameObject_returnsTrue() {
        String operatingHours = "09:00-22:00";
        OperatingHours test = new OperatingHours(operatingHours);
        assertEquals(test.hashCode(), Objects.hash(LocalTime.of(9, 0),
                LocalTime.of(22, 0)));
    }

    @Test
    public void toString_validInput_returnsTrue() {
        String operatingHours = "09:00-22:00";
        OperatingHours test = new OperatingHours(operatingHours);
        assertEquals(test.toString(), operatingHours);
    }
}
