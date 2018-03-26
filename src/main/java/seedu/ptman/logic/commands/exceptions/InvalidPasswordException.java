package seedu.ptman.logic.commands.exceptions;


/**
 * Signals that the operation will result in incorrect Password objects.
 */
public class InvalidPasswordException extends CommandException {
    public InvalidPasswordException() {
        super("Password is incorrect");
    }
}
