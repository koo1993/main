package seedu.ptman.logic.commands;

import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;

import seedu.ptman.commons.core.EventsCenter;
import seedu.ptman.commons.events.ui.AnnouncementChangedEvent;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.outlet.Announcement;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.exceptions.NoOutletInformationFieldChangeException;

/**
 * Edits the announcement of outlet in the ptman.
 */
public class AnnouncementCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "announcement";
    public static final String COMMAND_ALIAS = "announce";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the announcement of the outlet "
            + "in admin mode. Existing values will be overwritten by the input values.\n"
            + "Example: " + COMMAND_WORD + " "
            + "This is a new announcement.";
    public static final String MESSAGE_EDIT_OUTLET_SUCCESS = "Announcement successfully updated.";
    public static final String MESSAGE_EDIT_OUTLET_FAILURE = "New announcement cannot be empty."
            + MESSAGE_USAGE;

    private Announcement announcement;

    public AnnouncementCommand(Announcement announcement) {
        this.announcement = announcement;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }
        try {
            OutletInformation editedOutlet = new OutletInformation(model.getOutletInformation());
            editedOutlet.setAnnouncement(announcement);
            model.updateOutlet(editedOutlet);
            EventsCenter.getInstance().post(new AnnouncementChangedEvent(editedOutlet.getAnnouncement().value));
        } catch (NoOutletInformationFieldChangeException e) {
            throw new CommandException(MESSAGE_EDIT_OUTLET_FAILURE);
        }
        return new CommandResult(MESSAGE_EDIT_OUTLET_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AnnouncementCommand)) {
            return false;
        }

        // state check
        return announcement.equals(((AnnouncementCommand) other).announcement);
    }
}
