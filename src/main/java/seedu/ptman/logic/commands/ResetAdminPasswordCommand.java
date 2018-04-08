package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.sun.javafx.util.Logging;

import seedu.ptman.commons.services.EmailService;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.Password;
import seedu.ptman.model.outlet.OutletInformation;

//@@author koo1993
/**
 * Reset password for the outlet in PTMan.
 */
public class ResetAdminPasswordCommand extends Command {

    public static final String COMMAND_WORD = "resetadminpw";
    public static final String COMMAND_ALIAS = "rap";

    public static final String MESSAGE_USAGE = COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Email with the new password is sent to you at: %1$s";
    public static final String MESSAGE_SENTFAIL =
            "Reset password fail to send through email, please check your internet connection";

    public static final String MESSAGE_EMAILFAIL = "No such email address %1$s";


    @Override
    public CommandResult execute() throws CommandException {

        Password newPassword;
        OutletInformation outletRequested = model.getOutletInformation();

        try {
            newPassword = createAndSendRandomPassword(outletRequested);
        } catch (AddressException ae) {
            return new CommandResult(String.format(MESSAGE_EMAILFAIL, outletRequested.getOutletEmail()));
        } catch (MessagingException e) {
            Logging.getInputLogger().warning(e.toString());
            return new CommandResult(MESSAGE_SENTFAIL);
        }

        model.storeResetPassword(outletRequested, newPassword);
        return new CommandResult(String.format(MESSAGE_SUCCESS, outletRequested.getOutletEmail()));
    }

    /**
     * Generate random password with 8 characters
     * @return Password with the new password.
     */
    private Password createAndSendRandomPassword(OutletInformation outlet) throws MessagingException {
        String newPassword = Password.generateRandomPassword();

        EmailService email = EmailService.getInstance();
        email.sendResetPasswordMessage(outlet.getName().toString(),
                outlet.getOutletEmail().toString(), newPassword);

        return parsePassword(newPassword);
    }


    @Override
    public boolean equals(Object other) {
        // instanceof handles nulls
        return (other instanceof ResetAdminPasswordCommand);
    }

    /**
     * Parses a {@code String password} into an {@code Password}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Password parsePassword(String password) {
        requireNonNull(password);
        String trimmedPassword = password.trim();
        Password newPassword = new Password();
        newPassword.createPassword(trimmedPassword);
        return newPassword;
    }

}
