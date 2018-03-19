package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;

import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.PartTimeManager;

/**
 * Clears PTMan.
 */
public class ClearCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String COMMAND_ALIAS = "c";
    public static final String MESSAGE_SUCCESS = "PTMan has been cleared!";


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);

        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }

        model.resetData(new PartTimeManager());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
