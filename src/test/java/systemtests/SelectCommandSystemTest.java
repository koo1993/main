package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX;
import static seedu.ptman.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.ptman.logic.commands.CommandTestUtil.DEFAULT_DESC_ADMINPASSWORD;
import static seedu.ptman.logic.commands.SelectCommand.MESSAGE_SELECT_EMPLOYEE_SUCCESS;
import static seedu.ptman.testutil.TypicalEmployees.KEYWORD_MATCHING_MEIER;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalEmployees;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;

import org.junit.Test;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.commands.RedoCommand;
import seedu.ptman.logic.commands.SelectCommand;
import seedu.ptman.logic.commands.UndoCommand;
import seedu.ptman.model.Model;

public class SelectCommandSystemTest extends PartTimeManagerSystemTest {
    @Test
    public void select() {
        /* ------------------------ Perform select operations on the shown unfiltered list -------------------------- */

        /* Case: select the first card in the employee list, command with leading spaces and trailing spaces
         * -> selected
         */
        String command = "   " + SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_EMPLOYEE.getOneBased() + "   ";
        assertCommandSuccess(command, INDEX_FIRST_EMPLOYEE);

        /* Case: select the last card in the employee list -> selected */
        Index employeeCount = Index.fromOneBased(getTypicalEmployees().size());
        command = SelectCommand.COMMAND_WORD + " " + employeeCount.getOneBased();
        assertCommandSuccess(command, employeeCount);

        /* Case: undo previous selection -> rejected */
        command = UndoCommand.COMMAND_WORD + DEFAULT_DESC_ADMINPASSWORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo selecting last card in the list -> rejected */
        command = RedoCommand.COMMAND_WORD + DEFAULT_DESC_ADMINPASSWORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: select the middle card in the employee list -> selected */
        Index middleIndex = Index.fromOneBased(employeeCount.getOneBased() / 2);
        command = SelectCommand.COMMAND_WORD + " " + middleIndex.getOneBased();
        assertCommandSuccess(command, middleIndex);

        /* Case: select the current selected card -> selected */
        assertCommandSuccess(command, middleIndex);

        /* ------------------------ Perform select operations on the shown filtered list ---------------------------- */

        /* Case: filtered employee list, select index within bounds of ptman book but out of bounds of employee list
         * -> rejected
         */
        showEmployeesWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getPartTimeManager().getEmployeeList().size();
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + invalidIndex, MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        /* Case: filtered employee list, select index within bounds of ptman book and employee list -> selected */
        Index validIndex = Index.fromOneBased(1);
        assertTrue(validIndex.getZeroBased() < getModel().getFilteredEmployeeList().size());
        command = SelectCommand.COMMAND_WORD + " " + validIndex.getOneBased();
        assertCommandSuccess(command, validIndex);

        /* ----------------------------------- Perform invalid select operations ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + 0,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + -1,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredEmployeeList().size() + 1;
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + invalidIndex, MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(SelectCommand.COMMAND_WORD + " abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(SelectCommand.COMMAND_WORD + " 1 abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("SeLeCt 1", MESSAGE_UNKNOWN_COMMAND);

        /* Case: select from empty ptman book -> rejected */
        deleteAllEmployees();
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_EMPLOYEE.getOneBased(),
                MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing select command with the
     * {@code expectedSelectedCardIndex} of the selected employee.<br>
     * 4. {@code Model}, {@code Storage} and {@code EmployeeListPanel} remain unchanged.<br>
     * 5. Selected card is at {@code expectedSelectedCardIndex} and the browser url is updated accordingly.<br>
     * 6. Status bar remains unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code PartTimeManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see PartTimeManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see PartTimeManagerSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        String expectedResultMessage = String.format(
                MESSAGE_SELECT_EMPLOYEE_SUCCESS, expectedSelectedCardIndex.getOneBased());
        int preExecutionSelectedCardIndex = getEmployeeListPanel().getSelectedCardIndex();

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (preExecutionSelectedCardIndex == expectedSelectedCardIndex.getZeroBased()) {
            assertSelectedCardUnchanged();
        } else {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code EmployeeListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
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
