package seedu.ptman.model.shift;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.ptman.commons.util.CollectionUtil;
import seedu.ptman.model.shift.exceptions.DuplicateShiftException;
import seedu.ptman.model.shift.exceptions.ShiftNotFoundException;

//@@author shanwpf
/**
 * A list of shifts that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Shift#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueShiftList implements Iterable<Shift> {

    private final ObservableList<Shift> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent shift as the given argument.
     */
    public boolean contains(Shift toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a shift to the list.
     *
     * @throws DuplicateShiftException if the shift to add is a duplicate of an existing shift in the list.
     */
    public void add(Shift toAdd) throws DuplicateShiftException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateShiftException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the shift {@code target} in the list with {@code editedShift}.
     *
     * @throws DuplicateShiftException if the replacement is equivalent to another existing shift in the list.
     * @throws ShiftNotFoundException if {@code target} could not be found in the list.
     */
    public void setShift(Shift target, Shift editedShift)
            throws DuplicateShiftException, ShiftNotFoundException {
        requireNonNull(editedShift);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ShiftNotFoundException();
        }

        if (!target.equals(editedShift) && internalList.contains(editedShift)) {
            throw new DuplicateShiftException();
        }

        internalList.set(index, editedShift);
    }

    /**
     * Removes the equivalent shift from the list.
     *
     * @throws ShiftNotFoundException if no such shift could be found in the list.
     */
    public boolean remove(Shift toRemove) throws ShiftNotFoundException {
        requireNonNull(toRemove);
        final boolean shiftFoundAndDeleted = internalList.remove(toRemove);
        if (!shiftFoundAndDeleted) {
            throw new ShiftNotFoundException();
        }
        return shiftFoundAndDeleted;
    }

    public void setShifts(UniqueShiftList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setShifts(List<Shift> shifts) throws DuplicateShiftException {
        requireAllNonNull(shifts);
        final UniqueShiftList replacement = new UniqueShiftList();
        for (final Shift shift : shifts) {
            replacement.add(shift);
        }
        setShifts(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Shift> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Shift> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueShiftList // instanceof handles nulls
                        && this.internalList.equals(((UniqueShiftList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
