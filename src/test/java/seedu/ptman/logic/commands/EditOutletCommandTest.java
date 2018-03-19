package seedu.ptman.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.ptman.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalPartTimeManager;

import org.junit.Test;

import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.outlet.OperatingHours;
import seedu.ptman.model.outlet.OutletName;


public class EditOutletCommandTest {

    private Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs());

    @Test
    public void execute_allFieldsValid_success() {
        Password masterPassword = new Password();
        OutletName outletName = new OutletName("EditedOutlet");
        OperatingHours operatingHours = new OperatingHours("10:00-18:00");
        EditOutletCommand command = prepareCommand(masterPassword, outletName, operatingHours);
        String expectedMessage = EditOutletCommand.MESSAGE_EDIT_OUTLET_SUCCESS;
        Model expectedModel = new ModelManager(new PartTimeManager(model.getPartTimeManager()), new UserPrefs());
        expectedModel.updateOutlet(outletName, operatingHours);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeUndoRedo_allFieldsValid_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);

        Password masterPassword = new Password();
        OutletName outletName = new OutletName("EditedOutlet");
        OperatingHours operatingHours = new OperatingHours("10:00-18:00");
        EditOutletCommand command = prepareCommand(masterPassword, outletName, operatingHours);
        Model expectedModel = new ModelManager(new PartTimeManager(model.getPartTimeManager()), new UserPrefs());

        // edit -> outlet edited
        command.execute();
        undoRedoStack.push(command);

        //set Admin Mode
        model.setTrueAdminMode(new Password());
        // undo -> reverts parttimemanager back to previous state
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first employee edited again
        expectedModel.updateOutlet(outletName, operatingHours);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        Password masterPassword = new Password();
        OutletName outletName = new OutletName("EditedOutlet");
        OperatingHours operatingHours = new OperatingHours("10:00-18:00");
        final EditOutletCommand standardCommand = prepareCommand(masterPassword, outletName, operatingHours);

        // same values -> returns true
        Password samePassword = new Password();
        OutletName sameName = new OutletName("EditedOutlet");
        OperatingHours sameOperatingHours = new OperatingHours("10:00-18:00");
        EditOutletCommand commandWithSameValues = prepareCommand(samePassword, sameName, sameOperatingHours);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        Password differentPassword = new Password("different");
        OutletName differentName = new OutletName("different");
        OperatingHours differentOperatingHours = new OperatingHours("09:00-10:00");

        // different password -> returns false
        assertFalse(standardCommand.equals(new EditOutletCommand(differentPassword, outletName, operatingHours)));

        // different outlet name -> returns false
        assertFalse(standardCommand.equals(new EditOutletCommand(masterPassword, differentName, operatingHours)));

        // different operating hours -> returns false
        assertFalse(standardCommand.equals(new EditOutletCommand(masterPassword, outletName,
                differentOperatingHours)));
    }

    /**
     * Returns an {@code EditOutletCommand}
     */
    private EditOutletCommand prepareCommand(Password masterPassword, OutletName outletName,
                                             OperatingHours operatingHours) {
        EditOutletCommand editOutletCommand = new EditOutletCommand(masterPassword, outletName, operatingHours);
        editOutletCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editOutletCommand;
    }
}
