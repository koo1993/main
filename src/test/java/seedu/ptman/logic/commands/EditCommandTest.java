package seedu.ptman.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.ptman.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.ptman.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.ptman.logic.commands.CommandTestUtil.showEmployeeAtIndex;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalPartTimeManager;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_SECOND_EMPLOYEE;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;

import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.logic.commands.EditCommand.EditEmployeeDescriptor;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.testutil.EditEmployeeDescriptorBuilder;
import seedu.ptman.testutil.EmployeeBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs());

    @Before
    public void setMode_adminMode() {
        model.setTrueAdminMode(new Password());
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Employee editedEmployee = new EmployeeBuilder().build();
        EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder(editedEmployee).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EMPLOYEE_SUCCESS, editedEmployee);

        Model expectedModel = new ModelManager(new PartTimeManager(model.getPartTimeManager()), new UserPrefs());
        expectedModel.updateEmployee(model.getFilteredEmployeeList().get(0), editedEmployee);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastEmployee = Index.fromOneBased(model.getFilteredEmployeeList().size());
        Employee lastEmployee = model.getFilteredEmployeeList().get(indexLastEmployee.getZeroBased());

        EmployeeBuilder employeeInList = new EmployeeBuilder(lastEmployee);
        Employee editedEmployee = employeeInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = prepareCommand(indexLastEmployee, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EMPLOYEE_SUCCESS, editedEmployee);

        Model expectedModel = new ModelManager(new PartTimeManager(model.getPartTimeManager()), new UserPrefs());
        expectedModel.updateEmployee(lastEmployee, editedEmployee);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, new EditEmployeeDescriptor());
        Employee editedEmployee = model.getFilteredEmployeeList().get(INDEX_FIRST_EMPLOYEE.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EMPLOYEE_SUCCESS, editedEmployee);

        Model expectedModel = new ModelManager(new PartTimeManager(model.getPartTimeManager()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showEmployeeAtIndex(model, INDEX_FIRST_EMPLOYEE);

        Employee employeeInFilteredList = model.getFilteredEmployeeList().get(INDEX_FIRST_EMPLOYEE.getZeroBased());
        Employee editedEmployee = new EmployeeBuilder(employeeInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_EMPLOYEE,
                new EditEmployeeDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_EMPLOYEE_SUCCESS, editedEmployee);

        Model expectedModel = new ModelManager(new PartTimeManager(model.getPartTimeManager()), new UserPrefs());
        expectedModel.updateEmployee(model.getFilteredEmployeeList().get(0), editedEmployee);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateEmployeeUnfilteredList_failure() {
        Employee firstEmployee = model.getFilteredEmployeeList().get(INDEX_FIRST_EMPLOYEE.getZeroBased());
        EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder(firstEmployee).build();
        EditCommand editCommand = prepareCommand(INDEX_SECOND_EMPLOYEE, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_EMPLOYEE);
    }

    @Test
    public void execute_duplicateEmployeeFilteredList_failure() {
        showEmployeeAtIndex(model, INDEX_FIRST_EMPLOYEE);

        // edit employee in filtered list into a duplicate in ptman book
        Employee employeeInList =
                model.getPartTimeManager().getEmployeeList().get(INDEX_SECOND_EMPLOYEE.getZeroBased());
        EditCommand editCommand = prepareCommand(INDEX_FIRST_EMPLOYEE,
                new EditEmployeeDescriptorBuilder(employeeInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_EMPLOYEE);
    }

    @Test
    public void execute_invalidEmployeeIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEmployeeList().size() + 1);
        EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
    }

    @Test
    public void execute_notInAdminMode_accessDenied() throws Exception {
        model.setFalseAdminMode();
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEmployeeList().size());
        EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_ACCESS_DENIED);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of ptman book
     */
    @Test
    public void execute_invalidEmployeeIndexFilteredList_failure() {
        showEmployeeAtIndex(model, INDEX_FIRST_EMPLOYEE);
        Index outOfBoundIndex = INDEX_SECOND_EMPLOYEE;
        // ensures that outOfBoundIndex is still in bounds of ptman book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getPartTimeManager().getEmployeeList().size());

        EditCommand editCommand = prepareCommand(outOfBoundIndex,
                new EditEmployeeDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Employee editedEmployee = new EmployeeBuilder().build();
        Employee employeeToEdit = model.getFilteredEmployeeList().get(INDEX_FIRST_EMPLOYEE.getZeroBased());
        EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder(editedEmployee).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, descriptor);
        Model expectedModel = new ModelManager(new PartTimeManager(model.getPartTimeManager()), new UserPrefs());

        // edit -> first employee edited
        editCommand.execute();
        undoRedoStack.push(editCommand);

        // undo -> reverts parttimemanager back to previous state and filtered employee list to show all employees
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first employee edited again
        expectedModel.updateEmployee(employeeToEdit, editedEmployee);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEmployeeList().size() + 1);
        EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> editCommand not pushed into undoRedoStack
        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Employee} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited employee in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the employee object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameEmployeeEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Employee editedEmployee = new EmployeeBuilder().build();
        EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder(editedEmployee).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, descriptor);
        Model expectedModel = new ModelManager(new PartTimeManager(model.getPartTimeManager()), new UserPrefs());

        showEmployeeAtIndex(model, INDEX_SECOND_EMPLOYEE);
        Employee employeeToEdit = model.getFilteredEmployeeList().get(INDEX_FIRST_EMPLOYEE.getZeroBased());
        // edit -> edits second employee in unfiltered employee list / first employee in filtered employee list
        editCommand.execute();
        undoRedoStack.push(editCommand);

        // undo -> reverts parttimemanager back to previous state and filtered employee list to show all employees
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updateEmployee(employeeToEdit, editedEmployee);
        assertNotEquals(model.getFilteredEmployeeList().get(INDEX_FIRST_EMPLOYEE.getZeroBased()), employeeToEdit);
        // redo -> edits same second employee in unfiltered employee list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        final EditCommand standardCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, DESC_AMY);

        // same values -> returns true
        EditEmployeeDescriptor copyDescriptor = new EditEmployeeDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = prepareCommand(INDEX_FIRST_EMPLOYEE, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_EMPLOYEE, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_EMPLOYEE, DESC_BOB)));
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditCommand prepareCommand(Index index, EditEmployeeDescriptor descriptor) {
        EditCommand editCommand = new EditCommand(index, descriptor);
        editCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editCommand;
    }
}
