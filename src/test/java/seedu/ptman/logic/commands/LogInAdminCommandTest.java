package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;

import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.logic.commands.exceptions.InvalidPasswordException;
import seedu.ptman.model.Model;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.ReadOnlyPartTimeManager;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.Shift;
import seedu.ptman.model.outlet.exceptions.DuplicateShiftException;
import seedu.ptman.model.outlet.exceptions.NoOutletInformationFieldChangeException;
import seedu.ptman.model.outlet.exceptions.ShiftNotFoundException;
import seedu.ptman.model.tag.Tag;


//@@author koo1993
public class LogInAdminCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPassword_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new LogInAdminCommand(null);
    }

    @Test
    public void execute_passwordAcceptedByModel_logInSuccessful() throws Exception {
        ModelStubAcceptingAllPassword modelStub = new ModelStubAcceptingAllPassword();
        modelStub.setIsAdminMode(false); // not already adminMode
        modelStub.setIsSetTrueAdminMode(true); // password accepted.
        CommandResult commandResult = getLogInAdminCommandTest(new Password(), modelStub).execute();

        assertEquals(LogInAdminCommand.MESSAGE_SUCCESS, commandResult.feedbackToUser);
    }

    @Test

    public void execute_correctPassword_alreadyLoggedIn() throws Exception {
        ModelStubAcceptingAllPassword modelStub = new ModelStubAcceptingAllPassword();
        modelStub.setIsAdminMode(true); // already in adminMode
        modelStub.setIsSetTrueAdminMode(true); // password accepted.
        CommandResult commandResult = getLogInAdminCommandTest(new Password(), modelStub).execute();
        assertEquals(LogInAdminCommand.MESSAGE_LOGGEDIN, commandResult.feedbackToUser);
    }

    @Test
    public void execute_wrongPassword_invalidPasswordException() throws Exception {
        ModelStubAcceptingAllPassword modelStub = new ModelStubAcceptingAllPassword();
        modelStub.setIsAdminMode(false); // not in adminMode
        modelStub.setIsSetTrueAdminMode(false); // password rejected.
        thrown.expect(InvalidPasswordException.class);
        getLogInAdminCommandTest(new Password(), modelStub).execute();
    }

    /**
     * Generates a new LogInAdminCommand with the details of the given employee.
     */
    private LogInAdminCommand getLogInAdminCommandTest(Password password, Model model) {
        LogInAdminCommand command = new LogInAdminCommand(password);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addEmployee(Employee employee) throws DuplicateEmployeeException {
            fail("This method should not be called.");
        }

        @Override
        public boolean isAdminPassword(Password password) {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public void setAdminPassword(Password password) {
            fail("This method should not be called.");
        }

        @Override
        public boolean isAdminMode() {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public boolean setTrueAdminMode(Password password) {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public void setFalseAdminMode() {
            fail("This method should not be called.");
        }


        @Override
        public void storeResetPassword(OutletInformation outlet, Password tempPassword) {
            fail("This method should not be called.");
        }

        @Override
        public void storeResetPassword(Employee employee, Password tempPassword) {
            fail("This method should not be called.");
        }

        @Override
        public boolean isCorrectTempPwd(Employee employee, Password tempPassword) {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public boolean isCorrectTempPwd(OutletInformation outlet, Password tempPassword) {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public void addShift(Shift shift) throws DuplicateShiftException {
            fail("This method should not be called.");
        }


        @Override
        public void deleteTagFromAllEmployee(Tag tag) {
            fail("This method should not be called");
        }

        @Override
        public void resetData(ReadOnlyPartTimeManager newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyPartTimeManager getPartTimeManager() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deleteEmployee(Employee target) throws EmployeeNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updateEmployee(Employee target, Employee editedEmployee)
                throws DuplicateEmployeeException {
            fail("This method should not be called.");
        }

        @Override
        public void updateOutlet(OutletInformation outlet) throws NoOutletInformationFieldChangeException {
            fail("This method should not be called.");
        }

        @Override
        public String getOutletInformationMessage() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Employee> getFilteredEmployeeList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Shift> getFilteredShiftList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public OutletInformation getOutletInformation() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredEmployeeList(Predicate<Employee> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void deleteShift(Shift shiftToDelete) throws ShiftNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updateShift(Shift shiftToApply, Shift editedShift)
                throws ShiftNotFoundException, DuplicateShiftException {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredShiftList(Predicate<Shift> predicate) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always accept the password being given.
     */
    private class ModelStubAcceptingAllPassword extends ModelStub {
        private boolean isSetTrueAdminMode = false;
        private boolean isAdminMode = false;
        @Override
        public boolean setTrueAdminMode(Password password) {
            requireNonNull(password);
            return isSetTrueAdminMode;
        }

        @Override
        public boolean isAdminMode()  {
            return isAdminMode;
        }

        @Override
        public ReadOnlyPartTimeManager getPartTimeManager() {
            return new PartTimeManager();
        }

        /**
         * set for test case purpose only.
         */
        public void setIsAdminMode(boolean isAdminMode) {
            this.isAdminMode = isAdminMode;
        }

        /**
         * set for test case purpose only.
         */
        public void setIsSetTrueAdminMode(boolean isSetTrueAdminMode) {
            this.isSetTrueAdminMode = isSetTrueAdminMode;
        }
    }

}
