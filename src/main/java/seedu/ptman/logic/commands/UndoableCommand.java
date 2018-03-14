package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.ptman.model.Model.PREDICATE_SHOW_ALL_EMPLOYEES;

import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.ReadOnlyPartTimeManager;

/**
 * Represents a command which can be undone and redone.
 */
public abstract class UndoableCommand extends Command {
    private ReadOnlyPartTimeManager previousPartTimeManager;

    protected abstract CommandResult executeUndoableCommand() throws CommandException;

    /**
     * Stores the current state of {@code model#partTimeManager}.
     */
    private void savePartTimeManagerSnapshot() {
        requireNonNull(model);
        this.previousPartTimeManager = new PartTimeManager(model.getPartTimeManager());
    }

    /**
     * This method is called before the execution of {@code UndoableCommand}.
     * {@code UndoableCommand}s that require this preprocessing step should override this method.
     */
    protected void preprocessUndoableCommand() throws CommandException {}

    /**
     * Reverts the PartTimeManager to the state before this command
     * was executed and updates the filtered employee list to
     * show all employees.
     */
    protected final void undo() {
        requireAllNonNull(model, previousPartTimeManager);
        model.resetData(previousPartTimeManager);
        model.updateFilteredEmployeeList(PREDICATE_SHOW_ALL_EMPLOYEES);
    }

    /**
     * Executes the command and updates the filtered employee
     * list to show all employees.
     */
    protected final void redo() {
        requireNonNull(model);
        try {
            executeUndoableCommand();
        } catch (CommandException ce) {
            throw new AssertionError("The command has been successfully executed previously; "
                    + "it should not fail now");
        }
        model.updateFilteredEmployeeList(PREDICATE_SHOW_ALL_EMPLOYEES);
    }

    @Override
    public final CommandResult execute() throws CommandException {
        savePartTimeManagerSnapshot();
        preprocessUndoableCommand();
        return executeUndoableCommand();
    }
}
