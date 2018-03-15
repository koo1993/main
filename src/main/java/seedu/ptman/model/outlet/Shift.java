package seedu.ptman.model.outlet;

import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.UniqueEmployeeList;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.outlet.exceptions.IllegalTimeException;

/**
 * Represents a shift that employees can work in.
 */
public class Shift {
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek dayOfWeek;
    private UniqueEmployeeList uniqueEmployeeList;
    private int capacity;

    public Shift(LocalTime startTime, LocalTime endTime, DayOfWeek dayOfWeek, int capacity)
            throws IllegalTimeException {
        requireAllNonNull(startTime, endTime, capacity);
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new IllegalTimeException();
        }
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.dayOfWeek = dayOfWeek;
        this.uniqueEmployeeList = new UniqueEmployeeList();
    }

    protected boolean contains(Employee employee) {
        return uniqueEmployeeList.contains(employee);
    }

    /**
     * Adds an employee that is working in this shift.
     * @param employee
     * @throws DuplicateEmployeeException
     */
    protected void addEmployee(Employee employee) throws DuplicateEmployeeException {
        uniqueEmployeeList.add(employee);
    }

    /**
     * Removes an employee who is no longer working in this shift.
     * @param employee
     * @throws EmployeeNotFoundException
     */
    protected void removeEmployee(Employee employee) throws EmployeeNotFoundException {
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
        return capacity == shift.capacity
                && Objects.equals(startTime, shift.startTime)
                && Objects.equals(endTime, shift.endTime)
                && dayOfWeek == shift.dayOfWeek
                && Objects.equals(uniqueEmployeeList, shift.uniqueEmployeeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, dayOfWeek, uniqueEmployeeList, capacity);
    }

    public ObservableList<Employee> getEmployeeList() {
        return uniqueEmployeeList.asObservableList();
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
}
