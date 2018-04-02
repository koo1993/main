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

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.logic.commands.exceptions.InvalidPasswordException;
import seedu.ptman.logic.commands.exceptions.MissingPasswordException;
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
    public void execute_userModeEmployeeNotInShift_success() throws Exception {
        Employee employee = new EmployeeBuilder().build();
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);

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
    public void execute_userModeNoPassword_throwsMissingPasswordException() throws Exception {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        model.setFalseAdminMode();
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        Employee employee = new EmployeeBuilder().withName("Present").build();
        Shift shift = new ShiftBuilder().build();
        model.addEmployee(employee);
        model.addShift(shift);
        ApplyCommand applyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        thrown.expect(MissingPasswordException.class);
        applyCommand.execute();
    }

    @Test
    public void execute_userModeIncorrectPassword_throwsInvalidPasswordException() throws Exception {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        model.setFalseAdminMode();
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        Employee employee = new EmployeeBuilder().withName("Present").withPassword("incorrect").build();
        Shift shift = new ShiftBuilder().build();
        model.addEmployee(employee);
        model.addShift(shift);
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        thrown.expect(InvalidPasswordException.class);
        applyCommand.execute();
    }

    @Test
    public void execute_adminModeEmployeeNotInShift_success() throws Exception {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        Model expectedModel = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        expectedModel.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        model.setTrueAdminMode(new Password());
        expectedModel.setTrueAdminMode(new Password());
        Employee employee = new EmployeeBuilder().withName("Present").build();
        Employee expectedEmployee = new EmployeeBuilder().withName("Present").build();
        Shift shift = new ShiftBuilder().build();
        Shift expectedShift = new ShiftBuilder().build();
        model.addEmployee(employee);
        model.addShift(shift);
        expectedShift.addEmployee(expectedEmployee);
        expectedModel.addShift(expectedShift);
        expectedModel.addEmployee(expectedEmployee);
        ApplyCommand applyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);

        String expectedMessage =
                String.format(ApplyCommand.MESSAGE_APPLY_SHIFT_SUCCESS, expectedEmployee.getName(), 1);

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
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_SECOND_EMPLOYEE, INDEX_FIRST_SHIFT, model);
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
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertCommandFailure(applyCommand, model, MESSAGE_DUPLICATE_EMPLOYEE);
    }


    @Test
    public void equals_sameObject_returnsTrue() {
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertTrue(applyCommand.equals(applyCommand));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        ApplyCommand applyCommand1 = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        ApplyCommand applyCommand2 = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertTrue(applyCommand1.equals(applyCommand2));
    }

    @Test
    public void equals_differentTypes_returnsFalse() {
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(applyCommand.equals(1));
    }

    @Test
    public void equals_null_returnsFalse() {
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(applyCommand.equals(null));
    }

    @Test
    public void equals_differentShifts_returnsFalse() {
        ApplyCommand applyCommandFirst = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        ApplyCommand applyCommandSecond = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_SECOND_SHIFT, model);
        assertFalse(applyCommandFirst.equals(applyCommandSecond));
    }

    @Test
    public void equals_differentEmployees_returnsFalse() {
        ApplyCommand applyCommandFirst = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        ApplyCommand applyCommandSecond = prepareCommandWithPassword(INDEX_SECOND_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(applyCommandFirst.equals(applyCommandSecond));
    }

    @Test
    public void execute_shiftIndexOutOfRange_throwsCommandException() {
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, Index.fromOneBased(99), model);
        Assert.assertThrows(CommandException.class, applyCommand::execute);
    }

    @Test
    public void execute_employeeIndexOutOfRange_throwsCommandException() {
        ApplyCommand applyCommand = prepareCommandWithPassword(Index.fromOneBased(99), INDEX_FIRST_SHIFT, model);
        Assert.assertThrows(CommandException.class, applyCommand::execute);
    }

    @Test
    public void execute_incorrectPassword_throwsInvalidPasswordException() {
        model.setFalseAdminMode();
        ApplyCommand applyCommand = new ApplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT,
                Optional.of(new Password("wrongPassword")));
        applyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        Assert.assertThrows(InvalidPasswordException.class, applyCommand::execute);
    }

    @After
    public void reset() {
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
    }

    /**
     * Returns an {@code ApplyCommand} with parameters {@code employeeIndex} and {@code shiftIndex}
     * and a valid employee password
     */
    private ApplyCommand prepareCommandWithPassword(Index employeeIndex, Index shiftIndex, Model model) {
        ApplyCommand applyCommand = new ApplyCommand(employeeIndex, shiftIndex, Optional.of(new Password()));
        applyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return applyCommand;
    }

    /**
     * Returns an {@code ApplyCommand} with parameters {@code employeeIndex} and {@code shiftIndex}
     * without a password
     */
    private ApplyCommand prepareCommandWithoutPassword(Index employeeIndex, Index shiftIndex, Model model) {
        ApplyCommand applyCommand = new ApplyCommand(employeeIndex, shiftIndex, Optional.empty());
        applyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return applyCommand;
    }
}
