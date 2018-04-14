package seedu.ptman.logic.commands;

//@@author SunBangjie
/**
 * Displays the details of outlet in the ptman.
 */
public class ViewEncryptionCommand extends Command {

    public static final String COMMAND_WORD = "viewencryption";
    public static final String COMMAND_ALIAS = "ve";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": displays encryption status of "
            + "local storage files.\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        String messageToDisplay = model.getEncryptionModeMessage();
        return new CommandResult(messageToDisplay);
    }
}
