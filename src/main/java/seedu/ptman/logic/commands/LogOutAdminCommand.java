package seedu.ptman.logic.commands;


/**
 * Lists all employees in PTMan to the user.
 */
public class LogOutAdminCommand extends Command {

    public static final String COMMAND_WORD = "logout";

    public static final String MESSAGE_SUCCESS = "You have logged out from admin mode";


    @Override
    public CommandResult execute() {
        if (!model.isAdminMode()) {
            return new CommandResult("You already logged out");
        }
        model.setFalseAdminMode();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
