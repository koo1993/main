package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;

import java.util.ArrayList;

import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.logic.commands.exceptions.InvalidPasswordException;
import seedu.ptman.model.Model;
import seedu.ptman.model.Password;

/**
 * Change password of the outlet in PTMan.
 */
public class ChangeMasterPasswordCommand extends Command {

    public static final String COMMAND_WORD = "changemasterpw";
    public static final String COMMAND_ALIAS = "cmp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " pw/currentPassword "
            + "pw/NewPassword "  + "pw/ConfirmNewPassword ";


    public static final String MESSAGE_INVALID_CONFIMREDPASSWORD = "New password entered are not the same";
    public static final String MESSAGE_SUCCESS = "your master password is changed.";


    private final ArrayList<String> passwords;


    /**
     * @param passwords should contain 3 password String in the sequence of:
     *                 confirmed password, new password, confirmed new password
     */
    public ChangeMasterPasswordCommand(ArrayList<String> passwords) {
        requireNonNull(passwords);
        this.passwords = passwords;
    }

    @Override
    public CommandResult execute() throws CommandException {

        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }

        checkConfirmedPassword(passwords.get(1), passwords.get(2));

        Password enteredPassword = parsePassword(passwords.get(0));
        Password newPassword = parsePassword(passwords.get(1));

        if (!model.isAdminPassword(enteredPassword)) {
            throw new InvalidPasswordException();
        }

        model.setAdminPassword(newPassword);
        return new CommandResult(MESSAGE_SUCCESS);
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
        if (!(other instanceof ChangeMasterPasswordCommand)) {
            return false;
        }

        // state check
        ChangeMasterPasswordCommand e = (ChangeMasterPasswordCommand) other;
        return passwords.equals(e.passwords);
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
