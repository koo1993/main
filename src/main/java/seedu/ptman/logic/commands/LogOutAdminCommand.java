package seedu.ptman.logic.commands;

import seedu.ptman.commons.core.EventsCenter;
import seedu.ptman.commons.events.model.UserModeChangedEvent;

/**
 * Lists all employees in PTMan to the user.
 */
public class LogOutAdminCommand extends Command {

    public static final String COMMAND_WORD = "logout";

    public static final String MESSAGE_SUCCESS = "You have logged out from admin mode";

    public static final String MESSAGE_LOGGEDOUT = "You already logged out";


    @Override
    public CommandResult execute() {
        if (!model.isAdminMode()) {
            return new CommandResult(MESSAGE_LOGGEDOUT);
        }
        model.setFalseAdminMode();
        EventsCenter.getInstance().post(new UserModeChangedEvent(false));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
