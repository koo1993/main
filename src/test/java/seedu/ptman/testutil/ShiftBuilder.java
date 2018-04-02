package seedu.ptman.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.shift.Capacity;
import seedu.ptman.model.shift.Date;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.Time;

//@@author shanwpf
/**
 * A utility class to help with building Shift objects.
 */
public class ShiftBuilder {

    public static final String DEFAULT_DATE = "04-03-18";
    public static final String DEFAULT_TIME_START = "0900";
    public static final String DEFAULT_TIME_END = "1600";
    public static final String DEFAULT_CAPACITY = "5";

    private Date date;
    private Time startTime;
    private Time endTime;
    private Capacity capacity;
    private List<Employee> employees;

    public ShiftBuilder() {
        date = new Date(DEFAULT_DATE);
        startTime = new Time(DEFAULT_TIME_START);
        endTime = new Time(DEFAULT_TIME_END);
        capacity = new Capacity(DEFAULT_CAPACITY);
        employees = new ArrayList<>();
    }

    /**
     * Initializes the ShiftBuilder with the data of {@code shiftToCopy}.
     */
    public ShiftBuilder(Shift shiftToCopy) {
        date = shiftToCopy.getDate();
        startTime = shiftToCopy.getStartTime();
        endTime = shiftToCopy.getEndTime();
        capacity = shiftToCopy.getCapacity();
        employees = new ArrayList<>(shiftToCopy.getEmployeeList());
    }

    /**
     * Sets the {@code Date} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withDate(String date) {
        this.date = new Date(date);
        return this;
    }

    /**
     * Sets the start {@code Time} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withStartTime(String startTime) {
        this.startTime = new Time(startTime);
        return this;
    }

    /**
     * Sets the end {@code Time} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withEndTime(String endTime) {
        this.endTime = new Time(endTime);
        return this;
    }

    /**
     * Sets the {@code Capacity} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withCapacity(String capacity) {
        this.capacity = new Capacity(capacity);
        return this;
    }

    /**
     * Sets the {@code Employee} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withEmployees(Employee... employees) {
        this.employees.addAll(Arrays.asList(employees));
        return this;
    }

    /**
     * Returns the {@code Shift}
     * @return
     */
    public Shift build() {
        return new Shift(date, startTime, endTime, capacity, employees);
    }

}
