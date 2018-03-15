package seedu.ptman.model.employee;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.AppUtil.checkArgument;

/**
 * Represents a Employee's Salary earned so far in PTMan.
 * Guarantees: immutable; is valid as declared in {@link #isValidSalary(String)}
 */
public class Salary {

    public static final String MESSAGE_SALARY_CONSTRAINTS =
            "Salary can only contain positive numbers";
    public static final String SALARY_VALIDATION_REGEX = "^[0-9]\\d*$";
    public final String value;


    /**
     * Constructs a {@code Salary}
     *
     * @param salary A valid salary.
     */
    public Salary(String salary) {
        requireNonNull(salary);
        checkArgument(isValidSalary(salary), MESSAGE_SALARY_CONSTRAINTS);
        this.value = salary;
    }

    public static boolean isValidSalary(String test) {
        return test.matches(SALARY_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Salary // instanceof handles nulls
                && this.value.equals(((Salary) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
