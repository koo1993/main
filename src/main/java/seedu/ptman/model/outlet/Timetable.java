package seedu.ptman.model.outlet;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;

import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.outlet.exceptions.DuplicateShiftException;
import seedu.ptman.model.outlet.exceptions.ShiftNotFoundException;

/**
 * Represents a week where shifts can be added/removed on different days and times.
 */
public class Timetable {
    private static final int INDEX_OFFSET = 1;
    private static final int NUM_DAYS_IN_WEEK = 7;
    private ArrayList<TimetableDay> dayList = new ArrayList<>();
    private UniqueShiftList uniqueShiftList = new UniqueShiftList();
    private LocalDate mondayDate;

    /**
     * Creates a timetable starting from the Monday of the week of the date provided
     * @param date
     */
    public Timetable(LocalDate date) {
        requireNonNull(date);
        this.mondayDate = findStartOfWeekDate(date);
        for (int i = 0; i < NUM_DAYS_IN_WEEK; i++) {
            dayList.add(new TimetableDay(this.mondayDate.plusDays(i), new ArrayList<>()));
        }
    }

    /**
     * Gets the date for the Monday of this timetable week
     * @return LocalDate of Monday
     */
    public LocalDate getMondayDate() {
        return mondayDate;
    }

    /**
     * Gets the shift for the specified day and index
     * @param dayOfWeek
     * @param index
     * @return
     */
    public Shift getShift(DayOfWeek dayOfWeek, int index) {
        return getTimetableDay(dayOfWeek).getShift(index);
    }

    public boolean containsShift(Shift shift) {
        return uniqueShiftList.contains(shift);
    }

    /**
     * Removes a shift from the timetable
     * @param shift
     * @throws ShiftNotFoundException
     */
    public void removeShift(Shift shift) throws ShiftNotFoundException {
        uniqueShiftList.remove(shift);
        getTimetableDay(shift.getDay().toDayOfWeek()).removeShift(shift);
    }

    /**
     * Checks if a shift contains a specified employee
     * @param dayOfWeek
     * @param index
     * @param employee
     * @return
     */
    public boolean shiftContains(DayOfWeek dayOfWeek, int index, Employee employee) {
        return getShift(dayOfWeek, index).contains(employee);
    }

    public void addEmployeeToShift(DayOfWeek dayOfWeek, int index, Employee employee)
            throws DuplicateEmployeeException {
        getTimetableDay(dayOfWeek).getShifts().get(index).addEmployee(employee);
    }

    public void removeEmployeeFromShift(DayOfWeek dayOfWeek, int index, Employee employee)
            throws EmployeeNotFoundException {
        getTimetableDay(dayOfWeek).getShifts().get(index).removeEmployee(employee);
    }

    /**
     * Adds a shift to the timetable
     * @param shift
     * @throws DuplicateShiftException
     */
    public void addShift(Shift shift) throws DuplicateShiftException {
        uniqueShiftList.add(shift);
        getTimetableDay(shift.getDay().toDayOfWeek()).getShifts().add(shift);
    }

    private TimetableDay getTimetableDay(DayOfWeek dayOfWeek) {
        return dayList.get(dayOfWeek.getValue() - INDEX_OFFSET);
    }

    private int getWeekFromDate(LocalDate date) {
        TemporalField woy = WeekFields.of(Locale.FRANCE).weekOfWeekBasedYear();
        return date.get(woy);
    }

    /**
     * Returns the date of the start of the week
     * @param date
     * @return
     */
    private LocalDate findStartOfWeekDate(LocalDate date) {
        int week = getWeekFromDate(date);
        // We use Locale.FRANCE because it sets the first day of the week
        // to be Monday.
        WeekFields weekFields = WeekFields.of(Locale.FRANCE);
        return LocalDate.now()
                .withYear(date.getYear())
                .with(weekFields.weekOfYear(), week)
                .with(weekFields.dayOfWeek(), 1);
    }

}
