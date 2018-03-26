package seedu.ptman.commons.events.ui;

import seedu.ptman.commons.events.BaseEvent;

/**
 * Represents an announcement change in the Outlet Information
 */
public class AnnouncementChangedEvent extends BaseEvent {
    public final String information;

    public AnnouncementChangedEvent(String information) {
        this.information = information;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
