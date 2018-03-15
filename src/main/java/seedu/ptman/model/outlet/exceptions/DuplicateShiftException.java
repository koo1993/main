package seedu.ptman.model.outlet.exceptions;

import seedu.ptman.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Shift objects.
 */
public class DuplicateShiftException extends DuplicateDataException {
    public DuplicateShiftException() {
        super("Operation would result in duplicate employees");
    }
}
