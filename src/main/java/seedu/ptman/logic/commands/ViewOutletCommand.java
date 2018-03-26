package seedu.ptman.logic.commands;

/**
 * Displays the details of outlet in the ptman.
 */
public class ViewOutletCommand extends Command {

    public static final String COMMAND_WORD = "viewoutlet";
    public static final String COMMAND_ALIAS = "vo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": display basic outlet information\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        String messageToDisplay = "Outlet Name: " + model.getOutletInformation().getName() + " "
                + model.getOutletInformationMessage() + " Announcement: "
                + model.getOutletInformation().getAnnouncement();
        return new CommandResult(messageToDisplay);
    }
}
