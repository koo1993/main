package seedu.ptman.logic.commands.exceptions;

/**
 * Signals that the expected password is missing.
 */
public class MissingPasswordException extends CommandException {
    public MissingPasswordException() {
        super("Please include your employee password");
    }
}
