package seedu.ptman.model.shift;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.AppUtil.checkArgument;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

//@@author shanwpf
/**
 * Represents a shift's start or end time.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class Time {

    public static final String MESSAGE_TIME_CONSTRAINTS = "Time should be in 24-hour format.";

    public final LocalTime time;

    public Time(String time) {
        requireNonNull(time);
        checkArgument(isValidTime(time), MESSAGE_TIME_CONSTRAINTS);
        this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HHmm"));
    }

    /**
     * Returns true if a given string is a valid time.
     * @param test
     * @return
     */
    public static Boolean isValidTime(String test) {
        try {
            LocalTime.parse(test, DateTimeFormatter.ofPattern("HHmm"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public boolean isAfter(Time t) {
        return time.isAfter(t.time);
    }

    @Override
    public String toString() {
        return time.toString().replace(":", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Time time1 = (Time) o;
        return Objects.equals(time, time1.time);
    }

    @Override
    public int hashCode() {
        return time.hashCode();
    }

    public int compareTo(Time startTime) {
        return time.compareTo(startTime.getLocalTime());
    }

    public LocalTime getLocalTime() {
        return time;
    }
}
