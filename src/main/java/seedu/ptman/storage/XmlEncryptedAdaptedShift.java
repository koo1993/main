package seedu.ptman.storage;

import static seedu.ptman.commons.encrypter.DataEncrypter.decrypt;
import static seedu.ptman.commons.encrypter.DataEncrypter.encrypt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.shift.Capacity;
import seedu.ptman.model.shift.Date;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.Time;

//@@author SunBangjie
/**
 * JAXB-friendly version of the Shift.
 */
public class XmlEncryptedAdaptedShift {

    public static final String MISSING_FIELD_MESSAGE_FORMAT_SHIFT = "Shifts's %s field is missing!";
    public static final String DECRYPT_FAIL_MESSAGE = "Cannot decrypt %s.";

    @XmlElement(required = true)
    private String date;
    @XmlElement(required = true)
    private String startTime;
    @XmlElement(required = true)
    private String endTime;
    @XmlElement(required = true)
    private String capacity;

    @XmlElement
    private List<XmlEncryptedAdaptedEmployee> employees = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedShift.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlEncryptedAdaptedShift() {}

    /**
     * Constructs an {@code XmlAdaptedShift} with the given shift details.
     */
    public XmlEncryptedAdaptedShift(String date, String startTime, String endTime,
                                    String capacity, List<XmlEncryptedAdaptedEmployee> employees) {
        try {
            this.date = encrypt(date);
            this.startTime = encrypt(startTime);
            this.endTime = encrypt(endTime);
            this.capacity = encrypt(capacity);
        } catch (Exception e) {
            //Encryption should not fail
        }

        if (employees != null) {
            this.employees = new ArrayList<>(employees);
        }
    }

    /**
     * Converts a given Shift into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedShift
     */
    public XmlEncryptedAdaptedShift(Shift source) {
        try {
            date = encrypt(source.getDate().toString());
            startTime = encrypt(source.getStartTime().toString());
            endTime = encrypt(source.getEndTime().toString());
            capacity = encrypt(source.getCapacity().toString());
        } catch (Exception e) {
            //Encryption should not fail
        }

        employees = new ArrayList<>();
        for (Employee employee : source.getEmployeeList()) {
            employees.add(new XmlEncryptedAdaptedEmployee(employee));
        }
    }

    public void setAttributesFromSource(Shift source) {
        date = source.getDate().toString();
        startTime = source.getStartTime().toString();
        endTime = source.getEndTime().toString();
        capacity = source.getCapacity().toString();
    }

    public void setAttributesFromStrings(String date, String startTime, String endTime, String capacity) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
    }

    /**
     * Decrypts date
     * @return
     * @throws IllegalValueException
     */
    private Date decryptDate() throws IllegalValueException {
        String decryptedDate;
        try {
            decryptedDate = decrypt(this.date);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Date.class.getSimpleName()));
        }
        if (decryptedDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT,
                    Date.class.getSimpleName()));
        }
        if (!Date.isValidDate(decryptedDate)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        return new Date(decryptedDate);
    }

    /**
     * Decryptes time
     * @param time
     * @return
     * @throws IllegalValueException
     */
    private Time decryptTime(String time) throws IllegalValueException {
        String decryptedTime;
        try {
            decryptedTime = decrypt(time);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Time.class.getSimpleName()));
        }
        if (decryptedTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT,
                    Time.class.getSimpleName()));
        }
        if (!Time.isValidTime(decryptedTime)) {
            throw new IllegalValueException(Time.MESSAGE_TIME_CONSTRAINTS);
        }
        return new Time(decryptedTime);
    }

    /**
     * Decryptes capacity
     * @return
     * @throws IllegalValueException
     */
    private Capacity decryptCapacity() throws IllegalValueException {
        String decryptedCapacity;
        try {
            decryptedCapacity = decrypt(this.capacity);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Capacity.class.getSimpleName()));
        }
        if (decryptedCapacity == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT,
                    Capacity.class.getSimpleName()));
        }
        if (!Capacity.isValidCapacity(decryptedCapacity)) {
            throw new IllegalValueException(Capacity.MESSAGE_CAPACITY_CONSTRAINTS);
        }
        return new Capacity(decryptedCapacity);
    }

    /**
     * Converts this jaxb-friendly adapted shift object into the model's Shift object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted shift
     */
    public Shift toModelType() throws IllegalValueException {
        final List<Employee> employees = new ArrayList<>();
        for (XmlEncryptedAdaptedEmployee employee : this.employees) {
            employees.add(employee.toModelType());
        }

        final Date date = decryptDate();
        final Time startTime = decryptTime(this.startTime);
        final Time endTime = decryptTime(this.endTime);
        final Capacity capacity = decryptCapacity();

        return new Shift(date, startTime, endTime, capacity, new HashSet<>(employees));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlEncryptedAdaptedShift)) {
            return false;
        }

        XmlEncryptedAdaptedShift otherShift = (XmlEncryptedAdaptedShift) other;
        return Objects.equals(date, otherShift.date)
                && Objects.equals(startTime, otherShift.startTime)
                && Objects.equals(endTime, otherShift.endTime)
                && Objects.equals(capacity, otherShift.capacity)
                && employees.equals(otherShift.employees);
    }
}
