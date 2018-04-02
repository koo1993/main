package seedu.ptman.testutil;

import static seedu.ptman.logic.parser.CliSyntax.PREFIX_CAPACITY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TIME_END;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TIME_START;

import seedu.ptman.logic.commands.AddShiftCommand;
import seedu.ptman.model.shift.Shift;

//@@author shanwpf
/**
 * A utility class for Shift.
 */
public class ShiftUtil {
    /**
     * Returns an addshift command string for adding the {@code shift}.
     */
    public static String getAddShiftCommand(Shift shift) {
        return AddShiftCommand.COMMAND_WORD + " " + getShiftDetails(shift);
    }

    /**
     * Returns an aliased addshift command string for adding the {@code shift}.
     */
    public static String getAliasedAddShiftCommand(Shift shift) {
        return AddShiftCommand.COMMAND_ALIAS + " " + getShiftDetails(shift);
    }

    /**
     * Returns the part of command string for the given {@code shift}'s details.
     */
    public static String getShiftDetails(Shift shift) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DATE + shift.getDate().toString() + " ");
        sb.append(PREFIX_TIME_START + shift.getStartTime().toString() + " ");
        sb.append(PREFIX_TIME_END + shift.getEndTime().toString() + " ");
        sb.append(PREFIX_CAPACITY + shift.getCapacity().toString() + " ");
        return sb.toString();
    }
}
