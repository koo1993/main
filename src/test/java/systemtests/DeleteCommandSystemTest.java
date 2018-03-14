package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX;
import static seedu.ptman.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.ptman.logic.commands.DeleteCommand.MESSAGE_DELETE_EMPLOYEE_SUCCESS;
import static seedu.ptman.testutil.TestUtil.getEmployee;
import static seedu.ptman.testutil.TestUtil.getLastIndex;
import static seedu.ptman.testutil.TestUtil.getMidIndex;
import static seedu.ptman.testutil.TypicalEmployees.KEYWORD_MATCHING_MEIER;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;

import org.junit.Test;

import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.commands.DeleteCommand;
import seedu.ptman.logic.commands.RedoCommand;
import seedu.ptman.logic.commands.UndoCommand;
import seedu.ptman.model.Model;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;

public class DeleteCommandSystemTest extends PartTimeManagerSystemTest {

    private static final String MESSAGE_INVALID_DELETE_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);

    @Test
    public void delete() {
        /* ----------------- Performing delete operation while an unfiltered list is being shown -------------------- */

        /* Case: delete the first employee in the list, command with leading spaces and trailing spaces -> deleted */
        Model expectedModel = getModel();
        String command =
                "     " + DeleteCommand.COMMAND_WORD + "      " + INDEX_FIRST_EMPLOYEE.getOneBased() + "       ";
        Employee deletedEmployee = removeEmployee(expectedModel, INDEX_FIRST_EMPLOYEE);
        String expectedResultMessage = String.format(MESSAGE_DELETE_EMPLOYEE_SUCCESS, deletedEmployee);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: delete the last employee in the list -> deleted */
        Model modelBeforeDeletingLast = getModel();
        Index lastEmployeeIndex = getLastIndex(modelBeforeDeletingLast);
        assertCommandSuccess(lastEmployeeIndex);

        /* Case: undo deleting the last employee in the list -> last employee restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: redo deleting the last employee in the list -> last employee deleted again */
        command = RedoCommand.COMMAND_WORD;
        removeEmployee(modelBeforeDeletingLast, lastEmployeeIndex);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: delete the middle employee in the list -> deleted */
        Index middleEmployeeIndex = getMidIndex(getModel());
        assertCommandSuccess(middleEmployeeIndex);

        /* ------------------ Performing delete operation while a filtered list is being shown ---------------------- */

        /* Case: filtered employee list, delete index within bounds of ptman book and employee list -> deleted */
        showEmployeesWithName(KEYWORD_MATCHING_MEIER);
        Index index = INDEX_FIRST_EMPLOYEE;
        assertTrue(index.getZeroBased() < getModel().getFilteredEmployeeList().size());
        assertCommandSuccess(index);

        /* Case: filtered employee list, delete index within bounds of ptman book but out of bounds of employee list
         * -> rejected
         */
        showEmployeesWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getPartTimeManager().getEmployeeList().size();
        command = DeleteCommand.COMMAND_WORD + " " + invalidIndex;
        assertCommandFailure(command, MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        /* ------------------- Performing delete operation while a employee card is selected ---------------------- */

        /* Case: delete the selected employee -> employee list panel selects the employee before the deleted employee */
        showAllEmployees();
        expectedModel = getModel();
        Index selectedIndex = getLastIndex(expectedModel);
        Index expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectEmployee(selectedIndex);
        command = DeleteCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        deletedEmployee = removeEmployee(expectedModel, selectedIndex);
        expectedResultMessage = String.format(MESSAGE_DELETE_EMPLOYEE_SUCCESS, deletedEmployee);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

        /* --------------------------------- Performing invalid delete operation ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getPartTimeManager().getEmployeeList().size() + 1);
        command = DeleteCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " 1 abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("DelETE 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Employee} at the specified {@code index} in {@code model}'s ptman book.
     * @return the removed employee
     */
    private Employee removeEmployee(Model model, Index index) {
        Employee targetEmployee = getEmployee(model, index);
        try {
            model.deleteEmployee(targetEmployee);
        } catch (EmployeeNotFoundException pnfe) {
            throw new AssertionError("targetEmployee is retrieved from model.");
        }
        return targetEmployee;
    }

    /**
     * Deletes the employee at {@code toDelete} by creating a default {@code DeleteCommand} using {@code toDelete} and
     * performs the same verification as {@code assertCommandSuccess(String, Model, String)}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertCommandSuccess(Index toDelete) {
        Model expectedModel = getModel();
        Employee deletedEmployee = removeEmployee(expectedModel, toDelete);
        String expectedResultMessage = String.format(MESSAGE_DELETE_EMPLOYEE_SUCCESS, deletedEmployee);

        assertCommandSuccess(
                DeleteCommand.COMMAND_WORD + " " + toDelete.getOneBased(), expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card remains unchanged.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code PartTimeManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see PartTimeManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)} except that the browser url
     * and selected card are expected to update accordingly depending on the card at {@code expectedSelectedCardIndex}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     * @see PartTimeManagerSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarChangedExceptSaveLocation();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code PartTimeManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see PartTimeManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxAndResultDisplayShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
