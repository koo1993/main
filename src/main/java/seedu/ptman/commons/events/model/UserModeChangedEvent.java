package seedu.ptman.commons.events.model;

import seedu.ptman.commons.events.BaseEvent;

/**
 * Indicates that the user mode has changed. (Admin mode or not)
 */
public class UserModeChangedEvent extends BaseEvent {

    public final boolean isAdminMode;

    public UserModeChangedEvent(boolean isAdminMode) {
        this.isAdminMode = isAdminMode;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
