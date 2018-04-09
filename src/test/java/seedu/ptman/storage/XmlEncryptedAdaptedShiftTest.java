package seedu.ptman.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.storage.XmlAdaptedShift.MISSING_FIELD_MESSAGE_FORMAT_SHIFT;
import static seedu.ptman.testutil.TypicalShifts.THURSDAY_AM;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.model.shift.Capacity;
import seedu.ptman.model.shift.Date;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.Time;
import seedu.ptman.testutil.Assert;

//@@author SunBangjie
public class XmlEncryptedAdaptedShiftTest {
    private static final String INVALID_DATE = "1-1-18";
    private static final String INVALID_START_TIME = "asd";
    private static final String INVALID_END_TIME = "2500";
    private static final String INVALID_CAPACITY = "two";

    private static final String VALID_DATE = "01-01-18";
    private static final String VALID_CAPACITY = THURSDAY_AM.getCapacity().toString();
    private static final String VALID_START_TIME = THURSDAY_AM.getStartTime().toString();
    private static final String VALID_END_TIME = THURSDAY_AM.getEndTime().toString();

    private static final List<XmlEncryptedAdaptedEmployee> VALID_EMPLOYEES =
            THURSDAY_AM.getEmployeeList().stream()
            .map(XmlEncryptedAdaptedEmployee::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validShiftDetails_returnsShift() throws Exception {
        XmlEncryptedAdaptedShift shift = new XmlEncryptedAdaptedShift(THURSDAY_AM);
        assertEquals(THURSDAY_AM, shift.toModelType());
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(INVALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = Date.MESSAGE_DATE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(null, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT, Date.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_invalidStartTime_throwsIllegalValueException() {
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(VALID_DATE, INVALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = Time.MESSAGE_TIME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(VALID_DATE, null, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT, Time.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_invalidEndTime_throwsIllegalValueException() {
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(VALID_DATE, VALID_START_TIME, INVALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = Time.MESSAGE_TIME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_nullEndTime_throwsIllegalValueException() {
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(VALID_DATE, VALID_START_TIME, null, VALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT, Time.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_invalidCapacity_throwsIllegalValueException() {
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, INVALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = Capacity.MESSAGE_CAPACITY_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, null,
                        VALID_EMPLOYEES);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT, Capacity.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlEncryptedAdaptedEmployee> invalidEmployees = new ArrayList<>();
        invalidEmployees.add(new XmlEncryptedAdaptedEmployee(
                null, null, null, null, null, null, null));
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, null,
                        invalidEmployees);

        Assert.assertThrows(IllegalValueException.class, shift::toModelType);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);
        assertTrue(shift.equals(shift));
    }

    @Test
    public void equals_sameShift_returnsTrue() {
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);
        XmlEncryptedAdaptedShift shift1 =
                new XmlEncryptedAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);
        assertTrue(shift.equals(shift1));
    }

    @Test
    public void equals_differentShift_returnsTrue() {
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);
        XmlEncryptedAdaptedShift shift1 =
                new XmlEncryptedAdaptedShift("02-14-18", VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);
        assertFalse(shift.equals(shift1));
    }

    @Test
    public void equals_null_returnsFalse() {
        XmlEncryptedAdaptedShift shift =
                new XmlEncryptedAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);
        assertFalse(shift.equals(null));
    }

    @Test
    public void setAttributesFromSource_validInputs_returnsSameObject() {
        Shift shift = new Shift(new Date(VALID_DATE), new Time(VALID_START_TIME), new Time(VALID_END_TIME),
                new Capacity(VALID_CAPACITY), THURSDAY_AM.getEmployees());
        XmlEncryptedAdaptedShift xmlAdaptedShift = new XmlEncryptedAdaptedShift();
        XmlEncryptedAdaptedShift sameXmlAdaptedShift = new XmlEncryptedAdaptedShift();
        xmlAdaptedShift.setAttributesFromSource(shift);
        sameXmlAdaptedShift.setAttributesFromSource(shift);
        assertEquals(xmlAdaptedShift, sameXmlAdaptedShift);
    }

    @Test
    public void setAttributesFromStrings_validInputs_returnsSameObject() {
        XmlEncryptedAdaptedShift xmlAdaptedShift = new XmlEncryptedAdaptedShift();
        XmlEncryptedAdaptedShift sameXmlAdaptedShift = new XmlEncryptedAdaptedShift();
        xmlAdaptedShift.setAttributesFromStrings(VALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY);
        sameXmlAdaptedShift.setAttributesFromStrings(VALID_DATE, VALID_START_TIME, VALID_END_TIME,
                VALID_CAPACITY);
        assertEquals(xmlAdaptedShift, sameXmlAdaptedShift);
    }

}
