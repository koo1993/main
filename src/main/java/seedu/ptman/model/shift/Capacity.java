package seedu.ptman.model.shift;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.AppUtil.checkArgument;

import java.util.Objects;

//@@author shanwpf
/**
 * Represents a shift's capacity
 * Guarantees: immutable; is valid as declared in {@link #isValidCapacity(String)}
 */
public class Capacity {

    public static final String MESSAGE_CAPACITY_CONSTRAINTS = "Capacity should be a positive integer.";
    public static final String CAPACITY_VALIDATION_REGEX = "^[1-9]\\d*$";

    public final int capacity;

    public Capacity(String capacity) {
        requireNonNull(capacity);
        checkArgument(isValidCapacity(capacity), MESSAGE_CAPACITY_CONSTRAINTS);
        this.capacity = Integer.parseInt(capacity);
    }

    public static Boolean isValidCapacity(String test) {
        return test.matches(CAPACITY_VALIDATION_REGEX);
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return String.valueOf(capacity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Capacity capacity1 = (Capacity) o;
        return Objects.equals(capacity, capacity1.capacity);
    }

    @Override
    public int hashCode() {
        return new Integer(capacity).hashCode();
    }
}
