package seedu.ptman.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalPartTimeManager;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;

import java.util.ArrayList;

import org.junit.Before;
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
    private ArrayList<String> default1PasswordsTodefault2 = new ArrayList<>();

    @Before
    public void setup() {
        model.setTrueAdminMode(new Password());
        default1PasswordsTodefault2.add("DEFAULT1");
        default1PasswordsTodefault2.add("DEFAULT2");
        default1PasswordsTodefault2.add("DEFAULT2");
    }

    @Test
    public void constructor_nullPassword_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ChangeAdminPasswordCommand(null);
    }

    @Test
    public void constructor_passwordsNotFullyTabulated_throwsNullPointerException() {
        ArrayList<String> incompletePasswords  = new ArrayList<>();
        incompletePasswords.add("DEFAULT1");
        incompletePasswords.add("DEFAULT2");
        thrown.expect(IndexOutOfBoundsException.class);
        new ChangeAdminPasswordCommand(incompletePasswords);
    }

    @Test
    public void execute_notInAdminModeValidInputs_accessDenied() {
        model.setFalseAdminMode();
        assertCommandFailure(prepareCommand(default1PasswordsTodefault2), model, MESSAGE_ACCESS_DENIED);
    }

    @Test
    public void execute_validInputs_success() throws Exception {
        Employee employeeToEdit = model.getFilteredEmployeeList().get(INDEX_FIRST_EMPLOYEE.getZeroBased());


        ChangeAdminPasswordCommand changePwCommand = prepareCommand(default1PasswordsTodefault2);

        String expectedMessage = String.format(ChangeAdminPasswordCommand.MESSAGE_SUCCESS, employeeToEdit.getName());

        ModelManager expectedModel = new ModelManager(model.getPartTimeManager(),
                new UserPrefs(), new OutletInformation());
        expectedModel.setTrueAdminMode(new Password());

        Password newPassword = new Password();
        newPassword.createPassword(default1PasswordsTodefault2.get(1));

        expectedModel.setAdminPassword(newPassword);

        assertCommandSuccess(changePwCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPassword_throwsInvalidPasswordException() throws Exception {
        ArrayList<String> wrongPasswords = new ArrayList<>();
        wrongPasswords.add("wrongpassword");
        wrongPasswords.add("DEFAULT2");
        wrongPasswords.add("DEFAULT2");
        thrown.expect(InvalidPasswordException.class);
        prepareCommand(wrongPasswords).execute();
    }

    @Test
    public void execute_unmatchedNewPassword_throwsCommandException() throws Exception {
        ArrayList<String> unmatchNewpasswords = new ArrayList<>();
        unmatchNewpasswords.add("DEFAULT1");
        unmatchNewpasswords.add("DEFAULT3");
        unmatchNewpasswords.add("DEFAULT4");

        ChangeAdminPasswordCommand changePwCommand = prepareCommand(unmatchNewpasswords);
        assertCommandFailure(changePwCommand, model, ChangeAdminPasswordCommand.MESSAGE_INVALID_CONFIMREDPASSWORD);
    }


    @Test
    public void equals() throws Exception {
        ArrayList<String> default1PasswordsTodefault3 = new ArrayList<>();
        default1PasswordsTodefault3.add("DEFAULT1");
        default1PasswordsTodefault3.add("DEFAULT3");
        default1PasswordsTodefault3.add("DEFAULT3");

        ChangeAdminPasswordCommand changePwFirstCommand = prepareCommand(default1PasswordsTodefault2);
        ChangeAdminPasswordCommand changePwSecondCommand = prepareCommand(default1PasswordsTodefault3);

        // same object -> returns true
        assertTrue(changePwFirstCommand.equals(changePwFirstCommand));

        // same values -> returns true
        ChangeAdminPasswordCommand changePwFirstCommandCopy = prepareCommand(default1PasswordsTodefault2);
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
