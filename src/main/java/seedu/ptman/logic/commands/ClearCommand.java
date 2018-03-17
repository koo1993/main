package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.exceptions.InvalidPasswordException;

/**
 * Clears PTMan.
 */
public class ClearCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String COMMAND_ALIAS = "c";
    public static final String MESSAGE_SUCCESS = "PTMan has been cleared!";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Clear all data in PTMan.\n "
            + "Example: " + COMMAND_WORD + " " + "pw/AdminPassword";

    private final Password toCheck;

    public ClearCommand(Password password) {
        requireNonNull(password);
        toCheck = password;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);

        if (!model.isAdminPassword(toCheck)) {
            throw new InvalidPasswordException();
        }

        model.resetData(new PartTimeManager());
        return new CommandResult(MESSAGE_SUCCESS);

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ClearCommand // instanceof handles nulls
                && toCheck.equals(((ClearCommand) other).toCheck));
    }
}
