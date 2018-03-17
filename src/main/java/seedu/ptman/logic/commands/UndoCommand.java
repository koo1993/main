package seedu.ptman.logic.commands;

import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;

import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.Model;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.exceptions.InvalidPasswordException;

/**
 * Undo the previous {@code UndoableCommand}.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String COMMAND_ALIAS = "u";
    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "No more commands to undo!";


    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo Previous command.\n "
            + "Example: " + COMMAND_WORD + " " + "pw/AdminPassword";

    private Password toCheck;

    public UndoCommand(Password password) {
        toCheck = password;
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireAllNonNull(model, toCheck, undoRedoStack);

        if (!model.isAdminPassword(toCheck)) {
            throw new InvalidPasswordException();
        }

        if (!undoRedoStack.canUndo()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        undoRedoStack.popUndo().undo();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public void setData(Model model, CommandHistory commandHistory, UndoRedoStack undoRedoStack) {
        this.model = model;
        this.undoRedoStack = undoRedoStack;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UndoCommand // instanceof handles nulls
                && toCheck.equals(((UndoCommand) other).toCheck));
    }
}
