package seedu.ptman.model.shift;

import static seedu.ptman.commons.util.AppUtil.checkArgument;
import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Iterables;

import javafx.collections.ObservableList;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.UniqueEmployeeList;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.shift.exceptions.ShiftFullException;

//@@author shanwpf
/**
 * Represents a shift that employees can work in.
 */
public class Shift {
    public static final String MESSAGE_SHIFT_CONSTRAINTS = "Start time should be after the end time.";
    private Time startTime;
    private Time endTime;
    private Date date;
    private UniqueEmployeeList uniqueEmployeeList;
    private Capacity capacity;

    public Shift(Date date, Time startTime, Time endTime, Capacity capacity) {
        requireAllNonNull(startTime, endTime, capacity);
        checkArgument(endTime.isAfter(startTime), MESSAGE_SHIFT_CONSTRAINTS);
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.uniqueEmployeeList = new UniqueEmployeeList();
    }

    public Shift(Shift shift) {
        this.date = shift.getDate();
        this.startTime = shift.getStartTime();
        this.endTime = shift.getEndTime();
        this.capacity = shift.getCapacity();
        this.uniqueEmployeeList = new UniqueEmployeeList();
        setEmployees(shift);
    }

    public Shift(Date date, Time startTime, Time endTime, Capacity capacity, List<Employee> employees) {
        requireAllNonNull(date, startTime, endTime, capacity, employees);
        checkArgument(endTime.isAfter(startTime), MESSAGE_SHIFT_CONSTRAINTS);
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.date = date;
        this.uniqueEmployeeList = new UniqueEmployeeList();
        try {
            this.uniqueEmployeeList.setEmployees(employees);
        } catch (DuplicateEmployeeException e) {
            e.printStackTrace();
        }
    }

    protected boolean contains(Employee employee) {
        return uniqueEmployeeList.contains(employee);
    }

    /**
     * Adds an employee to this shift
     * @throws DuplicateEmployeeException
     * @throws ShiftFullException
     */
    public void addEmployee(Employee employee) throws DuplicateEmployeeException, ShiftFullException {
        if (this.isFull()) {
            throw new ShiftFullException();
        }
        uniqueEmployeeList.add(employee);
    }

    /**
     * Removes an employee from this shift
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
                && date.equals(shift.date)
                && uniqueEmployeeList.equals(shift.uniqueEmployeeList)
                && capacity.equals(shift.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, date, uniqueEmployeeList, capacity);
    }

    public ObservableList<Employee> getEmployeeList() {
        return uniqueEmployeeList.asObservableList();
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

    public boolean isFull() {
        return getEmployeeList().size() >= this.capacity.getCapacity();
    }

    /**
     * Compares this shift to another. Returns a negative integer if the argument is a later shift,
     * 0 if the shifts are equal, or a positive integer if the argument is a later shift.
     */
    public int compareTo(Shift other) {
        if (date.equals(other.getDate())) {
            return startTime.compareTo(other.getStartTime());
        } else if (date.compareTo(other.getDate()) < 0) {
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        return sb.append("Date: ")
                .append(date)
                .append(" Start time: ")
                .append(startTime)
                .append(" End time: ")
                .append(endTime)
                .append(" Capacity: ")
                .append(capacity).toString();
    }

    public Date getDate() {
        return date;
    }
}
