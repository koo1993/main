package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.ptman.model.PartTimeManager;

/**
 * Clears PTMan.
 */
public class ClearCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String COMMAND_ALIAS = "c";
    public static final String MESSAGE_SUCCESS = "PTMan has been cleared!";

    {
        isAdminCommand = true;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        model.resetData(new PartTimeManager());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
