package seedu.ptman.logic.commands;


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
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
