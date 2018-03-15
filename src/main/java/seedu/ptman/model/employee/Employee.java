package seedu.ptman.model.employee;

import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.ptman.model.Password;
import seedu.ptman.model.tag.Tag;
import seedu.ptman.model.tag.UniqueTagList;

/**
 * Represents a Employee in PTMan.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Employee {

    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private final Salary salary;
    private final UniqueTagList tags;
    private final Password password;


    /**
     * Every field must be present and not null.
     */
    public Employee(Name name, Phone phone, Email email, Address address, Salary salary,
                    Password password, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.salary = salary;
        this.password = password;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Salary getSalary() {
        return salary;
    }

    public Password getPassword() {
        return password;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Employee)) {
            return false;
        }

        Employee otherEmployee = (Employee) other;
        return otherEmployee.getName().equals(this.getName())
                && otherEmployee.getPhone().equals(this.getPhone())
                && otherEmployee.getEmail().equals(this.getEmail())
                && otherEmployee.getAddress().equals(this.getAddress())
                && otherEmployee.getSalary().equals(this.getSalary())
                && otherEmployee.getPassword().equals(this.getPassword());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, salary, password, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Salary: ")
                .append(getSalary())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
