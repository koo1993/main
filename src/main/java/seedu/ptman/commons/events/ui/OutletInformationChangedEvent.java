package seedu.ptman.commons.events.ui;

import seedu.ptman.commons.events.BaseEvent;

/**
 * Represents an information change in the Outlet Information
 */
public class OutletInformationChangedEvent extends BaseEvent {

    public final String operatingHours;
    public final String outletContact;
    public final String outletEmail;

    public OutletInformationChangedEvent(String operatingHours, String outletContact, String outletEmail) {
        this.operatingHours = operatingHours;
        this.outletContact = outletContact;
        this.outletEmail = outletEmail;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
