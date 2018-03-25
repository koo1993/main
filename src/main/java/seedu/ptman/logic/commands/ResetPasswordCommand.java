package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.model.Model.PREDICATE_SHOW_ALL_EMPLOYEES;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import seedu.ptman.model.employee.exceptions.InvalidPasswordException;
import seedu.ptman.model.tag.Tag;

/**
 * Edits the details of an existing employee in the ptman.
 */
public class ResetPasswordCommand extends Command {

    public static final String COMMAND_WORD = "reset";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " INDEX";


    public static final String MESSAGE_SUCCESS = "Email with the new password is sent to you at: %1%s";


    private final Index index;

    /**
     * @param index of the employee in the filtered employee list to edit
     * @param passwords should contain 3 password: confirmed password, new password, confirmed new password
     */
    public ResetPasswordCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<Employee> lastShownList = model.getFilteredEmployeeList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
        }

        Employee requestedEmployee = lastShownList.get(index.getZeroBased());
        String newPassword = Password.generateRandomPassword();
        System.out.println(newPassword);
        model.storeResetPassword(requestedEmployee, parsePassword(newPassword));
        return new CommandResult(String.format(MESSAGE_SUCCESS, requestedEmployee.getEmail()));
    }


    /**
     * Creates and returns a {@code Employee} with the details of {@code employeeToEdit}
     * edited with {@code editEmployeeDescriptor}.
     */
    private static Employee createNewPasswordEmployee(Employee employeeToEdit,
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

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ResetPasswordCommand)) {
            return false;
        }

        // state check
        ResetPasswordCommand e = (ResetPasswordCommand) other;
        return index.equals(e.index);
    }

    /**
     * Parses a {@code String password} into an {@code Password}.
     * Leading and trailing whitespaces will be trimmed.
     *
     */
    public static Password parsePassword(String password) {
        requireNonNull(password);
        String trimmedPassword = password.trim();
        Password newPassword = new Password();
        newPassword.createPassword(trimmedPassword);
        return newPassword;
    }

    /**
     * Parses a {@code Optional<String> password} into an {@code Optional<Password>} if {@code password} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Password> parsePassword(Optional<String> password) {
        requireNonNull(password);
        return password.isPresent() ? Optional.of(parsePassword(password.get())) : Optional.empty();
    }

}
