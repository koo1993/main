package seedu.ptman.model.outlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;
import java.util.Objects;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

//@@author SunBangjie
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
        assertFalse(OperatingHours.isValidOperatingHours("2500-2200")); // invalid hour in start time
        assertFalse(OperatingHours.isValidOperatingHours("09000-2200")); // more than two numbers of hour
        assertFalse(OperatingHours.isValidOperatingHours("0999-2200")); // invalid minute in start time
        assertFalse(OperatingHours.isValidOperatingHours("09000-2200")); // more than two numbers of minute
        assertFalse(OperatingHours.isValidOperatingHours("0900-2500")); // invalid hour in end time
        assertFalse(OperatingHours.isValidOperatingHours("0900-22000")); // more than two numbers of hour
        assertFalse(OperatingHours.isValidOperatingHours("0900-2299")); // invalid minute in end time
        assertFalse(OperatingHours.isValidOperatingHours("0900-22000")); // more than two numbers of minute
        assertFalse(OperatingHours.isValidOperatingHours("09.00-2200")); // invalid '.' symbol used in start
        assertFalse(OperatingHours.isValidOperatingHours("09/00-2200")); // invalid '/' symbol used in start
        assertFalse(OperatingHours.isValidOperatingHours("09@00-2200")); // invalid '@' symbol used in start
        assertFalse(OperatingHours.isValidOperatingHours("0900-22.00")); // invalid '.' symbol used in end
        assertFalse(OperatingHours.isValidOperatingHours("0900-22/00")); // invalid '/' symbol used in end
        assertFalse(OperatingHours.isValidOperatingHours("0900-22@00")); // invalid '@' symbol used in end
        assertFalse(OperatingHours.isValidOperatingHours(" 0900-2200")); // leading space
        assertFalse(OperatingHours.isValidOperatingHours("0900-2200 ")); // trailing space
        assertFalse(OperatingHours.isValidOperatingHours("0900--2200")); // double '-' symbol
        assertFalse(OperatingHours.isValidOperatingHours("0900/2200")); // invalid '/' symbol used to connect
        assertFalse(OperatingHours.isValidOperatingHours("0900.2200")); // invalid '.' symbol used to connect
        assertFalse(OperatingHours.isValidOperatingHours("0900@2200")); // invalid '@' symbol used to connect
    }

    @Test
    public void isValidOperatingHours_validOperatingHours_returnsTrue() {
        assertTrue(OperatingHours.isValidOperatingHours("0900-2200"));
        assertTrue(OperatingHours.isValidOperatingHours("1000-2100"));
        assertTrue(OperatingHours.isValidOperatingHours("0800-1800"));
    }

    @Test
    public void isValidStartTimeEndTimeOrder_invalidOrder_returnsFalse() {
        assertFalse(OperatingHours.isValidStartTimeEndTimeOrder("2200-1000"));
        assertFalse(OperatingHours.isValidStartTimeEndTimeOrder("1000-1000"));
    }

    @Test
    public void isValidStartTimeEndTimeOrder_validOrder_returnsTrue() {
        assertTrue(OperatingHours.isValidStartTimeEndTimeOrder("1200-2000"));
        assertTrue(OperatingHours.isValidStartTimeEndTimeOrder("1200-1230"));
    }

    @Test
    public void convertStringToLocalTime_validInput_returnsLocalTime() {
        LocalTime localTime = LocalTime.of(9, 0);
        String test = "0900";
        assertEquals(OperatingHours.convertStringToLocalTime(test), localTime);
    }

    @Test
    public void getStartTime_validInput_returnsTrue() {
        OperatingHours test = new OperatingHours("0900-2200");
        LocalTime startTime = LocalTime.of(9, 0);
        assertEquals(test.getStartTime(), startTime);
    }

    @Test
    public void getEndTime_validInput_returnsTrue() {
        OperatingHours test = new OperatingHours("0900-2200");
        LocalTime endTime = LocalTime.of(22, 0);
        assertEquals(test.getEndTime(), endTime);
    }

    @Test
    public void equals_sameOperatingHours_returnsTrue() {
        String operatingHours = "0900-2200";
        OperatingHours test = new OperatingHours(operatingHours);
        OperatingHours other = new OperatingHours(operatingHours);
        assertTrue(test.equals(other));
    }

    @Test
    public void hashCode_sameObject_returnsTrue() {
        String operatingHours = "0900-2200";
        OperatingHours test = new OperatingHours(operatingHours);
        assertEquals(test.hashCode(), Objects.hash(LocalTime.of(9, 0),
                LocalTime.of(22, 0)));
    }

    @Test
    public void toString_validInput_returnsTrue() {
        String operatingHours = "0900-2200";
        OperatingHours test = new OperatingHours(operatingHours);
        assertEquals(test.toString(), operatingHours);
    }

    @Test
    public void getDisplayedMessage_validInput_returnsCorrectMessage() {
        String operatingHours = "0900-2200";
        String expected = "09:00-22:00";
        OperatingHours test = new OperatingHours(operatingHours);
        assertEquals(test.getDisplayedMessage(), expected);
    }
}
