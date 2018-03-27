package seedu.ptman.model.outlet;

import static seedu.ptman.commons.util.AppUtil.checkArgument;
import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import com.google.common.collect.Iterables;

import javafx.collections.ObservableList;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.UniqueEmployeeList;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;

/**
 * Represents a shift that employees can work in.
 */
public class Shift {
    public static final String MESSAGE_SHIFT_CONSTRAINTS = "Start time should be after the end time.";
    private Time startTime;
    private Time endTime;
    private Day day;
    private UniqueEmployeeList uniqueEmployeeList;
    private Capacity capacity;

    public Shift(Day day, Time startTime, Time endTime, Capacity capacity) {
        requireAllNonNull(startTime, endTime, capacity);
        checkArgument(endTime.isAfter(startTime), MESSAGE_SHIFT_CONSTRAINTS);
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.day = day;
        this.uniqueEmployeeList = new UniqueEmployeeList();
    }

    public Shift(Shift shift) {
        this.startTime = shift.getStartTime();
        this.endTime = shift.getEndTime();
        this.capacity = shift.getCapacity();
        this.day = shift.getDay();
        this.uniqueEmployeeList = new UniqueEmployeeList();
        setEmployees(shift);
    }

    protected boolean contains(Employee employee) {
        return uniqueEmployeeList.contains(employee);
    }

    /**
     * Adds an employee that is working in this shift.
     * @param employee
     * @throws DuplicateEmployeeException
     */
    public void addEmployee(Employee employee) throws DuplicateEmployeeException {
        uniqueEmployeeList.add(employee);
    }

    /**
     * Removes an employee who is no longer working in this shift.
     * @param employee
     * @throws EmployeeNotFoundException
     */
    public void removeEmployee(Employee employee) throws EmployeeNotFoundException {
        uniqueEmployeeList.remove(employee);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shift shift = (Shift) o;
        return startTime.equals(shift.startTime)
                && endTime.equals(shift.endTime)
                && day.equals(shift.day)
                && uniqueEmployeeList.equals(shift.uniqueEmployeeList)
                && capacity.equals(shift.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, day, uniqueEmployeeList, capacity);
    }

    public ObservableList<Employee> getEmployeeList() {
        return uniqueEmployeeList.asObservableList();
    }

    public Day getDay() {
        return day;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public int getSlotsLeft() {
        int numEmployees = Iterables.size(uniqueEmployeeList);
        return capacity.getCapacity() - numEmployees;

    }

    /**
     * Compares this shift to another. Returns a negative integer if the argument is a later shift,
     * 0 if the shifts are equal, or a positive integer if the argument is a later shift.
     * @param other
     * @return
     */
    public int compareTo(Shift other) {
        if (day.equals(other.getDay())) {
            return startTime.compareTo(other.getStartTime());
        } else if (day.compareTo(other.getDay()) < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    public UniqueEmployeeList getUniqueEmployeeList() {
        return uniqueEmployeeList;
    }

    public void setEmployees(Shift shift) {
        for (final Employee employee : shift.getEmployeeList()) {
            try {
                uniqueEmployeeList.add(employee);
            } catch (DuplicateEmployeeException e) {
                e.printStackTrace();
            }
        }
    }
}
