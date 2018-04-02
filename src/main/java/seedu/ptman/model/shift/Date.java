package seedu.ptman.model.shift;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

//@@author shanwpf
/**
 * Represents a shift's date
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String STRING_DATE_PATTERN = "dd-MM-yy";
    public static final String MESSAGE_DATE_CONSTRAINTS = "Date should be in dd-mm-yy format";

    public final LocalDate date;

    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern(STRING_DATE_PATTERN));
    }

    /**
     * Returns true if a given string is a valid date.
     * @param test
     * @return
     */
    public static Boolean isValidDate(String test) {
        try {
            LocalDate.parse(test, DateTimeFormatter.ofPattern(STRING_DATE_PATTERN));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ofPattern(STRING_DATE_PATTERN));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Date date1 = (Date) o;
        return Objects.equals(date, date1.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    public int compareTo(Date startDate) {
        return date.compareTo(startDate.getLocalDate());
    }

    public LocalDate getLocalDate() {
        return date;
    }
}
