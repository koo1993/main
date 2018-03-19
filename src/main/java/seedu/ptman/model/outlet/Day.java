package seedu.ptman.model.outlet;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.AppUtil.checkArgument;

import java.time.DayOfWeek;
import java.util.Objects;

/**
 * Represents an outlet's day in PTMan.
 * Guarantees: immutable; is valid as declared in {@link #isValidDay(String)}
 */
public class Day {

    public static final String MESSAGE_DAY_CONSTRAINTS = "Capacity should be a positive integer.";

    public final DayOfWeek day;

    public Day(String day) {
        requireNonNull(day);
        checkArgument(isValidDay(day), MESSAGE_DAY_CONSTRAINTS);
        this.day = DayOfWeek.valueOf(day.toUpperCase());
    }

    /**
     * Returns true if a given string is a valid day of week.
     * @param test
     * @return
     */
    public static Boolean isValidDay(String test) {
        try {
            DayOfWeek.valueOf(test.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public DayOfWeek toDayOfWeek() {
        return day;
    }

    @Override
    public String toString() {
        return day.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Day day1 = (Day) o;
        return Objects.equals(day, day1.day);
    }

    @Override
    public int hashCode() {
        return day.toString().hashCode();
    }

    public int compareTo(Day other) {
        return day.compareTo(other.getDayOfWeek());
    }

    public DayOfWeek getDayOfWeek() {
        return day;
    }
}
