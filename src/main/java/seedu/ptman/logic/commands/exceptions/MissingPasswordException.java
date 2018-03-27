package seedu.ptman.logic.commands.exceptions;

/**
 * Signals that the operation will result in incorrect Password objects.
 */
public class MissingPasswordException extends CommandException {
    public MissingPasswordException() {
        super("Password not present");
    }
}
