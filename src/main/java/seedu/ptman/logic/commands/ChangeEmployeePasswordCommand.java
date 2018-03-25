package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;
import static seedu.ptman.commons.util.AppUtil.checkArgument;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.ptman.model.Model.PREDICATE_SHOW_ALL_EMPLOYEES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.sun.xml.internal.ws.api.pipe.PipelineAssembler;
import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.util.CollectionUtil;
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
public class ChangeEmployeePasswordCommand extends Command {

    public static final String COMMAND_WORD = "changepw";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " INDEX" + " pw/currentPassword "
            + "pw/ChangedPassword "  + "pw/ChangedPassword ";


    public static final String MESSAGE_INVALID_CONFIMREDPASSWORD = "New password entered are not the same";
    public static final String MESSAGE_SUCCESS = "%1$s ,your password is changed.";


    private final Index index;
    private final ArrayList<String> passwords;

    private Employee employeeToEdit;
    private Employee editedEmployee;

    /**
     * @param index of the employee in the filtered employee list to edit
     * @param passwords should contain 3 password: confirmed password, new password, confirmed new password
     */
    public ChangeEmployeePasswordCommand(Index index, ArrayList<String> passwords) {
        requireNonNull(index);
        requireNonNull(passwords);
        this.index = index;
        this.passwords = passwords;
    }

    @Override
    public CommandResult execute() throws CommandException {

        if (!passwords.get(2).equals(passwords.get(1))) {
            throw new CommandException(MESSAGE_INVALID_CONFIMREDPASSWORD);
        }

        List<Employee> lastShownList = model.getFilteredEmployeeList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
        }

        employeeToEdit = lastShownList.get(index.getZeroBased());

        Password currentPassword = parsePassword(passwords.get(0));

        if (!employeeToEdit.isCorrectPassword(currentPassword)) {
            throw new InvalidPasswordException();
        }

        editedEmployee = createNewPasswordEmployee(employeeToEdit, parsePassword(passwords.get(1)));

        try {
            model.updateEmployee(employeeToEdit, editedEmployee);
        } catch (DuplicateEmployeeException dpe) {
            throw new CommandException("MESSAGE_DUPLICATE_EMPLOYEE");
        } catch (EmployeeNotFoundException pnfe) {
            throw new AssertionError("The target employee cannot be missing");
        }
        model.updateFilteredEmployeeList(PREDICATE_SHOW_ALL_EMPLOYEES);
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedEmployee.getName()));
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
        if (!(other instanceof ChangeEmployeePasswordCommand)) {
            return false;
        }

        // state check
        ChangeEmployeePasswordCommand e = (ChangeEmployeePasswordCommand) other;
        return index.equals(e.index)
                && passwords.equals(e.passwords)
                && Objects.equals(employeeToEdit, e.employeeToEdit);
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
