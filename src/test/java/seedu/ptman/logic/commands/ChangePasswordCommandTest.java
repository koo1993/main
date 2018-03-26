package seedu.ptman.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalPartTimeManager;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_SECOND_EMPLOYEE;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.logic.commands.exceptions.InvalidPasswordException;
import seedu.ptman.logic.parser.ParserUtil;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.employee.Address;
import seedu.ptman.model.employee.Email;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.Name;
import seedu.ptman.model.employee.Phone;
import seedu.ptman.model.employee.Salary;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code ChangePasswordCommand}.
 */
public class ChangePasswordCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(), new OutletInformation());


    @Test
    public void execute_validInputs_success() throws Exception {
        Employee employeeToEdit = model.getFilteredEmployeeList().get(INDEX_FIRST_EMPLOYEE.getZeroBased());
        ArrayList<String> passwords = new ArrayList<>();
        passwords.add("DEFAULT1");
        passwords.add("DEFAULT2");
        passwords.add("DEFAULT2");

        ChangePasswordCommand changePwCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, passwords);

        String expectedMessage = String.format(ChangePasswordCommand.MESSAGE_SUCCESS, employeeToEdit.getName());

        ModelManager expectedModel = new ModelManager(model.getPartTimeManager(),
                new UserPrefs(), new OutletInformation());
        expectedModel.updateEmployee(employeeToEdit, createNewPasswordEmployee(employeeToEdit,
                ParserUtil.parsePassword(passwords.get(1))));

        assertCommandSuccess(changePwCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPassword_throwsInvalidPasswordException() throws Exception {
        ArrayList<String> passwords = new ArrayList<>();
        passwords.add("wrongpassword");
        passwords.add("DEFAULT2");
        passwords.add("DEFAULT2");
        thrown.expect(InvalidPasswordException.class);
        prepareCommand(INDEX_FIRST_EMPLOYEE, passwords).execute();
    }


    @Test
    public void execute_unmatchedNewPassword_throwsCommandException() throws Exception {
        ArrayList<String> passwords = new ArrayList<>();
        passwords.add("DEFAULT1");
        passwords.add("DEFAULT3");
        passwords.add("DEFAULT4");

        ChangePasswordCommand changePwCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, passwords);
        assertCommandFailure(changePwCommand, model, ChangePasswordCommand.MESSAGE_INVALID_CONFIMREDPASSWORD);
    }

    @Test
    public void equals() throws Exception {
        ArrayList<String> passwords = new ArrayList<>();
        passwords.add("DEFAULT1");
        passwords.add("DEFAULT2");
        passwords.add("DEFAULT2");
        ChangePasswordCommand changePwFirstCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, passwords);
        ChangePasswordCommand changePwSecondCommand = prepareCommand(INDEX_SECOND_EMPLOYEE, passwords);

        // same object -> returns true
        assertTrue(changePwFirstCommand.equals(changePwFirstCommand));

        // same values -> returns true
        ChangePasswordCommand changePwFirstCommandCopy = prepareCommand(INDEX_FIRST_EMPLOYEE, passwords);
        assertTrue(changePwFirstCommand.equals(changePwFirstCommandCopy));

        // different types -> returns false
        assertFalse(changePwFirstCommandCopy.equals(1));

        // null -> returns false
        assertFalse(changePwFirstCommandCopy.equals(null));

        // different employee -> returns false
        assertFalse(changePwFirstCommand.equals(changePwSecondCommand));
    }




    /**
     * Returns a {@code ChangePasswordCommand} with the parameter {@code index} and {@code passwords}.
     */
    private ChangePasswordCommand prepareCommand(Index index, ArrayList<String> passwords) {
        ChangePasswordCommand changePwCommand = new ChangePasswordCommand(index, passwords);
        changePwCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return changePwCommand;
    }


    /**
     * Create new employee with new password.
     * @param employeeToEdit
     * @param password
     * @return
     */
    public Employee createNewPasswordEmployee(Employee employeeToEdit,
                                     Password password) {
        assert employeeToEdit != null;

        Name name = employeeToEdit.getName();
        Phone phone = employeeToEdit.getPhone();
        Email email = employeeToEdit.getEmail();
        Address address = employeeToEdit.getAddress();
        Salary salary = employeeToEdit.getSalary();
        Set<Tag> tags = employeeToEdit.getTags();
        Password updatedPassword = password;
        return new Employee(name, phone, email, address, salary, updatedPassword, tags);
    }
}
