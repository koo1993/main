package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.services.EmailService;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.Employee;


//@@author koo1993
/**
 * Reset password for an existing employee in PTMan.
 */
public class ResetPasswordCommand extends Command {

    public static final String COMMAND_WORD = "resetpw";
    public static final String COMMAND_ALIAS = "rp";

    public static final String COMMAND_FORMAT = "EMPLOYEE_INDEX";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Resets password for the chosen employee.\n"
            + "Parameters: "
            + COMMAND_FORMAT
            + "\nExample: " + COMMAND_WORD + " 2";
    public static final String MESSAGE_SUCCESS = "Email with the new password is sent to you at: %1$s";

    public static final String MESSAGE_SENT_FAIL = "Reset password Fail, please check your internet connection";
    public static final String MESSAGE_EMAIL_FAIL = "No such email address %1$s";

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
        Password newPassword = null;

        try {
            newPassword = createAndSendRandomPassword(requestedEmployee);
        } catch (AddressException ae) {
            return new CommandResult(String.format(MESSAGE_EMAIL_FAIL, requestedEmployee.getEmail()));
        } catch (MessagingException e) {
            return new CommandResult(MESSAGE_SENT_FAIL);
        }

        model.storeResetPassword(requestedEmployee, newPassword);
        return new CommandResult(String.format(MESSAGE_SUCCESS, requestedEmployee.getEmail()));
    }

    /**
     * Generate random password with 8 characters
     * @return Password with the new password.
     */
    private Password createAndSendRandomPassword(Employee requestedEmployee) throws MessagingException {
        String newPassword = Password.generateRandomPassword();

        EmailService email = EmailService.getInstance();
        email.sendResetPasswordMessage(requestedEmployee.getName().toString(),
                requestedEmployee.getEmail().toString(), newPassword);

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
