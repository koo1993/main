package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.Employee;

/**
 * Reset password for an existing employee in PTMan.
 */
public class ResetPasswordCommand extends Command {

    public static final String COMMAND_WORD = "reset";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " INDEX";
    public static final String MESSAGE_SUCCESS = "Email with the new password is sent to you at: %1$s";

    private final Index index;

    /**
     * @param index of the employee in the filtered employee list to edit
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
        Password newPassword = createRandomPassword();
        model.storeResetPassword(requestedEmployee, newPassword);
        return new CommandResult(String.format(MESSAGE_SUCCESS, requestedEmployee.getEmail()));
    }

    /**
     * Generate random password with 8 characters
     * @return Password with the new password.
     */
    private Password createRandomPassword() {
        String newPassword = Password.generateRandomPassword();
        return parsePassword(newPassword);
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

}
