package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.ptman.model.Model.PREDICATE_SHOW_ALL_EMPLOYEES;

import seedu.ptman.commons.core.EventsCenter;
import seedu.ptman.commons.events.ui.AnnouncementChangedEvent;
import seedu.ptman.commons.events.ui.OutletInformationChangedEvent;
import seedu.ptman.commons.events.ui.OutletNameChangedEvent;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.ReadOnlyPartTimeManager;
import seedu.ptman.model.outlet.OutletInformation;

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
        OutletInformation previousOutlet = previousPartTimeManager.getOutletInformation();
        EventsCenter.getInstance().post(new OutletInformationChangedEvent(
                previousOutlet.getOperatingHours().getDisplayedMessage(),
                previousOutlet.getOutletContact().toString(), previousOutlet.getOutletEmail().toString()));
        EventsCenter.getInstance().post(new OutletNameChangedEvent(previousOutlet.getName().toString()));
        EventsCenter.getInstance().post(new AnnouncementChangedEvent(previousOutlet.getAnnouncement().toString()));
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
