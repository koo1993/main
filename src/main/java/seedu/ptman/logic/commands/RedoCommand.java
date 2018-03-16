package seedu.ptman.logic.commands;

import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;

import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.Model;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.exceptions.InvalidPasswordException;

/**
 * Redo the previously undone command.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String COMMAND_ALIAS = "r";
    public static final String MESSAGE_SUCCESS = "Redo success!";
    public static final String MESSAGE_FAILURE = "No more commands to redo!";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Redo previous undo command.\n "
            + "Example: " + COMMAND_WORD + " " + "pw/AdminPassword";

    private Password toCheck;

    public RedoCommand(Password password) {
        isAdminCommand = true;
        toCheck = password;
    }
    @Override
    public CommandResult execute() throws CommandException {
        requireAllNonNull(model, toCheck, undoRedoStack);

        if (!model.isAdminPassword(toCheck)) {
            throw new InvalidPasswordException();
        }


        if (!undoRedoStack.canRedo()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        undoRedoStack.popRedo().redo();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public void setData(Model model, CommandHistory commandHistory, UndoRedoStack undoRedoStack) {
        this.model = model;
        this.undoRedoStack = undoRedoStack;
    }
}
