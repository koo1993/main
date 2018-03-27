package seedu.ptman.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.logic.commands.ExportCommand.MESSAGE_EMAIL_SUCCESS;
import static seedu.ptman.logic.commands.ExportCommand.MESSAGE_SAVE_SUCCESS;

import org.junit.Rule;
import org.junit.Test;

import seedu.ptman.commons.events.ui.ExportTimetableAsImageAndEmailRequestEvent;
import seedu.ptman.commons.events.ui.ExportTimetableAsImageRequestEvent;
import seedu.ptman.model.employee.Email;
import seedu.ptman.ui.testutil.EventsCollectorRule;

//@@author hzxcaryn
public class ExportCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void execute_exportAndSave_success() {
        CommandResult result = new ExportCommand().execute();
        assertEquals(MESSAGE_SAVE_SUCCESS, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ExportTimetableAsImageRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }

    @Test
    public void execute_exportAndEmail_success() {
        CommandResult result = new ExportCommand(new Email("email@example.com")).execute();
        assertEquals(MESSAGE_EMAIL_SUCCESS, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent()
                instanceof ExportTimetableAsImageAndEmailRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }

    @Test
    public void equals() {
        Email aliceEmail = new Email("alice@example.com");
        Email bobEmail = new Email("bob@example.com");
        ExportCommand exportAliceCommand = new ExportCommand(aliceEmail);
        ExportCommand exportBobCommand = new ExportCommand(bobEmail);

        // same object -> returns true
        assertTrue(exportAliceCommand.equals(exportAliceCommand));

        // same values -> returns true
        ExportCommand exportAliceCommandCopy = new ExportCommand(aliceEmail);
        assertTrue(exportAliceCommand.equals(exportAliceCommandCopy));

        // different types -> returns false
        assertFalse(exportAliceCommand.equals(1));

        // null -> returns false
        assertFalse(exportAliceCommand.equals(null));

        // different email -> returns false
        assertFalse(exportAliceCommand.equals(exportBobCommand));
    }
}
