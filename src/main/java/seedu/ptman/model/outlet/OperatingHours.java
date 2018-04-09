package seedu.ptman.model.outlet;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.AppUtil.checkArgument;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

//@@author SunBangjie
/**
 * Represents operating hours in an outlet.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class OperatingHours {

    public static final String MESSAGE_OPERATING_HOUR_CONSTRAINTS =
            "Operating hours must be in the format of START-END where START and END must be in "
                    + "the format of HHMM and in terms of 24 hours. For example, 0900-2200";
    public static final String MESSAGE_START_END_TIME_CONSTRAINTS = "START time must be before END time.";
    public static final String OPERATING_HOUR_VALIDATION_REGEX = "\\d{4}" + "-" + "\\d{4}";

    public final String value;
    private final LocalTime startTime;
    private final LocalTime endTime;

    /**
     * Constructs an {@code OperatingHours}.
     *
     * @param operatingHours A valid string of operating hours.
     */
    public OperatingHours(String operatingHours) {
        requireNonNull(operatingHours);
        checkArgument(isValidOperatingHours(operatingHours), MESSAGE_OPERATING_HOUR_CONSTRAINTS);
        checkArgument(isValidStartTimeEndTimeOrder(operatingHours), MESSAGE_START_END_TIME_CONSTRAINTS);
        String[] splitedTime = operatingHours.split("-");
        this.startTime = convertStringToLocalTime(splitedTime[0]);
        this.endTime = convertStringToLocalTime(splitedTime[1]);
        this.value = operatingHours;
    }

    /**
     * Converts a valid string of time to Local Time
     */
    public static LocalTime convertStringToLocalTime(String time) {
        String hourString = time.substring(0, 2);
        String minuteString = time.substring(2);
        int hour = Integer.parseInt(hourString);
        int minute = Integer.parseInt(minuteString);
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
        if (!test.matches(OPERATING_HOUR_VALIDATION_REGEX)) {
            return false;
        }
        String[] splitedTime = test.split("-");
        try {
            LocalTime.parse(splitedTime[0], DateTimeFormatter.ofPattern("HHmm"));
            LocalTime.parse(splitedTime[1], DateTimeFormatter.ofPattern("HHmm"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if a given string has start time before end time.
     */
    public static boolean isValidStartTimeEndTimeOrder(String operatingHours) {
        String[] splitedTime = operatingHours.split("-");
        LocalTime startTime = convertStringToLocalTime(splitedTime[0]);
        LocalTime endTime = convertStringToLocalTime(splitedTime[1]);
        return startTime.isBefore(endTime);
    }

    @Override
    public String toString() {
        return value;
    }

    public String getDisplayedMessage() {
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
