package seedu.ptman.testutil;

import seedu.ptman.model.outlet.Shift;

/**
 * A utility class containing a list of {@code Employee} objects to be used in tests.
 */
public class TypicalShifts {

    public static final Shift MONDAY_AM = new ShiftBuilder().withDay("monday")
            .withStartTime("0800")
            .withEndTime("1300")
            .withCapacity("4").build();
    public static final Shift MONDAY_PM = new ShiftBuilder().withDay("monday")
            .withStartTime("1300")
            .withEndTime("2200")
            .withCapacity("4").build();
    public static final Shift TUESDAY_AM = new ShiftBuilder().withDay("tuesday")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("5").build();
    public static final Shift TUESDAY_PM = new ShiftBuilder().withDay("tuesday")
            .withStartTime("1200")
            .withEndTime("2200")
            .withCapacity("3").build();
    public static final Shift SUNDAY_AM = new ShiftBuilder().withDay("sunday")
            .withStartTime("1000")
            .withEndTime("1300")
            .withCapacity("4").build();
    public static final Shift SUNDAY_PM = new ShiftBuilder().withDay("sunday")
            .withStartTime("1300")
            .withEndTime("1700")
            .withCapacity("4").build();
    public static final Shift WEDNESDAY_AM = new ShiftBuilder().withDay("wednesday")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("5").build();
    public static final Shift WEDNESDAY_PM = new ShiftBuilder().withDay("wednesday")
            .withStartTime("1200")
            .withEndTime("2200")
            .withCapacity("3").build();
    public static final Shift THURSDAY_AM = new ShiftBuilder().withDay("thursday")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("5").build();
    public static final Shift THURSDAY_PM = new ShiftBuilder().withDay("thursday")
            .withStartTime("1200")
            .withEndTime("2200")
            .withCapacity("3").build();

    private TypicalShifts() {} // prevents instantiation

}
