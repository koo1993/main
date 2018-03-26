package seedu.ptman.testutil;

import static seedu.ptman.testutil.TypicalEmployees.getTypicalEmployees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.outlet.Shift;
import seedu.ptman.model.outlet.exceptions.DuplicateShiftException;

/**
 * A utility class containing a list of {@code Employee} objects to be used in tests.
 */
public class TypicalShifts {

    public static final Shift MONDAY_AM = new ShiftBuilder().withDay("monday")
            .withStartTime("0800")
            .withEndTime("1300")
            .withCapacity("4").build();
    public static final Shift MONDAY_PM = new ShiftBuilder().withDay("monday")
            .withStartTime("1300")
            .withEndTime("2200")
            .withCapacity("4").build();
    public static final Shift TUESDAY_AM = new ShiftBuilder().withDay("tuesday")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("5").build();
    public static final Shift TUESDAY_PM = new ShiftBuilder().withDay("tuesday")
            .withStartTime("1200")
            .withEndTime("2200")
            .withCapacity("3").build();
    public static final Shift SUNDAY_AM = new ShiftBuilder().withDay("sunday")
            .withStartTime("1000")
            .withEndTime("1300")
            .withCapacity("4").build();
    public static final Shift SUNDAY_PM = new ShiftBuilder().withDay("sunday")
            .withStartTime("1300")
            .withEndTime("1700")
            .withCapacity("4").build();
    public static final Shift WEDNESDAY_AM = new ShiftBuilder().withDay("wednesday")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("5").build();
    public static final Shift WEDNESDAY_PM = new ShiftBuilder().withDay("wednesday")
            .withStartTime("1200")
            .withEndTime("2200")
            .withCapacity("3").build();
    public static final Shift THURSDAY_AM = new ShiftBuilder().withDay("thursday")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("5").build();
    public static final Shift THURSDAY_PM = new ShiftBuilder().withDay("thursday")
            .withStartTime("1200")
            .withEndTime("2200")
            .withCapacity("3").build();

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

    public static List<Shift> getTypicalShifts() {
        return new ArrayList<>(Arrays.asList(MONDAY_AM, MONDAY_PM, TUESDAY_AM, TUESDAY_PM,
                WEDNESDAY_AM, WEDNESDAY_PM, SUNDAY_PM, SUNDAY_AM));
    }
}
