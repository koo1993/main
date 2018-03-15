package seedu.ptman.model.outlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.model.outlet.ShiftTest.TIME_AM;
import static seedu.ptman.model.outlet.ShiftTest.TIME_PM;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.Test;

import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.outlet.exceptions.DuplicateShiftException;
import seedu.ptman.model.outlet.exceptions.IllegalTimeException;
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
    public void addShift_validShift_shiftAdded() throws IllegalTimeException, DuplicateShiftException {
        Shift shift1 = new Shift(TIME_AM, TIME_PM, DayOfWeek.MONDAY, 3);
        Shift shift2 = new Shift(TIME_AM.plusHours(1), TIME_PM, DayOfWeek.SUNDAY, 3);
        Shift shift3 = new Shift(TIME_AM.plusHours(5), TIME_PM.minusHours(1), DayOfWeek.MONDAY, 3);
        Timetable timetable = new Timetable(DATE_MON);
        timetable.addShift(shift1);
        timetable.addShift(shift2);
        timetable.addShift(shift3);
        assertEquals(shift1, timetable.getShift(DayOfWeek.MONDAY, 0));
        assertEquals(shift3, timetable.getShift(DayOfWeek.MONDAY, 1));
        assertEquals(shift2, timetable.getShift(DayOfWeek.SUNDAY, 0));
    }

    @Test
    public void addShift_duplicateShift_throwsDuplicateShiftException() throws IllegalTimeException {
        Timetable timetable = new Timetable(DATE_MON);
        Shift shift1 = new Shift(TIME_AM, TIME_PM, DayOfWeek.MONDAY, 3);
        Assert.assertThrows(DuplicateShiftException.class, () -> {
            timetable.addShift(shift1);
            timetable.addShift(shift1);
        });
    }

    @Test
    public void removeShift_shiftNotInTimetable_throwsShiftNotFoundException() throws IllegalTimeException {
        Timetable timetable = new Timetable(DATE_MON);
        Shift shift1 = new Shift(TIME_AM, TIME_PM, DayOfWeek.MONDAY, 3);
        Assert.assertThrows(ShiftNotFoundException.class, () -> {
            timetable.removeShift(shift1);
        });
    }

    @Test
    public void removeShift_shiftExists_shiftRemoved()
            throws IllegalTimeException, DuplicateShiftException, ShiftNotFoundException {
        Shift shift1 = new Shift(TIME_AM, TIME_PM, DayOfWeek.MONDAY, 3);
        Shift shift2 = new Shift(TIME_AM.plusHours(1), TIME_PM, DayOfWeek.SUNDAY, 3);
        Shift shift3 = new Shift(TIME_AM.plusHours(5), TIME_PM.minusHours(1), DayOfWeek.MONDAY, 3);
        Timetable timetable = new Timetable(DATE_MON);
        timetable.addShift(shift1);
        timetable.addShift(shift2);
        timetable.addShift(shift3);
        timetable.removeShift(shift2);
        timetable.removeShift(shift3);
        timetable.removeShift(shift1);
        assertFalse(timetable.containsShift(shift2));
        assertFalse(timetable.containsShift(shift1));
        assertFalse(timetable.containsShift(shift3));
    }

    @Test
    public void addEmployeeToShift_validEmployee_employeeAdded()
            throws DuplicateEmployeeException, IllegalTimeException, DuplicateShiftException {
        Shift shift1 = new Shift(TIME_AM, TIME_PM, DayOfWeek.MONDAY, 3);
        Shift shift2 = new Shift(TIME_AM.plusHours(1), TIME_PM, DayOfWeek.MONDAY, 3);
        Shift shift3 = new Shift(TIME_AM.plusHours(5), TIME_PM.minusHours(1), DayOfWeek.TUESDAY, 3);
        Timetable timetable = new Timetable(DATE_MON);
        timetable.addShift(shift1);
        timetable.addShift(shift2);
        timetable.addShift(shift3);
        timetable.addEmployeeToShift(DayOfWeek.MONDAY, 0, TypicalEmployees.ALICE);
        timetable.addEmployeeToShift(DayOfWeek.MONDAY, 1, TypicalEmployees.BOB);
        timetable.addEmployeeToShift(DayOfWeek.TUESDAY, 0, TypicalEmployees.BOB);
        assertTrue(timetable.shiftContains(DayOfWeek.MONDAY, 0, TypicalEmployees.ALICE));
        assertTrue(timetable.shiftContains(DayOfWeek.MONDAY, 1, TypicalEmployees.BOB));
        assertTrue(timetable.shiftContains(DayOfWeek.TUESDAY, 0, TypicalEmployees.BOB));
    }

    @Test
    public void removeEmployeeFromShift_validEmployee_employeeRemoved()
            throws DuplicateEmployeeException, IllegalTimeException,
            EmployeeNotFoundException, DuplicateShiftException {
        Shift shift1 = new Shift(TIME_AM, TIME_PM, DayOfWeek.MONDAY, 3);
        Shift shift2 = new Shift(TIME_AM.plusHours(1), TIME_PM, DayOfWeek.MONDAY, 3);
        Shift shift3 = new Shift(TIME_AM.plusHours(5), TIME_PM.minusHours(1), DayOfWeek.TUESDAY, 3);
        Timetable timetable = new Timetable(DATE_MON);
        timetable.addShift(shift1);
        timetable.addShift(shift2);
        timetable.addShift(shift3);
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
