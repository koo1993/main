package seedu.ptman.model.outlet;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a day on the timetable
 */
public class TimetableDay {
    private LocalDate date;
    private ArrayList<Shift> shiftList;

    public TimetableDay(LocalDate date, ArrayList<Shift> shiftList) {
        this.date = date;
        this.shiftList = shiftList;
    }

    protected Shift getShift(int index) {
        return shiftList.get(index);
    }

    protected ArrayList<Shift> getShifts() {
        return shiftList;
    }

    public void removeShift(Shift shift) {
        shiftList.remove(shift);
    }

    public LocalDate getDate() {
        return date;
    }
}
