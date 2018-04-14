package seedu.ptman.logic.commands;

import seedu.ptman.commons.core.EventsCenter;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.events.ui.JumpToListRequestEvent;

/**
 * Returns back to main timetable view (of current week) in PTMan
 */
public class DeselectCommand extends Command {

    public static final String COMMAND_WORD = "deselect";
    public static final String COMMAND_ALIAS = "dsel";

    public static final String MESSAGE_SUCCESS = "Deselected any selected employees. Returned to top of employee list.";

    @Override
    public CommandResult execute() {
        EventsCenter eventsCenter = EventsCenter.getInstance();
        eventsCenter.post(new JumpToListRequestEvent(Index.fromZeroBased(0), false));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
