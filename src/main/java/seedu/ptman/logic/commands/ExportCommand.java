package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.ptman.ui.TimetablePanel.TIMETABLE_IMAGE_FILE_NAME_DEFAULT;

import seedu.ptman.commons.core.EventsCenter;
import seedu.ptman.commons.events.ui.ExportTimetableAsImageAndEmailRequestEvent;
import seedu.ptman.commons.events.ui.ExportTimetableAsImageRequestEvent;
import seedu.ptman.model.employee.Email;

/**
 * Exports current timetable view as an image locally
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String COMMAND_ALIAS = "exp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports timetable as image. "
            + "If email is stated, timetable image will be sent as an attachment to the stated email. "
            + "Else, timetable image will be saved locally.\n"
            + "Parameters: "
            + "[" + PREFIX_EMAIL + "EMAIL]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_EMAIL + "email@example.com";

    public static final String MESSAGE_SAVE_SUCCESS = "Timetable is successfully exported!";
    public static final String MESSAGE_EMAIL_SUCCESS = "Timetable is successfully sent to your email!";

    private final Email emailToSendImageTo;

    /**
     * Creates an ExportCommand to save the timetable as image locally
     */
    public ExportCommand() {
        emailToSendImageTo = null;
    }

    /**
     * Creates an ExportCommand to send the timetable image to the user's email
     * @param email
     */
    public ExportCommand(Email email) {
        requireNonNull(email);
        emailToSendImageTo = email;
    }

    @Override
    public CommandResult execute() {
        if (emailToSendImageTo != null) {
            EventsCenter.getInstance().post(new ExportTimetableAsImageAndEmailRequestEvent(
                    TIMETABLE_IMAGE_FILE_NAME_DEFAULT, emailToSendImageTo));
            return new CommandResult(MESSAGE_EMAIL_SUCCESS);
        } else {
            EventsCenter.getInstance().post(new ExportTimetableAsImageRequestEvent(TIMETABLE_IMAGE_FILE_NAME_DEFAULT));
            return new CommandResult(MESSAGE_SAVE_SUCCESS);
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExportCommand)) {
            return false;
        }

        // state check
        ExportCommand e = (ExportCommand) other;
        return emailToSendImageTo.equals(e.emailToSendImageTo);
    }

}
