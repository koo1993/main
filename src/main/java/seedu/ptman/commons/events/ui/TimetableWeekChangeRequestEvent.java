package seedu.ptman.commons.events.ui;

import seedu.ptman.commons.events.BaseEvent;

/**
 * Indicates a request to change the timetable view to the next or previous week.
 */
public class TimetableWeekChangeRequestEvent extends BaseEvent {

    public final boolean isNext;
    public final boolean isPrev;

    public TimetableWeekChangeRequestEvent(boolean isNext, boolean isPrev) {
        this.isNext = isNext;
        this.isPrev = isPrev;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
