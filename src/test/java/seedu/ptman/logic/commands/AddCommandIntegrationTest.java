package seedu.ptman.logic.commands;

import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalPartTimeManager;

import org.junit.Before;
import org.junit.Test;

import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.testutil.EmployeeBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    //@@author koo1993
    @Before
    public void setUp() {
        model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(), new OutletInformation());
        model.setTrueAdminMode(new Password());
    }

    @Test
    public void execute_newEmployeeNotAdminMode_accessDenied() throws Exception {
        model.setFalseAdminMode();
        Employee validEmployee = new EmployeeBuilder().build();

        Model expectedModel = new ModelManager(model.getPartTimeManager(), new UserPrefs(), new OutletInformation());
        expectedModel.addEmployee(validEmployee);

        assertCommandFailure(prepareCommand(validEmployee, model), model, MESSAGE_ACCESS_DENIED);

    }
    //@@author
    @Test
    public void execute_newEmployee_success() throws Exception {
        Employee validEmployee = new EmployeeBuilder().build();

        Model expectedModel = new ModelManager(model.getPartTimeManager(), new UserPrefs(), new OutletInformation());
        expectedModel.addEmployee(validEmployee);

        assertCommandSuccess(prepareCommand(validEmployee, model), model,
                String.format(AddCommand.MESSAGE_SUCCESS, validEmployee), expectedModel);
    }

    @Test
    public void execute_duplicateEmployee_throwsCommandException() {
        Employee employeeInList = model.getPartTimeManager().getEmployeeList().get(0);
        assertCommandFailure(prepareCommand(employeeInList, model), model, AddCommand.MESSAGE_DUPLICATE_EMPLOYEE);
    }

    /**
     * Generates a new {@code AddCommand} which upon execution, adds {@code employee} into the {@code model}.
     */
    private AddCommand prepareCommand(Employee employee, Model model) {
        AddCommand command = new AddCommand(employee);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
