package seedu.ptman.commons.events.model;

import seedu.ptman.commons.events.BaseEvent;
import seedu.ptman.model.ReadOnlyPartTimeManager;

/** Indicates the PartTimeManager in the model has changed*/
public class PartTimeManagerChangedEvent extends BaseEvent {

    public final ReadOnlyPartTimeManager data;

    public PartTimeManagerChangedEvent(ReadOnlyPartTimeManager data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of employees " + data.getEmployeeList().size() + ", number of tags " + data.getTagList().size();
    }
}
