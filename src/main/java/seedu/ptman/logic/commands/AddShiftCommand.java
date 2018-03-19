package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_CAPACITY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TIME_END;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TIME_START;

import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.outlet.Shift;
import seedu.ptman.model.outlet.exceptions.DuplicateShiftException;

/**
 * Adds a employee to PTMan.
 */
public class AddShiftCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addshift";
    public static final String COMMAND_ALIAS = "as";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a shift. "
            + "Parameters: "
            + PREFIX_DAY + "DAY "
            + PREFIX_TIME_START + "START_TIME "
            + PREFIX_TIME_END + "END_TIME "
            + PREFIX_CAPACITY + "CAPACITY "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DAY + "Monday "
            + PREFIX_TIME_START + "0900 "
            + PREFIX_TIME_END + "1600 "
            + PREFIX_CAPACITY + "5 ";

    public static final String MESSAGE_SUCCESS = "New shift added: %1$s";
    public static final String MESSAGE_DUPLICATE_SHIFT = "This shift already exists in PTMan";

    private final Shift toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Shift}
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
