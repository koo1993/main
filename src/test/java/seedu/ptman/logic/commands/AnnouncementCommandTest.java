package seedu.ptman.logic.commands;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalPartTimeManager;

import org.junit.Test;

import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.outlet.Announcement;
import seedu.ptman.model.outlet.OutletInformation;

/**
 * Contains integration tests (interaction with the Model) for {@code AnnouncementCommand}.
 */
public class AnnouncementCommandTest {
    private Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(), new OutletInformation());

    @Test
    public void equals() {
        Announcement firstAnnouncement = new Announcement("First Announcement.");
        Announcement secondAnnouncement = new Announcement("Second Announcement.");
        AnnouncementCommand firstAnnouncementCommand = new AnnouncementCommand(firstAnnouncement);
        AnnouncementCommand secondAnnouncementCommand = new AnnouncementCommand(secondAnnouncement);

        // same object -> return true
        assertTrue(firstAnnouncementCommand.equals(firstAnnouncementCommand));

        // same values -> return true
        AnnouncementCommand firstAnnouncementCommandCopy = new AnnouncementCommand(firstAnnouncement);
        assertTrue(firstAnnouncementCommand.equals(firstAnnouncementCommandCopy));

        // different types -> return false
        assertFalse(firstAnnouncementCommand.equals(1));

        // null -> return false
        assertFalse(firstAnnouncementCommand.equals(null));

        // different announcement -> return false
        assertFalse(firstAnnouncementCommand.equals(secondAnnouncementCommand));
    }

    @Test
    public void execute_nonAdminMode_failure() {
        AnnouncementCommand command = prepareCommand("New Announcement!");
        assertCommandFailure(command, model, MESSAGE_ACCESS_DENIED);
    }

    @Test
    public void execute_adminModeValidAnnouncement_commandFailed() {
        model.setTrueAdminMode(new Password());
        String expectedMessage = AnnouncementCommand.MESSAGE_EDIT_OUTLET_SUCCESS;
        AnnouncementCommand command = prepareCommand("Valid Announcement");
        assertCommandSuccess(command, model, expectedMessage, model);
    }

    /**
     * Parses {@code userInput} into a {@code AnnouncementCommand}
     */
    private AnnouncementCommand prepareCommand(String userInput) {
        AnnouncementCommand command = new AnnouncementCommand(new Announcement(userInput));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
