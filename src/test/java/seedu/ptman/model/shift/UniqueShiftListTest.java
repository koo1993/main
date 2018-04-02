package seedu.ptman.model.shift;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.testutil.Assert.assertThrows;
import static seedu.ptman.testutil.TypicalShifts.MONDAY_AM;
import static seedu.ptman.testutil.TypicalShifts.MONDAY_PM;
import static seedu.ptman.testutil.TypicalShifts.TUESDAY_AM;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.ptman.model.shift.exceptions.DuplicateShiftException;
import seedu.ptman.model.shift.exceptions.ShiftNotFoundException;

//@@author shanwpf
public class UniqueShiftListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void setShift_shiftDoesNotExist_throwsShiftNotFoundException() {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        assertThrows(ShiftNotFoundException.class, () -> {
            uniqueShiftList.setShift(MONDAY_AM, MONDAY_PM);
        });
    }

    @Test
    public void setShift_editedShiftExists_throwsDuplicateShiftException()
            throws DuplicateShiftException {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        uniqueShiftList.add(MONDAY_AM);
        uniqueShiftList.add(MONDAY_PM);
        assertThrows(DuplicateShiftException.class, () -> {
            uniqueShiftList.setShift(MONDAY_AM, MONDAY_PM);
        });
    }

    @Test
    public void setShift_validShifts_shiftReplaced()
            throws DuplicateShiftException, ShiftNotFoundException {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        uniqueShiftList.add(MONDAY_AM);
        uniqueShiftList.add(MONDAY_PM);
        uniqueShiftList.setShift(MONDAY_AM, TUESDAY_AM);
        assertFalse(uniqueShiftList.contains(MONDAY_AM));
        assertTrue(uniqueShiftList.contains(TUESDAY_AM));
    }

    @Test
    public void setShifts_validShifts_shiftsReplaced() throws DuplicateShiftException {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        List<Shift> shiftList = new ArrayList<>();
        shiftList.add(MONDAY_AM);
        shiftList.add(MONDAY_PM);
        shiftList.add(TUESDAY_AM);
        uniqueShiftList.setShifts(shiftList);
        assertTrue(uniqueShiftList.contains(MONDAY_AM));
        assertTrue(uniqueShiftList.contains(MONDAY_PM));
        assertTrue(uniqueShiftList.contains(TUESDAY_AM));
    }

    @Test
    public void equals_sameUniqueShiftLists_returnsTrue() throws DuplicateShiftException {
        UniqueShiftList uniqueShiftList1 = new UniqueShiftList();
        UniqueShiftList uniqueShiftList2 = new UniqueShiftList();
        uniqueShiftList1.add(MONDAY_AM);
        uniqueShiftList2.add(MONDAY_AM);
        assertTrue(uniqueShiftList1.equals(uniqueShiftList2));
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueShiftList.asObservableList().remove(0);
    }

    @Test
    public void remove_shiftDoesNotExist_throwsShiftNotFoundException() {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        assertThrows(ShiftNotFoundException.class, () -> uniqueShiftList.remove(MONDAY_AM));
    }
}
