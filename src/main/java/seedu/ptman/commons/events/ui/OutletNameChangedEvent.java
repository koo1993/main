package seedu.ptman.commons.events.ui;

import seedu.ptman.commons.events.BaseEvent;

//@@author SunBangjie
/**
 * Represents an information change in the Outlet Information
 */
public class OutletNameChangedEvent extends BaseEvent {

    public final String message;

    public OutletNameChangedEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
