package seedu.ptman.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.logic.commands.ApplyCommand.MESSAGE_DUPLICATE_EMPLOYEE;
import static seedu.ptman.logic.commands.ApplyCommand.MESSAGE_SHIFT_FULL;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandFailure;
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
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.exceptions.DuplicateShiftException;
import seedu.ptman.model.shift.exceptions.ShiftFullException;
import seedu.ptman.testutil.Assert;
import seedu.ptman.testutil.EmployeeBuilder;
import seedu.ptman.testutil.ShiftBuilder;

//@@author shanwpf
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

    @Before
    public void showAllShifts() {
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
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
        expectedModel.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);

        Shift editedShift = new Shift(MONDAY_AM);
        editedShift.addEmployee(ALICE);
        expectedModel.updateShift(model.getFilteredShiftList().get(0), editedShift);
        assertCommandSuccess(applyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_shiftFull_throwsCommandException()
            throws ShiftFullException, DuplicateEmployeeException, DuplicateShiftException {
        Model model = new ModelManager();
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        Employee employee1 = new EmployeeBuilder().withName("first").build();
        Employee employee2 = new EmployeeBuilder().withName("second").build();
        Shift shift = new ShiftBuilder().withCapacity("1").build();
        shift.addEmployee(employee1);
        model.addEmployee(employee1);
        model.addEmployee(employee2);
        model.addShift(shift);
        String expectedMessage = String.format(MESSAGE_SHIFT_FULL, INDEX_FIRST_SHIFT.getOneBased());
        ApplyCommand applyCommand = prepareCommand(INDEX_SECOND_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertCommandFailure(applyCommand, model, expectedMessage);
    }

    @Test
    public void execute_duplicateEmployee_throwsCommandException()
            throws ShiftFullException, DuplicateEmployeeException, DuplicateShiftException {
        Model model = new ModelManager();
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        Employee employee1 = new EmployeeBuilder().withName("first").build();
        Shift shift = new ShiftBuilder().withCapacity("2").build();
        shift.addEmployee(employee1);
        model.addEmployee(employee1);
        model.addShift(shift);
        ApplyCommand applyCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertCommandFailure(applyCommand, model, MESSAGE_DUPLICATE_EMPLOYEE);
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
