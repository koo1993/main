package seedu.ptman.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.ptman.model.Password;
import seedu.ptman.model.employee.Address;
import seedu.ptman.model.employee.Email;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.Name;
import seedu.ptman.model.employee.Phone;
import seedu.ptman.model.employee.Salary;
import seedu.ptman.model.tag.Tag;
import seedu.ptman.model.util.SampleDataUtil;

/**
 * A utility class to help with building Employee objects.
 */
public class EmployeeBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_SALARY = "10";
    public static final String DEFAULT_TAGS = "friends";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Salary salary;
    private Set<Tag> tags;
    private Password password;

    public EmployeeBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        salary = new Salary(DEFAULT_SALARY);
        password = new Password();
        tags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
    }

    /**
     * Initializes the EmployeeBuilder with the data of {@code employeeToCopy}.
     */
    public EmployeeBuilder(Employee employeeToCopy) {
        name = employeeToCopy.getName();
        phone = employeeToCopy.getPhone();
        email = employeeToCopy.getEmail();
        address = employeeToCopy.getAddress();
        salary = employeeToCopy.getSalary();
        password = employeeToCopy.getPassword();
        tags = new HashSet<>(employeeToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Employee} that we are building.
     */
    public EmployeeBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Employee} that we are building.
     */
    public EmployeeBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Employee} that we are building.
     */
    public EmployeeBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Employee} that we are building.
     */
    public EmployeeBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Employee} that we are building.
     */
    public EmployeeBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Salary} of the {@code Employee} that we are building.
     */
    public EmployeeBuilder withSalary(String salary) {
        this.salary = new Salary(salary);
        return this;
    }
    /**
     * Sets the {@code Password} of the {@code Employee} that we are building.
     */
    public EmployeeBuilder withPassword(String passwordHash) {
        this.password = new Password(passwordHash);
        return this;
    }

    public Employee build() {
        return new Employee(name, phone, email, address, salary, password, tags);
    }

}
