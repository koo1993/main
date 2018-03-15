package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.ptman.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.DEFAULT_DESC_ADMINPASSWORD;
import static seedu.ptman.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.ptman.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_SALARY_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.ptman.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.ptman.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.SALARY_DESC_AMY;
import static seedu.ptman.logic.commands.CommandTestUtil.SALARY_DESC_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.ptman.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.ptman.model.Model.PREDICATE_SHOW_ALL_EMPLOYEES;
import static seedu.ptman.testutil.TypicalEmployees.AMY;
import static seedu.ptman.testutil.TypicalEmployees.BOB;
import static seedu.ptman.testutil.TypicalEmployees.KEYWORD_MATCHING_MEIER;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;

import org.junit.Test;

import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.commands.EditCommand;
import seedu.ptman.logic.commands.RedoCommand;
import seedu.ptman.logic.commands.UndoCommand;
import seedu.ptman.model.Model;
import seedu.ptman.model.employee.Address;
import seedu.ptman.model.employee.Email;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.Name;
import seedu.ptman.model.employee.Phone;
import seedu.ptman.model.employee.Salary;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.tag.Tag;
import seedu.ptman.testutil.EmployeeBuilder;
import seedu.ptman.testutil.EmployeeUtil;

public class EditCommandSystemTest extends PartTimeManagerSystemTest {

    @Test
    public void edit() throws Exception {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each field
         * -> edited
         */
        Index index = INDEX_FIRST_EMPLOYEE;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + NAME_DESC_BOB + "  "
                + PHONE_DESC_BOB + " " + EMAIL_DESC_BOB + "  " + ADDRESS_DESC_BOB + " " + TAG_DESC_HUSBAND + " "
                + DEFAULT_DESC_ADMINPASSWORD;
        Employee editedEmployee = new EmployeeBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertCommandSuccess(command, index, editedEmployee);

        /* Case: undo editing the last employee in the list -> last employee restored */
        command = UndoCommand.COMMAND_WORD + DEFAULT_DESC_ADMINPASSWORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last employee in the list -> last employee edited again */
        command = RedoCommand.COMMAND_WORD + DEFAULT_DESC_ADMINPASSWORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updateEmployee(
                getModel().getFilteredEmployeeList().get(INDEX_FIRST_EMPLOYEE.getZeroBased()), editedEmployee);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit a employee with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + DEFAULT_DESC_ADMINPASSWORD;
        assertCommandSuccess(command, index, BOB);

        /* Case: edit some fields -> edited */
        index = INDEX_FIRST_EMPLOYEE;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + TAG_DESC_FRIEND + DEFAULT_DESC_ADMINPASSWORD;
        Employee employeeToEdit = getModel().getFilteredEmployeeList().get(index.getZeroBased());
        editedEmployee = new EmployeeBuilder(employeeToEdit).withTags(VALID_TAG_FRIEND).build();
        assertCommandSuccess(command, index, editedEmployee);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_EMPLOYEE;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix()
                + DEFAULT_DESC_ADMINPASSWORD;
        editedEmployee = new EmployeeBuilder(employeeToEdit).withTags().build();
        assertCommandSuccess(command, index, editedEmployee);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered employee list, edit index within bounds of ptman book and employee list -> edited */
        showEmployeesWithName(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_EMPLOYEE;
        assertTrue(index.getZeroBased() < getModel().getFilteredEmployeeList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_BOB
                + DEFAULT_DESC_ADMINPASSWORD;
        employeeToEdit = getModel().getFilteredEmployeeList().get(index.getZeroBased());
        editedEmployee = new EmployeeBuilder(employeeToEdit).withName(VALID_NAME_BOB).build();
        assertCommandSuccess(command, index, editedEmployee);

        /* Case: filtered employee list, edit index within bounds of ptman book but out of bounds of employee list
         * -> rejected
         */
        showEmployeesWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getPartTimeManager().getEmployeeList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB + DEFAULT_DESC_ADMINPASSWORD,
                Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        /* ------------------- Performing edit operation while a employee card is selected ------------------------ */

        /* Case: selects first card in the employee list, edit a employee -> edited,
         * card selection remains unchanged but browser url changes
         */
        showAllEmployees();
        index = INDEX_FIRST_EMPLOYEE;
        selectEmployee(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + SALARY_DESC_AMY + TAG_DESC_FRIEND + DEFAULT_DESC_ADMINPASSWORD;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new employee's name
        assertCommandSuccess(command, index, AMY, index);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_BOB + DEFAULT_DESC_ADMINPASSWORD,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_BOB + DEFAULT_DESC_ADMINPASSWORD,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredEmployeeList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB + DEFAULT_DESC_ADMINPASSWORD,
                Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_BOB + DEFAULT_DESC_ADMINPASSWORD,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_EMPLOYEE.getOneBased()
                        + DEFAULT_DESC_ADMINPASSWORD, EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_EMPLOYEE.getOneBased()
                        + INVALID_NAME_DESC + DEFAULT_DESC_ADMINPASSWORD, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_EMPLOYEE.getOneBased()
                        + INVALID_PHONE_DESC + DEFAULT_DESC_ADMINPASSWORD, Phone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_EMPLOYEE.getOneBased()
                        + INVALID_EMAIL_DESC + DEFAULT_DESC_ADMINPASSWORD, Email.MESSAGE_EMAIL_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_EMPLOYEE.getOneBased()
                        + INVALID_ADDRESS_DESC + DEFAULT_DESC_ADMINPASSWORD, Address.MESSAGE_ADDRESS_CONSTRAINTS);

        /* Case: invalid salary -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_EMPLOYEE.getOneBased()
                        + INVALID_SALARY_DESC + DEFAULT_DESC_ADMINPASSWORD, Salary.MESSAGE_SALARY_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_EMPLOYEE.getOneBased()
                        + INVALID_TAG_DESC + DEFAULT_DESC_ADMINPASSWORD, Tag.MESSAGE_TAG_CONSTRAINTS);

        /* Case: edit a employee with new values same as another employee's values -> rejected */
        executeCommand(EmployeeUtil.getAddCommand(BOB));
        assertTrue(getModel().getPartTimeManager().getEmployeeList().contains(BOB));
        index = INDEX_FIRST_EMPLOYEE;
        assertFalse(getModel().getFilteredEmployeeList().get(index.getZeroBased()).equals(BOB));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + SALARY_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + DEFAULT_DESC_ADMINPASSWORD;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_EMPLOYEE);

        /* Case: edit a employee with new values same as another employee's values
         * but with different tags -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + SALARY_DESC_BOB + TAG_DESC_HUSBAND + DEFAULT_DESC_ADMINPASSWORD;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_EMPLOYEE);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Employee, Index)} except that
     * the browser url and selected card remain unchanged.
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Employee, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Employee editedEmployee) {
        assertCommandSuccess(command, toEdit, editedEmployee, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the employee at index {@code toEdit} being
     * updated to values specified {@code editedEmployee}.<br>
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Employee editedEmployee,
            Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        try {
            expectedModel.updateEmployee(
                    expectedModel.getFilteredEmployeeList().get(toEdit.getZeroBased()), editedEmployee);
            expectedModel.updateFilteredEmployeeList(PREDICATE_SHOW_ALL_EMPLOYEES);
        } catch (DuplicateEmployeeException | EmployeeNotFoundException e) {
            throw new IllegalArgumentException(
                    "editedEmployee is a duplicate in expectedModel, or it isn't found in the model.");
        }

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_EMPLOYEE_SUCCESS, editedEmployee), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code PartTimeManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see PartTimeManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see PartTimeManagerSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredEmployeeList(PREDICATE_SHOW_ALL_EMPLOYEES);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
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
