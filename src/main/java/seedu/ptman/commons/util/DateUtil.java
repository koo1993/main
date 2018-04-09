package seedu.ptman.commons.util;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

//@@author shanwpf
/**
 * Utility methods for handling dates
 */
public class DateUtil {

    private static final int NUM_DAYS_IN_WEEK = 7;

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

    //@@author hzxcaryn
    /**
     * Given a {@code date}, returns the date of the week's Thursday
     */
    public static LocalDate getThursdayOfDate(LocalDate date) {
        requireNonNull(date);
        int week = getWeekFromDate(date);
        WeekFields weekFields = WeekFields.of(Locale.FRANCE);
        return LocalDate.now()
                .withYear(date.getYear())
                .with(weekFields.weekOfYear(), week)
                .with(weekFields.dayOfWeek(), 4);
    }


    /**
     * Given {@code currDate}, returns the date one week later
     */
    public static LocalDate getNextWeekDate(LocalDate currDate) {
        requireNonNull(currDate);
        LocalDate nextWeekDate = currDate.plusDays(NUM_DAYS_IN_WEEK);
        return nextWeekDate;
    }

    /**
     * Given {@code currDate}, returns the date one week before
     */
    public static LocalDate getPrevWeekDate(LocalDate currDate) {
        requireNonNull(currDate);
        LocalDate prevWeekDate = currDate.minusDays(NUM_DAYS_IN_WEEK);
        return prevWeekDate;
    }

    /**
     * Given {@code date}, returns the month and year as a string
     */
    public static String getMonthYearFromDate(LocalDate date) {
        requireNonNull(date);
        Month month = date.getMonth();
        int year = date.getYear();
        return month.name() + " " + year;
    }

}
