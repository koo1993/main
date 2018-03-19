package seedu.ptman.model.outlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.testutil.TypicalShifts.MONDAY_AM;
import static seedu.ptman.testutil.TypicalShifts.MONDAY_PM;
import static seedu.ptman.testutil.TypicalShifts.SUNDAY_AM;
import static seedu.ptman.testutil.TypicalShifts.SUNDAY_PM;
import static seedu.ptman.testutil.TypicalShifts.TUESDAY_AM;
import static seedu.ptman.testutil.TypicalShifts.TUESDAY_PM;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.Test;

import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.outlet.exceptions.DuplicateShiftException;
import seedu.ptman.model.outlet.exceptions.ShiftNotFoundException;
import seedu.ptman.testutil.Assert;
import seedu.ptman.testutil.TypicalEmployees;

public class TimetableTest {
    private static final LocalDate DATE_MON = LocalDate.of(2018, 3, 12);
    private static final LocalDate DATE_TUE = LocalDate.of(2018, 3, 13);
    private static final LocalDate DATE_WED = LocalDate.of(2018, 3, 14);
    private static final LocalDate DATE_THU = LocalDate.of(2018, 3, 15);
    private static final LocalDate DATE_FRI = LocalDate.of(2018, 3, 16);
    private static final LocalDate DATE_SAT = LocalDate.of(2018, 3, 17);
    private static final LocalDate DATE_SUN = LocalDate.of(2018, 3, 18);

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
                new Timetable(null)
        );
    }

    @Test
    public void getStartOfWeekDate_datesInWeek_returnsMondayDate() {
        Timetable timetableMon = new Timetable(DATE_MON);
        Timetable timetableTue = new Timetable(DATE_TUE);
        Timetable timetableWed = new Timetable(DATE_WED);
        Timetable timetableThu = new Timetable(DATE_THU);
        Timetable timetableFri = new Timetable(DATE_FRI);
        Timetable timetableSat = new Timetable(DATE_SAT);
        Timetable timetableSun = new Timetable(DATE_SUN);
        assertEquals(DATE_MON, timetableMon.getMondayDate());
        assertEquals(DATE_MON, timetableTue.getMondayDate());
        assertEquals(DATE_MON, timetableWed.getMondayDate());
        assertEquals(DATE_MON, timetableThu.getMondayDate());
        assertEquals(DATE_MON, timetableFri.getMondayDate());
        assertEquals(DATE_MON, timetableSat.getMondayDate());
        assertEquals(DATE_MON, timetableSun.getMondayDate());
    }

    @Test
    public void addShift_validShift_shiftAdded() throws DuplicateShiftException {
        Timetable timetable = new Timetable(DATE_MON);
        timetable.addShift(MONDAY_AM);
        timetable.addShift(MONDAY_PM);
        timetable.addShift(TUESDAY_AM);
        timetable.addShift(SUNDAY_PM);
        assertEquals(MONDAY_AM, timetable.getShift(DayOfWeek.MONDAY, 0));
        assertEquals(MONDAY_PM, timetable.getShift(DayOfWeek.MONDAY, 1));
        assertEquals(SUNDAY_PM, timetable.getShift(DayOfWeek.SUNDAY, 0));
        assertEquals(TUESDAY_AM, timetable.getShift(DayOfWeek.TUESDAY, 0));
    }

    @Test
    public void addShift_duplicateShift_throwsDuplicateShiftException() {
        Timetable timetable = new Timetable(DATE_MON);
        Assert.assertThrows(DuplicateShiftException.class, () -> {
            timetable.addShift(MONDAY_PM);
            timetable.addShift(MONDAY_PM);
        });
    }

    @Test
    public void removeShift_shiftNotInTimetable_throwsShiftNotFoundException() {
        Timetable timetable = new Timetable(DATE_MON);
        Assert.assertThrows(ShiftNotFoundException.class, () -> {
            timetable.removeShift(SUNDAY_AM);
        });
    }

    @Test
    public void removeShift_shiftExists_shiftRemoved()
            throws DuplicateShiftException, ShiftNotFoundException {
        Timetable timetable = new Timetable(DATE_MON);
        timetable.addShift(MONDAY_PM);
        timetable.addShift(TUESDAY_PM);
        timetable.addShift(TUESDAY_AM);
        timetable.removeShift(TUESDAY_PM);
        timetable.removeShift(TUESDAY_AM);
        timetable.removeShift(MONDAY_PM);
        assertFalse(timetable.containsShift(TUESDAY_PM));
        assertFalse(timetable.containsShift(MONDAY_PM));
        assertFalse(timetable.containsShift(TUESDAY_AM));
    }

    @Test
    public void addEmployeeToShift_validEmployee_employeeAdded()
            throws DuplicateEmployeeException, DuplicateShiftException {
        Timetable timetable = new Timetable(DATE_MON);
        timetable.addShift(MONDAY_AM);
        timetable.addShift(MONDAY_PM);
        timetable.addShift(TUESDAY_AM);
        timetable.addEmployeeToShift(DayOfWeek.MONDAY, 0, TypicalEmployees.ALICE);
        timetable.addEmployeeToShift(DayOfWeek.MONDAY, 1, TypicalEmployees.BOB);
        timetable.addEmployeeToShift(DayOfWeek.TUESDAY, 0, TypicalEmployees.BOB);
        assertTrue(timetable.shiftContains(DayOfWeek.MONDAY, 0, TypicalEmployees.ALICE));
        assertTrue(timetable.shiftContains(DayOfWeek.MONDAY, 1, TypicalEmployees.BOB));
        assertTrue(timetable.shiftContains(DayOfWeek.TUESDAY, 0, TypicalEmployees.BOB));
    }

    @Test
    public void removeEmployeeFromShift_validEmployee_employeeRemoved()
            throws DuplicateEmployeeException, EmployeeNotFoundException, DuplicateShiftException {
        Timetable timetable = new Timetable(DATE_MON);
        timetable.addShift(MONDAY_AM);
        timetable.addShift(MONDAY_PM);
        timetable.addShift(TUESDAY_AM);
        timetable.addEmployeeToShift(DayOfWeek.MONDAY, 0, TypicalEmployees.ALICE);
        timetable.addEmployeeToShift(DayOfWeek.MONDAY, 1, TypicalEmployees.BOB);
        timetable.addEmployeeToShift(DayOfWeek.TUESDAY, 0, TypicalEmployees.BOB);
        timetable.removeEmployeeFromShift(DayOfWeek.MONDAY, 1, TypicalEmployees.BOB);
        timetable.removeEmployeeFromShift(DayOfWeek.TUESDAY, 0, TypicalEmployees.BOB);
        timetable.removeEmployeeFromShift(DayOfWeek.MONDAY, 0, TypicalEmployees.ALICE);
        assertFalse(timetable.shiftContains(DayOfWeek.MONDAY, 0, TypicalEmployees.ALICE));
        assertFalse(timetable.shiftContains(DayOfWeek.MONDAY, 1, TypicalEmployees.BOB));
        assertFalse(timetable.shiftContains(DayOfWeek.TUESDAY, 0, TypicalEmployees.BOB));
    }

}
