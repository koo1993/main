package seedu.ptman.commons.events.ui;

import seedu.ptman.commons.events.BaseEvent;

/**
 * An event requesting to export the timetable view as an image locally
 */
public class ExportTimetableAsImageRequestEvent extends BaseEvent {

    public final String filename;

    public ExportTimetableAsImageRequestEvent(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
