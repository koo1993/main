package seedu.ptman.model.outlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.testutil.TypicalEmployees.ALICE;
import static seedu.ptman.testutil.TypicalEmployees.BOB;

import org.junit.Test;

import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.testutil.Assert;
import seedu.ptman.testutil.ShiftBuilder;

public class ShiftTest {

    @Test
    public void constructor_illegalTime_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () ->
                new Shift(new Day("monday"), new Time("2200"), new Time("1000"), new Capacity("4"))
        );
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
                new Shift(null, null, null, null)
        );
        Assert.assertThrows(NullPointerException.class, () ->
                new Shift(new Day("monday"), new Time("1000"), null, null)
        );
    }

    @Test
    public void setEmployees() throws DuplicateEmployeeException {
        Shift shift = new ShiftBuilder().build();
        shift.addEmployee(ALICE);
        shift.addEmployee(BOB);
        Shift other = new ShiftBuilder().build();
        other.setEmployees(shift);
        assertTrue(other.contains(ALICE));
        assertTrue(other.contains(BOB));
    }

    @Test
    public void equals_sameShift_returnsTrue() throws DuplicateEmployeeException {
        Shift shift1 = new ShiftBuilder().withDay("monday")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        Shift shift2 = new ShiftBuilder().withDay("monday")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        shift1.addEmployee(ALICE);
        shift2.addEmployee(ALICE);
        assertEquals(shift1, shift2);
    }

}
