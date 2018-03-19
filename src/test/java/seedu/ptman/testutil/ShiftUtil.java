package seedu.ptman.testutil;

import static seedu.ptman.logic.parser.CliSyntax.PREFIX_CAPACITY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TIME_END;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TIME_START;

import seedu.ptman.logic.commands.AddShiftCommand;
import seedu.ptman.model.outlet.Shift;

/**
 * A utility class for Shift.
 */
public class ShiftUtil {
    public static final String DEFAULT_PASSWORD = "DEFAULT1";
    /**
     * Returns an add command string for adding the {@code employee}.
     */
    public static String getAddShiftCommand(Shift shift) {
        return AddShiftCommand.COMMAND_WORD + " " + getShiftDetails(shift);
    }

    /**
     * Returns an aliased add command string for adding the {@code employee}.
     */
    public static String getAliasedAddShiftCommand(Shift shift) {
        return AddShiftCommand.COMMAND_ALIAS + " " + getShiftDetails(shift);
    }

    /**
     * Returns the part of command string for the given {@code employee}'s details.
     */
    public static String getShiftDetails(Shift shift) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DAY + shift.getDay().toString() + " ");
        sb.append(PREFIX_TIME_START + shift.getStartTime().toString() + " ");
        sb.append(PREFIX_TIME_END + shift.getEndTime().toString() + " ");
        sb.append(PREFIX_CAPACITY + shift.getCapacity().toString() + " ");
        return sb.toString();
    }
}
