package seedu.ptman.commons.events.ui;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of employees
 */
public class JumpToListRequestEvent extends BaseEvent {

    public final int targetIndex;

    public JumpToListRequestEvent(Index targetIndex) {
        this.targetIndex = targetIndex.getZeroBased();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
