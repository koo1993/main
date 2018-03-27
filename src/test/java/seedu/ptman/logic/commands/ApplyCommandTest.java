package seedu.ptman.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.testutil.TypicalEmployees.ALICE;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_SECOND_EMPLOYEE;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;
import static seedu.ptman.testutil.TypicalShifts.MONDAY_AM;
import static seedu.ptman.testutil.TypicalShifts.getTypicalPartTimeManagerWithShifts;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.logic.commands.exceptions.InvalidPasswordException;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.Shift;
import seedu.ptman.testutil.Assert;
import seedu.ptman.testutil.EmployeeBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for ApplyCommand.
 */
public class ApplyCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(new PartTimeManager(getTypicalPartTimeManagerWithShifts()), new UserPrefs(),
            new OutletInformation());

    @Before
    public void setMode_adminMode() {
        model.setTrueAdminMode(new Password());
    }

    @Test
    public void execute_employeeNotInShift_success() throws Exception {
        Employee employee = new EmployeeBuilder().build();
        ApplyCommand applyCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);

        String expectedMessage = String.format(ApplyCommand.MESSAGE_APPLY_SHIFT_SUCCESS,
                employee.getName(), INDEX_FIRST_SHIFT.getOneBased());

        Model expectedModel = new ModelManager(new PartTimeManager(model.getPartTimeManager()), new UserPrefs(),
                new OutletInformation());
        expectedModel.setTrueAdminMode(new Password());

        Shift editedShift = new Shift(MONDAY_AM);
        editedShift.addEmployee(ALICE);
        expectedModel.updateShift(model.getFilteredShiftList().get(0), editedShift);
        assertCommandSuccess(applyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        ApplyCommand applyCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertTrue(applyCommand.equals(applyCommand));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        ApplyCommand applyCommand1 = prepareCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        ApplyCommand applyCommand2 = prepareCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertTrue(applyCommand1.equals(applyCommand2));
    }

    @Test
    public void equals_differentTypes_returnsFalse() {
        ApplyCommand applyCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(applyCommand.equals(1));
    }

    @Test
    public void equals_null_returnsFalse() {
        ApplyCommand applyCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(applyCommand.equals(null));
    }

    @Test
    public void equals_differentShifts_returnsFalse() {
        ApplyCommand applyCommandFirst = prepareCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        ApplyCommand applyCommandSecond = prepareCommand(INDEX_FIRST_EMPLOYEE, INDEX_SECOND_SHIFT, model);
        assertFalse(applyCommandFirst.equals(applyCommandSecond));
    }

    @Test
    public void equals_differentEmployees_returnsFalse() {
        ApplyCommand applyCommandFirst = prepareCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        ApplyCommand applyCommandSecond = prepareCommand(INDEX_SECOND_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(applyCommandFirst.equals(applyCommandSecond));
    }

    @Test
    public void execute_shiftIndexOutOfRange_throwsCommandException() {
        ApplyCommand applyCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, Index.fromOneBased(99), model);
        Assert.assertThrows(CommandException.class, applyCommand::execute);
    }

    @Test
    public void execute_employeeIndexOutOfRange_throwsCommandException() {
        ApplyCommand applyCommand = prepareCommand(Index.fromOneBased(99), INDEX_FIRST_SHIFT, model);
        Assert.assertThrows(CommandException.class, applyCommand::execute);
    }

    @Test
    public void execute_incorrectPassword_throwsInvalidPasswordException() {
        ApplyCommand applyCommand = new ApplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT,
                new Password("wrongPassword"));
        applyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        Assert.assertThrows(InvalidPasswordException.class, applyCommand::execute);
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private ApplyCommand prepareCommand(Index employeeIndex, Index shiftIndex, Model model) {
        ApplyCommand applyCommand = new ApplyCommand(employeeIndex, shiftIndex, new Password());
        applyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return applyCommand;
    }
}
