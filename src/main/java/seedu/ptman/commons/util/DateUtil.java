package seedu.ptman.commons.util;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

//@@author shanwpf
/**
 * Utility methods for handling dates
 */
public class DateUtil {
    /**
     * Returns the week number for {@code date} from the start of the year
     */
    public static int getWeekFromDate(LocalDate date) {
        requireNonNull(date);
        TemporalField woy = WeekFields.of(Locale.FRANCE).weekOfWeekBasedYear();
        return date.get(woy);
    }

    /**
     * Given a {@code date}, returns the date of the week's Monday
     */
    public static LocalDate getMondayOfDate(LocalDate date) {
        requireNonNull(date);
        int week = getWeekFromDate(date);
        // We use Locale.FRANCE because it sets the first day of the week
        // to be Monday.
        WeekFields weekFields = WeekFields.of(Locale.FRANCE);
        return LocalDate.now()
                .withYear(date.getYear())
                .with(weekFields.weekOfYear(), week)
                .with(weekFields.dayOfWeek(), 1);
    }
}
