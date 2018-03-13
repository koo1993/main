package seedu.ptman.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.model.employee.Address;
import seedu.ptman.model.employee.Email;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.Name;
import seedu.ptman.model.employee.Phone;
import seedu.ptman.model.employee.Salary;
import seedu.ptman.model.tag.Tag;

/**
 * JAXB-friendly version of the Employee.
 */
public class XmlAdaptedEmployee {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Employee's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;
    @XmlElement(required = true)
    private String salary;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedEmployee.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedEmployee() {}

    /**
     * Constructs an {@code XmlAdaptedEmployee} with the given employee details.
     */
    public XmlAdaptedEmployee(String name, String phone, String email, String address,
                              String salary, List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.salary = salary;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Employee into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedEmployee
     */
    public XmlAdaptedEmployee(Employee source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        salary = source.getSalary().value;
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted employee object into the model's Employee object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted employee
     */
    public Employee toModelType() throws IllegalValueException {
        final List<Tag> employeeTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            employeeTags.add(tag.toModelType());
        }

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name name = new Name(this.name);

        if (this.phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(this.phone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        final Phone phone = new Phone(this.phone);

        if (this.email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(this.email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email email = new Email(this.email);

        if (this.address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(this.address)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Address address = new Address(this.address);

        if (this.salary == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Salary.class.getSimpleName()));
        }
        if (!Salary.isValidSalary(this.salary)) {
            throw new IllegalValueException(Salary.MESSAGE_SALARY_CONSTRAINTS);
        }
        final Salary salary = new Salary(this.salary);

        final Set<Tag> tags = new HashSet<>(employeeTags);
        return new Employee(name, phone, email, address, salary, tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedEmployee)) {
            return false;
        }

        XmlAdaptedEmployee otherEmployee = (XmlAdaptedEmployee) other;
        return Objects.equals(name, otherEmployee.name)
                && Objects.equals(phone, otherEmployee.phone)
                && Objects.equals(email, otherEmployee.email)
                && Objects.equals(address, otherEmployee.address)
                && tagged.equals(otherEmployee.tagged);
    }
}
