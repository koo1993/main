package seedu.ptman.model.outlet;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.AppUtil.checkArgument;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents operating hours in an outlet.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class OperatingHours {

    public static final String MESSAGE_OPERATING_HOUR_CONSTRAINTS =
            "Operating hours must be in the format of START-END where START and END must be in "
                    + "the format of hh:mm and in terms of 24 hours. For example, 09:00-22:00";
    public static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    public static final String OPERATING_HOUR_VALIDATION_REGEX = TIME24HOURS_PATTERN + "-"
            + TIME24HOURS_PATTERN;
    public final LocalTime startTime;
    public final LocalTime endTime;

    /**
     * Constructs an {@code OperatingHours}.
     *
     * @param operatingHours A valid string of operating hours.
     */
    public OperatingHours(String operatingHours) {
        requireNonNull(operatingHours);
        checkArgument(isValidOperatingHours(operatingHours), MESSAGE_OPERATING_HOUR_CONSTRAINTS);
        String[] splitedTime = operatingHours.split("-");
        this.startTime = convertStringToLocalTime(splitedTime[0]);
        this.endTime = convertStringToLocalTime(splitedTime[1]);
    }

    /**
     * Converts a valid string of time to Local Time
     */
    public static LocalTime convertStringToLocalTime(String time) {
        String[] splitedTime = time.split(":");
        int hour = Integer.parseInt(splitedTime[0]);
        int minute = Integer.parseInt(splitedTime[1]);
        return LocalTime.of(hour, minute);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Returns true if a given string is a valid operating hours of an outlet.
     */
    public static boolean isValidOperatingHours(String test) {
        return test.matches(OPERATING_HOUR_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getStartTime())
                .append("-")
                .append(getEndTime());
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OperatingHours // instanceof handles nulls
                && this.startTime.equals(((OperatingHours) other).startTime)
                && this.endTime.equals(((OperatingHours) other).endTime)); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

}
