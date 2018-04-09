package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_CAPACITY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TIME_END;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TIME_START;

import java.time.LocalDate;

import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.exceptions.DuplicateShiftException;

//@@author shanwpf
/**
 * Adds a shift to PTMan.
 */
public class AddShiftCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addshift";
    public static final String COMMAND_ALIAS = "as";

    public static final String COMMAND_FORMAT = PREFIX_DATE + "DATE (in dd-mm-yy format) "
            + PREFIX_TIME_START + "START_TIME "
            + PREFIX_TIME_END + "END_TIME "
            + PREFIX_CAPACITY + "CAPACITY ";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a shift. "
            + "Parameters: "
            + COMMAND_FORMAT
            + "\nExample: " + COMMAND_WORD + " "
            + PREFIX_DATE + "12-03-18 "
            + PREFIX_TIME_START + "0900 "
            + PREFIX_TIME_END + "1600 "
            + PREFIX_CAPACITY + "5 ";

    public static final String MESSAGE_SUCCESS = "New shift added: %1$s";
    public static final String MESSAGE_DUPLICATE_SHIFT = "This shift already exists in PTMan";
    public static final String MESSAGE_DATE_OVER = "You cannot add a shift to a date that is already over";

    private final Shift toAdd;

    /**
     * Creates an AddShiftCommand to add the specified {@code Shift}
     */
    public AddShiftCommand(Shift shift) {
        requireNonNull(shift);
        toAdd = shift;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);

        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }

        LocalDate shiftDate = toAdd.getDate().getLocalDate();
        if (shiftDate.isBefore(LocalDate.now())) {
            throw new CommandException(MESSAGE_DATE_OVER);
        }

        try {
            model.addShift(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateShiftException e) {
            throw new CommandException(MESSAGE_DUPLICATE_SHIFT);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddShiftCommand // instanceof handles nulls
                && toAdd.equals(((AddShiftCommand) other).toAdd));
    }
}
