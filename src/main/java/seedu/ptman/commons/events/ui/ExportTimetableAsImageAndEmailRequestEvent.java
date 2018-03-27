package seedu.ptman.commons.events.ui;

import seedu.ptman.commons.events.BaseEvent;
import seedu.ptman.model.employee.Email;

/**
 * An event requesting to export the timetable view as an image and email it to the given email
 */
public class ExportTimetableAsImageAndEmailRequestEvent extends BaseEvent {

    public final String filename;
    public final Email email;

    public ExportTimetableAsImageAndEmailRequestEvent(String filename, Email email) {
        this.filename = filename;
        this.email = email;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
