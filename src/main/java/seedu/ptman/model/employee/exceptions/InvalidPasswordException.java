package seedu.ptman.model.employee.exceptions;

import seedu.ptman.commons.exceptions.IllegalValueException;

/**
 * Signals that the operation will result in incorrect Password objects.
 */
public class InvalidPasswordException extends IllegalValueException {
    public InvalidPasswordException(String string) {
        super(string);
    }
}
