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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.ptman.commons.core.Messages;
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

//@@author koo1993

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code AddSalaryCommand}.
 */
public class AddSalaryCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(), new OutletInformation());
    private Salary salaryToAdd = new Salary("100");

    @Before
    public void setup(){
        model.setTrueAdminMode(new Password());
    }

    @Test
    public void execute_validInputs_success() throws Exception {
        Employee employeeToEdit = model.getFilteredEmployeeList().get(INDEX_FIRST_EMPLOYEE.getZeroBased());

        AddSalaryCommand addSalaryCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, salaryToAdd);

        String expectedMessage = String.format(AddSalaryCommand.MESSAGE_EDIT_EMPLOYEE_SUCCESS,
                salaryToAdd.toString(), employeeToEdit.getName());

        ModelManager expectedModel = new ModelManager(model.getPartTimeManager(),
                new UserPrefs(), new OutletInformation());
        expectedModel.updateEmployee(employeeToEdit, createNewSalaryEmployee(employeeToEdit,
                salaryToAdd));

        assertCommandSuccess(addSalaryCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidEmployeeIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEmployeeList().size() + 1);
        AddSalaryCommand editCommand = prepareCommand(outOfBoundIndex, salaryToAdd);
        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
    }

    @Test
    public void execute_notInAdminMode_accessDenied() throws Exception {
        model.setFalseAdminMode();
        AddSalaryCommand addSalaryCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, salaryToAdd);
        assertCommandFailure(addSalaryCommand, model, Messages.MESSAGE_ACCESS_DENIED);
    }



    @Test
    public void equals() throws Exception {
        Salary salaryToAdd = new Salary("100");

        AddSalaryCommand addSalaryFirstCommand = prepareCommand(INDEX_FIRST_EMPLOYEE, salaryToAdd);
        AddSalaryCommand addSalarySecondCommand = prepareCommand(INDEX_SECOND_EMPLOYEE, salaryToAdd);

        // same object -> returns true
        assertTrue(addSalaryFirstCommand.equals(addSalaryFirstCommand));

        // same values -> returns true
        AddSalaryCommand changePwFirstCommandCopy = prepareCommand(INDEX_FIRST_EMPLOYEE, salaryToAdd);
        assertTrue(addSalaryFirstCommand.equals(changePwFirstCommandCopy));

        // different types -> returns false
        assertFalse(changePwFirstCommandCopy.equals(1));
        // null -> returns false
        assertFalse(changePwFirstCommandCopy.equals(null));

        // different employee -> returns false
        assertFalse(addSalaryFirstCommand.equals(addSalarySecondCommand));
    }




    /**
     * Returns a {@code AddSalaryCommand} with the parameter {@code index} and {@code salaryToAdd}.
     */
    private AddSalaryCommand prepareCommand(Index index, Salary salaryToAdd) {
        AddSalaryCommand addSalaryCommand = new AddSalaryCommand(index, salaryToAdd);
        addSalaryCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return addSalaryCommand;
    }


    /**
     * Create new employee with added Salary.
     * @param employeeToEdit
     * @param salaryToAdd
     * @return
     */
    public Employee createNewSalaryEmployee(Employee employeeToEdit,
                                     Salary salaryToAdd) {
        assert employeeToEdit != null;

        Name name = employeeToEdit.getName();
        Phone phone = employeeToEdit.getPhone();
        Email email = employeeToEdit.getEmail();
        Address address = employeeToEdit.getAddress();
        Set<Tag> tags = employeeToEdit.getTags();
        Password password = employeeToEdit.getPassword();
        Salary newSalary = addSalary(employeeToEdit.getSalary(), salaryToAdd);
        return new Employee(name, phone, email, address, newSalary, password, tags);
    }

    /**
     * Adds up two salary.
     * @param salary
     * @param salaryToadd
     * @return
     */
    private static Salary addSalary(Salary salary, Salary salaryToadd) {
        int salaryAmount = Integer.parseInt(salary.value);
        salaryAmount += Integer.parseInt(salaryToadd.value);
        return new Salary(Integer.toString(salaryAmount));
    }
}
