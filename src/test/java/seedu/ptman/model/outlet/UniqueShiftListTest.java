package seedu.ptman.model.outlet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.ptman.model.outlet.exceptions.DuplicateShiftException;
import seedu.ptman.model.outlet.exceptions.IllegalTimeException;
import seedu.ptman.model.outlet.exceptions.ShiftNotFoundException;
import seedu.ptman.testutil.Assert;

public class UniqueShiftListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void setShift_shiftDoesNotExist_throwsShiftNotFoundException() throws IllegalTimeException {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        Shift shift1 = new Shift(LocalTime.of(10, 0), LocalTime.of(14, 0), DayOfWeek.MONDAY, 3);
        Shift shift2 = new Shift(LocalTime.of(10, 0), LocalTime.of(14, 0), DayOfWeek.MONDAY, 5);
        Assert.assertThrows(ShiftNotFoundException.class, () -> {
            uniqueShiftList.setShift(shift1, shift2);
        });
    }

    @Test
    public void setShift_editedShiftExists_throwsDuplicateShiftException()
            throws IllegalTimeException, DuplicateShiftException {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        Shift shift1 = new Shift(LocalTime.of(10, 0), LocalTime.of(14, 0), DayOfWeek.MONDAY, 3);
        Shift shift2 = new Shift(LocalTime.of(10, 0), LocalTime.of(14, 0), DayOfWeek.MONDAY, 5);
        uniqueShiftList.add(shift1);
        uniqueShiftList.add(shift2);
        Assert.assertThrows(DuplicateShiftException.class, () -> {
            uniqueShiftList.setShift(shift1, shift2);
        });
    }

    @Test
    public void setShift_validShifts_shiftReplaced()
            throws IllegalTimeException, DuplicateShiftException, ShiftNotFoundException {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        Shift shift1 = new Shift(LocalTime.of(10, 0), LocalTime.of(14, 0), DayOfWeek.MONDAY, 3);
        Shift shift2 = new Shift(LocalTime.of(10, 0), LocalTime.of(14, 0), DayOfWeek.MONDAY, 5);
        Shift shift3 = new Shift(LocalTime.of(12, 0), LocalTime.of(14, 0), DayOfWeek.MONDAY, 5);
        uniqueShiftList.add(shift1);
        uniqueShiftList.add(shift2);
        uniqueShiftList.setShift(shift1, shift3);
        assertFalse(uniqueShiftList.contains(shift1));
        assertTrue(uniqueShiftList.contains(shift3));
    }

    @Test
    public void setShifts_validShifts_shiftsReplaced()
            throws IllegalTimeException, DuplicateShiftException {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        Shift shift1 = new Shift(LocalTime.of(10, 0), LocalTime.of(14, 0), DayOfWeek.MONDAY, 3);
        Shift shift2 = new Shift(LocalTime.of(10, 0), LocalTime.of(14, 0), DayOfWeek.MONDAY, 5);
        Shift shift3 = new Shift(LocalTime.of(12, 0), LocalTime.of(14, 0), DayOfWeek.MONDAY, 5);
        List<Shift> shiftList = new ArrayList<>();
        shiftList.add(shift1);
        shiftList.add(shift2);
        shiftList.add(shift3);
        uniqueShiftList.setShifts(shiftList);
        assertTrue(uniqueShiftList.contains(shift1));
        assertTrue(uniqueShiftList.contains(shift2));
        assertTrue(uniqueShiftList.contains(shift3));
    }

    @Test
    public void equals_sameUniqueShiftLists_returnsTrue()
            throws IllegalTimeException, DuplicateShiftException {
        UniqueShiftList uniqueShiftList1 = new UniqueShiftList();
        UniqueShiftList uniqueShiftList2 = new UniqueShiftList();
        Shift shift1 = new Shift(LocalTime.of(10, 0), LocalTime.of(14, 0), DayOfWeek.MONDAY, 3);
        uniqueShiftList1.add(shift1);
        uniqueShiftList2.add(shift1);
        assertTrue(uniqueShiftList1.equals(uniqueShiftList2));
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueShiftList.asObservableList().remove(0);
    }
}
