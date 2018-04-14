package seedu.ptman.logic.commands;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static seedu.ptman.logic.commands.DeselectCommand.MESSAGE_SUCCESS;

import org.junit.Rule;
import org.junit.Test;

import seedu.ptman.commons.events.ui.JumpToListRequestEvent;
import seedu.ptman.ui.testutil.EventsCollectorRule;

//@@author hzxcaryn
public class DeselectCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void execute_main_success() {
        CommandResult result = new DeselectCommand().execute();
        assertEquals(MESSAGE_SUCCESS, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof JumpToListRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }
}
