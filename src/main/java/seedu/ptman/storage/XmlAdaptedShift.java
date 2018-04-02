package seedu.ptman.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.shift.Capacity;
import seedu.ptman.model.shift.Date;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.Time;

//@@author shanwpf
/**
 * JAXB-friendly version of the Shift.
 */
public class XmlAdaptedShift {

    public static final String MISSING_FIELD_MESSAGE_FORMAT_SHIFT = "Shifts's %s field is missing!";

    @XmlElement(required = true)
    private String date;
    @XmlElement(required = true)
    private String startTime;
    @XmlElement(required = true)
    private String endTime;
    @XmlElement(required = true)
    private String capacity;

    @XmlElement
    private List<XmlAdaptedEmployee> employees = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedShift.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedShift() {}

    /**
     * Constructs an {@code XmlAdaptedShift} with the given shift details.
     */
    public XmlAdaptedShift(String date, String startTime, String endTime,
                           String capacity, List<XmlAdaptedEmployee> employees) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        if (employees != null) {
            this.employees = new ArrayList<>(employees);
        }
    }

    /**
     * Converts a given Shift into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedShift
     */
    public XmlAdaptedShift(Shift source) {
        date = source.getDate().toString();
        startTime = source.getStartTime().toString();
        endTime = source.getEndTime().toString();
        capacity = source.getCapacity().toString();
        employees = new ArrayList<>();
        for (Employee employee : source.getEmployeeList()) {
            employees.add(new XmlAdaptedEmployee(employee));
        }
    }

    /**
     * Converts this jaxb-friendly adapted shift object into the model's Shift object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted shift
     */
    public Shift toModelType() throws IllegalValueException {
        final List<Employee> employees = new ArrayList<>();
        for (XmlAdaptedEmployee employee : this.employees) {
            employees.add(employee.toModelType());
        }

        if (this.date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT,
                    Date.class.getSimpleName()));
        }
        if (!Date.isValidDate(this.date)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        final Date date = new Date(this.date);

        if (this.startTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT,
                    Time.class.getSimpleName()));
        }
        if (!Time.isValidTime(this.startTime)) {
            throw new IllegalValueException(Time.MESSAGE_TIME_CONSTRAINTS);
        }
        final Time startTime = new Time(this.startTime);

        if (this.endTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT,
                    Time.class.getSimpleName()));
        }
        if (!Time.isValidTime(this.endTime)) {
            throw new IllegalValueException(Time.MESSAGE_TIME_CONSTRAINTS);
        }
        final Time endTime = new Time(this.endTime);


        if (this.capacity == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT,
                    Capacity.class.getSimpleName()));
        }
        if (!Capacity.isValidCapacity(this.capacity)) {
            throw new IllegalValueException(Capacity.MESSAGE_CAPACITY_CONSTRAINTS);
        }
        final Capacity capacity = new Capacity(this.capacity);

        return new Shift(date, startTime, endTime, capacity, employees);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedShift)) {
            return false;
        }

        XmlAdaptedShift otherShift = (XmlAdaptedShift) other;
        return Objects.equals(date, otherShift.date)
                && Objects.equals(startTime, otherShift.startTime)
                && Objects.equals(endTime, otherShift.endTime)
                && Objects.equals(capacity, otherShift.capacity)
                && employees.equals(otherShift.employees);
    }
}
