package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.ptman.model.Model.PREDICATE_SHOW_ALL_EMPLOYEES;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.Address;
import seedu.ptman.model.employee.Email;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.Name;
import seedu.ptman.model.employee.Phone;
import seedu.ptman.model.employee.Salary;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.tag.Tag;

//@@author koo1993
/**
 * Increase the salary of an existing employee in the ptman.
 */
public class AddSalaryCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addsalary";
    public static final String COMMAND_ALIAS = "adds";

    public static final String COMMAND_FORMAT = "EMPLOYEE_INDEX " + PREFIX_SALARY + "INCREASEAMOUNT ";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds the salary of the employee identified "
            + "by the index number used in the last employee listing. "
            + "Existing values will be added by the input values.\n"
            + "Parameters: "
            + COMMAND_FORMAT
            + "\nExample: " + COMMAND_WORD + " 1 "
            + PREFIX_SALARY + "100 ";


    public static final String MESSAGE_EDIT_EMPLOYEE_SUCCESS = "Added $%s to %s's Pay";
    public static final String MESSAGE_DUPLICATE_EMPLOYEE = "This employee already exists in PTMan.";

    private final Index index;
    private Salary salaryToIncrease;

    private Employee employeeToEdit;
    private Employee editedEmployee;


    /**
     * @param index of the employee in the filtered employee list to edit
     * @param salaryToIncrease amount of salary to increase with.
     */
    public AddSalaryCommand(Index index, Salary salaryToIncrease) {
        requireNonNull(index);
        requireNonNull(salaryToIncrease);
        this.index = index;
        this.salaryToIncrease = salaryToIncrease;

    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }

        try {
            model.updateEmployee(employeeToEdit, editedEmployee);
        } catch (DuplicateEmployeeException dpe) {
            throw new AssertionError("The target employee should not be duplicated");
        } catch (EmployeeNotFoundException pnfe) {
            throw new AssertionError("The target employee cannot be missing");
        }
        model.updateFilteredEmployeeList(PREDICATE_SHOW_ALL_EMPLOYEES);
        return new CommandResult(String.format(MESSAGE_EDIT_EMPLOYEE_SUCCESS,
                editedEmployee.getSalary(), editedEmployee.getName()));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Employee> lastShownList = model.getFilteredEmployeeList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
        }
        employeeToEdit = lastShownList.get(index.getZeroBased());
        editedEmployee = createAddedSalaryEmployee(employeeToEdit, salaryToIncrease);
    }

    /**
     * Creates and returns a {@code Employee} with the details of {@code employeeToEdit}
     */
    private static Employee createAddedSalaryEmployee(Employee employeeToEdit,
                                                 Salary salaryToAdd) {
        assert employeeToEdit != null;

        Name name = employeeToEdit.getName();
        Phone phone = employeeToEdit.getPhone();
        Email email = employeeToEdit.getEmail();
        Address address = employeeToEdit.getAddress();
        Set<Tag> tags = employeeToEdit.getTags();
        Password password = employeeToEdit.getPassword();
        Salary salary = addSalary(employeeToEdit.getSalary(), salaryToAdd);
        return new Employee(name, phone, email, address, salary, password, tags);

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

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddSalaryCommand)) {
            return false;
        }

        // state check
        AddSalaryCommand e = (AddSalaryCommand) other;
        return index.equals(e.index)
                && salaryToIncrease.equals(e.salaryToIncrease)
                && Objects.equals(employeeToEdit, e.employeeToEdit);
    }
}
