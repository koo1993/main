package seedu.ptman.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.ptman.logic.commands.CommandTestUtil.deleteFirstEmployee;
import static seedu.ptman.logic.commands.CommandTestUtil.showEmployeeAtIndex;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalPartTimeManager;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;

import org.junit.Test;

import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;

public class UndoableCommandTest {
    private final Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs());
    private final DummyCommand dummyCommand = new DummyCommand(model);

    private Model expectedModel = new ModelManager(getTypicalPartTimeManager(), new UserPrefs());

    @Test
    public void executeUndo() throws Exception {
        dummyCommand.execute();
        deleteFirstEmployee(expectedModel);
        assertEquals(expectedModel, model);

        showEmployeeAtIndex(model, INDEX_FIRST_EMPLOYEE);

        // undo() should cause the model's filtered list to show all employees
        dummyCommand.undo();
        expectedModel = new ModelManager(getTypicalPartTimeManager(), new UserPrefs());
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo() {
        showEmployeeAtIndex(model, INDEX_FIRST_EMPLOYEE);

        // redo() should cause the model's filtered list to show all employees
        dummyCommand.redo();
        deleteFirstEmployee(expectedModel);
        assertEquals(expectedModel, model);
    }

    /**
     * Deletes the first employee in the model's filtered list.
     */
    class DummyCommand extends UndoableCommand {
        DummyCommand(Model model) {
            this.model = model;
        }

        @Override
        public CommandResult executeUndoableCommand() throws CommandException {
            Employee employeeToDelete = model.getFilteredEmployeeList().get(0);
            try {
                model.deleteEmployee(employeeToDelete);
            } catch (EmployeeNotFoundException pnfe) {
                fail("Impossible: employeeToDelete was retrieved from model.");
            }
            return new CommandResult("");
        }
    }
}
