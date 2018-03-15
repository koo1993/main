package seedu.ptman.model.outlet.exceptions;

/**
 * Signals an error when the start time is after the end time.
 */
public class IllegalTimeException extends Exception {
    public IllegalTimeException() {
        super("Start time must be earlier than the end time.");
    }
}
