package seedu.ptman.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_SECOND_EMPLOYEE;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_SECOND_SHIFT;
import static seedu.ptman.testutil.TypicalShifts.getTypicalPartTimeManagerWithShifts;

import java.util.Optional;

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
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.testutil.Assert;
import seedu.ptman.testutil.EmployeeBuilder;
import seedu.ptman.testutil.ShiftBuilder;

//@@author shanwpf
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for UnapplyCommand.
 */
public class UnapplyCommandTest {
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
    public void execute_employeeNotInShift_throwsCommandException() throws Exception {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        model.setTrueAdminMode(new Password());
        Employee employee = new EmployeeBuilder().withName("Absent").build();
        Shift shift = new ShiftBuilder().build();
        model.addEmployee(employee);
        model.addShift(shift);
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);

        assertCommandFailure(unapplyCommand, model, UnapplyCommand.MESSAGE_EMPLOYEE_NOT_FOUND);
    }

    @Test
    public void execute_adminModeEmployeeInShift_success() throws Exception {
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
        shift.addEmployee(employee);
        model.addEmployee(employee);
        model.addShift(shift);
        expectedModel.addShift(expectedShift);
        expectedModel.addEmployee(expectedEmployee);
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);

        String expectedMessage =
                String.format(UnapplyCommand.MESSAGE_UNAPPLY_SHIFT_SUCCESS, expectedEmployee.getName(), 1);

        assertCommandSuccess(unapplyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_userModeEmployeeInShift_success() throws Exception {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        Model expectedModel = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        model.setFalseAdminMode();
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        expectedModel.setFalseAdminMode();
        expectedModel.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        Employee employee = new EmployeeBuilder().withName("Present").build();
        Employee expectedEmployee = new EmployeeBuilder().withName("Present").build();
        Shift shift = new ShiftBuilder().build();
        Shift expectedShift = new ShiftBuilder().build();
        shift.addEmployee(employee);
        model.addEmployee(employee);
        model.addShift(shift);
        expectedModel.addShift(expectedShift);
        expectedModel.addEmployee(expectedEmployee);
        UnapplyCommand unapplyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);

        String expectedMessage =
                String.format(UnapplyCommand.MESSAGE_UNAPPLY_SHIFT_SUCCESS, expectedEmployee.getName(), 1);

        assertCommandSuccess(unapplyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_userModeNoPassword_throwsMissingPasswordException() throws Exception {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        model.setFalseAdminMode();
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        Employee employee = new EmployeeBuilder().withName("Present").build();
        Shift shift = new ShiftBuilder().build();
        shift.addEmployee(employee);
        model.addEmployee(employee);
        model.addShift(shift);
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        thrown.expect(MissingPasswordException.class);
        unapplyCommand.execute();
    }

    @Test
    public void execute_userModeIncorrectPassword_throwsInvalidPasswordException() throws Exception {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        model.setFalseAdminMode();
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        Employee employee = new EmployeeBuilder().withName("Present").withPassword("incorrect").build();
        Shift shift = new ShiftBuilder().build();
        shift.addEmployee(employee);
        model.addEmployee(employee);
        model.addShift(shift);
        UnapplyCommand unapplyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        thrown.expect(InvalidPasswordException.class);
        unapplyCommand.execute();
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPw = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertTrue(unapplyCommand.equals(unapplyCommand));
        assertTrue(unapplyCommandPw.equals(unapplyCommandPw));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        UnapplyCommand unapplyCommand1 = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommand2 = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPw1 = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPw2 = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertTrue(unapplyCommand1.equals(unapplyCommand2));
        assertTrue(unapplyCommandPw1.equals(unapplyCommandPw2));
    }

    @Test
    public void equals_differentTypes_returnsFalse() {
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPw = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(unapplyCommand.equals(1));
        assertFalse(unapplyCommandPw.equals(1));
    }

    @Test
    public void equals_null_returnsFalse() {
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPw = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(unapplyCommand.equals(null));
        assertFalse(unapplyCommandPw.equals(null));
    }

    @Test
    public void equals_differentShifts_returnsFalse() {
        UnapplyCommand unapplyCommandFirst =
                prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandSecond =
                prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_SECOND_SHIFT, model);
        UnapplyCommand unapplyCommandPwFirst =
                prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPwSecond =
                prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_SECOND_SHIFT, model);
        assertFalse(unapplyCommandFirst.equals(unapplyCommandSecond));
        assertFalse(unapplyCommandPwFirst.equals(unapplyCommandPwSecond));
    }

    @Test
    public void equals_differentEmployees_returnsFalse() {
        UnapplyCommand unapplyCommandFirst =
                prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandSecond =
                prepareCommandWithoutPassword(INDEX_SECOND_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPwFirst =
                prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPwSecond =
                prepareCommandWithPassword(INDEX_SECOND_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(unapplyCommandFirst.equals(unapplyCommandSecond));
        assertFalse(unapplyCommandPwFirst.equals(unapplyCommandPwSecond));
    }

    @Test
    public void execute_shiftIndexOutOfRange_throwsCommandException() {
        UnapplyCommand unapplyCommand =
                prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, Index.fromOneBased(99), model);
        UnapplyCommand unapplyCommandPw =
                prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, Index.fromOneBased(99), model);
        Assert.assertThrows(CommandException.class, unapplyCommand::execute);
        Assert.assertThrows(CommandException.class, unapplyCommandPw::execute);
    }

    @Test
    public void execute_employeeIndexOutOfRange_throwsCommandException() {
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(Index.fromOneBased(99), INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPw = prepareCommandWithPassword(Index.fromOneBased(99), INDEX_FIRST_SHIFT, model);
        Assert.assertThrows(CommandException.class, unapplyCommand::execute);
        Assert.assertThrows(CommandException.class, unapplyCommandPw::execute);
    }

    @Test
    public void execute_incorrectPassword_throwsInvalidPasswordException() {
        ApplyCommand applyCommand = new ApplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT,
                new Password("wrongPassword"));
        applyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        Assert.assertThrows(InvalidPasswordException.class, applyCommand::execute);
    }

    @Test
    public void hashCode_sameValues_returnsSameHashCode() {
        UnapplyCommand unapplyCommand1 =
                prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommand2 =
                prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertEquals(unapplyCommand1.hashCode(), unapplyCommand2.hashCode());
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private UnapplyCommand prepareCommandWithPassword(Index employeeIndex, Index shiftIndex, Model model) {
        UnapplyCommand unapplyCommand = new UnapplyCommand(employeeIndex, shiftIndex, Optional.of(new Password()));
        unapplyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return unapplyCommand;
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private UnapplyCommand prepareCommandWithoutPassword(Index employeeIndex, Index shiftIndex, Model model) {
        UnapplyCommand unapplyCommand = new UnapplyCommand(employeeIndex, shiftIndex, Optional.empty());
        unapplyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return unapplyCommand;
    }
}
