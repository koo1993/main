package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;

import java.util.List;
import java.util.Objects;

import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.exceptions.ShiftNotFoundException;

//@@author shanwpf
/**
 * Deletes a shift identified using it's last displayed index in the timetable.
 */
public class DeleteShiftCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deleteshift";
    public static final String COMMAND_ALIAS = "ds";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the shift identified by the index number used in the timetable.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_SHIFT_SUCCESS = "Deleted Shift: %1$s";

    private final Index targetIndex;

    private Shift shiftToDelete;

    public DeleteShiftCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(shiftToDelete);

        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }

        try {
            model.deleteShift(shiftToDelete);
        } catch (ShiftNotFoundException pnfe) {
            throw new AssertionError("The target shift cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_SHIFT_SUCCESS, shiftToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Shift> lastShownList = model.getFilteredShiftList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }

        shiftToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteShiftCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteShiftCommand) other).targetIndex) // state check
                && Objects.equals(this.shiftToDelete, ((DeleteShiftCommand) other).shiftToDelete));
    }
}
