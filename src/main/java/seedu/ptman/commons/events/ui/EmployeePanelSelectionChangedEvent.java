package seedu.ptman.commons.events.ui;

import seedu.ptman.commons.events.BaseEvent;
import seedu.ptman.ui.EmployeeCard;

/**
 * Represents a selection change in the Employee List Panel
 */
public class EmployeePanelSelectionChangedEvent extends BaseEvent {

    private final boolean hasNewSelection;
    private final EmployeeCard newSelection;

    public EmployeePanelSelectionChangedEvent(EmployeeCard newSelection) {
        hasNewSelection = (newSelection != null) ? true : false;
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public EmployeeCard getNewSelection() {
        return newSelection;
    }

    public boolean hasNewSelection() {
        return hasNewSelection;
    }
}
