package seedu.ptman.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.ptman.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_OUT_OF_BOUNDS_SHIFT;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;
import static seedu.ptman.testutil.TypicalShifts.getTypicalPartTimeManagerWithShifts;

import org.junit.Before;
import org.junit.Test;

import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.Shift;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteShiftCommandTest {

    private Model model = new ModelManager(getTypicalPartTimeManagerWithShifts(), new UserPrefs(),
            new OutletInformation());

    @Before
    public void setupAdminMode() {
        model.setTrueAdminMode(new Password());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Shift shiftToDelete = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());
        DeleteShiftCommand deleteShiftCommand = prepareCommand(INDEX_FIRST_SHIFT);

        String expectedMessage = String.format(DeleteShiftCommand.MESSAGE_DELETE_SHIFT_SUCCESS, shiftToDelete);

        ModelManager expectedModel = new ModelManager(model.getPartTimeManager(), new UserPrefs(),
                new OutletInformation());
        expectedModel.deleteShift(shiftToDelete);

        assertCommandSuccess(deleteShiftCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        DeleteShiftCommand deleteShiftCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteShiftCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        Shift shiftToDelete = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());
        DeleteShiftCommand deleteShiftCommand = prepareCommand(INDEX_FIRST_SHIFT);

        String expectedMessage = String.format(DeleteShiftCommand.MESSAGE_DELETE_SHIFT_SUCCESS, shiftToDelete);

        Model expectedModel = new ModelManager(model.getPartTimeManager(), new UserPrefs(), new OutletInformation());
        expectedModel.deleteShift(shiftToDelete);
        assertNotEquals(shiftToDelete, expectedModel.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased()));

        assertCommandSuccess(deleteShiftCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        Index outOfBoundIndex = INDEX_OUT_OF_BOUNDS_SHIFT;

        DeleteShiftCommand deleteShiftCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteShiftCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Shift shiftToDelete = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());
        DeleteShiftCommand deleteShiftCommand = prepareCommand(INDEX_FIRST_SHIFT);
        Model expectedModel = new ModelManager(model.getPartTimeManager(), new UserPrefs(), new OutletInformation());

        // delete -> first employee deleted
        deleteShiftCommand.execute();
        undoRedoStack.push(deleteShiftCommand);

        // undo -> reverts parttimemanager back to previous state and filtered employee list to show all employees
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first employee deleted again
        expectedModel.deleteShift(shiftToDelete);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        DeleteShiftCommand deleteShiftCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> deleteCommand not pushed into undoRedoStack
        assertCommandFailure(deleteShiftCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() throws Exception {
        DeleteShiftCommand deleteFirstCommand = prepareCommand(INDEX_FIRST_SHIFT);
        DeleteShiftCommand deleteSecondCommand = prepareCommand(INDEX_SECOND_SHIFT);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteShiftCommand deleteFirstCommandCopy = prepareCommand(INDEX_FIRST_SHIFT);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        deleteFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different employee -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteShiftCommand prepareCommand(Index index) {
        DeleteShiftCommand deleteShiftCommand = new DeleteShiftCommand(index);
        deleteShiftCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteShiftCommand;
    }
}
