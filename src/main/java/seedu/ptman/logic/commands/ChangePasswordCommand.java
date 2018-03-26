package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.Model;
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
 * Change password of an existing employee in PTMan.
 */
public class ChangePasswordCommand extends Command {

    public static final String COMMAND_WORD = "changepw";
    public static final String COMMAND_ALIAS = "cp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " INDEX" + " pw/currentPassword "
            + "pw/NewPassword "  + "pw/ConfirmNewPassword ";


    public static final String MESSAGE_INVALID_CONFIMREDPASSWORD = "New password entered are not the same";
    public static final String MESSAGE_SUCCESS = "%1$s, your password is changed.";


    private final Index index;
    private final ArrayList<String> passwords;

    private Employee employeeToEdit;
    private Employee editedEmployee;

    /**
     * @param index of the employee in the filtered employee list to edit
     * @param passwords should contain 3 password String in the sequence of:
     *                 confirmed password, new password, confirmed new password
     */
    public ChangePasswordCommand(Index index, ArrayList<String> passwords) {
        requireNonNull(index);
        requireNonNull(passwords);
        this.index = index;
        this.passwords = passwords;
    }

    @Override
    public CommandResult execute() throws CommandException {

        checkConfirmedPassword(passwords.get(1), passwords.get(2));

        List<Employee> lastShownList = model.getFilteredEmployeeList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
        }

        Password currentPassword = parsePassword(passwords.get(0));
        employeeToEdit = lastShownList.get(index.getZeroBased());

        checkAuthenticity(currentPassword, employeeToEdit);

        editedEmployee = createNewPasswordEmployee(employeeToEdit, parsePassword(passwords.get(1)));

        try {
            model.updateEmployee(employeeToEdit, editedEmployee);
        } catch (DuplicateEmployeeException dpe) {
            throw new CommandException("MESSAGE_DUPLICATE_EMPLOYEE");
        } catch (EmployeeNotFoundException pnfe) {
            throw new AssertionError("The target employee cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, editedEmployee.getName()));
    }

    /**
     * Check password given is employee's or it's temp password.
     * @param currentPassword
     * @throws InvalidPasswordException if password is invalid
     */
    private void checkAuthenticity(Password currentPassword, Employee employeeToEdit)
            throws InvalidPasswordException {
        if (!employeeToEdit.isCorrectPassword(currentPassword)
                && !model.isCorrectTempPwd(employeeToEdit, currentPassword)) {
            throw new InvalidPasswordException();
        }
    }

    /**
     * Check confirmed new password with new password
     * @throws CommandException if both password are not the same
     */
    private void checkConfirmedPassword(String newPassword, String confirmedPassword) throws CommandException {
        if (!newPassword.equals(confirmedPassword)) {
            throw new CommandException(MESSAGE_INVALID_CONFIMREDPASSWORD);
        }
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
    public void setData(Model model, CommandHistory history, UndoRedoStack undoRedoStack) {
        super.setData(model, history, undoRedoStack);
        undoRedoStack.resetRedoUndoStack();
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ChangePasswordCommand)) {
            return false;
        }

        // state check
        ChangePasswordCommand e = (ChangePasswordCommand) other;
        return index.equals(e.index)
                && passwords.equals(e.passwords)
                && Objects.equals(employeeToEdit, e.employeeToEdit)
                && Objects.equals(editedEmployee, e.editedEmployee);
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
}
