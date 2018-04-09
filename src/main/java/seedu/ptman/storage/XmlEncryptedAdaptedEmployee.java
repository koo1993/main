package seedu.ptman.storage;

import static seedu.ptman.commons.encrypter.DataEncrypter.decrypt;
import static seedu.ptman.commons.encrypter.DataEncrypter.encrypt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.Address;
import seedu.ptman.model.employee.Email;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.Name;
import seedu.ptman.model.employee.Phone;
import seedu.ptman.model.employee.Salary;
import seedu.ptman.model.tag.Tag;

//@@author SunBangjie
/**
 * JAXB-friendly version of the Employee.
 */
public class XmlEncryptedAdaptedEmployee {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Employee's %s field is missing!";
    public static final String DECRYPT_FAIL_MESSAGE = "Cannot decrypt %s";

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
    @XmlElement(required = true)
    private String passwordHash;

    @XmlElement
    private List<XmlEncryptedAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedEmployee.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlEncryptedAdaptedEmployee() {}

    /**
     * Constructs an {@code XmlAdaptedEmployee} with the given employee details.
     */
    public XmlEncryptedAdaptedEmployee(String name, String phone, String email, String address,
                                       String salary, String passwordHash, List<XmlEncryptedAdaptedTag> tagged) {
        try {
            this.name = encrypt(name);
            this.phone = encrypt(phone);
            this.email = encrypt(email);
            this.address = encrypt(address);
            this.salary = encrypt(salary);
            this.passwordHash = encrypt(passwordHash);
        } catch (Exception e) {
            //Encryption should not fail
        }

        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Employee into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedEmployee
     */
    public XmlEncryptedAdaptedEmployee(Employee source) {
        try {
            name = encrypt(source.getName().fullName);
            phone = encrypt(source.getPhone().value);
            email = encrypt(source.getEmail().value);
            address = encrypt(source.getAddress().value);
            salary = encrypt(source.getSalary().value);
            passwordHash = encrypt(source.getPassword().getPasswordHash());
        } catch (Exception e) {
            //Encryption should not fail
        }

        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlEncryptedAdaptedTag(tag));
        }
    }

    public void setAttributesFromStrings(String name, String phone, String email, String address,
                                          String salary, String passwordHash) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.salary = salary;
        this.passwordHash = passwordHash;
    }

    public void setAttributesFromSource(Employee source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        salary = source.getSalary().value;
        passwordHash = source.getPassword().getPasswordHash();
    }

    private Name setName() throws IllegalValueException {
        String decryptedName;
        try {
            decryptedName = decrypt(this.name);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Name.class.getSimpleName()));
        }
        if (decryptedName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Name.class.getSimpleName()));
        }
        if (!Name.isValidName(decryptedName)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        return new Name(decryptedName);
    }

    private Phone setPhone() throws IllegalValueException {
        String decryptedPhone;
        try {
            decryptedPhone = decrypt(this.phone);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Phone.class.getSimpleName()));
        }
        if (decryptedPhone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(decryptedPhone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        return new Phone(decryptedPhone);
    }

    private Email setEmail() throws IllegalValueException {
        String decryptedEmail;
        try {
            decryptedEmail = decrypt(this.email);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Email.class.getSimpleName()));
        }
        if (decryptedEmail == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(decryptedEmail)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        return new Email(decryptedEmail);
    }

    private Address setAddress() throws IllegalValueException {
        String decryptedAddress;
        try {
            decryptedAddress = decrypt(this.address);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Address.class.getSimpleName()));
        }
        if (decryptedAddress == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(decryptedAddress)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        return new Address(decryptedAddress);
    }

    private Salary setSalary() throws IllegalValueException {
        String decryptedSalary;
        try {
            decryptedSalary = decrypt(this.salary);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Salary.class.getSimpleName()));
        }
        if (decryptedSalary == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Salary.class.getSimpleName()));
        }
        if (!Salary.isValidSalary(decryptedSalary)) {
            throw new IllegalValueException(Salary.MESSAGE_SALARY_CONSTRAINTS);
        }
        return new Salary(decryptedSalary);
    }

    private Password setPassword() throws IllegalValueException {
        String decryptedPasswordHash;
        try {
            decryptedPasswordHash = decrypt(this.passwordHash);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Password.class.getSimpleName()));
        }
        if (decryptedPasswordHash == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Password.class.getSimpleName()));
        }
        return new Password(decryptedPasswordHash);
    }

    /**
     * Converts this jaxb-friendly adapted employee object into the model's Employee object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted employee
     */
    public Employee toModelType() throws IllegalValueException {
        final List<Tag> employeeTags = new ArrayList<>();
        for (XmlEncryptedAdaptedTag tag : tagged) {
            employeeTags.add(tag.toModelType());
        }

        final Name name = setName();
        final Phone phone = setPhone();
        final Email email = setEmail();
        final Address address = setAddress();
        final Salary salary = setSalary();
        final Password password = setPassword();

        final Set<Tag> tags = new HashSet<>(employeeTags);
        return new Employee(name, phone, email, address, salary, password, tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlEncryptedAdaptedEmployee)) {
            return false;
        }

        XmlEncryptedAdaptedEmployee otherEmployee = (XmlEncryptedAdaptedEmployee) other;
        return Objects.equals(name, otherEmployee.name)
                && Objects.equals(phone, otherEmployee.phone)
                && Objects.equals(email, otherEmployee.email)
                && Objects.equals(address, otherEmployee.address)
                && tagged.equals(otherEmployee.tagged);
    }
}
