package seedu.ptman.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalPartTimeManager;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.logic.commands.exceptions.InvalidPasswordException;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.outlet.OutletInformation;

//@@author koo1993
/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code ChangeAdminPasswordCommand}.
 */
public class ChangeAdminPasswordCommandTest {

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

        ChangeAdminPasswordCommand changePwCommand = prepareCommand(passwords);

        String expectedMessage = String.format(ChangeAdminPasswordCommand.MESSAGE_SUCCESS, employeeToEdit.getName());

        ModelManager expectedModel = new ModelManager(model.getPartTimeManager(),
                new UserPrefs(), new OutletInformation());
        expectedModel.setTrueAdminMode(new Password());

        Password newPassword = new Password();
        newPassword.createPassword(passwords.get(1));

        expectedModel.setAdminPassword(newPassword);


        assertCommandSuccess(changePwCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPassword_throwsInvalidPasswordException() throws Exception {
        ArrayList<String> passwords = new ArrayList<>();
        passwords.add("wrongpassword");
        passwords.add("DEFAULT2");
        passwords.add("DEFAULT2");
        thrown.expect(InvalidPasswordException.class);
        prepareCommand(passwords).execute();
    }

    @Test
    public void execute_unmatchedNewPassword_throwsCommandException() throws Exception {
        ArrayList<String> passwords = new ArrayList<>();
        passwords.add("DEFAULT1");
        passwords.add("DEFAULT3");
        passwords.add("DEFAULT4");

        ChangeAdminPasswordCommand changePwCommand = prepareCommand(passwords);
        assertCommandFailure(changePwCommand, model, ChangeAdminPasswordCommand.MESSAGE_INVALID_CONFIMREDPASSWORD);
    }


    @Test
    public void equals() throws Exception {
        ArrayList<String> passwords = new ArrayList<>();
        ArrayList<String> passwords2 = new ArrayList<>();
        passwords.add("DEFAULT1");
        passwords.add("DEFAULT2");
        passwords.add("DEFAULT2");

        passwords2.add("DEFAULT1");
        passwords2.add("DEFAULT3");
        passwords2.add("DEFAULT3");

        ChangeAdminPasswordCommand changePwFirstCommand = prepareCommand(passwords);
        ChangeAdminPasswordCommand changePwSecondCommand = prepareCommand(passwords2);

        // same object -> returns true
        assertTrue(changePwFirstCommand.equals(changePwFirstCommand));

        // same values -> returns true
        ChangeAdminPasswordCommand changePwFirstCommandCopy = prepareCommand(passwords);
        assertTrue(changePwFirstCommand.equals(changePwFirstCommandCopy));

        // different types -> returns false
        assertFalse(changePwFirstCommandCopy.equals(1));

        // null -> returns false
        assertFalse(changePwFirstCommandCopy.equals(null));

        // different employee -> returns false
        assertFalse(changePwFirstCommand.equals(changePwSecondCommand));
    }


    /**
     * Returns a {@code ChangeAdminPasswordCommand} with the parameter {@code index} and {@code passwords}.
     */
    private ChangeAdminPasswordCommand prepareCommand(ArrayList<String> passwords) {
        ChangeAdminPasswordCommand changePwCommand = new ChangeAdminPasswordCommand(passwords);
        changePwCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return changePwCommand;
    }



}
