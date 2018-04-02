package seedu.ptman.model.shift.exceptions;

import seedu.ptman.commons.exceptions.DuplicateDataException;

//@@author shanwpf
/**
 * Signals that the operation will result in duplicate Shift objects.
 */
public class DuplicateShiftException extends DuplicateDataException {
    public DuplicateShiftException() {
        super("Operation would result in duplicate employees");
    }
}
