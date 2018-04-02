package seedu.ptman.model.shift;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.testutil.TypicalEmployees.ALICE;
import static seedu.ptman.testutil.TypicalEmployees.BOB;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.shift.exceptions.ShiftFullException;
import seedu.ptman.testutil.Assert;
import seedu.ptman.testutil.ShiftBuilder;

//@@author shanwpf
public class ShiftTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_illegalTime_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () ->
                new Shift(new Date("19-03-18"), new Time("2200"), new Time("1000"), new Capacity("4"))
        );
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
                new Shift(null, null, null, null)
        );
        Assert.assertThrows(NullPointerException.class, () ->
                new Shift(new Date("19-03-18"), new Time("1000"), null, null)
        );
    }

    @Test
    public void setEmployees() throws DuplicateEmployeeException, ShiftFullException {
        Shift shift = new ShiftBuilder().build();
        shift.addEmployee(ALICE);
        shift.addEmployee(BOB);
        Shift other = new ShiftBuilder().build();
        other.setEmployees(shift);
        assertTrue(other.contains(ALICE));
        assertTrue(other.contains(BOB));
    }

    @Test
    public void isFull_shiftFull_returnsTrue() throws ShiftFullException, DuplicateEmployeeException {
        Shift shift = new ShiftBuilder().withCapacity("1").build();
        shift.addEmployee(ALICE);
        assertTrue(shift.isFull());
    }

    @Test
    public void isFull_shiftNotFull_returnsFalse() throws ShiftFullException, DuplicateEmployeeException {
        Shift shift = new ShiftBuilder().withCapacity("2").build();
        shift.addEmployee(ALICE);
        assertFalse(shift.isFull());
    }

    @Test
    public void addEmployee_shiftFull_throwsShiftFullException() throws ShiftFullException, DuplicateEmployeeException {
        Shift shift = new ShiftBuilder().withCapacity("1").build();
        shift.addEmployee(ALICE);
        thrown.expect(ShiftFullException.class);
        shift.addEmployee(BOB);
    }

    @Test
    public void equals_sameShift_returnsTrue() throws DuplicateEmployeeException, ShiftFullException {
        Shift shift1 = new ShiftBuilder().withDate("19-03-18")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        Shift shift2 = new ShiftBuilder().withDate("19-03-18")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        shift1.addEmployee(ALICE);
        shift2.addEmployee(ALICE);
        assertEquals(shift1, shift2);
    }

    @Test
    public void hashCode_sameShift_sameHashCode() {
        Shift shift1 = new ShiftBuilder().withDate("19-03-18")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        Shift shift2 = new ShiftBuilder().withDate("19-03-18")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        assertEquals(shift1.hashCode(), shift2.hashCode());
    }

    @Test
    public void hashCode_differentShift_differentHashCode() {
        Shift shift1 = new ShiftBuilder().withDate("12-03-18")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        Shift shift2 = new ShiftBuilder().withDate("19-03-18")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        assertNotEquals(shift1.hashCode(), shift2.hashCode());
    }

}
