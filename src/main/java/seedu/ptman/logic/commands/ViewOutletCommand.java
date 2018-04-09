package seedu.ptman.logic.commands;

//@@author SunBangjie
/**
 * Displays the details of outlet in the ptman.
 */
public class ViewOutletCommand extends Command {

    public static final String COMMAND_WORD = "viewoutlet";
    public static final String COMMAND_ALIAS = "vo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": displays outlet information and "
            + "encryption mode.\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        String messageToDisplay = model.getOutletInformationMessage();
        return new CommandResult(messageToDisplay);
    }
}
