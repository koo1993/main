package seedu.ptman.testutil;

import static seedu.ptman.testutil.TypicalEmployees.getTypicalEmployees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.exceptions.DuplicateShiftException;

//@@author shanwpf
/**
 * A utility class containing a list of {@code Shift} objects to be used in tests.
 */
public class TypicalShifts {

    public static final Shift MONDAY_AM = new ShiftBuilder().withDate("19-03-18")
            .withStartTime("0800")
            .withEndTime("1300")
            .withCapacity("4").build();
    public static final Shift MONDAY_PM = new ShiftBuilder().withDate("19-03-18")
            .withStartTime("1300")
            .withEndTime("2200")
            .withCapacity("4").build();
    public static final Shift TUESDAY_AM = new ShiftBuilder().withDate("20-03-18")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("5").build();
    public static final Shift TUESDAY_PM = new ShiftBuilder().withDate("20-03-18")
            .withStartTime("1200")
            .withEndTime("2200")
            .withCapacity("3").build();
    public static final Shift SUNDAY_AM = new ShiftBuilder().withDate("25-03-18")
            .withStartTime("1000")
            .withEndTime("1300")
            .withCapacity("4").build();
    public static final Shift SUNDAY_PM = new ShiftBuilder().withDate("25-03-18")
            .withStartTime("1300")
            .withEndTime("1700")
            .withCapacity("4").build();
    public static final Shift WEDNESDAY_AM = new ShiftBuilder().withDate("21-03-18")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("5").build();
    public static final Shift WEDNESDAY_PM = new ShiftBuilder().withDate("21-03-18")
            .withStartTime("1200")
            .withEndTime("2200")
            .withCapacity("3").build();
    public static final Shift THURSDAY_AM = new ShiftBuilder().withDate("22-03-18")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("5")
            .withEmployees(new EmployeeBuilder().build()).build();
    public static final Shift THURSDAY_PM = new ShiftBuilder().withDate("22-03-18")
            .withStartTime("1200")
            .withEndTime("2200")
            .withCapacity("3").build();

    public static final Shift SHIFT_RUNNING_OUT = new ShiftBuilder().withDate("22-03-18")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("1").build();

    private TypicalShifts() {} // prevents instantiation

    public static PartTimeManager getTypicalPartTimeManagerWithShifts() {
        PartTimeManager ptman = new PartTimeManager();
        for (Employee employee : getTypicalEmployees()) {
            try {
                ptman.addEmployee(employee);
            } catch (DuplicateEmployeeException e) {
                throw new AssertionError("not possible");
            }
        }
        for (Shift shift : getTypicalShifts()) {
            try {
                ptman.addShift(shift);
            } catch (DuplicateShiftException e) {
                throw new AssertionError("not possible");
            }
        }
        return ptman;
    }

    // TODO: Update this when new structure of Shifts (with dates) is out.
    // Created because Sunday is causing some problems for the UI tests
    public static PartTimeManager getTypicalPartTimeManagerWithShiftsWithoutSunday() {
        PartTimeManager ptman = new PartTimeManager();
        for (Employee employee : getTypicalEmployees()) {
            try {
                ptman.addEmployee(employee);
            } catch (DuplicateEmployeeException e) {
                throw new AssertionError("not possible");
            }
        }
        for (Shift shift : getTypicalShiftsWithoutSunday()) {
            try {
                ptman.addShift(shift);
            } catch (DuplicateShiftException e) {
                throw new AssertionError("not possible");
            }
        }
        return ptman;
    }

    public static List<Shift> getTypicalShifts() {
        return new ArrayList<>(Arrays.asList(MONDAY_AM, MONDAY_PM, TUESDAY_AM, TUESDAY_PM,
                WEDNESDAY_AM, WEDNESDAY_PM, SUNDAY_PM, SUNDAY_AM));
    }

    public static List<Shift> getTypicalShiftsWithoutSunday() {
        return new ArrayList<>(Arrays.asList(MONDAY_AM, MONDAY_PM, TUESDAY_AM, TUESDAY_PM,
                WEDNESDAY_AM, WEDNESDAY_PM, SHIFT_RUNNING_OUT));
    }
}
